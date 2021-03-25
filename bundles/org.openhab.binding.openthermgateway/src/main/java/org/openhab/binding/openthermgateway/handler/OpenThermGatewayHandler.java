/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.openthermgateway.handler;

import static org.openhab.binding.openthermgateway.internal.OpenThermGatewayBindingConstants.*;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.openthermgateway.internal.DataItemGroup;
import org.openhab.binding.openthermgateway.internal.GatewayCommand;
import org.openhab.binding.openthermgateway.internal.GatewayCommandCode;
import org.openhab.binding.openthermgateway.internal.Message;
import org.openhab.binding.openthermgateway.internal.OpenThermGatewayCallback;
import org.openhab.binding.openthermgateway.internal.OpenThermGatewayConfiguration;
import org.openhab.binding.openthermgateway.internal.OpenThermGatewayConnector;
import org.openhab.binding.openthermgateway.internal.OpenThermGatewaySocketConnector;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.SIUnits;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link OpenThermGatewayHandler} is responsible for communication to the OpenTherm Gateway device.
 *
 * @author Arjen Korevaar - Initial contribution
 */
@NonNullByDefault
public class OpenThermGatewayHandler extends BaseBridgeHandler implements OpenThermGatewayCallback {

    private final Logger logger = LoggerFactory.getLogger(OpenThermGatewayHandler.class);

    private @Nullable OpenThermGatewayConfiguration config;
    private @Nullable OpenThermGatewayConnector connector;
    private @Nullable ScheduledFuture<?> reconnectTask;

    private boolean connecting = false;
    private boolean explicitDisconnect = false;

    public OpenThermGatewayHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing OpenTherm Gateway handler for uid '{}'", getThing().getUID());

        updateStatus(ThingStatus.UNKNOWN, ThingStatusDetail.NONE, "Initializing");

        config = getConfigAs(OpenThermGatewayConfiguration.class);

        connect();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        logger.debug("Received command {} {}", channelUID, command);

        if (!(command instanceof RefreshType)) {
            String channel = channelUID.getId();
            String code = getGatewayCodeFromChannel(channel);

            GatewayCommand gatewayCommand = null;

            if (command instanceof OnOffType) {
                OnOffType onOff = (OnOffType) command;
                gatewayCommand = GatewayCommand.parse(code, onOff == OnOffType.ON ? "1" : "0");
            }
            if (command instanceof QuantityType<?>) {
                QuantityType<?> quantityType = ((QuantityType<?>) command).toUnit(SIUnits.CELSIUS);

                if (quantityType != null) {
                    double value = quantityType.doubleValue();
                    gatewayCommand = GatewayCommand.parse(code, Double.toString(value));
                }
            }

            if (gatewayCommand == null) {
                gatewayCommand = GatewayCommand.parse(code, command.toFullString());
            }

            sendCommand(gatewayCommand);

            if (code == GatewayCommandCode.ControlSetpoint) {
                if (gatewayCommand.getMessage().equals("0.0")) {
                    updateState(CHANNEL_OVERRIDE_CENTRAL_HEATING_WATER_SETPOINT, UnDefType.UNDEF);
                }
                updateState(CHANNEL_OVERRIDE_CENTRAL_HEATING_ENABLED,
                        OnOffType.from(!gatewayCommand.getMessage().equals("0.0")));
            } else if (code == GatewayCommandCode.ControlSetpoint2) {
                if (gatewayCommand.getMessage().equals("0.0")) {
                    updateState(CHANNEL_OVERRIDE_CENTRAL_HEATING2_WATER_SETPOINT, UnDefType.UNDEF);
                }
                updateState(CHANNEL_OVERRIDE_CENTRAL_HEATING2_ENABLED,
                        OnOffType.from(!gatewayCommand.getMessage().equals("0.0")));
            }
        }
    }

    public void sendCommand(GatewayCommand gatewayCommand) {
        @Nullable
        OpenThermGatewayConnector conn = connector;

        if (conn != null && conn.isConnected()) {
            conn.sendCommand(gatewayCommand);
        }
    }

    @Override
    public void connecting() {
        connecting = true;
        updateStatus(ThingStatus.UNKNOWN, ThingStatusDetail.NONE, "Connecting");
    }

    @Override
    public void connected() {
        connecting = false;
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void disconnected() {
        @Nullable
        OpenThermGatewayConfiguration conf = config;

        connecting = false;

        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Disconnected");

        // retry connection if disconnect is not explicitly requested
        if (!explicitDisconnect && conf != null && conf.connectionRetryInterval > 0) {
            logger.debug("Scheduling to reconnect in {} seconds.", conf.connectionRetryInterval);
            reconnectTask = scheduler.schedule(this::connect, conf.connectionRetryInterval, TimeUnit.SECONDS);
        }
    }

    public void receiveMessage(Message message) {
        if (!DataItemGroup.dataItemGroups.containsKey(message.getID())) {
            logger.debug("Unsupported message id {}", message.getID());
            return;
        }

        for (Thing thing : getThing().getThings()) {
            BaseDeviceHandler handler = (BaseDeviceHandler) thing.getHandler();

            if (handler != null) {
                handler.receiveMessage(message);
            }
        }
    }

    @Override
    public void handleRemoval() {
        logger.debug("Removing OpenTherm Gateway handler");
        disconnect();
        super.handleRemoval();
    }

    @Override
    public void dispose() {
        disconnect();

        ScheduledFuture<?> localReconnectTask = reconnectTask;
        if (localReconnectTask != null) {
            localReconnectTask.cancel(true);
            reconnectTask = null;
        }

        super.dispose();
    }

    private void connect() {
        @Nullable
        OpenThermGatewayConfiguration conf = config;

        explicitDisconnect = false;

        if (connecting) {
            logger.debug("OpenTherm Gateway connector is already connecting ...");
            return;
        }

        disconnect();

        if (conf != null) {
            logger.debug("Starting OpenTherm Gateway connector");

            connector = new OpenThermGatewaySocketConnector(this, conf.ipaddress, conf.port);

            Thread thread = new Thread(connector, "OpenTherm Gateway Binding - socket listener thread");
            thread.setDaemon(true);
            thread.start();

            logger.debug("OpenTherm Gateway connector started");
        }
    }

    private void disconnect() {
        @Nullable
        OpenThermGatewayConnector conn = connector;

        explicitDisconnect = true;

        if (conn != null) {
            if (conn.isConnected()) {
                logger.debug("Stopping OpenTherm Gateway connector");
                conn.stop();
            }

            connector = null;
        }
    }

    private @Nullable String getGatewayCodeFromChannel(String channel) throws IllegalArgumentException {
        switch (channel) {
            case CHANNEL_OVERRIDE_SETPOINT_TEMPORARY:
                return GatewayCommandCode.TemperatureTemporary;
            case CHANNEL_OVERRIDE_SETPOINT_CONSTANT:
                return GatewayCommandCode.TemperatureConstant;
            case CHANNEL_OUTSIDE_TEMPERATURE:
                return GatewayCommandCode.TemperatureOutside;
            case CHANNEL_OVERRIDE_DHW_SETPOINT:
                return GatewayCommandCode.SetpointWater;
            case CHANNEL_OVERRIDE_CENTRAL_HEATING_WATER_SETPOINT:
                return GatewayCommandCode.ControlSetpoint;
            case CHANNEL_OVERRIDE_CENTRAL_HEATING_ENABLED:
                return GatewayCommandCode.CentralHeating;
            case CHANNEL_OVERRIDE_CENTRAL_HEATING2_WATER_SETPOINT:
                return GatewayCommandCode.ControlSetpoint2;
            case CHANNEL_OVERRIDE_CENTRAL_HEATING2_ENABLED:
                return GatewayCommandCode.CentralHeating2;
            case CHANNEL_VH_VENTILATION_SETPOINT:
                return GatewayCommandCode.VentilationSetpoint;
            case CHANNEL_SEND_COMMAND:
                return null;
            default:
                throw new IllegalArgumentException(String.format("Unknown channel %s", channel));
        }
    }
}

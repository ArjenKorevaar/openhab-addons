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

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.openthermgateway.internal.DataItemGroup;
import org.openhab.binding.openthermgateway.internal.GatewayCallback;
import org.openhab.binding.openthermgateway.internal.GatewayConfiguration;
import org.openhab.binding.openthermgateway.internal.GatewayConnector;
import org.openhab.binding.openthermgateway.internal.GatewaySocketConnector;
import org.openhab.binding.openthermgateway.internal.Message;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link GatewayHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Arjen Korevaar - Initial contribution
 */
@NonNullByDefault
public class GatewayHandler extends BaseBridgeHandler implements GatewayCallback {

    private final Logger logger = LoggerFactory.getLogger(GatewayHandler.class);

    private @Nullable GatewayConfiguration config;
    private @Nullable GatewayConnector connector;
    private @Nullable ScheduledFuture<?> reconnectTask;

    private boolean connecting = false;
    private boolean explicitDisconnect = false;

    public GatewayHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Gateway handler for uid '{}'", getThing().getUID());

        updateStatus(ThingStatus.UNKNOWN, ThingStatusDetail.NONE, "Initializing");

        config = getConfigAs(GatewayConfiguration.class);

        connect();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        @Nullable
        GatewayConnector conn = connector;

        logger.debug("Received channel: {}, command: {}", channelUID, command);

        if (!(command instanceof RefreshType)) {
            String channel = channelUID.getId();
            // String code = getGatewayCodeFromChannel(channel);

            // GatewayCommand gatewayCommand = null;

            // if (command instanceof OnOffType) {
            // OnOffType onOff = (OnOffType) command;
            // gatewayCommand = GatewayCommand.parse(code, onOff == OnOffType.ON ? "1" : "0");
            // }
            // if (command instanceof QuantityType<?>) {
            // QuantityType<?> quantityType = ((QuantityType<?>) command).toUnit(SIUnits.CELSIUS);

            // if (quantityType != null) {
            // double value = quantityType.doubleValue();
            // gatewayCommand = GatewayCommand.parse(code, Double.toString(value));
            // }
            // }

            // if (gatewayCommand == null) {
            // gatewayCommand = GatewayCommand.parse(code, command.toFullString());
            // }

            // if (conn != null && conn.isConnected()) {
            // conn.sendCommand(gatewayCommand);

            // if (code == GatewayCommandCode.ControlSetpoint) {
            // if (gatewayCommand.getMessage().equals("0.0")) {
            // updateState(OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING_WATER_SETPOINT,
            // UnDefType.UNDEF);
            // }
            // updateState(OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING_ENABLED,
            // OnOffType.from(!gatewayCommand.getMessage().equals("0.0")));
            // } else if (code == GatewayCommandCode.ControlSetpoint2) {
            // if (gatewayCommand.getMessage().equals("0.0")) {
            // updateState(OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING2_WATER_SETPOINT,
            // UnDefType.UNDEF);
            // }
            // updateState(OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING2_ENABLED,
            // OnOffType.from(!gatewayCommand.getMessage().equals("0.0")));
            // }
            // } else {
            // connect();
            // }
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
        GatewayConfiguration conf = config;

        connecting = false;

        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Disconnected");

        // retry connection if disconnect is not explicitly requested
        if (!explicitDisconnect && conf != null && conf.connectionRetryInterval > 0) {
            logger.debug("Scheduling to reconnect in {} seconds.", conf.connectionRetryInterval);
            reconnectTask = scheduler.schedule(this::connect, conf.connectionRetryInterval, TimeUnit.SECONDS);
        }
    }

    @Override
    public void receiveMessage(Message message) {

        int msgId = message.getID();

        if (!DataItemGroup.dataItemGroups.containsKey(msgId)) {
            logger.debug("Unsupported message id {}", msgId);
            return;
        }

        for (Thing thing : getThing().getThings()) {
            BaseDeviceHandler handler = (BaseDeviceHandler) thing.getHandler();
            handler.receiveMessage(message);
        }
    }

    @Override
    public void handleRemoval() {
        logger.debug("Removing Gateway handler");
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
        GatewayConfiguration conf = config;

        explicitDisconnect = false;

        if (connecting) {
            logger.debug("Gateway connector is already connecting ...");
            return;
        }

        disconnect();

        if (conf != null) {
            logger.debug("Starting Gateway connector");

            connector = new GatewaySocketConnector(this, conf.ipaddress, conf.port);

            Thread thread = new Thread(connector, "OpenTherm Gateway Binding - socket listener thread");
            thread.setDaemon(true);
            thread.start();

            logger.debug("Gateway connector started");
        }
    }

    private void disconnect() {
        @Nullable
        GatewayConnector conn = connector;

        explicitDisconnect = true;

        if (conn != null) {
            if (conn.isConnected()) {
                logger.debug("Stopping Gateway connector");
                conn.stop();
            }

            connector = null;
        }
    }

    // private @Nullable String getGatewayCodeFromChannel(String channel) throws IllegalArgumentException {
    // switch (channel) {
    // case OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_SETPOINT_TEMPORARY:
    // return GatewayCommandCode.TemperatureTemporary;
    // case OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_SETPOINT_CONSTANT:
    // return GatewayCommandCode.TemperatureConstant;
    // case OpenThermGatewayBindingConstants.CHANNEL_OUTSIDE_TEMPERATURE:
    // return GatewayCommandCode.TemperatureOutside;
    // case OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_DHW_SETPOINT:
    // return GatewayCommandCode.SetpointWater;
    // case OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING_WATER_SETPOINT:
    // return GatewayCommandCode.ControlSetpoint;
    // case OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING_ENABLED:
    // return GatewayCommandCode.CentralHeating;
    // case OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING2_WATER_SETPOINT:
    // return GatewayCommandCode.ControlSetpoint2;
    // case OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING2_ENABLED:
    // return GatewayCommandCode.CentralHeating2;
    // case OpenThermGatewayBindingConstants.CHANNEL_SEND_COMMAND:
    // return null;
    // default:
    // throw new IllegalArgumentException(String.format("Unknown channel %s", channel));
    // }
    // }

    public static final String CHANNEL_SEND_COMMAND = "sendcommand";
}

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

import javax.measure.Unit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.openthermgateway.internal.DataItem;
import org.openhab.binding.openthermgateway.internal.Message;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BaseDeviceHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Arjen Korevaar - Initial contribution
 */
@NonNullByDefault
public class BaseDeviceHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(BaseDeviceHandler.class);

    public BaseDeviceHandler(Thing thing) {
        super(thing);
    }
    
    @Override
    public void initialize() {
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    public void receiveMessage(Message message) {
    }

    public void updateState(Message message, DataItem dataItem) {
        String channelId = dataItem.getSubject();

        State state = null;

        switch (dataItem.getDataType()) {
            case FLAGS:
                state = OnOffType.from(message.getBit(dataItem.getByteType(), dataItem.getBitPos()));
                break;
            case UINT8:
            case UINT16:
                state = new DecimalType(message.getUInt(dataItem.getByteType()));
                break;
            case INT8:
            case INT16:
                state = new DecimalType(message.getInt(dataItem.getByteType()));
                break;
            case FLOAT:
                float value = message.getFloat();
                @Nullable
                Unit<?> unit = dataItem.getUnit();
                state = (unit == null) ? new DecimalType(value) : new QuantityType<>(value, unit);
                break;
            case DOWTOD:
                break;
        }

        if (state != null) {
            logger.debug("Received update for channel '{}': {}", channelId, state);
            updateState(channelId, state);
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
}

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

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.openthermgateway.OpenThermGatewayBindingConstants;
import org.openhab.binding.openthermgateway.internal.DataItem;
import org.openhab.binding.openthermgateway.internal.DataItemGroup;
import org.openhab.binding.openthermgateway.internal.Message;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BaseDeviceHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Arjen Korevaar - Initial contribution
 */
@NonNullByDefault
public class BoilerHandler extends BaseDeviceHandler {

    private final Logger logger = LoggerFactory.getLogger(BoilerHandler.class);

    public BoilerHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void receiveMessage(Message message) {
        // Generic method to handle the update of a channel for any base Device type
        // if (!OpenThermGatewayBindingConstants.SUPPORTED_CHANNEL_IDS.contains(channelId)
        // || (dataItem.getFilteredCode() != null && dataItem.getFilteredCode() != message.getCode())) {
        // continue;
        // }

        DataItem[] dataItems = DataItemGroup.dataItemGroups.get(message.getID());

        for (DataItem dataItem : dataItems) {
            String channelId = dataItem.getSubject();

            if (SUPPORTED_CHANNEL_IDS.contains(channelId)) {
                super.updateState(message, dataItem);
            }
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // String channelId = dataItem.getSubject();

        // if (handler.supportsChannel(channelId)) {

        // }
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

    private static final Set<String> SUPPORTED_CHANNEL_IDS = Set.of(
            OpenThermGatewayBindingConstants.CHANNEL_ROOM_TEMPERATURE,
            OpenThermGatewayBindingConstants.CHANNEL_ROOM_SETPOINT,
            OpenThermGatewayBindingConstants.CHANNEL_FLOW_TEMPERATURE,
            OpenThermGatewayBindingConstants.CHANNEL_RETURN_TEMPERATURE,
            OpenThermGatewayBindingConstants.CHANNEL_OUTSIDE_TEMPERATURE,
            OpenThermGatewayBindingConstants.CHANNEL_CENTRAL_HEATING_WATER_PRESSURE,
            OpenThermGatewayBindingConstants.CHANNEL_CENTRAL_HEATING_ENABLED,
            OpenThermGatewayBindingConstants.CHANNEL_REQUESTED_CENTRAL_HEATING_ENABLED,
            OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING_ENABLED,
            OpenThermGatewayBindingConstants.CHANNEL_CENTRAL_HEATING2_ENABLED,
            OpenThermGatewayBindingConstants.CHANNEL_REQUESTED_CENTRAL_HEATING2_ENABLED,
            OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING2_ENABLED,
            OpenThermGatewayBindingConstants.CHANNEL_CENTRAL_HEATING_MODE,
            OpenThermGatewayBindingConstants.CHANNEL_CENTRAL_HEATING_WATER_SETPOINT,
            OpenThermGatewayBindingConstants.CHANNEL_REQUESTED_CENTRAL_HEATING_WATER_SETPOINT,
            OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING_WATER_SETPOINT,
            OpenThermGatewayBindingConstants.CHANNEL_CENTRAL_HEATING2_WATER_SETPOINT,
            OpenThermGatewayBindingConstants.CHANNEL_REQUESTED_CENTRAL_HEATING2_WATER_SETPOINT,
            OpenThermGatewayBindingConstants.CHANNEL_OVERRIDE_CENTRAL_HEATING2_WATER_SETPOINT,
            OpenThermGatewayBindingConstants.CHANNEL_DOMESTIC_HOT_WATER_TEMPERATURE,
            OpenThermGatewayBindingConstants.CHANNEL_DOMESTIC_HOT_WATER_ENABLED,
            OpenThermGatewayBindingConstants.CHANNEL_DOMESTIC_HOT_WATER_MODE,
            OpenThermGatewayBindingConstants.CHANNEL_DOMESTIC_HOT_WATER_SETPOINT,
            OpenThermGatewayBindingConstants.CHANNEL_FLAME,
            OpenThermGatewayBindingConstants.CHANNEL_RELATIVE_MODULATION_LEVEL,
            OpenThermGatewayBindingConstants.CHANNEL_MAXIMUM_MODULATION_LEVEL,
            OpenThermGatewayBindingConstants.CHANNEL_FAULT, OpenThermGatewayBindingConstants.CHANNEL_SERVICEREQUEST,
            OpenThermGatewayBindingConstants.CHANNEL_REMOTE_RESET,
            OpenThermGatewayBindingConstants.CHANNEL_LOW_WATER_PRESSURE,
            OpenThermGatewayBindingConstants.CHANNEL_GAS_FLAME_FAULT,
            OpenThermGatewayBindingConstants.CHANNEL_AIR_PRESSURE_FAULT,
            OpenThermGatewayBindingConstants.CHANNEL_WATER_OVER_TEMP,
            OpenThermGatewayBindingConstants.CHANNEL_OEM_FAULTCODE,
            OpenThermGatewayBindingConstants.CHANNEL_DIAGNOSTICS_INDICATION);
}

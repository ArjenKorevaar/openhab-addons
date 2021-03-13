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
import org.openhab.core.thing.Thing;

/**
 * The {@link BaseDeviceHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Arjen Korevaar - Initial contribution
 */
@NonNullByDefault
public class BoilerHandler extends BaseDeviceHandler {

    public BoilerHandler(Thing thing) {
        super(thing);
    }

    @Override
    public boolean supportsChannelId(String channelId) {
        return SUPPORTED_CHANNEL_IDS.contains(channelId);
    }

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

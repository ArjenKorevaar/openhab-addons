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
public class VentilationHeatRecoveryHandler extends BaseDeviceHandler {

    public VentilationHeatRecoveryHandler(Thing thing) {
        super(thing);
    }

    @Override
    public boolean supportsChannelId(String channelId) {
        return SUPPORTED_CHANNEL_IDS.contains(channelId);
    }

    private static final Set<String> SUPPORTED_CHANNEL_IDS = Set.of(
            OpenThermGatewayBindingConstants.CHANNEL_VH_VENTILATION_ENABLE,
            OpenThermGatewayBindingConstants.CHANNEL_VH_BYPASS_POSITION,
            OpenThermGatewayBindingConstants.CHANNEL_VH_BYPASS_MODE,
            OpenThermGatewayBindingConstants.CHANNEL_VH_FREE_VENTILATION_MODE,
            OpenThermGatewayBindingConstants.CHANNEL_VH_FAULT_INDICATION,
            OpenThermGatewayBindingConstants.CHANNEL_VH_VENTILATION_MODE,
            OpenThermGatewayBindingConstants.CHANNEL_VH_BYPASS_STATUS,
            OpenThermGatewayBindingConstants.CHANNEL_VH_BYPASS_AUTOMATIC_STATUS,
            OpenThermGatewayBindingConstants.CHANNEL_VH_FREE_VENTILATION_STATUS,
            OpenThermGatewayBindingConstants.CHANNEL_VH_DIAGNOSTIC_INDICATION,
            OpenThermGatewayBindingConstants.CHANNEL_VH_CONTROL_SETPOINT,
            OpenThermGatewayBindingConstants.CHANNEL_VH_DIAGNOSTIC_CODE,
            OpenThermGatewayBindingConstants.CHANNEL_VH_SYSTEM_TYPE, OpenThermGatewayBindingConstants.CHANNEL_VH_BYPASS,
            OpenThermGatewayBindingConstants.CHANNEL_VH_SPEED_CONTROL,
            OpenThermGatewayBindingConstants.CHANNEL_VH_MEMBER_ID,
            OpenThermGatewayBindingConstants.CHANNEL_VH_OPENTHERM_VERSION,
            OpenThermGatewayBindingConstants.CHANNEL_VH_VERSION_TYPE,
            OpenThermGatewayBindingConstants.CHANNEL_VH_RELATIVE_VENTILATION,
            OpenThermGatewayBindingConstants.CHANNEL_VH_CO2_LEVEL,
            OpenThermGatewayBindingConstants.CHANNEL_VH_SUPPLY_INLET_TEMP,
            OpenThermGatewayBindingConstants.CHANNEL_VH_SUPPLY_OUTLET_TEMP,
            OpenThermGatewayBindingConstants.CHANNEL_VH_EXHAUST_INLET_TEMP,
            OpenThermGatewayBindingConstants.CHANNEL_VH_EXHAUST_OUTLET_TEMP,
            OpenThermGatewayBindingConstants.CHANNEL_VH_ACTUAL_EXHAUST_FAN_SPEED,
            OpenThermGatewayBindingConstants.CHANNEL_VH_ACTUAL_INLET_FAN_SPEED,
            OpenThermGatewayBindingConstants.CHANNEL_VH_NOMINAL_VENTILATION_VALUE,
            OpenThermGatewayBindingConstants.CHANNEL_VH_TSP_NUMBER,
            OpenThermGatewayBindingConstants.CHANNEL_VH_TSP_ENTRY);
}

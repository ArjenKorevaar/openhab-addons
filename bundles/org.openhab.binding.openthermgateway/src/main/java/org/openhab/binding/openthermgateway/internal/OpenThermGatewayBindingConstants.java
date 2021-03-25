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
package org.openhab.binding.openthermgateway.internal;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link OpenThermGatewayBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Arjen Korevaar - Initial contribution
 */
@NonNullByDefault
public class OpenThermGatewayBindingConstants {

    // Binding Id
    public static final String BINDING_ID = "openthermgateway";

    // List of all the ThingType UID's
    public static final ThingTypeUID OPENTHERM_GATEWAY_THING_TYPE_UID = new ThingTypeUID(BINDING_ID,
            "openthermgateway");
    public static final ThingTypeUID BOILER_THING_TYPE_UID = new ThingTypeUID(BINDING_ID, "boiler");
    public static final ThingTypeUID VH_THING_TYPE_UID = new ThingTypeUID(BINDING_ID, "ventilationheatrecovery");

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPE_UIDS = Set.of(OPENTHERM_GATEWAY_THING_TYPE_UID,
            BOILER_THING_TYPE_UID, VH_THING_TYPE_UID);

    // List of all Channel id's
    public static final String CHANNEL_SEND_COMMAND = "sendcommand";

    public static final String CHANNEL_OVERRIDE_SETPOINT_TEMPORARY = "temperaturetemporary";
    public static final String CHANNEL_OVERRIDE_SETPOINT_CONSTANT = "temperatureconstant";
    public static final String CHANNEL_OVERRIDE_DHW_SETPOINT = "overridedhwsetpoint";
    public static final String CHANNEL_ROOM_TEMPERATURE = "roomtemp";
    public static final String CHANNEL_ROOM_SETPOINT = "roomsetpoint";
    public static final String CHANNEL_FLOW_TEMPERATURE = "flowtemp";
    public static final String CHANNEL_RETURN_TEMPERATURE = "returntemp";
    public static final String CHANNEL_OUTSIDE_TEMPERATURE = "outsidetemp";
    public static final String CHANNEL_CENTRAL_HEATING_WATER_SETPOINT = "controlsetpoint";
    public static final String CHANNEL_REQUESTED_CENTRAL_HEATING_WATER_SETPOINT = "controlsetpointrequested";
    public static final String CHANNEL_OVERRIDE_CENTRAL_HEATING_WATER_SETPOINT = "controlsetpointoverride";
    public static final String CHANNEL_CENTRAL_HEATING2_WATER_SETPOINT = "controlsetpoint2";
    public static final String CHANNEL_REQUESTED_CENTRAL_HEATING2_WATER_SETPOINT = "controlsetpoint2requested";
    public static final String CHANNEL_OVERRIDE_CENTRAL_HEATING2_WATER_SETPOINT = "controlsetpoint2override";
    public static final String CHANNEL_CENTRAL_HEATING_WATER_PRESSURE = "waterpressure";
    public static final String CHANNEL_CENTRAL_HEATING_ENABLED = "ch_enable";
    public static final String CHANNEL_REQUESTED_CENTRAL_HEATING_ENABLED = "ch_enablerequested";
    public static final String CHANNEL_OVERRIDE_CENTRAL_HEATING_ENABLED = "ch_enableoverride";
    public static final String CHANNEL_CENTRAL_HEATING2_ENABLED = "ch2_enable";
    public static final String CHANNEL_REQUESTED_CENTRAL_HEATING2_ENABLED = "ch2_enablerequested";
    public static final String CHANNEL_OVERRIDE_CENTRAL_HEATING2_ENABLED = "ch2_enableoverride";
    public static final String CHANNEL_CENTRAL_HEATING_MODE = "ch_mode";
    public static final String CHANNEL_DOMESTIC_HOT_WATER_TEMPERATURE = "dhwtemp";
    public static final String CHANNEL_DOMESTIC_HOT_WATER_ENABLED = "dhw_enable";
    public static final String CHANNEL_DOMESTIC_HOT_WATER_MODE = "dhw_mode";
    public static final String CHANNEL_DOMESTIC_HOT_WATER_SETPOINT = "tdhwset";
    public static final String CHANNEL_FLAME = "flame";
    public static final String CHANNEL_RELATIVE_MODULATION_LEVEL = "modulevel";
    public static final String CHANNEL_MAXIMUM_MODULATION_LEVEL = "maxrelmdulevel";
    public static final String CHANNEL_FAULT = "fault";
    public static final String CHANNEL_SERVICEREQUEST = "servicerequest";
    public static final String CHANNEL_REMOTE_RESET = "lockout-reset";
    public static final String CHANNEL_LOW_WATER_PRESSURE = "lowwaterpress";
    public static final String CHANNEL_GAS_FLAME_FAULT = "gasflamefault";
    public static final String CHANNEL_AIR_PRESSURE_FAULT = "airpressfault";
    public static final String CHANNEL_WATER_OVER_TEMP = "waterovtemp";
    public static final String CHANNEL_OEM_FAULTCODE = "oemfaultcode";
    public static final String CHANNEL_DIAGNOSTICS_INDICATION = "diag";

    public static final String CHANNEL_VH_VENTILATION_ENABLE = "vh_ventilationenable";
    public static final String CHANNEL_VH_BYPASS_POSITION = "vh_bypassposition";
    public static final String CHANNEL_VH_BYPASS_MODE = "vh_bypassmode";
    public static final String CHANNEL_VH_FREE_VENTILATION_MODE = "vh_freeventilationmode";
    public static final String CHANNEL_VH_FAULT_INDICATION = "vh_faultindication";
    public static final String CHANNEL_VH_VENTILATION_MODE = "vh_ventilationmode";
    public static final String CHANNEL_VH_BYPASS_STATUS = "vh_bypassstatus";
    public static final String CHANNEL_VH_BYPASS_AUTOMATIC_STATUS = "vh_bypassautomaticstatus";
    public static final String CHANNEL_VH_FREE_VENTILATION_STATUS = "vh_freeventilationstatus";
    public static final String CHANNEL_VH_DIAGNOSTIC_INDICATION = "vh_diagnosticindication";
    public static final String CHANNEL_VH_CONTROL_SETPOINT = "vh_controlsetpoint";
    public static final String CHANNEL_VH_DIAGNOSTIC_CODE = "vh_diagnosticcode";
    public static final String CHANNEL_VH_SYSTEM_TYPE = "vh_systemtype";
    public static final String CHANNEL_VH_BYPASS = "vh_bypass";
    public static final String CHANNEL_VH_SPEED_CONTROL = "vh_speedcontrol";
    public static final String CHANNEL_VH_MEMBER_ID = "vh_memberid";
    public static final String CHANNEL_VH_OPENTHERM_VERSION = "vh_openthermversion";
    public static final String CHANNEL_VH_VERSION_TYPE = "vh_versiontype";
    public static final String CHANNEL_VH_RELATIVE_VENTILATION = "vh_relativeventilation";
    public static final String CHANNEL_VH_CO2_LEVEL = "vh_co2level";
    public static final String CHANNEL_VH_SUPPLY_INLET_TEMP = "vh_supplyinlettemp";
    public static final String CHANNEL_VH_SUPPLY_OUTLET_TEMP = "vh_supplyoutlettemp";
    public static final String CHANNEL_VH_EXHAUST_INLET_TEMP = "vh_exhaustinlettemp";
    public static final String CHANNEL_VH_EXHAUST_OUTLET_TEMP = "vh_exhaustoutlettemp";
    public static final String CHANNEL_VH_ACTUAL_EXHAUST_FAN_SPEED = "vh_actualexhaustfanspeed";
    public static final String CHANNEL_VH_ACTUAL_INLET_FAN_SPEED = "vh_actualinletfanspeed";
    public static final String CHANNEL_VH_NOMINAL_VENTILATION_VALUE = "vh_nominalventilationvalue";
    public static final String CHANNEL_VH_TSP_NUMBER = "vh_tspnumber";
    public static final String CHANNEL_VH_TSP_ENTRY = "vh_tspentry";
    public static final String CHANNEL_VH_VENTILATION_SETPOINT = "vh_ventilationsetpoint";
}

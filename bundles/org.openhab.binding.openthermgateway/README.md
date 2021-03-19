# OpenTherm Gateway Binding

This binding is used to integrate the OpenTherm Gateway into openHAB.
The OpenTherm Gateway is a module designed by Schelte Bron that is connected in between a boiler and a thermostat and communicates using the OpenTherm protocol.

More information on the OpenTherm Gateway device can be found at http://otgw.tclcode.com/

## Discovery

The binding does not support auto discovery.

## Supported Things

The OpenTherm Gateway binding supports three Things: an OpenThermGateway bridge, a Boiler thing and a Ventilation / Heat Recovery thing.

### OpenThermGateway Bridge

The OpenThermGateway is a Bridge that handles communication with the actual OpenTherm Gateway device.

#### Configuration

The OpenThermGateway is designed to support various ways of connecting to the OpenTherm Gateway device, but currently only supports a TCP socket connection.
The configuration settings for the Bridge are Hostname/IP address and Port, which are used to connect to the gateway, and an automatic connection retry interval in case the connection to the OpenTherm Gateway device is lost.

| Parameter                 | Name                      | Description                                                     | Required | Default |
|---------------------------|---------------------------|-----------------------------------------------------------------|----------|---------|
| `ipaddress`               | Hostname or IP address    | The hostname or IP address to connect to the OpenTherm Gateway. | yes      |         |
| `port`                    | Port                      | The port used to connect to the OpenTherm Gateway.              | yes      |         |
| `connectionRetryInterval` | Connection Retry Interval | The interval in seconds to retry connecting (0 = disabled).     | yes      | 60      |

#### Channels

The OpenTherm Gateway supports the following channels:

| Channel ID | Item Type | Description | Access |
|------------|-----------|-------------|--------|
| sendcommand | String | Channel to send commands to the OpenTherm Gateway device | R/W |

#### Example

```
Bridge openthermgateway:openthermgateway:1 "OpenTherm Gateway" [ ipaddress="192.168.1.100", port=8000, connectionRetryInterval=60 ] {
    // ...
}
```

### Boiler

The Boiler represents a boiler/thermostat combination to monitor and control central heating and domestic hot water.

#### Configuration

The Boiler thing does not require any configuration.

#### Channels

The Boiler supports the following channels:

| Channel ID | Item Type | Description | Access |
|------------|-----------|-------------|--------|
| roomtemp | Number:Temperature | Current sensed room temperature | R |
| roomsetpoint | Number:Temperature | Current room temperature setpoint | R |
| temperaturetemporary | Number:Temperature | Temporary override room temperature setpoint | R/W |
| temperatureconstant | Number:Temperature | Constant override room temperature setpoint | R/W |
| controlsetpoint | Number:Temperature | Central heating water setpoint set at boiler | R |
| controlsetpointrequested | Number:Temperature | Central heating water setpoint requested by Thermostat | R |
| controlsetpointoverride | Number:Temperature | Central heating water setpoint configured on OTGW | R/W |
| controlsetpoint2 | Number:Temperature | Central heating 2 water setpoint set at boiler | R |
| controlsetpoint2requested | Number:Temperature | Central heating 2 water setpoint requested by Thermostat | R |
| controlsetpoint2override | Number:Temperature | Central heating 2 water setpoint configured on OTGW | R/W |
| dhwtemp | Number:Temperature | Domestic hot water temperature | R |
| tdhwset | Number:Temperature | Domestic hot water temperature setpoint | R |
| overridedhwsetpoint | Number:Temperature | Domestic hot water temperature setpoint override | R/W |
| flowtemp | Number:Temperature | Boiler water temperature | R |
| returntemp | Number:Temperature | Return water temperature | R |
| outsidetemp | Number:Temperature | Outside temperature | R/W |
| waterpressure | Number:Dimensionless | Central heating water pressure | R |
| ch_enable | Switch | Central heating enabled set at boiler | R |
| ch_enablerequested | Switch | Central heating enabled requested by thermostat | R |
| ch_enableoverride | Switch | Central heating enabled overridden at OTGW | R/W |
| ch2_enable | Switch | Central heating 2 enabled set at boiler | R |
| ch2_enablerequested | Switch | Central heating 2 enabled requested by thermostat | R |
| ch2_enableoverride | Switch | Central heating 2 enabled overridden at OTGW | R/W |
| ch_mode | Switch | Central heating active | R |
| dhw_enable | Switch | Domestic hot water enabled | R |
| dhw_mode | Switch | Domestic hot water active | R |
| flame | Switch | Burner active | R |
| modulevel | Number:Dimensionless | Relative modulation level | R |
| maxrelmdulevel | Number:Dimensionless | Maximum relative modulation level | R |
| fault | Switch | Fault indication | R |
| servicerequest | Switch | Service required | R |
| lockout-reset | Switch | Lockout-reset enabled | R |
| lowwaterpress | Switch | Low water pressure fault | R |
| gasflamefault | Switch | Gas or flame fault | R |
| airpressfault | Switch | Air pressure fault | R |
| waterovtemp | Switch | Water over-temperature fault | R |
| oemfaultcode | Number:Dimensionless | OEM fault code | R |

#### Example

```
Bridge openthermgateway:openthermgateway:1 "OpenTherm Gateway" [ ipaddress="192.168.1.100", port=8000, connectionRetryInterval=60 ] {
    Thing boiler remeha "Remeha Avanta"
}
```

### Ventilation / Heat Recovery

This thing represents a ventilation or heat recovery unit.

#### Configuration

The Ventilaton / Heat Recovery Thing supports a configurable number of channels for Transparent Slave Parameters and an option to send priority messages (PM=xx) at given intervals.

| Parameter           | Label             | Description                                         | Required | Default |
|---------------------|-------------------|-----------------------------------------------------|----------|---------|
| `numberOfTSPs`      | Number of TSPs    | The number of Transparent Slave Parameters (0-255)  | yes      |    75   |
| `sendPMIntervals`   | Send PM Intervals | Comma separated list of priority message (PM) id's with the interval at wich the PM message is sent to the OpenTherm Gataway           | no      |         |

The sendPMIntervals configuration setting uses the format "id=ss,id=ss,id=ss" where id is the message id of the priority message (PM) and ss is the interval in seconds at which the message is sent to the OpenTherm Gateway.

#### Channels

The Ventilation / Heat Recovery supports the following channels:

| Channel ID | Item Type | Description | Access |
|------------|-----------|-------------|--------|
| vh_ventilationenable | Switch | Ventilation enabled | R |
| vh_bypassposition | Number:Dimensionless | Bypass position | R |
| vh_bypassmode | Number:Dimensionless | Bypass mode | R |
| vh_freeventilationmode | Switch | Free ventilation mode | R |
| vh_faultindication | Switch | Fault indication | R |
| vh_ventilationmode | Switch | Ventilation mode | R |
| vh_bypassstatus | Switch | Bypass status | R |
| vh_bypassautomaticstatus | Number:Dimensionless | Bypass automatic status | R |
| vh_freeventilationstatus | Switch | Free ventilation status | R |
| vh_diagnosticindication | Switch | Diagnostic indication | R |
| vh_controlsetpoint | Number:Dimensionless | Control setpoint | R |
| vh_servicerequest | Switch | Service request | R |
| vh_exhaustfanfault | Switch | Exhaust fan fault | R |
| vh_inletfanfault | Switch | Inlet fan fault | R |
| vh_frostprotection | Switch | Frost protection | R |
| vh_faultcode | Number:Dimensionless | Fault code | R |
| vh_diagnosticcode | Number:Dimensionless | Diagnostic code | R |
| vh_systemtype | Number:Dimensionless | System type | R |
| vh_bypass | Switch | Bypass | R |
| vh_speedcontrol | Number:Dimensionless | Speed control | R |
| vh_memberid | Number:Dimensionless | Member ID | R |
| vh_openthermversion | Number:Dimensionless | OpenTherm version | R |
| vh_versiontype | Number:Dimensionless | Version type | R |
| vh_relativeventilation | Number:Dimensionless | Relative ventilation position | R |
| vh_relativehumidity | Number:Dimensionless | Relative humidity exhaust air | R |
| vh_co2level | Number:Dimensionless | CO2 level exhaust air | R |
| vh_supplyinlettemp | Number:Temperature | Supply inlet temperature | R |
| vh_supplyoutlettemp | Number:Temperature | Supply outlet temperature | R |
| vh_exhaustinlettemp | Number:Temperature | Exhaust inlet temperature | R |
| vh_exhaustoutlettemp | Number:Temperature | Exhaust outlet temperature | R |
| vh_actualexhaustfanspeed | Number:Dimensionless | Actual exhaust fan speed | R |
| vh_actualinletfanspeed | Number:Dimensionless | Actual inlet fan speed | R |
| vh_nominalventenable | Switch | Nominal ventilation value transfer enabled | R |
| vh_nominalventrw | Number:Dimensionless | Nominal ventilation value | R |
| vh_nominalventilationvalue | Number:Dimensionless | Nominal ventilation value | R |
| vh_tspnumber | Number:Dimensionless | Number of Transparent-Slave-Parameters | R |
| vh_ventilationsetpoint | Number:Dimensionless | Ventilation setpoint override | R/W |

#### Example

```
Bridge openthermgateway:openthermgateway:1 "OpenTherm Gateway" [ ipaddress="192.168.1.100", port=8000, connectionRetryInterval=60 ] {
    Thing ventilationheatrecovery brink "Brink Renovent" [ numberOfTSPs=73 sendPMIntervals="72=60,73=60,74=60,88=600" ]
}
```


### demo.items

```
Number:Temperature RoomTemperature "Room temperature [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:roomtemp" }
Number:Temperature RoomSetpoint "Room setpoint [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:roomsetpoint" }
Number:Temperature TemporaryRoomSetpointOverride "Temporary room setpoint override [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:temperaturetemporary" }
Number:Temperature ConstantRoomSetpointOverride "Constant room setpoint override [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:temperatureconstant" }
Number:Temperature ControlSetpoint "Control setpoint [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:controlsetpoint" }
Number:Temperature ControlSetpointRequested "Control setpoint requested [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:controlsetpointrequested" }
Number:Temperature ControlSetpointOverride "Control setpoint override [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:controlsetpointoverride" }
Number:Temperature ControlSetpoint2 "Control setpoint 2 [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:controlsetpoint2" }
Number:Temperature ControlSetpoint2Requested "Control setpoint 2 requested [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:controlsetpoint2requested" }
Number:Temperature ControlSetpoint2Override "Control setpoint 2 override [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:controlsetpoint2override" }
Number:Temperature DomesticHotWaterTemperature "Domestic hot water temperature [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:dhwtemp" }
Number:Temperature DomesticHotWaterSetpoint "Domestic hot water setpoint [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:tdhwset" }
Number:Temperature DomesticHotWaterSetpointOverride "Domestic hot water setpoint override [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:overridedhwsetpoint" }
Number:Temperature BoilerWaterTemperature "Boiler water temperature [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:flowtemp" }
Number:Temperature ReturnWaterTemperature "Return water temperature [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:returntemp" }
Number:Temperature OutsideTemperature "Outside temperature [%.1f °C]" <temperature> { channel="openthermgateway:otgw:1:outsidetemp" }
Number:Pressure CentralHeatingWaterPressure "Central heating water pressure [%.1f bar]" { channel="openthermgateway:otgw:1:waterpressure" }
Switch CentralHeatingEnabled "Central heating enabled" <switch> { channel="openthermgateway:otgw:1:ch_enable" }
Switch CentralHeatingEnabledRequested "Central heating enabled requested" <switch> { channel="openthermgateway:otgw:1:ch_enablerequested" }
Switch CentralHeatingEnabledOverride "Central heating enabled override" <switch> { channel="openthermgateway:otgw:1:ch_enableoverride" }
Switch CentralHeating2Enabled "Central heating 2 enabled" <switch> { channel="openthermgateway:otgw:1:ch2_enable" }
Switch CentralHeating2EnabledRequested "Central 2 heating enabled requested" <switch> { channel="openthermgateway:otgw:1:ch2_enablerequested" }
Switch CentralHeating2EnabledOverride "Central 2 heating enabled override" <switch> { channel="openthermgateway:otgw:1:ch2_enableoverride" }
Switch CentralHeatingActive "Central heating active" <switch> { channel="openthermgateway:otgw:1:ch_mode" }
Switch DomesticHotWaterEnabled "Domestic hot water enabled" <switch> { channel="openthermgateway:otgw:1:dhw_enable" }
Switch DomesticHotWaterActive "Domestic hot water active" <switch> { channel="openthermgateway:otgw:1:dhw_mode" }
Switch BurnerActive "Burner active" <switch> { channel="openthermgateway:otgw:1:flame" }
Number:Dimensionless RelativeModulationLevel "Relative modulation level [%.1f %%]" { channel="openthermgateway:otgw:1:modulevel" }
Number:Dimensionless MaximumRelativeModulationLevel "Maximum relative modulation level [%.1f %%]" { channel="openthermgateway:otgw:1:maxrelmdulevel" }
Switch Fault "Fault indication" <switch> { channel="openthermgateway:otgw:1:fault" }
Switch ServiceRequest "Service required" <switch> { channel="openthermgateway:otgw:1:servicerequest" }
Switch LockoutReset "Lockout-reset" <switch> { channel="openthermgateway:otgw:1:lockout-reset" }
Switch LowWaterPress "Low water pressure fault" <switch> { channel="openthermgateway:otgw:1:lowwaterpress" }
Switch GasFlameFault "Gas or flame fault" <switch> { channel="openthermgateway:otgw:1:gasflamefault" }
Switch AirPressFault "Air pressure fault" <switch> { channel="openthermgateway:otgw:1:airpressfault" }
Switch WaterOvTemp "Water over-temperature fault" <switch> { channel="openthermgateway:otgw:1:waterovtemp" }
Number OemFaultCode "OEM fault code" { channel="openthermgateway:otgw:1:oemfaultcode" }
Switch Diagnostics "Diagnostics indication" { channel="openthermgateway:otgw:1:diag" }
Text SendCommand "Send command channel" { channel="openthermgateway:otgw:1:sendcommand" }
```

### demo.sitemap

```
sitemap demo label="Main Menu" {
    Frame label="OpenTherm Gateway" {
        Text item="RoomTemperature" icon="temperature" label="Room temperature [%.1f °C]"
        Text item="RoomSetpoint" icon="temperature" label="Room setpoint [%.1f °C]"
        Setpoint item="TemporaryRoomSetpointOverride" icon="temperature" label="Temporary room setpoint override [%.1f °C]" minValue="0" maxValue="30" step="0.1"
        Setpoint item="ConstantRoomSetpointOverride" icon="temperature" label="Constant room setpoint override [%.1f °C]" minValue="0" maxValue="30" step="0.1"
        Text item="ControlSetpoint" icon="temperature" label="Control setpoint [%.1f °C]"
        Text item="ControlSetpointRequested" icon="temperature" label="Control setpoint requested [%.1f °C]"
        Setpoint item="ControlSetpointOverride" icon="temperature" label="Control setpoint override [%.1f °C]" minValue="0" maxValue="100" step="1"
        Text item="DomesticHotWaterTemperature" icon="temperature" label="Domestic hot water temperature [%.1f °C]"
        Text item="DomesticHotWaterSetpoint" icon="temperature" label="Domestic hot water setpoint [%.1f °C]"
        Setpoint item="DomesticHotWaterSetpointOverride" icon="temperature" label="Domestic hot water setpoint override [%.1f °C]" minValue="0" maxValue="100" step="0.1"
        Text item="BoilerWaterTemperature" icon="temperature" label="Boiler water temperature [%.1f °C]"
        Text item="ReturnWaterTemperature" icon="temperature" label="Return water temperature [%.1f °C]"
        Setpoint item="OutsideTemperature" icon="temperature" label="Outside temperature [%.1f °C]" minValue="-40" maxValue="100" step="0.1"
        Text item="CentralHeatingWaterPressure" icon="" label="Central heating water pressure [%.1f bar]"
        Switch item="CentralHeatingEnabled" icon="switch" label="Central heating enabled"
        Switch item="CentralHeatingEnabledRequested" icon="switch" label="Central heating enabled requested"
        Switch item="CentralHeatingEnabledOverride" icon="switch" label="Central heating enabled override"
        Switch item="CentralHeatingActive" icon="switch" label="Central heating active"
        Switch item="DomesticHotWaterEnabled" icon="switch" label="Domestic hot water enabled"
        Switch item="DomesticHotWaterActive" icon="switch" label="Domestic hot water active"
        Switch item="BurnerActive" icon="switch" label="Burner active"
        Text item="RelativeModulationLevel" icon="" label="Relative modulation level [%.1f %%]"
        Text item="MaximumRelativeModulationLevel" icon="" label="Maximum relative modulation level [%.1f %%]"        
        Switch item="Fault" icon="" label="Fault indication"
        Switch item="ServiceRequest" icon="" label="Service required"
        Switch item="LockoutReset" icon="" label="Lockout-reset"
        Switch item="LowWaterPress" icon="" label="Low water pressure fault"
        Switch item="GasFlameFault" icon="" label="Gas or flame fault"
        Switch item="AirPressFault" icon="" label="Air pressure fault"
        Switch item="waterOvTemp" icon="" label="Water over-temperature fault"
        Text item="OemFaultCode" icon="" label="OEM fault code"
        Switch item="Diagnostics" icon="" label="Diagnostics indication"
    }
}
```

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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Component;

import org.openhab.binding.openthermgateway.handler.GatewayHandler;
import org.openhab.binding.openthermgateway.handler.BoilerHandler;
import org.openhab.binding.openthermgateway.handler.VentilationHeatRecoveryHandler;

/**
 * The {@link OpenThermGatewayHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Arjen Korevaar - Initial contribution
 */
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.openthermgateway")
@NonNullByDefault
public class OpenThermGatewayHandlerFactory extends BaseThingHandlerFactory {

    private static final String BINDING_ID = "openthermgateway";

    private static final ThingTypeUID GATEWAY_THING_TYPE = new ThingTypeUID(BINDING_ID, "gateway");
    private static final ThingTypeUID BOILER_THING_TYPE = new ThingTypeUID(BINDING_ID, "boiler");
    private static final ThingTypeUID VENTILATION_HEAT_RECOVERY_THING_TYPE = new ThingTypeUID(BINDING_ID,
            "ventilationheatrecovery");

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return thingTypeUID.equals(GATEWAY_THING_TYPE) || thingTypeUID.equals(BOILER_THING_TYPE)
                || thingTypeUID.equals(VENTILATION_HEAT_RECOVERY_THING_TYPE);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(GATEWAY_THING_TYPE)) {
            return new GatewayHandler(thing);
        } else if (thingTypeUID.equals(BOILER_THING_TYPE)) {
            return new BoilerHandler(thing);
        } else if (thingTypeUID.equals(VENTILATION_HEAT_RECOVERY_THING_TYPE)) {
            return new VentilationHeatRecoveryHandler(thing);
        }

        return null;
    }
}

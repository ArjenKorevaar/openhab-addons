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
            OpenThermGatewayBindingConstants.CHANNEL_ROOM_TEMPERATURE,
            OpenThermGatewayBindingConstants.CHANNEL_ROOM_SETPOINT);
}

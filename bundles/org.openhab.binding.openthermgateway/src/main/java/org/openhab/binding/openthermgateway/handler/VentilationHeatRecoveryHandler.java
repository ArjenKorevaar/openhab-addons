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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.openthermgateway.internal.GatewayCommand;
import org.openhab.binding.openthermgateway.internal.VentilationHeatRecoveryConfiguration;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.binding.ThingHandlerCallback;
import org.openhab.core.thing.type.ChannelKind;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BaseDeviceHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Arjen Korevaar - Initial contribution
 */
@NonNullByDefault
public class VentilationHeatRecoveryHandler extends BaseDeviceHandler {

    private final Logger logger = LoggerFactory.getLogger(VentilationHeatRecoveryHandler.class);

    private @Nullable VentilationHeatRecoveryConfiguration config;
    private @Nullable ScheduledFuture<?> sendPMJob;
    private int sendPMJobTick = 0;

    private Map<Integer, List<Integer>> sendPMIntervals = new HashMap<>();

    public VentilationHeatRecoveryHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        super.initialize();

        config = getConfigAs(VentilationHeatRecoveryConfiguration.class);

        addTSPChannels();

        initializePM();
    }

    private void addTSPChannels() {
        @Nullable
        VentilationHeatRecoveryConfiguration conf = config;

        ThingHandlerCallback callback = getCallback();

        if (conf != null && callback != null) {
            ChannelTypeUID channelTypeUID = new ChannelTypeUID("openthermgateway", "vh_tspentry");

            List<Channel> channels = new ArrayList<>();

            channels.addAll(getThing().getChannels());

            for (int index = 0; index < conf.numberOfTSPs; index++) {
                String channelId = "vh_tspentry_" + index;
                if (thing.getChannel(channelId) == null) {
                    ChannelUID channelUID = new ChannelUID(thing.getUID(), channelId);

                    channels.add(callback.createChannelBuilder(channelUID, channelTypeUID).withKind(ChannelKind.STATE)
                            .withLabel("TSP Entry " + index).build());
                }
            }

            updateThing(editThing().withChannels(channels).build());
        }
    }

    private void initializePM() {

        if (config != null) {
            String[] parts = config.sendPMIntervals.split(",");

            for (String part : parts) {
                try {
                    String[] elements = part.split("=");
                    Integer messageId = Integer.parseInt(elements[0]);
                    Integer interval = Integer.parseInt(elements[1]);

                    if (!sendPMIntervals.containsKey(interval)) {
                        sendPMIntervals.put(interval, new ArrayList<Integer>());
                    }

                    sendPMIntervals.get(interval).add(messageId);
                } catch (NumberFormatException nfe) {
                    logger.debug("Unable to parse sendPMIntervals value {}", part);
                }
            }
        }

        if (sendPMIntervals.size() > 0) {
            logger.debug("Starting PM job");
            sendPMJob = scheduler.scheduleAtFixedRate(this::sendPM, 10, 1, TimeUnit.SECONDS);
        } else {
            logger.debug("No PM intervals specified, skipping starting PM job");
        }
    }

    public void sendPM() {
        Bridge bridge = getBridge();

        if (bridge != null) {
            OpenThermGatewayHandler handler = (OpenThermGatewayHandler) bridge.getHandler();
            if (handler != null) {
                for (Integer interval : sendPMIntervals.keySet()) {
                    if (sendPMJobTick >= interval && sendPMJobTick % interval == 0) {
                        for (Integer messageid : sendPMIntervals.get(interval)) {
                            GatewayCommand gatewayCommand = GatewayCommand.parse("PM", String.valueOf(messageid));
                            handler.sendCommand(gatewayCommand);
                        }
                    }
                }
            }
        }

        sendPMJobTick++;

        if (sendPMJobTick > 1800) {
            // Reset sendPMJobTick every 30 minutes
            sendPMJobTick = 0;
        }
    }

    @Override
    public void dispose() {
        ScheduledFuture<?> job = sendPMJob;
        if (job != null) {
            job.cancel(true);
            sendPMJob = null;
        }

        super.dispose();
    }
}

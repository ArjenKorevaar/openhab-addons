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
import org.openhab.binding.openthermgateway.internal.DataItemGroup;
import org.openhab.binding.openthermgateway.internal.Message;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingStatusInfo;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
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
public abstract class BaseDeviceHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(BaseDeviceHandler.class);

    public BaseDeviceHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        Bridge bridge = getBridge();

        if (bridge != null) {
            bridgeStatusChanged(bridge.getStatusInfo());
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Bridge is missing");
        }
    }

    @Override
    public void bridgeStatusChanged(ThingStatusInfo bridgeStatusInfo) {
        if (bridgeStatusInfo.getStatus() == ThingStatus.ONLINE) {
            updateStatus(ThingStatus.ONLINE);
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        Bridge bridge = getBridge();

        if (bridge != null) {
            OpenThermGatewayHandler handler = (OpenThermGatewayHandler) bridge.getHandler();
            if (handler != null) {
                handler.handleCommand(channelUID, command);
            }
        } else {
            logger.debug("Bridge is missing");
        }
    }

    public void receiveMessage(Message message) {
        DataItem[] dataItems = DataItemGroup.dataItemGroups.get(message.getID());

        for (DataItem dataItem : dataItems) {
            String channelId = dataItem.getSubject();

            if (!supportsChannelId(channelId)) {
                continue;
            }

            if (dataItem.getFilteredCode() != null && dataItem.getFilteredCode() != message.getCode()) {
                continue;
            }

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
    }

    public boolean supportsChannelId(String channelId) {
        // Overridden by Thing handlers
        return false;
    }
}

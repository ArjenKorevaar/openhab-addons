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
package org.openhab.binding.raspberrypipwm.internal;

import static org.openhab.binding.raspberrypipwm.internal.RaspberryPiPWMBindingConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.*;

/**
 * The {@link RaspberryPiPWMHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Arjen Korevaar - Initial contribution
 */
@NonNullByDefault
public class HardwarePWMHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(HardwarePWMHandler.class);

    private @Nullable HardwarePWMConfiguration config;

    private @Nullable GpioController gpio;

    private @Nullable Pin pin;

    private @Nullable GpioPinPwmOutput pwm;

    public HardwarePWMHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        config = getConfigAs(HardwarePWMConfiguration.class);

        gpio = GpioFactory.getInstance();

        pin = RaspiPin.getPinByAddress(config.gpio);

        pwm = gpio.provisionPwmOutputPin(pin);

        com.pi4j.wiringpi.Gpio.pwmSetMode(config.mode);
        com.pi4j.wiringpi.Gpio.pwmSetRange(config.range);
        com.pi4j.wiringpi.Gpio.pwmSetClock(config.clock);

        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (CHANNEL_WPM.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                int value = pwm.getPwm();
                updateState(channelUID.getId(), new DecimalType(value));
            } else {
                int value = Integer.parseInt(command.toFullString());
                pwm.setPwm(value);
            }
        }
    }
}

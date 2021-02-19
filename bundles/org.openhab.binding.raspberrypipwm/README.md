# RaspberryPi PWM Binding

This binding can be used to control PWM on the RaspberryPi GPIO pins directly from openHAB.
It can be used to control motors, fans, dim LED's etc.

## Supported Things

This binding supports two things: hardware PWM and software PWM pins.

The hardware PWM is based on an internal 19.2 MHz clock.
This frequency, divided by the specified Clock divider value, is the frequency at which the PWM counter is incremented.
When the counter reaches the value specified by Range, it resets to zero.
While the counter is less than the specified duty cycle, the output is high, otherwise the output is low.

The software PWM produces a software generated PWM signal.
To maintain a low CPU usage, the minimum pulse width is 100μS.
That combined with the default suggested Range of 100 gives a PWM frequency of 100Hz.
You can lower the range to get a higher frequency, at the expense of resolution, or increase to get more resolution, but that will lower the frequency.

## Discovery

This binding does not support auto-discovery.

## Binding Configuration

The binding itself does not need any configuration, however openHAB must be run with root privileges to allow access to the GPIO pins through the [PI4J library](https://pi4j.com/).
Before you run openHAB as root, please make sure you understand the risks and implications.
To run openHAB as root (assuming you use systemd):

- edit the file /usr/lib/systemd/system/openhab2.service and change to User and Group to root
- reload the daemon settings with systemctl daemon-reload
- restart openHAB with systemctl restart openhab2

## Thing Configuration

### Hardware PWM pin

| Parameter                 | Name                      | Description                                    | Required | Default |
|---------------------------|---------------------------|------------------------------------------------|----------|---------|
| `gpio`                    | GPIO                      | The GPIO pin number (WiringPi scheme)          | yes      | 1       |
| `mode`                    | PWM Mode                  | Mark:space (0) or Balanced (1)                 | yes      | 0       |
| `clock`                   | Clock                     | Clock divider                                  | yes      |         |
| `range`                   | Range                     | Duty cycle range                               | yes      |         |

### Software PWM pin

| Parameter                 | Name                      | Description                                    | Required | Default |
|---------------------------|---------------------------|------------------------------------------------|----------|---------|
| `gpio`                    | GPIO                      | The GPIO pin number (WiringPi scheme)          | yes      | 0       |
| `range`                   | Range                     | Duty cycle range                               | yes      | 100     |

## Channels

Both the hardware and software PWM things have one channel:

| Channel       | Item Type | Description             | Read/Write |
|---------------|-----------|-------------------------|------------|
| `pwm`         | Number    | PWM rate                | r/w        |

## Full Example

### pwm.things

```
Thing raspberrypipwm:hardwarepwm:1 [ gpio=26, mode=0, clock=768, range=100 ]
Thing raspberrypipwm:softwarepwm:1 [ gpio=1, range=100 ]
```

### pwm.items

```
Number HardwarePWM_Rate "Hardware PWM Rate" { channel="raspberrypipwm:hardwarepwm:1:pwm" }
Number SoftwarePWM_Rate "Software PWM Rate" { channel="raspberrypipwm:softwarepwm:1:pwm" }
```

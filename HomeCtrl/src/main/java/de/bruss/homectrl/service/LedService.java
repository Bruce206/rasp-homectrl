package de.bruss.homectrl.service;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

@Service
public class LedService {
	final GpioController gpio = GpioFactory.getInstance();

	HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>> pins = new HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>>();

	static final public Integer MIN_INTENSITY = 0; // don't change!
	static final public Integer MAX_INTENSITY = 100;

	public enum Color {
		RED, GREEN, BLUE
	}

	@PostConstruct
	private void init() {

		HashMap<Color, GpioPinDigitalOutput> colors = new HashMap<Color, GpioPinDigitalOutput>();
		colors.put(Color.RED, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Strip 1 RED", PinState.LOW));
		colors.put(Color.GREEN, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Strip 1 GREEN", PinState.LOW));
		colors.put(Color.BLUE, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Strip 1 BLUE", PinState.LOW));
		pins.put(1, colors);

		HashMap<Color, GpioPinDigitalOutput> colors2 = new HashMap<Color, GpioPinDigitalOutput>();
		colors.put(Color.RED, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Strip 2 RED", PinState.LOW));
		colors.put(Color.GREEN, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Strip 2 GREEN", PinState.LOW));
		colors.put(Color.BLUE, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Strip 2 BLUE", PinState.LOW));
		pins.put(2, colors2);
	}

	public String testRed() {
		pins.get(1).get(Color.RED).setState(PinState.HIGH);
		return "Done";
	}

	public String stopRed() {
		pins.get(1).get(Color.RED).setState(PinState.LOW);
		return "Done";
	}

	private GpioPinDigitalOutput getPinForStripAndColor(Integer stripNo, Color color) {
		try {
			return pins.get(stripNo).get(color);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	public void setColorIntensity(Integer stripNo, Color color, Integer intensity) throws InterruptedException {
		Preconditions.checkArgument(intensity >= MIN_INTENSITY && intensity <= MAX_INTENSITY, "Intensity not in range!");

		GpioPinDigitalOutput pin = getPinForStripAndColor(stripNo, color);
		pwm(pin, intensity);
	}

	private void pwm(GpioPinDigitalOutput pin, Integer intensity) throws InterruptedException {
		Gpio.wiringPiSetup();

		SoftPwm.softPwmCreate(pin.getPin().getAddress(), 0, MAX_INTENSITY);
		SoftPwm.softPwmWrite(pin.getPin().getAddress(), intensity);
	}
}

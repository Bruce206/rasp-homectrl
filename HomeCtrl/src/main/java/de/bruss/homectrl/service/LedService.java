package de.bruss.homectrl.service;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

@Service
public class LedService {
	final GpioController gpio = GpioFactory.getInstance();

	HashMap<Integer, HashMap<Color, Pin>> pins = new HashMap<Integer, HashMap<Color, Pin>>();

	static final public Integer MIN_INTENSITY = 0; // don't change!
	static final public Integer MAX_INTENSITY = 100;

	public enum Color {
		RED, GREEN, BLUE
	}

	@PostConstruct
	private void init() {

		HashMap<Color, Pin> colors = new HashMap<Color, Pin>();
		colors.put(Color.RED, RaspiPin.GPIO_02);
		colors.put(Color.GREEN, RaspiPin.GPIO_03);
		colors.put(Color.BLUE, RaspiPin.GPIO_04);
		pins.put(1, colors);

		HashMap<Color, Pin> colors2 = new HashMap<Color, Pin>();
		colors.put(Color.RED, RaspiPin.GPIO_05);
		colors.put(Color.GREEN, RaspiPin.GPIO_06);
		colors.put(Color.BLUE, RaspiPin.GPIO_07);
		pins.put(1, colors2);
	}

	private Pin getPinForStripAndColor(Integer stripNo, Color color) {
		try {
			return pins.get(stripNo).get(color);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	public void setColorIntensity(Integer stripNo, Color color, Integer intensity) throws InterruptedException {
		Preconditions.checkArgument(intensity > MIN_INTENSITY && intensity < MAX_INTENSITY, "Intensity not in range!");

		Pin pin = getPinForStripAndColor(stripNo, color);
		pwm(pin, intensity);
	}

	private void pwm(Pin pin, Integer intensity) throws InterruptedException {
		Gpio.wiringPiSetup();
		SoftPwm.softPwmCreate(pin.getAddress(), 0, MAX_INTENSITY);
		SoftPwm.softPwmWrite(pin.getAddress(), intensity);
	}
}

package de.bruss.homectrl.service;

import java.util.HashMap;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class LedService {
	protected HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>> pins = new HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>>();

	static final public Integer MIN_INTENSITY = 0; // don't change!
	static final public Integer MAX_INTENSITY = 100;

	public enum Color {
		RED, GREEN, BLUE
	}

	public HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>> getPins() {
		return pins;
	}

	public void setPins(HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>> pins) {
		this.pins = pins;
	}

	public static Integer getMinIntensity() {
		return MIN_INTENSITY;
	}

	public static Integer getMaxIntensity() {
		return MAX_INTENSITY;
	}

	public void setColorIntensity(int strip, Color valueOf, Integer intensity) {
		// TODO Auto-generated method stub

	}

	public void testRed() {
		// TODO Auto-generated method stub

	}

	public void stopRed() {
		// TODO Auto-generated method stub

	}

}

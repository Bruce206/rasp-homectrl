package de.bruss.homectrl.service;

public abstract class LedService {

	public enum Color {
		RED, GREEN, BLUE
	}

	public abstract void testRed();

	public abstract void stopRed();

	public abstract void setColorIntensity(Integer stripNo, Color color, Integer intensity) throws InterruptedException;

	public abstract String getColorsRGBForStripe(int stripe);

	public abstract void setColorsRGBForStripe(int stripe, String rgb);

}

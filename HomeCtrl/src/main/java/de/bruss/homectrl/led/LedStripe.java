package de.bruss.homectrl.led;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class LedStripe {

	public enum Color {
		RED, GREEN, BLUE
	}

	private List<StripeColor> colors = new ArrayList<StripeColor>();
	private int id;

	public LedStripe(int id) {
		this.setId(id);
		this.colors.add(new StripeColor(Color.RED, 0));
		this.colors.add(new StripeColor(Color.GREEN, 0));
		this.colors.add(new StripeColor(Color.BLUE, 0));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<StripeColor> getColors() {
		return colors;
	}

	public StripeColor getColor(Color color) {
		for (StripeColor stripeColor : colors) {
			if (stripeColor.getColor().equals(color)) {
				return stripeColor;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "LedStripe [colors=" + colors + ", id=" + id + "]";
	}

	public class StripeColor {
		private Color color;
		private int Intensity;
		private GpioPinDigitalOutput pin;

		public StripeColor(Color color, int intensity) {
			super();
			this.color = color;
			Intensity = intensity;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public int getIntensity() {
			return Intensity;
		}

		public void setIntensity(int intensity) {
			Intensity = intensity;
		}

		public GpioPinDigitalOutput getPin() {
			return pin;
		}

		public void setPin(GpioPinDigitalOutput pin) {
			this.pin = pin;
		}

		@Override
		public String toString() {
			return "StripeColor [color=" + color + ", Intensity=" + Intensity + ", pin=" + pin + "]";
		}

	}

}

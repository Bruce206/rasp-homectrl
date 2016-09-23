package de.bruss.homectrl.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import de.bruss.homectrl.led.LedStripe;
import de.bruss.homectrl.led.LedStripe.Color;

public abstract class LedService {

	List<LedStripe> stripes = new ArrayList<LedStripe>();

	@PostConstruct
	protected abstract void init();

	public abstract void setColorIntensity(LedStripe stripNo, Color color, Integer intensity) throws InterruptedException;

	public abstract String getColorsRGBForStripe(int stripe);

	public abstract void setColorsRGBForStripe(int stripe, String rgb);

	protected void initStripes() {
		stripes.add(new LedStripe(1));
		stripes.add(new LedStripe(2));
	}

	protected LedStripe getStripeById(int id) {
		for (LedStripe stripe : stripes) {
			if (stripe.getId() == id) {
				return stripe;
			}
		}
		return null;
	}

	public void setColorsRGBForStripe(int stripeId, int redIntensity, int greenIntensity, int blueIntensity) throws InterruptedException {
		LedStripe stripe = getStripeById(stripeId);
		setColorIntensity(stripe, Color.RED, redIntensity);
		setColorIntensity(stripe, Color.GREEN, greenIntensity);
		setColorIntensity(stripe, Color.BLUE, blueIntensity);
	}
}

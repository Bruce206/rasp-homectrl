package de.bruss.homectrl.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import de.bruss.homectrl.led.LedStripe;
import de.bruss.homectrl.led.LedStripe.Color;
import de.bruss.homectrl.led.LedStripe.StripeColor;

@Service
@Profile("PC")
public class DummyLedService extends LedService {

	@Override
	public void setColorIntensity(LedStripe stripe, Color color, Integer intensity) throws InterruptedException {

		StripeColor stripeColor = stripe.getColor(color);
		stripeColor.setIntensity(intensity);
	}

	@Override
	public String getColorsRGBForStripe(int stripeId) {
		LedStripe stripe = getStripeById(stripeId);
		System.out.println(stripe);
		return "rgb(" + stripe.getColor(Color.RED).getIntensity() + ", " + stripe.getColor(Color.GREEN).getIntensity() + ", " + stripe.getColor(Color.BLUE).getIntensity() + ")";
	}

	@Override
	public void setColorsRGBForStripe(int stripeId, String rgb) {

		Pattern regexPattern = Pattern.compile("(\\d{1,3})\\,.(\\d{1,3})\\,.(\\d{1,3})", Pattern.CASE_INSENSITIVE);
		Matcher regexMatcher = regexPattern.matcher(rgb);

		while (regexMatcher.find()) {
			Integer redIntensity = Integer.parseInt(regexMatcher.group(1));
			Integer greenIntensity = Integer.parseInt(regexMatcher.group(2));
			Integer blueIntensity = Integer.parseInt(regexMatcher.group(3));

			try {
				LedStripe stripe = getStripeById(stripeId);
				setColorIntensity(stripe, Color.RED, redIntensity);
				setColorIntensity(stripe, Color.GREEN, greenIntensity);
				setColorIntensity(stripe, Color.BLUE, blueIntensity);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void init() {
		initStripes();
	}

}

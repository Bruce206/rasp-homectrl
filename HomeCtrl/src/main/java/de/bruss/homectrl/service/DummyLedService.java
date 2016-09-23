package de.bruss.homectrl.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import de.bruss.homectrl.led.LedStripe;
import de.bruss.homectrl.led.LedStripe.Color;
import de.bruss.homectrl.led.LedStripe.StripeColor;

@Service
@Profile("PC")
public class DummyLedService extends LedService {

	Logger logger = LoggerFactory.getLogger(DummyLedService.class);

	@Override
	public void setColorIntensity(LedStripe stripe, Color color, Integer intensity) throws InterruptedException {
		StripeColor stripeColor = stripe.getColor(color);
		stripeColor.setIntensity(intensity);
		logger.debug("Color " + color.name() + " set for stripe " + stripe.getId() + ": " + intensity);
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
				setColorsRGBForStripe(stripeId, redIntensity, greenIntensity, blueIntensity);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void init() {
		initStripes();
	}

}

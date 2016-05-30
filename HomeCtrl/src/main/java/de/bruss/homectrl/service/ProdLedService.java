package de.bruss.homectrl.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.SoftPwm;

import de.bruss.homectrl.led.LedStripe;
import de.bruss.homectrl.led.LedStripe.Color;
import de.bruss.homectrl.led.LedStripe.StripeColor;

@Service
@Profile("Raspberry")
public class ProdLedService extends LedService {

	Logger logger = LoggerFactory.getLogger(ProdLedService.class);
	ObjectMapper mapper = new ObjectMapper();

	final GpioController gpio = GpioFactory.getInstance();

	// private boolean wiringPiInitialized = false;

	@PostConstruct
	protected void init() {
		System.out.println("Initializing ProdLedService...");
		// if (!wiringPiInitialized) {
		// wiringPiInitialized = true;
		// Gpio.wiringPiSetup();
		// }

		initStripes();

		getStripeById(1).getColor(Color.RED).setPin(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Strip 1 RED", PinState.LOW));
		getStripeById(1).getColor(Color.GREEN).setPin(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Strip 1 GREEN", PinState.LOW));
		getStripeById(1).getColor(Color.BLUE).setPin(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Strip 1 BLUE", PinState.LOW));

		getStripeById(2).getColor(Color.RED).setPin(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Strip 2 RED", PinState.LOW));
		getStripeById(2).getColor(Color.GREEN).setPin(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Strip 2 GREEN", PinState.LOW));
		getStripeById(2).getColor(Color.BLUE).setPin(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Strip 2 BLUE", PinState.LOW));
	}

	@Override
	public void setColorIntensity(LedStripe stripe, Color color, Integer intensity) throws InterruptedException {
		StripeColor stripeColor = stripe.getColor(color);

		SoftPwm.softPwmCreate(stripeColor.getPin().getPin().getAddress(), 0, 100);

		double intensityPercent = 100 * ((double) intensity / 255);
		logger.error("intensity: " + intensity + " percent: " + intensityPercent);

		SoftPwm.softPwmWrite(stripeColor.getPin().getPin().getAddress(), (int) Math.round(intensityPercent));

		stripeColor.setIntensity(intensity);
	}

	@Override
	public String getColorsRGBForStripe(int stripeId) {
		LedStripe stripe = getStripeById(stripeId);
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

}

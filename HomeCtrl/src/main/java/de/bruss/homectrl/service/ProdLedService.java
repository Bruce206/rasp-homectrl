package de.bruss.homectrl.service;

import java.util.HashMap;
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
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

@Service
@Profile("Raspberry")
public class ProdLedService extends LedService {

	Logger logger = LoggerFactory.getLogger(ProdLedService.class);
	ObjectMapper mapper = new ObjectMapper();

	final GpioController gpio = GpioFactory.getInstance();

	protected HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>> pins = new HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>>();

	private HashMap<Integer, Integer> pinIdToIntensityMapping = new HashMap<Integer, Integer>();

	@PostConstruct
	private void init() {

		for (int i = 2; i <= 7; i++) {
			pinIdToIntensityMapping.put(i, 0);
		}

		HashMap<Color, GpioPinDigitalOutput> colors = new HashMap<Color, GpioPinDigitalOutput>();
		colors.put(Color.RED, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Strip 1 RED", PinState.LOW));
		colors.put(Color.GREEN, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Strip 1 GREEN", PinState.HIGH));
		colors.put(Color.BLUE, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Strip 1 BLUE", PinState.LOW));
		pins.put(1, colors);

		HashMap<Color, GpioPinDigitalOutput> colors2 = new HashMap<Color, GpioPinDigitalOutput>();
		colors2.put(Color.RED, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Strip 2 RED", PinState.LOW));
		colors2.put(Color.GREEN, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Strip 2 GREEN", PinState.LOW));
		colors2.put(Color.BLUE, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Strip 2 BLUE", PinState.LOW));
		pins.put(2, colors2);
	}

	public HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>> getPins() {
		return pins;
	}

	public void setPins(HashMap<Integer, HashMap<Color, GpioPinDigitalOutput>> pins) {
		this.pins = pins;
	}

	public void testRed() {
		logger.error("activating red");
		pins.get(1).get(Color.RED).setState(PinState.HIGH);
	}

	public void stopRed() {
		((GpioPinDigitalOutput) pins.get(1).get(Color.RED)).setState(PinState.LOW);
	}

	private GpioPinDigitalOutput getPinForStripAndColor(Integer stripNo, Color color) {
		try {
			return pins.get(stripNo).get(color);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	@Override
	public void setColorIntensity(Integer stripNo, Color color, Integer intensity) throws InterruptedException {
		GpioPinDigitalOutput pin = getPinForStripAndColor(stripNo, color);
		pwm(pin, intensity);
	}

	private void pwm(GpioPinDigitalOutput pin, Integer intensity) throws InterruptedException {
		Gpio.wiringPiSetup();

		SoftPwm.softPwmCreate(pin.getPin().getAddress(), 0, 100);

		double intensityPercent = 100 * ((double) intensity / 255);
		logger.error("intensity: " + intensity + " percent: " + intensityPercent);

		SoftPwm.softPwmWrite(pin.getPin().getAddress(), (int) Math.round(intensityPercent));

		pinIdToIntensityMapping.put(pin.getPin().getAddress(), intensity);
	}

	@Override
	public String getColorsRGBForStripe(int stripeId) {
		System.out.println(pins.get(stripeId).get(Color.RED).getProperties());
		HashMap<Color, GpioPinDigitalOutput> stripe = pins.get(stripeId);
		return "rgb(" + stripe.get(Color.RED).getPin().getAddress() + ", " + stripe.get(Color.GREEN).getPin().getAddress() + ", " + stripe.get(Color.BLUE).getPin().getAddress() + ")";
	}

	@Override
	public void setColorsRGBForStripe(int stripe, String rgb) {

		Pattern regexPattern = Pattern.compile("(\\d{1,3})\\,.(\\d{1,3})\\,.(\\d{1,3})", Pattern.CASE_INSENSITIVE);
		Matcher regexMatcher = regexPattern.matcher(rgb);

		while (regexMatcher.find()) {
			Integer redIntensity = Integer.parseInt(regexMatcher.group(1));
			Integer greenIntensity = Integer.parseInt(regexMatcher.group(2));
			Integer blueIntensity = Integer.parseInt(regexMatcher.group(3));

			try {
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

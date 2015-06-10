package de.bruss.homectrl.service;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

import de.bruss.homectrl.service.LedService.Color;

@Service
@Profile("Raspberry")
public class ProdLedService extends LedService {
	ObjectMapper mapper = new ObjectMapper();

	final GpioController gpio = GpioFactory.getInstance();

	@PostConstruct
	private void init() {

		HashMap<Color, GpioPinOutput> colors = new HashMap<Color, GpioPinOutput>();
		colors.put(Color.RED, gpio.provisionPwmOutputPin(RaspiPin.GPIO_02));
		colors.put(Color.GREEN, gpio.provisionPwmOutputPin(RaspiPin.GPIO_03));
		colors.put(Color.BLUE, gpio.provisionPwmOutputPin(RaspiPin.GPIO_04));
		pins.put(1, colors);

		HashMap<Color, GpioPinOutput> colors2 = new HashMap<Color, GpioPinOutput>();
		colors.put(Color.RED, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Strip 2 RED", PinState.LOW));
		colors.put(Color.GREEN, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Strip 2 GREEN", PinState.LOW));
		colors.put(Color.BLUE, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Strip 2 BLUE", PinState.LOW));
		pins.put(2, colors2);
	}

	public void testRed() {
		((GpioPinDigitalOutput) pins.get(1).get(Color.RED)).setState(PinState.HIGH);
	}

	public void stopRed() {
		((GpioPinDigitalOutput) pins.get(1).get(Color.RED)).setState(PinState.LOW);
	}

	private GpioPinOutput getPinForStripAndColor(Integer stripNo, Color color) {
		try {
			return pins.get(stripNo).get(color);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	public void setColorIntensity(Integer stripNo, Color color, Integer intensity) throws InterruptedException {
		GpioPinOutput pin = getPinForStripAndColor(stripNo, color);
		pwm(pin, intensity);
	}

	private void pwm(GpioPinOutput pin, Integer intensity) throws InterruptedException {
		Gpio.wiringPiSetup();

		SoftPwm.softPwmCreate(pin.getPin().getAddress(), 0, MAX_INTENSITY);
		SoftPwm.softPwmWrite(pin.getPin().getAddress(), intensity);
	}

	public ObjectNode getJson() {
		ObjectNode info = mapper.createObjectNode();
		ArrayNode stripes = info.putArray("stripes");
		
		HashMap<Integer, HashMap<Color, GpioPinOutput>> pins = getPins();

		int stripCounter = 1;
		
		for (HashMap<Color, GpioPinOutput> map : pins.values()) {
			ObjectNode strip = mapper.createObjectNode();
			strip.put("no", stripCounter++);
			ArrayNode colors = strip.putArray("colors");
			
			for (Entry<Color, GpioPinOutput> entry2 : map) {
				ObjectNode color = mapper.createObjectNode();
				color.put("color", entry2.getKey().toString());
				
				GpioPinDigitalOutput pinOut = entry2.getValue();
				color.put("state", pinOut.getState().getValue());
				
				color.put("state", pinOut.);
				colors.add(color);
			}

			stripes.add(strip);
		}
		return info;
	}
}

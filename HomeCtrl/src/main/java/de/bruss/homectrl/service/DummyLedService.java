package de.bruss.homectrl.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("PC")
public class DummyLedService extends LedService {

	public enum Color {
		RED, GREEN, BLUE
	}

	@Override
	public void testRed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopRed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorIntensity(Integer stripNo, de.bruss.homectrl.service.LedService.Color color, Integer intensity) throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getColorsRGBForStripe(int stripe) {
		return null;
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorsRGBForStripe(int stripe, String rgb) {
		// TODO Auto-generated method stub

	}

}

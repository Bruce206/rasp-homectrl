package de.bruss.homectrl.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("PC")
public class DummyLedService extends LedService {

	public enum Color {
		RED, GREEN, BLUE
	}

}

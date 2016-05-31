package de.bruss.homectrl.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;

@Service
public class TemperatureService {
	W1Master master;

	@PostConstruct
	private void init() {
		master = new W1Master();
	}

	public Double getTemperatures() {
		List<W1Device> w1Devices = master.getDevices(TmpDS18B20DeviceType.FAMILY_CODE);
		for (W1Device device : w1Devices) {
			// this line is enought if you want to read the temperature
			System.out.println("Temperature: " + ((TemperatureSensor) device).getTemperature());
			// returns the temperature as double rounded to one decimal place after the point

			try {
				System.out.println("1-Wire ID: " + device.getId() + " value: " + device.getValue());
				return ((TemperatureSensor) device).getTemperature();
				// returns the ID of the Sensor and the full text of the virtual file
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public double getTemperature(String deviceId) {
		List<W1Device> w1Devices = master.getDevices(TmpDS18B20DeviceType.FAMILY_CODE);
		for (W1Device device : w1Devices) {
			if (device.getId().contains(deviceId)) {
				return ((TemperatureSensor) device).getTemperature();
			}
		}
		return 0;
	}
}

package de.bruss.homectrl.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class RCService {

	public void switchPlug(String groupAddress, int plugAddress, boolean targetState) throws IOException {
		String command = "/homectrl/433Utils-master/RPi_utils/send" + " " + groupAddress + " " + plugAddress + " " + (targetState ? 1 : 0);
		System.out.println(command);
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
		pb.start();
	}
}

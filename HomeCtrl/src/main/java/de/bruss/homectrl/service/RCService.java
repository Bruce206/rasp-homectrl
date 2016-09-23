package de.bruss.homectrl.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class RCService {

	private static Map<String, Boolean> states = new HashMap<String, Boolean>();

	public void switchPlug(String groupAddress, int plugAddress, boolean targetState) throws IOException {
		String command = "/homectrl/433Utils-master/RPi_utils/send" + " " + groupAddress + " " + plugAddress + " " + (targetState ? 1 : 0);
		System.out.println(command);
		states.put(groupAddress + "-" + plugAddress, targetState);
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
		pb.start();
	}

	public boolean switchStatus(String groupAddress, int plugAddress) {
		return states.get(groupAddress + "-" + plugAddress) != null ? states.get(groupAddress + "-" + plugAddress) : false;
	}
}

package de.bruss.homectrl.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bruss.homectrl.service.RCService;

@RestController
@RequestMapping("/rc")
public class WirelessController {

	@Autowired
	RCService rcService;

	ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/switch")
	public void switchPlug(@RequestParam String groupAddress, @RequestParam int plugAddress, @RequestParam boolean targetState) throws IOException {
		rcService.switchPlug(groupAddress, plugAddress, targetState);
	}

	@RequestMapping(value = "/switch/status")
	public String switchPlug(@RequestParam String groupAddress, @RequestParam int plugAddress) throws IOException {
		return mapper.createObjectNode().put("status", rcService.switchStatus(groupAddress, plugAddress)).toString();
	}

	@RequestMapping(value = "/teston")
	public void testPlugON() throws IOException {
		// Sun's ProcessBuilder and Process example
		String command = "/homectrl/433Utils-master/RPi_utils/send" + " " + "10101" + " " + "1" + " " + "1";
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
		pb.start();
	}

	@RequestMapping(value = "/testoff")
	public void testPlugOFF() throws IOException {
		String command = "/homectrl/433Utils-master/RPi_utils/send" + " " + "10101" + " " + "1" + " " + "0";
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
		pb.start();
	}

}

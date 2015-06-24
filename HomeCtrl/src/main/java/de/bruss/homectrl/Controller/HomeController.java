package de.bruss.homectrl.Controller;

import java.util.BitSet;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pi4j.io.gpio.RaspiPin;

import de.pi3g.pi.rcswitch.RCSwitch;

@RestController
@RequestMapping(value = "/api")
public class HomeController {

	@RequestMapping(method = RequestMethod.GET)
	public void get() throws InterruptedException {
		// our switching group address is 01011 (marked with 1 to 5 on the DIP
		// switch
		// on the switching unit itself)
		BitSet address = RCSwitch.getSwitchGroupAddress("01011");

		RCSwitch transmitter = new RCSwitch(RaspiPin.GPIO_00);
		transmitter.switchOn(address, 1); // switches the switch unit A (A = 1, B = 2, ...) on
		Thread.sleep(5000); // wait 5 sec.
		transmitter.switchOff(address, 1); // switches the switch unit A off
	}

}

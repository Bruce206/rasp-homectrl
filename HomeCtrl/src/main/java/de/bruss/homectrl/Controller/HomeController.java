package de.bruss.homectrl.Controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.bruss.homectrl.service.ImagesService;
import de.bruss.homectrl.service.TemperatureService;

@RestController
@RequestMapping(value = "/api")
public class HomeController {

	@Autowired
	ImagesService imagesService;

	@Autowired
	TemperatureService temperatureService;

	ObjectMapper mapper = new ObjectMapper();

	public static String[] images = new String[] { "http://onehungrymind.com/demos/slideshow/images/image02.jpg", "http://onehungrymind.com/demos/slideshow/images/image03.jpg", "http://onehungrymind.com/demos/slideshow/images/image04.jpg" };

	@RequestMapping(value = "/images", method = RequestMethod.GET)
	public String getRandomImage() {
		ObjectNode image = mapper.createObjectNode();
		image.put("url", imagesService.getRandomImage().toString());
		return image.toString();
	}

	@RequestMapping(value = "/temp", method = RequestMethod.GET)
	public String getTemps() {
		ObjectNode temps = mapper.createObjectNode();
		temps.put("values", temperatureService.getTemperatures());
		return temps.toString();
	}

	@RequestMapping(value = "/temp/{id}", method = RequestMethod.GET)
	public String getTemps(@PathVariable String id) {
		ObjectNode temps = mapper.createObjectNode();
		temps.put("value", temperatureService.getTemperature(id));
		return temps.toString();
	}
	// RCSwitch transmitter = new RCSwitch(RaspiPin.GPIO_00);
	//
	// @RequestMapping(method = RequestMethod.GET)
	// public void get() throws InterruptedException {
	// // our switching group address is 01011 (marked with 1 to 5 on the DIP
	// // switch
	// // on the switching unit itself)
	// BitSet address = RCSwitch.getSwitchGroupAddress("01011");
	//
	// transmitter.switchOn(address, 1); // switches the switch unit A (A = 1, B = 2, ...) on
	// Thread.sleep(5000); // wait 5 sec.
	// transmitter.switchOff(address, 1); // switches the switch unit A off
	// }

}

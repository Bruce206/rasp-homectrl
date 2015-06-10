package de.bruss.homectrl.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.bruss.homectrl.service.LedService;
import de.bruss.homectrl.service.LedService.Color;

@RestController
@RequestMapping(value = "/led")
public class LedController {

	@Autowired
	private LedService ledService;

	@RequestMapping(value = "/{strip}/{color}/{intensity}", method = RequestMethod.GET)
	public void setSingleColor(@PathVariable int strip, @PathVariable String color, @PathVariable Integer intensity) throws InterruptedException {
		ledService.setColorIntensity(strip, Color.valueOf(color), intensity);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void setSingleColor() {
		ledService.testRed();
	}

	@RequestMapping(value = "/stop", method = RequestMethod.GET)
	public void setSingleColorLow() {
		ledService.stopRed();
	}

}

package com.exchange.rate.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchange.rate.pojo.ExchangeDto;
import com.exchange.rate.service.ExchangeService;

@RestController
@CrossOrigin(maxAge = 6000, origins = "*")
@RequestMapping(value = "/exchange")
public class RateController {

	@Autowired
	ExchangeService exchangeService;

	@GetMapping(value = "/getRate")
	public ExchangeDto getExchangeRate(@RequestParam(value = "exchangeFrom", defaultValue = "USD") String exchangeFrom,
			@RequestParam(value = "exchangeTo", defaultValue = "MYR") String exchangeTo, HttpSession httpSession,
			HttpServletRequest request) {
		try {
			return new ExchangeDto(exchangeService.getCurrentRate(exchangeFrom),
					exchangeService.getExchangeRate(exchangeFrom, exchangeTo));
		} catch (Exception e) {
			System.out.println("failed to get exchange");
			e.printStackTrace();
		}
		return new ExchangeDto(0f, 0f);
	}
}

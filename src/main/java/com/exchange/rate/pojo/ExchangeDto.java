package com.exchange.rate.pojo;

import lombok.Data;

@Data
public class ExchangeDto {
	Float currentRate;
	Float exchangeRate;
	public ExchangeDto(Float currentRate, Float exchangeRate) {
		this.currentRate = currentRate;
		this.exchangeRate = exchangeRate;
	}
}

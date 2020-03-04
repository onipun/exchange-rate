package com.exchange.rate.service;

public interface ExchangeService {
	Float getExchangeRate(String exchangeFrom, String exchangeTo) throws Exception;
	Float getCurrentRate(String exchangeFrom) throws Exception;
}

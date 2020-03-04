package com.exchange.rate.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exchange.rate.service.ExchangeService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service(value = "exchangeService")
@Transactional
public class ExchangeServiceImpl implements ExchangeService {

	public static String URL = "https://api.exchangerate-api.com/v4/latest/%s";
	public static String BASE_COMPARE = "USD";

	@Override
	public Float getExchangeRate(String exchangeFrom, String exchangeTo) throws Exception {

		// Making Request
		URL url = getUrl(exchangeFrom).orElseThrow(() -> new Exception("Failed to parse url"));
		JsonObject jsonObj = verify(url);
		System.out.println(jsonObj);
		float result = jsonObj.get(exchangeTo.toUpperCase()).getAsFloat();
		float convertedTo2Devimal = convertTo2Decimal(result);
		System.out
				.println("[" + new Date() + "] " + String.format("%s -> %s :: before convert [%f], after convert [%f]",
						exchangeFrom, exchangeTo, result, convertedTo2Devimal));
		return convertedTo2Devimal;
	}

	@Override
	public Float getCurrentRate(String exchangeTo) throws Exception {

		// Making Request
		URL url = getUrl(BASE_COMPARE).orElseThrow(() -> new Exception("Failed to parse url"));
		JsonObject jsonObj = verify(url);
		float result = jsonObj.get(exchangeTo.toUpperCase()).getAsFloat();
		float convertedTo2Devimal = convertTo2Decimal(result);
		System.out
				.println("[" + new Date() + "] " + String.format("%s -> %s :: before convert [%f], after convert [%f]",
						BASE_COMPARE, exchangeTo, result, convertedTo2Devimal));
		return convertedTo2Devimal;
	}

	private JsonObject verify(URL url) throws Exception {
		HttpURLConnection request = makeRequest(url).orElseThrow(() -> new Exception("Failed to make request"));
		JsonElement root = getResult(request).orElseThrow(() -> new Exception("Failed to extract result"));
		JsonObject jsonObj = root.getAsJsonObject().getAsJsonObject("rates");
		return jsonObj;
	}

	private float convertTo2Decimal(float result) {
		DecimalFormat df = new DecimalFormat("#.##");
		return Float.valueOf(df.format(result));

	}

	private Optional<JsonElement> getResult(HttpURLConnection request) {
		try {
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			return Optional.of(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private Optional<URL> getUrl(String exchangeFrom) {
		try {
			URL url = new URL(String.format(URL, exchangeFrom.toUpperCase()));
			return Optional.of(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private Optional<HttpURLConnection> makeRequest(URL url) {
		try {
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			return Optional.of(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

}

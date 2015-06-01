package com.train.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.naming.CommunicationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;

public class BaseHttpClient {
	protected final int timeOut = 120 * 1000;
	public BaseHttpClient() {
		// TODO Auto-generated constructor stub
	}
	protected int executeWithStatus(HttpClient client, HttpMethod method)
			throws CommunicationException {
		int status = 0;
		try {
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(timeOut);
			status = client.executeMethod(method);
			System.out.println(status);
		} catch (HttpException e) {
			throw new CommunicationException(e.getMessage());
		} catch (IOException e) {
			throw new CommunicationException(e.getMessage());
		}

		return status;
	}

	protected static String getResponse(HttpMethod method) {
		StringBuilder response = new StringBuilder();
		InputStream response_is;
		try {
			response_is = method.getResponseBodyAsStream();

			InputStreamReader reader = new InputStreamReader(response_is);
			BufferedReader reader_buffered = new BufferedReader(reader);
			String line = reader_buffered.readLine();
			
			while (line != null) {
                System.out.println(line);
                response.append(line);
                line = reader_buffered.readLine();
            }

			reader_buffered.close();			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.toString();
	}

	protected int executeWithResponse(HttpClient client, HttpMethod method)
			throws CommunicationException {
		int status = 0;
		try {
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(timeOut);
			status = client.executeMethod(method);
			
			getResponse(method);
		} catch (HttpException e) {
			throw new CommunicationException(e.getMessage());
		} catch (IOException e) {
			throw new CommunicationException(e.getMessage());
		}

		return status;
	}
}

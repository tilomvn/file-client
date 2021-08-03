package com.treasuredata.fileclient.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientFileComponent implements IClientFileComponent {

    private static final String BLANK = "";

    private String clientId;

    @Value("${service.server.url:http://localhost:4001/}")
    private String serverUrl;

    private final RestTemplate restTemplate;

    public ClientFileComponent(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    /**
     * Connect to server.
     */
    @Override
    public boolean connect() {
        try {
            System.out.println("Server: " + serverUrl);
            ResponseEntity<String> response = restTemplate.getForEntity(serverUrl + "client/connect", String.class);
            if (response.getStatusCode() == HttpStatus.ACCEPTED) {
                clientId = response.getBody();
                return true;
            }
            return false;
        } catch (HttpClientErrorException ex) {
            System.out.println(ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Close connection.
     */
    @Override
    public boolean closeConnect() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(serverUrl + "client/disconnect/" + clientId, String.class);
            System.out.println(response.getBody());
            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            }
            return false;
        } catch (HttpClientErrorException ex) {
            System.out.println(ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Send command to server.
     */
    @Override
    public String sendAction(String command) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(serverUrl + "client/" + clientId + "/command/" + command, String.class);
            String body = response.getBody();
            if (body == null) {
                return BLANK;
            }
            System.out.println(body);
            return body;
        } catch (HttpClientErrorException ex) {
            System.out.println(ex.getResponseBodyAsString());
            return ex.getResponseBodyAsString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

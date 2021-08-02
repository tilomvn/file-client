package com.treasuredata.fileclient.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientFileComponent {

    private static final String BLANK = "";

    private String clientId;

    @Value("${server.url:http://localhost:4001/}")
    private String serverUrl;

    private final RestTemplate restTemplate;

    public ClientFileComponent(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    /**
     * Connect to server.
     */
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
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Close connection.
     */
    public boolean closeConnect() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(serverUrl + "client/disconnect/" + clientId, String.class);
            System.out.println(response.getBody());
            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Send command to server.
     */
    public String sendAction(String command) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(serverUrl + "client/" + clientId + "/command/" + command, String.class);
            String body = response.getBody();
            if (body == null) {
                return BLANK;
            }
            return body;
        } catch (HttpClientErrorException ex) {
            return ex.getResponseBodyAsString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}

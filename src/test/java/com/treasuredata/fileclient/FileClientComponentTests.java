package com.treasuredata.fileclient;

import com.treasuredata.fileclient.components.ClientFileComponent;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(ClientFileComponent.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileClientComponentTests {

    @Value("${server.url:http://localhost:4001/}")
    private String serverUrl;

    @Autowired
    ClientFileComponent clientFileComponent;

    //Virtual Server Object
    @Autowired
    MockRestServiceServer serviceServer;

    String clientId = UUID.randomUUID().toString();
    String responseList = "NghiaHo 2021-06-03 10:30 2021-06-03 15:22 readme.txt";

    @Test
    @Order(1)
    void testConnection() {
        serviceServer.expect(requestTo(serverUrl + "client/connect"))
                .andRespond(withStatus(HttpStatus.ACCEPTED).body(clientId).contentType(MediaType.TEXT_PLAIN));

        boolean connection = clientFileComponent.connect();
        assertEquals(true, connection);
    }

    @Test
    @Order(2)
    void testSendCommandSuccess() {
        serviceServer.expect(requestTo(serverUrl + "client/" + clientId + "/command/index"))
                .andRespond(withStatus(HttpStatus.OK).body(responseList).contentType(MediaType.TEXT_PLAIN));

        String responseString = clientFileComponent.sendAction("index");
        assertEquals(responseList, responseString);
    }

    @Test
    @Order(3)
    void testSendCommandReturnUnknownCommand() {
        String command = "abc";
        String expectedResponse = "unknown command";
        serviceServer.expect(requestTo(serverUrl + "client/" + clientId + "/command/" + command))
                .andRespond(withStatus(HttpStatus.OK).body(expectedResponse).contentType(MediaType.TEXT_PLAIN));

        String responseString = clientFileComponent.sendAction(command);
        assertEquals(expectedResponse, responseString);
    }

    @Test
    @Order(4)
    void testCloseConnectionSuccess() {
        serviceServer.expect(requestTo(serverUrl + "client/disconnect/" + clientId))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.TEXT_PLAIN));

        boolean connection = clientFileComponent.closeConnect();
        assertEquals(true, connection);
    }

    @Test
    @Order(5)
    void testCloseConnectionFailed() {
        serviceServer.expect(requestTo(serverUrl + "client/disconnect/" + clientId))
                .andRespond(withServerError());

        boolean connection = clientFileComponent.closeConnect();
        assertEquals(false, connection);
    }

    @Test
    @Order(6)
    void testConnectionFailedByInternalServerError() {
        serviceServer.expect(requestTo(serverUrl + "client/connect"))
                .andRespond(withServerError());

        boolean connection = clientFileComponent.connect();
        assertEquals(false, connection);
    }

    @Test
    @Order(7)
    void testConnectionFailedByOthers() {
        serviceServer.expect(requestTo(serverUrl + "client/connect"))
                .andRespond(withNoContent());

        boolean connection = clientFileComponent.connect();
        assertEquals(false, connection);
    }
}

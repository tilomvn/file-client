package com.treasuredata.fileclient;

import com.treasuredata.fileclient.components.SocketClientFileComponent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SocketFileClientComponentTests {

    SocketClientFileComponent socketClientFileComponent = new SocketClientFileComponent("localhost", (short)4001);

    @BeforeAll
    static void init() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket( 4001);
                while (true) {
                    Socket socket = serverSocket.accept();
                    new ClientProcessor(socket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Test
    void testConnection() {
        boolean connection = socketClientFileComponent.connect();
        assertEquals(true, connection);
    }

    @Test
    void testCloseConnection() {
        socketClientFileComponent.connect();
        boolean connection = socketClientFileComponent.closeConnect();
        assertEquals(true, connection);
    }

    @Test
    void testSendCommandSuccess() {
        socketClientFileComponent.connect();
        String response = socketClientFileComponent.sendAction("index");
        assertEquals("Test Data", response);
    }

    @Test
    void testSendCommandFailed() {
        String response = socketClientFileComponent.sendAction("index");
        assertEquals(null, response);
    }

    static class ClientProcessor extends Thread {
        Socket socket;
        DataOutputStream out;
        DataInputStream in;

        public ClientProcessor(Socket socket) {
            this.socket = socket;
        }

        void writeContent(byte[] content) throws Exception {
            out.writeInt(content.length);
            out.write(content);
        }

        @Override
        public void run() {
            try {
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());

                String command;
                byte[] inputContent;
                int inputLength;
                while ((inputLength = in.readInt()) > 0) {
                    inputContent = new byte[inputLength];
                    in.readFully(inputContent, 0, inputLength);
                    command = new String(inputContent, StandardCharsets.UTF_8);
                    if ("exit".equals(command)) {
                        writeContent("DONE".getBytes());
                        break;
                    } else {
                        if ("index".equals(command) || command.startsWith("get ")) {
                            try {
                                writeContent("Test Data".getBytes());
                            } catch (Exception e) {
                                writeContent(e.getMessage().getBytes());
                            }
                        } else {
                            writeContent("unknown command".getBytes());
                        }
                    }
                }

                in.close();
                out.close();
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

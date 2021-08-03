package com.treasuredata.fileclient.components;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketClientFileComponent implements IClientFileComponent {

    Socket clientSocket;
    DataOutputStream out;
    DataInputStream in;

    String serverUrl;
    short port;

    public SocketClientFileComponent(String host, short port) {
        this.serverUrl = host;
        this.port = port;
    }

    @Override
    public boolean connect() {
        try {
            System.out.println("Socket Server: " + serverUrl);
            clientSocket = new Socket(serverUrl, port);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean closeConnect() {
        try {
            byte[] commands = "exit".getBytes();
            out.writeInt(commands.length);
            out.write(commands);

            int length = in.readInt();
            if (length > 0) {
                in.close();
                out.close();
                clientSocket.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String sendAction(String command) {
        try {
            byte[] commands = command.getBytes();
            out.writeInt(commands.length);
            out.write(commands);

            int length = in.readInt();
            String responseStr = null;
            if (length > 0) {
                byte[] response = new byte[length];
                in.readFully(response, 0, length);
                responseStr = new String(response, StandardCharsets.UTF_8);
                System.out.println(responseStr);
            }
            return responseStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

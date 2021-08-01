package com.treasuredata.fileclient.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ApplicationConsole {

    @Autowired
    ClientFileComponent clientFileComponent;

    public int run(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("Connecting to server...");
        if (!clientFileComponent.connect()) {
            System.out.println("Could not connect to server");
            return 0;
        }
        System.out.println("Connected");
        System.out.println("Command Description:");
        System.out.println(" 1.index : get list file from server.");
        System.out.println(" 2.get <file-name> : get content of file.");
        System.out.println(" 3.exit : stop application");
        while(true) {
            System.out.print(">");
            command = scanner.nextLine();
            if (!new Scanner(command).hasNext()) {
                //Do nothing
            } else if ("exit".equals(command)) {
                clientFileComponent.closeConnect();
                break;
            } else {
                System.out.println(clientFileComponent.sendAction(command));
            }
        }
        System.out.println("Stopping client...");
        System.out.println("Stopped");
        return 1;
    }
}

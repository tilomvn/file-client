package com.treasuredata.fileclient;

import com.treasuredata.fileclient.components.ClientFileComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class FileClientApplication {

	public static void main(String[] args) {
		System.out.println("Starting File Client Application...");
		ApplicationContext app = SpringApplication.run(FileClientApplication.class, args);
		ClientFileComponent clientFileComponent = app.getBean(ClientFileComponent.class);
		System.out.println("Started File Client Application");

		Scanner scanner = new Scanner(System.in);
		String command;

		System.out.println("Connecting to server...");
		if (!clientFileComponent.connect()) {
			System.out.println("Could not connect to server");
			System.exit(0);
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
		System.exit(1);
	}
}

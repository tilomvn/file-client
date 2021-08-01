package com.treasuredata.fileclient;

import com.treasuredata.fileclient.components.ApplicationConsole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class FileClientApplication {

	public static void main(String[] args){
		System.out.println("Starting File Client Application...");
		ApplicationContext app = SpringApplication.run(FileClientApplication.class, args);
		System.out.println("Started File Client Application");

		ApplicationConsole applicationConsole = app.getBean(ApplicationConsole.class);
		int retCode = applicationConsole.run(args);
		System.exit(retCode);
	}
}

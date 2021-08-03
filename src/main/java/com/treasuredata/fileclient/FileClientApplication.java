package com.treasuredata.fileclient;

import com.treasuredata.fileclient.components.ClientFileComponent;
import com.treasuredata.fileclient.console.ApplicationConsole;
import com.treasuredata.fileclient.components.SocketClientFileComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class FileClientApplication {

	public static void main(String[] args) {
		System.out.println("Starting File Client Application...");
		ApplicationContext app = SpringApplication.run(FileClientApplication.class, args);
		System.out.println("Started File Client Application");

		String runMode = app.getEnvironment().getProperty("app.run.mode");
		ApplicationConsole applicationConsole = null;

		if (RUNMODE.SERVICE.name().equals(runMode)) {
            ClientFileComponent clientFileComponent = app.getBean(ClientFileComponent.class);
			applicationConsole = new ApplicationConsole(clientFileComponent);
		} else if (RUNMODE.SOCKET.name().equals(runMode)) {
			String host = app.getEnvironment().getProperty("socket.server.host");
			short port = Short.parseShort(app.getEnvironment().getProperty("socket.server.port"));
			applicationConsole = new ApplicationConsole(new SocketClientFileComponent(host, port));
		}

		applicationConsole.run(args);
	}

	enum RUNMODE {
		SERVICE, SOCKET
	}
}

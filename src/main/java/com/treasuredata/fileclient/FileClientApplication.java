package com.treasuredata.fileclient;

import com.treasuredata.fileclient.components.ClientFileComponent;
import com.treasuredata.fileclient.console.ApplicationConsole;
import com.treasuredata.fileclient.components.SocketClientFileComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class FileClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println("Starting File Client Application...");

        Path path = Paths.get("src", "main", "resources", "application.properties");
        List<String> properties = Files.lines(path).collect(Collectors.toList());
        String runMode = properties.get(0);

        ApplicationConsole applicationConsole = null;
        if (runMode.endsWith(RUNMODE.SOCKET.name())) {
            String host = properties.stream().filter(p -> p.startsWith("socket.server.host"))
                    .map(p -> p.split("=")[1]).findFirst().get();
			short port = Short.parseShort(properties.stream().filter(p -> p.startsWith("socket.server.port"))
                    .map(p -> p.split("=")[1]).findFirst().get());
			applicationConsole = new ApplicationConsole(new SocketClientFileComponent(host, port));
        } else if (runMode.endsWith(RUNMODE.SERVICE.name())) {
            ApplicationContext app = SpringApplication.run(FileClientApplication.class, args);
            ClientFileComponent clientFileComponent = app.getBean(ClientFileComponent.class);
			applicationConsole = new ApplicationConsole(clientFileComponent);
        }
        System.out.println("Started File Client Application");
        applicationConsole.run(args);
	}

	enum RUNMODE {
		SERVICE, SOCKET
	}
}

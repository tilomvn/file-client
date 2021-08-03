# Application Protocols
* Application can run on TCP/IP or HTTP protocol.
* By default, it runs on TCP/IP, change key app.run.mode=SERVICE if want to use HTTP protocol.
# Environment Requirement
* Must have Java 8 or above.
# How to build and run
* Clone source, go to file-client directory, type command [mvnw verify] to build.
* Go to directory target, type command [java -jar file-client.jar] to run.
# Deployment
* This application's architecture is client-server, so we can run it in any environment.
* Ensure server address which can be specified in application.properties file.
* By default, server address is localhost with meaning client and server is
running in same machine.

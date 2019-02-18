package br.com.joaomassan.transfer;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class App {

  public static final String BASE_URI = "http://localhost:8080/";

  public static HttpServer startServer() {
     ResourceConfig rc = new ResourceConfig().packages("br.com.joaomassan.transfer.service");
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format("Running on ", BASE_URI));
    System.in.read();
    server.shutdown();
    System.out.println("Server halted.");
  }
}

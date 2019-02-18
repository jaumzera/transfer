package br.com.joaomassan.transfer;

import br.com.joaomassan.transfer.service.AccountService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class App {

  private static final String BASE_URI = "http://localhost:%s/";

  public static HttpServer startServer() {
    return startServer("8080");
  }

  public static HttpServer startServer(String port) {
    if(port == null) {
      port = "8080";
    }
    ResourceConfig rc = new ResourceConfig().packages(AccountService.class.getPackage().getName());
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(String.format(BASE_URI, port)), rc);
  }

  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format("Running on %s", BASE_URI));
    //noinspection ResultOfMethodCallIgnored
    System.in.read();
    server.shutdown();
    System.out.println("Server halted.");
  }
}

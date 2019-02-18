package br.com.joaomassan.transfer.service;

import br.com.joaomassan.transfer.App;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class AccountControllerIT {

  private static HttpServer httpServer;

  private Client client;

  @BeforeClass
  public static void beforeClass() {
    AccountService accountService = AccountService.getInstance();
    accountService.createAccount(AccountCreationRequest.of("Teste 1", "teste1@email.com", "10000.0000"));
    accountService.createAccount(AccountCreationRequest.of("Teste 2", "teste2@email.com", "10000.0000"));
    accountService.createAccount(AccountCreationRequest.of("Teste 3", "teste3@email.com", "10000.0000"));
    accountService.createAccount(AccountCreationRequest.of("Teste 4", "teste4@email.com", "10000.0000"));
    accountService.createAccount(AccountCreationRequest.of("Teste 5", "teste5@email.com", "0.00"));
    accountService.take(1L, new BigDecimal("100.00"));
    accountService.take(1L, new BigDecimal("100.00"));
    accountService.take(1L, new BigDecimal("100.00"));
    accountService.take(1L, new BigDecimal("100.00"));
    accountService.put(1L, new BigDecimal("50.00"));
    accountService.put(1L, new BigDecimal("50.00"));
    accountService.put(1L, new BigDecimal("50.00"));
    accountService.put(1L, new BigDecimal("50.00"));
    accountService.take(2L, new BigDecimal("100.00"));
    accountService.take(2L, new BigDecimal("100.00"));
    accountService.take(2L, new BigDecimal("100.00"));
    accountService.take(2L, new BigDecimal("100.00"));
    accountService.put(2L, new BigDecimal("50.00"));
    accountService.put(2L, new BigDecimal("50.00"));
    accountService.put(2L, new BigDecimal("50.00"));
    accountService.put(2L, new BigDecimal("50.00"));
    accountService.take(3L, new BigDecimal("100.00"));
    accountService.take(3L, new BigDecimal("100.00"));
    accountService.take(3L, new BigDecimal("100.00"));
    accountService.take(3L, new BigDecimal("100.00"));
    accountService.put(3L, new BigDecimal("50.00"));
    accountService.put(3L, new BigDecimal("50.00"));
    accountService.put(3L, new BigDecimal("50.00"));
    accountService.put(3L, new BigDecimal("50.00"));
    accountService.take(4L, new BigDecimal("100.00"));
    accountService.take(4L, new BigDecimal("100.00"));
    accountService.take(4L, new BigDecimal("100.00"));
    accountService.take(4L, new BigDecimal("100.00"));
    accountService.put(4L, new BigDecimal("50.00"));
    accountService.put(4L, new BigDecimal("50.00"));
    accountService.put(4L, new BigDecimal("50.00"));
    accountService.put(4L, new BigDecimal("50.00"));

    httpServer = App.startServer("9999");
  }

  @AfterClass
  public static void afterClass() {
    httpServer.shutdown();
  }

  @Before
  public void before() {
    client = ClientBuilder.newClient();
  }

  @After
  public void after() {
    client.close();
  }

  @Test
  public void shouldCreateAccount() {
    WebTarget webTarget = client.target("http://localhost:9999/accounts");
    Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
    AccountCreationRequest accountCreationRequest = AccountCreationRequest.of(
        "Mamute Caninho",
        "caninho@gmail.com",
        "9000.00");
    Response response = invocationBuilder.post(Entity.entity(accountCreationRequest, MediaType.APPLICATION_JSON));
    assertThat(response.getStatus(), equalTo(HTTP_CREATED));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldGetTransactions() {
    WebTarget webTarget = client.target("http://localhost:9999/accounts/1/transactions");
    Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
    Response response = invocationBuilder.get();
    assertThat(response.getStatus(), equalTo(HTTP_OK));
    List<String> responseList = (List<String>) response.readEntity(List.class);
    assertThat(responseList.size(), equalTo(8));
  }

  @Test
  public void shouldGetCurrentBallance() {
    WebTarget webTarget = client.target("http://localhost:9999/accounts/1/ballance");
    Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
    Response response = invocationBuilder.get();
    assertThat(response.getStatus(), equalTo(HTTP_OK));
    BigDecimal value = response.readEntity(BigDecimal.class);
    assertThat(value, equalTo(new BigDecimal("9800.00")));
  }

  @Test
  public void shouldTake() {
    WebTarget debitTarget = client.target("http://localhost:9999/accounts/debit");
    Invocation.Builder invocationBuilder = debitTarget.request(MediaType.APPLICATION_JSON);
    OperationRequest takeRequest = OperationRequest.of(2L, new BigDecimal("1000.00"));
    Response takeResponse = invocationBuilder.put(Entity.entity(takeRequest, MediaType.APPLICATION_JSON));
    assertThat(takeResponse.getStatus(), equalTo(HTTP_OK));
    WebTarget ballanceTarget = client.target("http://localhost:9999/accounts/2/ballance");
    invocationBuilder = ballanceTarget.request(MediaType.APPLICATION_JSON);
    Response ballanceResponse = invocationBuilder.get();
    BigDecimal value = ballanceResponse.readEntity(BigDecimal.class);
    assertThat(value, equalTo(new BigDecimal("8800.00")));
  }

  @Test
  public void shouldPut() {
    WebTarget debitTarget = client.target("http://localhost:9999/accounts/credit");
    Invocation.Builder invocationBuilder = debitTarget.request(MediaType.APPLICATION_JSON);
    OperationRequest putRequest = OperationRequest.of(3L, new BigDecimal("1000.00"));
    Response takeResponse = invocationBuilder.put(Entity.entity(putRequest, MediaType.APPLICATION_JSON));
    assertThat(takeResponse.getStatus(), equalTo(HTTP_OK));
    WebTarget ballanceTarget = client.target("http://localhost:9999/accounts/2/ballance");
    invocationBuilder = ballanceTarget.request(MediaType.APPLICATION_JSON);
    Response ballanceResponse = invocationBuilder.get();
    BigDecimal value = ballanceResponse.readEntity(BigDecimal.class);
    assertThat(value, equalTo(new BigDecimal("9800.00")));
  }

  @Test
  public void shouldTransfer() {
    WebTarget debitTarget = client.target("http://localhost:9999/accounts/transfer");
    Invocation.Builder invocationBuilder = debitTarget.request(MediaType.APPLICATION_JSON);
    TransferRequest transferRequest = TransferRequest.of(4L, 5L, "bill payment", new BigDecimal("1000.00"));
    Response transferResponse = invocationBuilder.put(Entity.entity(transferRequest, MediaType.APPLICATION_JSON));
    assertThat(transferResponse.getStatus(), equalTo(HTTP_OK));
    WebTarget ballanceTarget = client.target("http://localhost:9999/accounts/5/ballance");
    invocationBuilder = ballanceTarget.request(MediaType.APPLICATION_JSON);
    Response ballanceResponse = invocationBuilder.get();
    BigDecimal value = ballanceResponse.readEntity(BigDecimal.class);
    assertThat(value, equalTo(new BigDecimal("1000.00")));
  }

}
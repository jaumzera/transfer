package br.com.joaomassan.transfer.service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

@Path("/accounts")
public class AccountController {

  private AccountService accountService = AccountService.getInstance();

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createAccount(AccountCreationRequest request) {
    accountService.createAccount(request);
    return Response.status(HTTP_CREATED).build();
  }

  @GET
  @Path("/{accountId}/transactions")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> getTransactions(@PathParam( "accountId") Long accountId) {
    return accountService.getTransactions(accountId);
  }

  @GET
  @Path("/{accountId}/ballance")
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal getCurrentBallance(@PathParam( "accountId") Long accountId) {
    return accountService.getBallance(accountId);
  }

  @PUT
  @Path("/debit")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response take(OperationRequest request) {
    accountService.take(request.getAccountId(), request.getValue());
    return Response.status(HTTP_OK).build();
  }

  @PUT
  @Path("/credit")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response put(OperationRequest request) {
    accountService.put(request.getAccountId(), request.getValue());
    return Response.status(HTTP_OK).build();
  }

  @PUT
  @Path("/transfer")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response transfer(TransferRequest request) {
    accountService.transfer(request);
    return Response.status(HTTP_OK).build();
  }
}

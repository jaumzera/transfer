package br.com.joaomassan.transfer.service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/accounts")
public class AccountController {

  private AccountService accountService = new AccountService();

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createAccount(AccountCreationRequest request) {
    accountService.createAccount(request);
    return Response.status(201).build();
  }

  @GET
  @Path("/{accountId}/ballance")
  @Produces(MediaType.APPLICATION_JSON)
  public BigDecimal getCurrentBallance(@PathParam( "accountId") Long accountId) {
    return accountService.getBallance(accountId);
  }

  @PUT
  @Path("/{accountId}/debit")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response take(OperationRequest request) {
    accountService.take(request.getAccountId(), request.getValue());
    return Response.status(200).build();
  }

  @PUT
  @Path("/{accountId}/credit")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response put(OperationRequest request) {
    accountService.put(request.getAccountId(), request.getValue());
    return Response.status(201).build();
  }

  @PUT
  @Path("/{accountId}/transfer")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response put(TransferRequest request) {
    accountService.transfer(request);
    return Response.status(201).build();
  }


}

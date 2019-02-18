package br.com.joaomassan.transfer.service;

import br.com.joaomassan.transfer.domain.Account;
import br.com.joaomassan.transfer.domain.Customer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccountService {

  private static final Map<Long, Account> accountById = new HashMap<>();

  public void createAccount(AccountCreationRequest request) {
    Customer customer = Customer.newCustomer(request.getCustomerName(), request.getCustomerEmail());
    Account account = Account.newAccount(customer, request.getInitialValue());
    // TODO validate email
    accountById.put(account.getId(), account);
  }

  public BigDecimal getBallance(Long accountId) {
    return accountById.get(accountId).getCurrentBallance();
  }

  public void take(Long accountId, BigDecimal value) {
    accountById.get(accountId).take(value);
  }

  public void put(Long accountId, BigDecimal value) {
    accountById.get(accountId).put(value);
  }

  public void transfer(TransferRequest request) {
    Account origin = accountById.get(request.getFrom());
    Account destiny = accountById.get(request.getTo());
    origin.transfer(request.getValue(), request.getIdentifier(), destiny);
  }
}

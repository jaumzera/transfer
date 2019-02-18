package br.com.joaomassan.transfer.service;

import br.com.joaomassan.transfer.domain.Account;
import br.com.joaomassan.transfer.domain.Customer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {

  private static AccountService instance;

  private static final Map<Long, Account> accountById = new HashMap<>();

  private AccountService() {
  }

  public static AccountService getInstance() {
    if(instance == null) {
      synchronized (AccountService.class) {
        instance = new AccountService();
      }
    }
    return instance;
  }


  public void createAccount(AccountCreationRequest request) {
    validate(request);
    Customer customer = Customer.newCustomer(request.getCustomerName(), request.getCustomerEmail());
    Account account = Account.newAccount(customer, request.getInitialValue());
    accountById.put(account.getId(), account);
  }

  private void validate(AccountCreationRequest request) {
    AccountServiceException.requireNotNull(request.getCustomerName(), "Customer name must not be empty");
    AccountServiceException.requireNotNull(request.getCustomerEmail(), "Email must not be empty" );
    AccountServiceException.requireNotNull(request.getInitialValue(), "Initial value must not be empty" );

    if(accountById.values()
        .stream()
        .anyMatch(item -> request.getCustomerEmail().equals(item.getCustomer().getEmail()))) {
      throw AccountServiceException.emailAlreadyTaken();
    }
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

  public List<String> getTransactions(Long accountId) {
    return accountById.get(accountId).getTransactions();
  }
}

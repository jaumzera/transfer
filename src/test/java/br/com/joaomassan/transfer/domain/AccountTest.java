package br.com.joaomassan.transfer.domain;

import br.com.joaomassan.transfer.domain.Account;
import br.com.joaomassan.transfer.domain.AccountValidationException;
import br.com.joaomassan.transfer.domain.Customer;
import br.com.joaomassan.transfer.domain.TransactionValidationException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AccountTest {

  private Account createSampleAccount1() {
    Customer customer = Customer.newCustomer("João Massan", "joaomassan@gmail.com");
    return Account.newAccount(customer,  new BigDecimal("10000.00"));
  }

  private Account createSampleAccount2() {
    Customer customer2 = Customer.newCustomer("Mamute Caninho", "caninho@gmail.com");
    return Account.newAccount(customer2,  new BigDecimal("20000.00"));
  }

  @Test
  public void getCurrentBallanceTest() {
    Account account = createSampleAccount1();
    account.take(new BigDecimal("100.00"));
    account.take(new BigDecimal("500.00"));
    assertThat(account.getCurrentBallance(), equalTo(new BigDecimal("9400.00")));
    account.put(new BigDecimal("600.00"));
    assertThat(account.getCurrentBallance(), equalTo(new BigDecimal("10000.00")));
  }

  @Test
  public void transferTest() {
    Account account1 = createSampleAccount1();
    Account account2 = createSampleAccount2();
    account1.transfer(new BigDecimal("100.00"), "payment", account2);
    assertThat(account1.getCurrentBallance(), equalTo(new BigDecimal("9900.00")));
    assertThat(account2.getCurrentBallance(), equalTo(new BigDecimal("20100.00")));
  }

  @Test(expected = AccountValidationException.class)
  public void shouldNotTransferIfNotEnoughBallance() {
    try {
      Account account1 = createSampleAccount1();
      Account account2 = createSampleAccount2();
      account1.transfer(new BigDecimal("10001.00"), "payment", account2);
    } catch(Exception ex) {
      assertThat(ex.getMessage(), equalTo("Not enough ballance"));
      throw ex;
    }
  }

  @Test(expected = TransactionValidationException.class)
  public void shouldNotTransferNegativeValue() {
    try {
      Account account1 = createSampleAccount1();
      Account account2 = createSampleAccount2();
      account1.transfer(new BigDecimal("-100.00"), "payment1", account2);
      assertThat(account1.getCurrentBallance(), equalTo(new BigDecimal("9900.00")));
      assertThat(account2.getCurrentBallance(), equalTo(new BigDecimal("20100.00")));
    } catch(Exception ex) {
      assertThat(ex.getMessage(), equalTo("Not a valid amount"));
      throw ex;
    }
  }
}
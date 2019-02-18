package br.com.joaomassan.transfer.domain;

import br.com.joaomassan.transfer.domain.Transaction;
import br.com.joaomassan.transfer.domain.TransactionType;
import br.com.joaomassan.transfer.domain.TransactionValidationException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class TransactionTest {

  @Test
  public void shouldCreateACreditTransaction() {
    Transaction credit = Transaction.credit().value(new BigDecimal("100.00")).build();
    assertNotNull(credit.getId());
    assertThat(credit.getType(), equalTo(TransactionType.CREDIT));
    assertThat(credit.getValue(), equalTo(new BigDecimal("100.00")));
  }

  @Test(expected = TransactionValidationException.class)
  public void shouldNotAcceptNegativeValueForCredit() {
    Transaction.credit().value(new BigDecimal("-10")).build();
  }

  @Test(expected = TransactionValidationException.class)
  public void shouldNotAcceptNegativeValueForDebit() {
    Transaction.debit().value(new BigDecimal("-10")).build();
  }

}
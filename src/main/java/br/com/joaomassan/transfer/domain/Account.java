package br.com.joaomassan.transfer.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Account {

 private static final int DEFAULT_SCALE = 2;

 private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_DOWN;

  public static Account newAccount(Customer customer, BigDecimal initialValue) {
    return new Account(customer, initialValue);
  }

  private final Customer customer;

  private final BigDecimal initialValue;

  private final Set<Transaction> transactions = new TreeSet<>();

  private boolean changed = true;

  private BigDecimal currentBallanceCache;

  private synchronized void changed() {
      changed = true;
  }

  private synchronized void updated() {
    changed = false;
  }

  private Account(Customer customer, BigDecimal initialValue) {
    this.customer = customer;
    this.initialValue = initialValue;
  }

  private void validate(BigDecimal value) {
    if(value.compareTo(getCurrentBallance()) > 0) {
      throw AccountValidationException.notEnoughBallance();
    }
  }

  public BigDecimal getCurrentBallance() {
    if(changed) {
      currentBallanceCache = transactions.stream().map(item -> item.getType() == TransactionType.CREDIT
          ? item.getValue()
          : item.getValue().negate())
          .reduce(initialValue, BigDecimal::add)
          .setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
      updated();
    }
    return currentBallanceCache;
  }

  public void transfer(BigDecimal value, String identifier, Account to) {
    Objects.requireNonNull(identifier);
    validate(value);
    to.put(this.take(value));
  }

  public void put(BigDecimal value) {
    synchronized (transactions) {
      transactions.add(Transaction.credit().value(value).build());
      changed();
    }
  }

  public BigDecimal take(BigDecimal value) {
    synchronized (transactions) {
      validate(value);
      transactions.add(Transaction.debit().value(value).build());
      changed();
    }
    return value;
  }
}

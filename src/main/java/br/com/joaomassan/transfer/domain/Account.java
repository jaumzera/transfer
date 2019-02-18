package br.com.joaomassan.transfer.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Data
@EqualsAndHashCode(of = "customer")
public class Account {

  private static final int DEFAULT_SCALE = 2;

  private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_DOWN;

  private static long nextId = 1;

  public static Account newAccount(Customer customer, BigDecimal initialValue) {
    return new Account(customer, initialValue);
  }

  private final Long id;

  private final Customer customer;

  private final BigDecimal initialValue;

  @Getter(AccessLevel.NONE)
  private final Set<Transaction> transactions = new TreeSet<>();

  @Getter(AccessLevel.NONE)
  private volatile boolean changed = true;

  @Getter(AccessLevel.NONE)
  private BigDecimal currentBallanceCache;

  private synchronized void changed() {
    changed = true;
  }

  private synchronized void updated() {
    changed = false;
  }

  private Account(Customer customer, BigDecimal initialValue) {
    this.id = nextId();
    this.customer = customer;
    this.initialValue = initialValue;
  }

  private Long nextId() {
    synchronized (this) {
      return nextId++;
    }
  }

  private void validate(BigDecimal value) {
    if (value.compareTo(getCurrentBallance()) > 0) {
      throw AccountValidationException.notEnoughBallance();
    }
  }

  public BigDecimal getCurrentBallance() {
    if (changed) {
      updateCurrentBallance();
    }
    return currentBallanceCache;
  }

  private synchronized void updateCurrentBallance() {
    currentBallanceCache = transactions.stream().map(item -> item.getType() == TransactionType.CREDIT
        ? item.getValue()
        : item.getValue().negate())
        .reduce(initialValue, BigDecimal::add)
        .setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    updated();
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

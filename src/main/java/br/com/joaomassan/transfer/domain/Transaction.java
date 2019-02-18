package br.com.joaomassan.transfer.domain;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;

@Value
public class Transaction implements Comparable<Transaction> {

  private static long nextId = 1;

  static TransactionBuilder credit() {
    return new TransactionBuilder().type(TransactionType.CREDIT);
  }

  static TransactionBuilder debit() {
    return new TransactionBuilder().type(TransactionType.DEBIT);
  }

  private Long id;

  private String identifier;

  private TransactionType type;

  private LocalDateTime time;

  private BigDecimal value;

  private Transaction(String identifier, TransactionType type, LocalDateTime time, BigDecimal value) {
    this.id = nextId();
    this.identifier = identifier;
    this.type = type;
    this.time = time;
    this.value = value;
  }

  private Long nextId() {
    synchronized (this) {
      return nextId++;
    }
  }

  @Override
  public int compareTo(Transaction other) {
      return this.id.compareTo(other.id);
  }

  public static class TransactionBuilder {
    private String identifier;
    private TransactionType type;
    private BigDecimal value;

    private TransactionBuilder() {
    }

    TransactionBuilder identifier(String identifier) {
      this.identifier = identifier;
      return this;
    }

    private TransactionBuilder type(TransactionType type) {
      this.type = type;
      return this;
    }

    TransactionBuilder value(BigDecimal value) {
      if(Objects.requireNonNull(value, "Value cannot be null").signum() < 0) {
        throw TransactionValidationException.notAValidAmount();
      }

      this.value = value;
      return this;
    }

    Transaction build() {
      LocalDateTime now = LocalDateTime.now();

      return new Transaction(
          identifier == null
            ? String.valueOf(now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
            : identifier,
          type,
          now,
          value);
    }
  }
}

package br.com.joaomassan.transfer.domain;

public class TransactionValidationException extends RuntimeException {

  private TransactionValidationException(String message) {
    super(message);
  }

  public static TransactionValidationException notAVAlidAmount() {
    return new TransactionValidationException("Not a valid amount");
  }
}

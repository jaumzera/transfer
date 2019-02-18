package br.com.joaomassan.transfer.domain;

public class AccountValidationException extends RuntimeException {

  public static AccountValidationException notEnoughBallance() {
    return new AccountValidationException("Not enough ballance");
  }

  private AccountValidationException(String message) {
    super(message);
  }

}

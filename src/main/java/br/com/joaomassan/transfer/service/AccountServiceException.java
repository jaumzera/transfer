package br.com.joaomassan.transfer.service;

public class AccountServiceException extends RuntimeException {

  public AccountServiceException(String message) {
    super(message);
  }

  public static AccountServiceException emailAlreadyTaken() {
    return new AccountServiceException("Email already taken");
  }

  public static void requireNotNull(Object object, String message) {
    if(object == null) {
      throw new AccountServiceException(message);
    }
  }
}

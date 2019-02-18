package br.com.joaomassan.transfer.domain;

import lombok.Data;

@Data
public class Customer {

  private static long nextId = 1;

  private Long id;

  private String name;

  private String email;

  public Customer(String name, String email) {
    this.id = nextId();
    this.name = name;
    this.email = email;
  }

  private static Long nextId() {
    return nextId++;
  }

  public static Customer newCustomer(String name, String email) {
    return new Customer(name, email);
  }
}

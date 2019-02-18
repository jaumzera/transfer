package br.com.joaomassan.transfer.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountCreationRequest {

  public static AccountCreationRequest of(String customerName, String customerEmail, String value) {
    return new AccountCreationRequest(customerName, customerEmail, new BigDecimal(value));
  }

  private String customerName;

  private String customerEmail;

  private BigDecimal initialValue;

}

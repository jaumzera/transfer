package br.com.joaomassan.transfer.service;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountCreationRequest {

  private String customerName;

  private String customerEmail;

  private BigDecimal initialValue;

}

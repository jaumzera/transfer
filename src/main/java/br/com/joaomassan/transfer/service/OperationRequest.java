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
public class OperationRequest {

  public static OperationRequest of(Long accountId, BigDecimal value) {
    return new OperationRequest(accountId, value);
  }

  private Long accountId;

  private BigDecimal value;

}

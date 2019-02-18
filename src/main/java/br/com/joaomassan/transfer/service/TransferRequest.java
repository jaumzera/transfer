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
public class TransferRequest {

  public static TransferRequest of(Long originId, Long toId, String identifier, BigDecimal value) {
    return new TransferRequest(originId, toId, identifier, value);
  }

  private Long from;

  private Long to;

  private String identifier;

  private BigDecimal value;

}

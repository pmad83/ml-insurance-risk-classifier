package pl.pm.mlinsuranceriskclassifier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/*
 * Klasa Customer reprezentuje ubezpieczającego, dla którego szacowane jest ryzyko szkodowe.
 */
@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer age;
    private Integer accidentsQty;
    private Integer insuranceSum;
    private Integer claimsHistory;
    private Integer riskLevel;
}

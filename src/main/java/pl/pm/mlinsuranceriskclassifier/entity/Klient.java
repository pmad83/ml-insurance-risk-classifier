package pl.pm.mlinsuranceriskclassifier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/*
 * Klasa Klient reprezentuje ubezpieczającego, dla którego szacowane jest ryzyko szkodowe.
 */
@Entity
@Data
public class Klient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer wiek;
    private Integer liczbaWypadkow;
    private Integer wartoscUbezpieczenia;
    private Integer historiaSzkod;
    private Integer ryzyko;
}

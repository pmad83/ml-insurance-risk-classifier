package pl.pm.mlinsuranceriskclassifier.entity;

import lombok.Data;

/*
 * Klasa Klient reprezentuje ubezpieczającego, dla którego szacowane jest ryzyko szkodowe.
 */
@Data
public class Klient {

    private Long id;
    private Integer wiek;
    private Integer liczbaWypadkow;
    private Integer wartoscUbezpieczenia;
    private Integer historiaSzkod;
    private Double ryzyko;
}

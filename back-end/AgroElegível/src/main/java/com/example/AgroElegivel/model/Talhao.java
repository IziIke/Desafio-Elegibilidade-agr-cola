package com.example.AgroElegivel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "talhao")
public class Talhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "propriedadeid", nullable = false) // FK
    private PropriedadeRural propriedadeRural;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "areaha", nullable = false)
    private Double areaHa;

    // --------- Getters e Setters ---------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PropriedadeRural getPropriedadeRural() {
        return propriedadeRural;
    }

    public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
        this.propriedadeRural = propriedadeRural;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getAreaHa() {
        return areaHa;
    }

    public void setAreaHa(Double areaHa) {
        this.areaHa = areaHa;
    }
}

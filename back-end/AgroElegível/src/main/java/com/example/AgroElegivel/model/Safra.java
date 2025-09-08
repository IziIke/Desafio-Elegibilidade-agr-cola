package com.example.AgroElegivel.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "safra")
public class Safra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo", nullable = false, unique = true, length = 100)
    private String codigo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "talhaoid", nullable = false) // FK
    private Talhao talhao;

    private String cultura;
    private Integer ano;
    private LocalDate dataPlantio;
    private LocalDate dataColheitaPrevista;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Talhao getTalhao() {
        return talhao;
    }

    public void setTalhao(Talhao talhao) {
        this.talhao = talhao;
    }

    public String getCultura() {
        return cultura;
    }

    public void setCultura(String cultura) {
        this.cultura = cultura;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public LocalDate getDataPlantio() {
        return dataPlantio;
    }

    public void setDataPlantio(LocalDate dataPlantio) {
        this.dataPlantio = dataPlantio;
    }

    public LocalDate getDataColheitaPrevista() {
        return dataColheitaPrevista;
    }

    public void setDataColheitaPrevista(LocalDate dataColheitaPrevista) {
        this.dataColheitaPrevista = dataColheitaPrevista;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}

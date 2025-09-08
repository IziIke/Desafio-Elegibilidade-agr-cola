package com.example.AgroElegivel.model;

public enum Uf {
    AC("Acre"),
    AL("Alagoas"),
    AP("Amapá"),
    AM("Amazonas"),
    BA("Bahia"),
    CE("Ceará"),
    DF("Distrito Federal"),
    ES("Espírito Santo"),
    GO("Goiás"),
    MA("Maranhão"),
    MT("Mato Grosso"),
    MS("Mato Grosso do Sul"),
    MG("Minas Gerais"),
    PA("Pará"),
    PB("Paraíba"),
    PR("Paraná"),
    PE("Pernambuco"),
    PI("Piauí"),
    RJ("Rio de Janeiro"),
    RN("Rio Grande do Norte"),
    RS("Rio Grande do Sul"),
    RO("Rondônia"),
    RR("Roraima"),
    SC("Santa Catarina"),
    SP("São Paulo"),
    SE("Sergipe"),
    TO("Tocantins");

    private final String nome;

    Uf(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return this.name(); // O próprio nome do enum (AC, AL, etc.)
    }

    public static Uf fromSigla(String sigla) {
        for (Uf uf : Uf.values()) {
            if (uf.name().equalsIgnoreCase(sigla)) {
                return uf;
            }
        }
        throw new IllegalArgumentException("Sigla inválida: " + sigla);
    }
}
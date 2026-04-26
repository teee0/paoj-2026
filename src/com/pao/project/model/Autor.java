package com.pao.project.model;

public class Autor extends Persoana {
    private String domeniu;

    public Autor(String nume, String prenume, int anNastere, String domeniu) {
        super(nume, prenume, anNastere);
        this.domeniu = domeniu;
    }

    @Override
    public String getRol() {
        return "Autor";
    }

    public String getDomeniu() { return domeniu; }
    public void setDomeniu(String domeniu) { this.domeniu = domeniu; }
}
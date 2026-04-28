package com.pao.project.model;

import java.util.Objects;

public class Autor extends Persoana implements Comparable<Autor>{
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


    public int compareTo(Autor altAutor) {
        return this.nume.compareToIgnoreCase(altAutor.getNume());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(nume+prenume, autor.nume+autor.prenume);
    }
/*
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    */
}


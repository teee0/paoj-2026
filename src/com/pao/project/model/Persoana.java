package com.pao.project.model;

public abstract class Persoana {
    protected String nume;
    protected String prenume;
    protected int anNastere;

    public Persoana(String nume, String prenume, int anNastere) {
        this.nume = nume;
        this.prenume = prenume;
        this.anNastere = anNastere;
    }

    // Metoda abstracta
    public abstract String getRol();

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public int getAnNastere() { return anNastere; }
    public void setAnNastere(int anNastere) { this.anNastere = anNastere; }
}
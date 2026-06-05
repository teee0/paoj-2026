// Fisier: src/com/pao/project/model/Cititor.java
package com.pao.project.model;
import java.util.Objects;

public class Cititor extends Persoana {
    private String cod;

    public Cititor(String nume, String prenume, int anNastere, String cod) {
        this(0, nume, prenume, anNastere, cod);
    }

    public Cititor(int id, String nume, String prenume, int anNastere, String cod) {
        super(id, nume, prenume, anNastere);
        this.cod = cod;
    }

    @Override
    public String getRol() {
        return "Cititor";
    }

    public String getCod() { return cod; }
    public void setCod(String cod) { this.cod = cod; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cititor cititor = (Cititor) o;
        return Objects.equals(cod, cititor.cod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }

    @Override
    public String toString() {
        return "Cititor: " + nume + " " + prenume + " (Cod: " + cod + ")";
    }
}
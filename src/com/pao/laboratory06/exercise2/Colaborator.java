package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;
    protected TipColaborator tip;

    public Colaborator(TipColaborator tip) {
        this.tip = tip;
    }

    public abstract double calculeazaVenitNetAnual();

    public TipColaborator getTip() {
        return tip;
    }

    public String getNumeComplet() {
        return nume + " " + prenume;
    }

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
    }

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s, venit net anual: %.2f lei%n",
                tipContract(),
                getNumeComplet(),
                calculeazaVenitNetAnual());
    }
}
package com.pao.laboratory06.exercise2;

public class PFAColaborator extends PersoanaFizica {
    private static final double SALARIU_MINIM_ANUAL = 4050 * 12;

    private double cheltuieliLunare;

    public PFAColaborator() {
        super(TipColaborator.PFA);
    }

    @Override
    public void citeste(java.util.Scanner in) {
        super.citeste(in);
        venitBrutLunar = in.nextDouble();
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetBrut = (venitBrutLunar - cheltuieliLunare) * 12;

        double impozit = venitNetBrut * 0.10;

        double cass;
        if (venitNetBrut < 6 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * (6 * SALARIU_MINIM_ANUAL);
        } else if (venitNetBrut <= 72 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * venitNetBrut;
        } else {
            cass = 0.10 * (72 * SALARIU_MINIM_ANUAL);
        }

        double cas;
        if (venitNetBrut < 12 * SALARIU_MINIM_ANUAL) {
            cas = 0;
        } else if (venitNetBrut <= 24 * SALARIU_MINIM_ANUAL) {
            cas = 0.25 * (12 * SALARIU_MINIM_ANUAL);
        } else {
            cas = 0.25 * (24 * SALARIU_MINIM_ANUAL);
        }

        return venitNetBrut - impozit - cass - cas;
    }

    @Override
    public String tipContract() {
        return "PFA";
    }
}
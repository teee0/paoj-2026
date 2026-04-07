package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    public CIMColaborator() {
        super(TipColaborator.CIM);
    }

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        venitBrutLunar = in.nextDouble();

        if (in.hasNext()) {
            String bonusText = in.next();
            bonus = bonusText.equalsIgnoreCase("DA");
        }
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venit = venitBrutLunar * 12 * 0.55;

        if (bonus) {
            venit *= 1.10;
        }

        return venit;
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }
}
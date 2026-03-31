package com.pao.laboratory05.biblioteca;

import java.util.Comparator;

public class CarteAutorComparator implements Comparator<Carte>
{
    @Override
    public int compare ( Carte c1, Carte c2)
    {
        return CharSequence.compare(c1.getTitlu(), c2.getTitlu());
    }
}

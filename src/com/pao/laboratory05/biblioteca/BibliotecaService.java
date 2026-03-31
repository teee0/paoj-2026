package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti;
    private BibliotecaService() {
        this.carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return BibliotecaService.Holder.INSTANCE;
    }


    public void listSortedByRating() {
        if (carti.length == 0) {
            System.out.println("Nu există cărți.");
            return;
        }
        Carte[] tmp = carti.clone();
        Arrays.sort(tmp);

        for (int i = 0; i < tmp.length; i++) {
            System.out.println((i + 1) + ". " + tmp[i]);
        }
    }

    public void listSortedBy (Comparator<Carte> comparator)
    {
        if (carti.length == 0) {
            System.out.println("Nu există cărți.");
            return;
        }
        Carte[] tmp = carti.clone();
        Arrays.sort(tmp,comparator);

        for (int i = 0; i < tmp.length; i++) {
            System.out.println((i + 1) + ". " + tmp[i]);
        }
    }

    public void addCarte(Carte carte) {
        Carte[] tmp = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, tmp, 0, carti.length);
        tmp[tmp.length - 1] = carte;
        carti = tmp;
        System.out.println("Carte adaugată: " + carte.getTitlu() );
    }

}


// Fisier: src/com/pao/project/service/GestiuneService.java
package com.pao.project.service;

import com.pao.project.exception.ItemNedisponibilException;
import com.pao.project.exception.PersoanaInexistentaException;
import com.pao.project.model.*;

import java.util.*;

public class GestiuneService {
    private static GestiuneService instance;

    // Map pentru cititori indexati dupa cod
    private Map<String, Cititor> cititoriMap;
    private List<Imprumut> imprumuturiActive;

    private GestiuneService() {
        cititoriMap = new HashMap<>();
        imprumuturiActive = new ArrayList<>();
    }

    public static GestiuneService getInstance() {
        if (instance == null) {
            instance = new GestiuneService();
        }
        return instance;
    }

    public void inregistreazaCititor(Cititor c) {
        if (c != null && c.getCod() != null) {
            cititoriMap.put(c.getCod(), c);
        }
    }

    public void stergeCititor(String cod) {
        cititoriMap.remove(cod);
        System.out.println("Cititorul cu codul " + cod + " a fost sters.");
    }

    public boolean verificaDisponibilitate(Item item) {
        for (Imprumut i : imprumuturiActive) {
            if (i.getItem().equals(item)) return false; // E deja imprumutat
        }
        return true;
    }

    public void imprumutaItem(String codCititor, Item item) throws PersoanaInexistentaException, ItemNedisponibilException {
        Cititor c = cititoriMap.get(codCititor);
        if (c == null) throw new PersoanaInexistentaException("Eroare: Cititorul nu exista in sistem!");
        if (item == null) throw new ItemNedisponibilException("Eroare: Item invalid.");
        if (!verificaDisponibilitate(item)) throw new ItemNedisponibilException("Eroare: Itemul este deja imprumutat!");

        imprumuturiActive.add(new Imprumut(c, item));
        System.out.println("Succes: " + item.getTitlu() + " a fost imprumutat de " + c.getNume());
    }

    public void returneazaItem(Item item) {
        imprumuturiActive.removeIf(i -> i.getItem().equals(item));
        System.out.println("Itemul '" + item.getTitlu() + "' a fost returnat.");
    }

    public void afiseazaImprumuturiCititor(String codCititor) {
        System.out.println("--- Imprumuturi pentru " + codCititor + " ---");
        boolean gasit = false;
        for (Imprumut i : imprumuturiActive) {
            if (i.getCititor().getCod().equals(codCititor)) {
                System.out.println(i);
                gasit = true;
            }
        }
        if (!gasit) System.out.println("Niciun imprumut activ.");
    }
}
// Fisier: src/com/pao/project/service/CatalogService.java
package com.pao.project.service;

import com.pao.project.model.Item;
import com.pao.project.model.Autor;
import java.util.*;

public class CatalogService {
    private static CatalogService instance;


    private Set<Item> catalog;
    private Set<Autor> catalog_autor;

    private CatalogService() {
        catalog = new TreeSet<>();
        catalog_autor = new TreeSet<>();
    }

    public static CatalogService getInstance() {
        if (instance == null) {
            instance = new CatalogService();
        }
        return instance;
    }

    public void adaugaItem(Item item) {
        if (item != null) catalog.add(item);
    }
    public void adaugaAutor(Autor autor) {
        if (autor != null) catalog_autor.add(autor);
    }

    public Autor cautaAutor(String nume)
    {
        for (Autor a : catalog_autor) {
            if (a.getNume().equalsIgnoreCase(nume)) {
                return a;
            }
        }
        return null;
    }

    public void afiseazaCatalog() {
        System.out.println("--- Catalog Complet ---");
        for (Item item : catalog) {
            System.out.println(item);
        }
    }

    public Item cautaDupaTitlu(String titlu) {
        for (Item item : catalog) {
            if (item.getTitlu().equalsIgnoreCase(titlu)) {
                return item;
            }
        }
        return null;
    }

    public void afiseazaGrupatDupaTip() {
        System.out.println("--- Itemuri grupate dupa tip (Map) ---");
        Map<String, List<Item>> grupare = new HashMap<>();

        for (Item item : catalog) {
            String tip = item.getClass().getSimpleName();
            grupare.putIfAbsent(tip, new ArrayList<>());
            grupare.get(tip).add(item);
        }

        for (Map.Entry<String, List<Item>> entry : grupare.entrySet()) {
            System.out.println("Tip: " + entry.getKey());
            for (Item i : entry.getValue()) {
                System.out.println("  - " + i.getTitlu());
            }
        }
    }
}
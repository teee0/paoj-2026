package com.pao.project.service;

import com.pao.project.model.Autor;
import com.pao.project.model.Item;
import com.pao.project.repository.AutorRepository;
import com.pao.project.repository.ItemRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogService {
    private static CatalogService instance;

    private final AutorRepository autorRepository = new AutorRepository();
    private final ItemRepository itemRepository = new ItemRepository();
    private final AuditService auditService = AuditService.getInstance();

    private CatalogService() {
    }

    public static CatalogService getInstance() {
        if (instance == null) {
            instance = new CatalogService();
        }
        return instance;
    }

    public void adaugaItem(Item item) {
        if (item == null) {
            return;
        }
        itemRepository.save(item);
        auditService.logActiune("adauga_item");
    }

    public void adaugaAutor(Autor autor) {
        if (autor == null) {
            return;
        }
        autorRepository.save(autor);
        auditService.logActiune("adauga_autor");
    }

    public Autor cautaAutor(String nume) {
        auditService.logActiune("cauta_autor");
        return autorRepository.findByNume(nume).orElse(null);
    }

    public void afiseazaCatalog() {
        auditService.logActiune("listeaza_catalog");
        System.out.println("--- Catalog Complet ---");
        for (Item item : itemRepository.findAll()) {
            System.out.println(item);
        }
    }

    public Item cautaDupaTitlu(String titlu) {
        auditService.logActiune("cauta_item");
        return itemRepository.findByTitlu(titlu).orElse(null);
    }

    public void afiseazaGrupatDupaTip() {
        auditService.logActiune("grupeaza_dupa_tip");
        System.out.println("--- Itemuri grupate dupa tip (Map) ---");
        Map<String, List<Item>> grupare = new HashMap<>();

        for (Item item : itemRepository.findAll()) {
            String tip = item.getClass().getSimpleName();
            grupare.computeIfAbsent(tip, k -> new ArrayList<>()).add(item);
        }

        for (Map.Entry<String, List<Item>> entry : grupare.entrySet()) {
            System.out.println("Tip: " + entry.getKey());
            for (Item i : entry.getValue()) {
                System.out.println("  - " + i.getTitlu());
            }
        }
    }

    public void afiseazaTopItemeImprumutate(int limit) {
        auditService.logActiune("afiseaza_top_iteme_imprumutate");
        System.out.println("--- Top iteme împrumutate ---");
        for (String linie : itemRepository.findTopItemeImprumutate(limit)) {
            System.out.println(linie);
        }
    }
}

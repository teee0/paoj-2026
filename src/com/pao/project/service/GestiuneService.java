package com.pao.project.service;

import com.pao.project.exception.ItemNedisponibilException;
import com.pao.project.exception.PersoanaInexistentaException;
import com.pao.project.model.Cititor;
import com.pao.project.model.Imprumut;
import com.pao.project.model.Item;
import com.pao.project.repository.CititorRepository;
import com.pao.project.repository.ImprumutRepository;

public class GestiuneService {
    private static GestiuneService instance;

    private final CititorRepository cititorRepository = new CititorRepository();
    private final ImprumutRepository imprumutRepository = new ImprumutRepository();
    private final AuditService auditService = AuditService.getInstance();

    private GestiuneService() {
    }

    public static GestiuneService getInstance() {
        if (instance == null) {
            instance = new GestiuneService();
        }
        return instance;
    }

    public void inregistreazaCititor(Cititor c) {
        if (c == null || c.getCod() == null) {
            return;
        }
        cititorRepository.save(c);
        auditService.logActiune("inregistreaza_cititor");
    }

    public void stergeCititor(String cod) {
        cititorRepository.deleteByCod(cod);
        auditService.logActiune("elimina_cititor");
        System.out.println("Cititorul cu codul " + cod + " a fost sters.");
    }

    public boolean verificaDisponibilitate(Item item) {
        auditService.logActiune("verifica_disponibilitate");
        if (item == null) {
            return false;
        }
        return !imprumutRepository.isItemImprumutat(item.getId());
    }

    public void imprumutaItem(String codCititor, Item item)
            throws PersoanaInexistentaException, ItemNedisponibilException {
        Cititor c = cititorRepository.findByCod(codCititor)
                .orElseThrow(() -> new PersoanaInexistentaException("Eroare: Cititorul nu exista in sistem!"));
        if (item == null) {
            throw new ItemNedisponibilException("Eroare: Item nu exista in sistem.");
        }
        if (imprumutRepository.isItemImprumutat(item.getId())) {
            throw new ItemNedisponibilException("Eroare: Item-ul este deja imprumutat!");
        }

        imprumutRepository.save(new Imprumut(c, item));
        auditService.logActiune("imprumuta_item");
        System.out.println("Succes: " + item.getTitlu() + " a fost imprumutat de " + c.getNume());
    }

    public void returneazaItem(Item item) {
        if (item == null) {
            return;
        }
        imprumutRepository.deleteByItemId(item.getId());
        auditService.logActiune("returneaza_item");
        System.out.println("Item-ul '" + item.getTitlu() + "' a fost returnat.");
    }

    public void afiseazaImprumuturiCititor(String codCititor) {
        auditService.logActiune("afiseaza_imprumuturi");
        System.out.println("--- Imprumuturi pentru " + codCititor + " ---");
        var imprumuturi = imprumutRepository.findByCititorCod(codCititor);
        if (imprumuturi.isEmpty()) {
            System.out.println("Niciun imprumut activ.");
            return;
        }
        for (Imprumut i : imprumuturi) {
            System.out.println(i);
        }
    }

    public void afiseazaImprumuturiActiveCuDetalii() {
        auditService.logActiune("afiseaza_imprumuturi_active_detalii");
        System.out.println("--- Toate împrumuturile active (JOIN) ---");
        for (String linie : imprumutRepository.findImprumuturiActiveCuDetalii()) {
            System.out.println(linie);
        }
    }

    public void afiseazaCititoriCuNumarImprumuturi() {
        auditService.logActiune("afiseaza_cititori_cu_numar_imprumuturi");
        System.out.println("--- Cititori cu număr împrumuturi (JOIN) ---");
        for (String linie : cititorRepository.findCititoriCuNumarImprumuturi()) {
            System.out.println(linie);
        }
    }
}

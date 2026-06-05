package com.pao.project;

import com.pao.project.exception.ItemNedisponibilException;
import com.pao.project.exception.PersoanaInexistentaException;
import com.pao.project.model.*;
import com.pao.project.service.AuditService;
import com.pao.project.service.CatalogService;
import com.pao.project.service.GestiuneService;

//audit-ul se face si in servicii

public class Main {
    public static void main(String[] args) {
        CatalogService catalogService = CatalogService.getInstance();
        GestiuneService gestiuneService = GestiuneService.getInstance();
        AuditService auditService = AuditService.getInstance();

        // Creare Autori (persistați în DB)
        Autor nolan = new Autor("Nolan", "Christopher", 1970, "Cinema");
        Autor pinkFloyd = new Autor("Pink", "Floyd", 1965, "Muzica");
        catalogService.adaugaAutor(nolan);
        catalogService.adaugaAutor(pinkFloyd);

        // 1. Adaugă un item nou
        Item f1 = new Film("F01", "Interstellar", 2014, 169, nolan);
        Item m1 = new ItemMuzica("M01", "The Dark Side of the Moon", 1973, 42, pinkFloyd);
        Item f2 = new Film("F02", "Inception", 2010, 148, nolan);

        catalogService.adaugaItem(f1);
        catalogService.adaugaItem(m1);
        catalogService.adaugaItem(f2);

        // 2. Înregistrează un cititor
        Cititor c1 = new Cititor("Popescu", "Ion", 1990, "CIT-001");
        Cititor c2 = new Cititor("Ionescu", "Ana", 1995, "CIT-002");
        gestiuneService.inregistreazaCititor(c1);
        gestiuneService.inregistreazaCititor(c2);

        // 6. Listează tot catalogul
        catalogService.afiseazaCatalog();
        System.out.println();

        // 7. Grupează și afișează itemii după tip
        catalogService.afiseazaGrupatDupaTip();
        System.out.println();

        // 5. Caută un item după titlu
        System.out.println("Cautare: " + catalogService.cautaDupaTitlu("Inception"));
        System.out.println();

        // 8. Verifică disponibilitatea
        System.out.println("E disponibil Interstellar? " +
                (gestiuneService.verificaDisponibilitate(f1) ? "da" : "nu"));
        System.out.println();

        try {
            // 3. Împrumută un item
            gestiuneService.imprumutaItem("CIT-001", f1);

            // Testăm excepțiile (re-împrumutare)
            gestiuneService.imprumutaItem("CIT-002", f1);
        } catch (PersoanaInexistentaException | ItemNedisponibilException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        // 9. Afișează istoricul de împrumuturi pentru un cititor
        gestiuneService.afiseazaImprumuturiCititor("CIT-001");
        System.out.println();

        // Interogări JOIN suplimentare
        gestiuneService.afiseazaImprumuturiActiveCuDetalii();
        System.out.println();
        gestiuneService.afiseazaCititoriCuNumarImprumuturi();
        System.out.println();
        catalogService.afiseazaTopItemeImprumutate(5);
        System.out.println();

        // 4. Returnează un item
        gestiuneService.returneazaItem(f1);

        System.out.println("E disponibil Interstellar dupa returnare? " +
                (gestiuneService.verificaDisponibilitate(f1) ? "da" : "nu"));
        System.out.println();

        // 10. Elimină un cititor
        gestiuneService.stergeCititor("CIT-002");

        System.out.println("\nAudit log salvat în audit.csv");
        auditService.logActiune("finalizare_demo");
    }
}

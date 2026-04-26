package com.pao.project;

import com.pao.project.exception.ItemNedisponibilException;
import com.pao.project.exception.PersoanaInexistentaException;
import com.pao.project.model.*;
import com.pao.project.service.CatalogService;
import com.pao.project.service.GestiuneService;

public class Main {
    public static void main(String[] args) {
        CatalogService catalogService = CatalogService.getInstance();
        GestiuneService gestiuneService = GestiuneService.getInstance();

        // 1. Adaugă un item nou
        Item f1 = new Film("F01", "Interstellar", 2014, 169, "Christopher Nolan");
        Item m1 = new ItemMuzica("M01", "The Dark Side of the Moon", 1973, 42, "Pink Floyd");
        Item f2 = new Film("F02", "Inception", 2010, 148, "Christopher Nolan");

        catalogService.adaugaItem(f1);
        catalogService.adaugaItem(m1);
        catalogService.adaugaItem(f2);

        // 2. Înregistrează un cititor
        Cititor c1 = new Cititor("Popescu", "Ion", 1990, "CIT-001");
        Cititor c2 = new Cititor("Ionescu", "Ana", 1995, "CIT-002");
        gestiuneService.inregistreazaCititor(c1);
        gestiuneService.inregistreazaCititor(c2);

        // 6. Listează tot catalogul (va fi sortat alfabetic datorita TreeSet)
        catalogService.afiseazaCatalog();

        // 7. Grupează și afișează itemii după tip (Map)
        catalogService.afiseazaGrupatDupaTip();

        // 5. Caută un item după titlu
        System.out.println("\nCautare: " + catalogService.cautaDupaTitlu("Inception"));

        // 8. Verifică disponibilitatea
        System.out.println("E disponibil Interstellar? " + gestiuneService.verificaDisponibilitate(f1));

        try {
            // 3. Împrumută un item
            gestiuneService.imprumutaItem("CIT-001", f1);

            // Testam exceptiile (re-imprumutare)
            gestiuneService.imprumutaItem("CIT-002", f1);
        } catch (PersoanaInexistentaException | ItemNedisponibilException e) {
            System.out.println(e.getMessage());
        }

        // 9. Afișează istoricul de împrumuturi pentru un cititor
        gestiuneService.afiseazaImprumuturiCititor("CIT-001");

        // 4. Returnează un item
        gestiuneService.returneazaItem(f1);

        // Verificam iar disponibilitatea dupa returnare
        System.out.println("E disponibil Interstellar dupa returnare? " + gestiuneService.verificaDisponibilitate(f1));

        // 10. Elimină un cititor
        gestiuneService.stergeCititor("CIT-002");
    }
}
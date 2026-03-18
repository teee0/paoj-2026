package com.pao.laboratory03.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Exercițiul 1 — Colecții: HashMap și TreeMap
 */
public class Main {
    public static void main(String[] args) {

        // === PARTEA A — HashMap (frecvența cuvintelor) ===
        System.out.println("=== PARTEA A: HashMap — frecvența cuvintelor ===");

        // 1. Declararea array-ului
        String[] words = {"java", "python", "java", "c++", "python", "java", "rust", "c++", "go"};

        // 2. Crearea HashMap-ului pentru contorizare
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : words) {
            // Dacă cuvântul nu există, returnează 0. Apoi adună 1.
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }

        // 3. Afișarea map-ului
        System.out.println("Frecvență: " + wordFrequency);

        // 4. Verificare cheie "rust"
        System.out.println("Conține 'rust'? " + wordFrequency.containsKey("rust"));

        // 5. Afișare DOAR chei și DOAR valori
        System.out.println("Chei: " + wordFrequency.keySet());
        System.out.println("Valori: " + wordFrequency.values());

        // 6. Parcurgere cu entrySet()
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println(); // Linie liberă pentru lizibilitate


        // === PARTEA B — TreeMap (sortare automată) ===
        System.out.println("=== PARTEA B: TreeMap — sortare automată ===");

        // 7. Crearea TreeMap-ului din HashMap-ul anterior
        TreeMap<String, Integer> sortedMap = new TreeMap<>(wordFrequency);

        // 8. Afișare TreeMap
        System.out.println("Sortat: " + sortedMap);

        // 9. Prima și ultima cheie
        System.out.println("Prima cheie: " + sortedMap.firstKey());
        System.out.println("Ultima cheie: " + sortedMap.lastKey());

        System.out.println();


        // === PARTEA C — Map cu obiecte ===
        System.out.println("=== PARTEA C: Map cu obiecte ===");

        // 10. Crearea map-ului cu materii și liste de studenți
        Map<String, List<String>> courseStudents = new HashMap<>();

        // Folosim `new ArrayList<>(...)` pentru a ne asigura că listele pot fi modificate ulterior
        courseStudents.put("PAOJ", new ArrayList<>(Arrays.asList("Ana", "Mihai", "Ion")));
        courseStudents.put("BD", new ArrayList<>(Arrays.asList("Ana", "Elena")));

        // 11. Afișare studenți de la "PAOJ"
        System.out.println("Studenți la PAOJ: " + courseStudents.get("PAOJ"));

        // 12. Adăugare student nou la "BD"
        courseStudents.get("BD").add("George");
        System.out.println("Studenți la BD (actualizat): " + courseStudents.get("BD"));
    }
}
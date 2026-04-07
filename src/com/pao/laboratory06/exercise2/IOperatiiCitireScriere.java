package com.pao.laboratory06.exercise2;
import java.util.Scanner;
public interface IOperatiiCitireScriere {
    void citeste(Scanner in); // — citește datele obiectului din input
    void afiseaza(); // — afișează datele obiectului în formatul cerut
    String tipContract(); // — returnează tipul contractului
    default boolean areBonus() { return false; }
}

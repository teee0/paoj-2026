package com.pao.laboratory00;

import java.util.Scanner;
/**
 * Exercitiul 2
 *
 * Cititi de la tastatura o matrice de n ori n elemente REALE.
 *
 * 1. Afisati matricea in consola.
 * 2. Afisati suma elementelor de pe diagonala principala
 *    si produsul elementelor de pe diagonala secundara.
 *
 */

public class DiagonaleleMatricei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("n: ");
        int n = scanner.nextInt();

        double[][] matrice = new double[n][n];

        System.out.println("Matricea:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrice[i][j] = scanner.nextDouble();
            }
        }

        //afisare matrice
        System.out.println("\nMatricea este:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%.2f ", matrice[i][j]);
            }
            System.out.println();
        }

        // 2. Calculul sumei si produsului
        double suma = 0;
        double produs = 1;

        for (int i = 0; i < n; i++) {
            // diagonala principala
            suma += matrice[i][i];

            // diagonala secundara
            produs *= matrice[i][n - 1 - i];
        }

        // Afisarea rezultatelor
        System.out.println("Suma elementelor de pe diagonala principala: " + suma);
        System.out.println("Produsul elementelor de pe diagonala secundara: " + produs);

        scanner.close();
    }
}

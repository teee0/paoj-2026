package com.pao.laboratory05.audit;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează audit log");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune) {
                case 1 -> {
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine();
                    System.out.print("Departament (nume): ");
                    String deptNume = scanner.nextLine();
                    System.out.print("Departament (locatie): ");
                    String deptLoc = scanner.nextLine();
                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();

                    service.addAngajat(new Angajat(nume, new Departament(deptNume, deptLoc), salariu));
                }
                case 2 -> service.listBySalary();
                case 3 -> {
                    System.out.print("Departament: ");
                    String cautaDept = scanner.nextLine();
                    service.findByDepartament(cautaDept);
                }
                case 4 -> service.printAuditLog();
                case 0 -> {
                    System.out.println("La revedere!");
                    return;
                }
                default -> System.out.println("Opțiune invalidă.");
            }
        }
    }
}
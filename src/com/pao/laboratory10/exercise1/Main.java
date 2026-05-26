package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNext()) {
            String comanda = scanner.next();
            switch (comanda) {

                //   ENQUEUE id suma data tip   → addLast  (niciun output)
                case "ENQUEUE":
                    coada.addLast(citesteTranzactie(scanner));
                    break;
                //   DEQUEUE                    → removeFirst sau "Coada goala."
                //                                format: "Procesat: [id] data tip: suma RON"
                case "DEQUEUE":
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        System.out.println("Procesat: " + coada.removeFirst());
                    }
                    break;
                //   PUSH id suma data tip      → addFirst  (niciun output)
                case "PUSH":
                    coada.addFirst(citesteTranzactie(scanner));
                    break;
                //   POP                        → removeFirst sau "Coada goala."
                //                                format: "Extras: [id] data tip: suma RON"
                case "POP":
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        System.out.println("Extras: " + coada.removeFirst());
                    }
                    break;
                //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
                //                                afișează "Eliminat N tranzactii DEBIT."
                case "REMOVE_DEBIT":
                    int eliminariDebit = 0;
                    Iterator<Tranzactie> itDebit = coada.iterator();
                    while (itDebit.hasNext()) {
                        if (itDebit.next().getTip() == TipTranzactie.DEBIT) {
                            itDebit.remove();
                            eliminariDebit++;
                        }
                    }
                    System.out.println("Eliminat " + eliminariDebit + " tranzactii DEBIT.");
                    break;
                //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
                //                                afișează "Eliminat N tranzactii sub threshold RON."
                case "REMOVE_BELOW":
                    double prag = Double.parseDouble(scanner.next());
                    int eliminariPrag = 0;
                    Iterator<Tranzactie> itPrag = coada.iterator();
                    while (itPrag.hasNext()) {
                        if (itPrag.next().getSuma() < prag) {
                            itPrag.remove();
                            eliminariPrag++;
                        }
                    }
                    System.out.format(Locale.US, "Eliminat %d tranzactii sub %.2f RON.\n", eliminariPrag, prag);
                    break;
                //   PRINT                      → afișează toate, câte una pe linie
                case "PRINT":
                    for (Tranzactie t : coada) {
                        System.out.println(t);
                    }
                    break;
                //   SIZE                       → "Dimensiune coada: N"
                case "SIZE":
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
            }
        }
        scanner.close();
    }

    private static Tranzactie citesteTranzactie(Scanner sc) {
        int id = Integer.parseInt(sc.next());
        double suma = Double.parseDouble(sc.next());
        String data = sc.next();
        TipTranzactie tip = TipTranzactie.valueOf(sc.next());
        return new Tranzactie(id, suma, data, tip);
    }
}
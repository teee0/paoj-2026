package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        int N = scanner.nextInt();
        for (int i=0; i<N; i++) coada.addLast(citesteTranzactie(scanner));

        // 2. Procesează comenzile din stdin până la EOF:
        //
        while (scanner.hasNext()) {
            String comanda = scanner.next();
            switch (comanda) {
                case "UNIQUE_IDS":
                    LinkedHashSet<Integer> id_uri = new LinkedHashSet<Integer>();
                    for (int i = 0; i < coada.size(); i++) {
                        id_uri.add(coada.get(i).getId());
                    }
                    System.out.println("IDs unice (" + id_uri.size() + "): " + id_uri);
                    break;

                case "SORT_ASC":
                    coada.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                    coada.forEach(System.out::println);
                    break;
                case "SORT_DESC":
                    coada.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    coada.forEach(System.out::println);
                    break;
                case "REVERSE":
                    coada = coada.reversed();
                    break;
                case "MIN_MAX":
                    Optional<Tranzactie> min = coada.stream().min(Comparator.comparingDouble(Tranzactie::getSuma));
                    Optional<Tranzactie> max = coada.stream().max(Comparator.comparingDouble(Tranzactie::getSuma));
                    System.out.println("MIN: " + min.get());
                    System.out.println("MAX: " + max.get());
                    break;
                case "CME_DEMO":
                    try {
                        for (Tranzactie t : coada) {
                            coada.remove(t);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                case "MONTHLY_REPORT":
                    Map<String, double[]> raport = new TreeMap<>();
                    for (Tranzactie t : coada) {
                        String luna = t.getData().substring(0, 7);
                        raport.putIfAbsent(luna, new double[2]); // [0] -> CREDIT, [1] -> DEBIT
                        if (t.getTip() == TipTranzactie.CREDIT) {
                            raport.get(luna)[0] += t.getSuma();
                        } else {
                            raport.get(luna)[1] += t.getSuma();
                        }
                    }
                    raport.forEach((luna, sume) ->
                            System.out.format(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON\n",
                                    luna, sume[0], sume[1]));
                    break;

                case "TOP":
                    int topN = scanner.nextInt();
                    List<Tranzactie> copieTop = new ArrayList<>(coada);
                    copieTop.sort((t1, t2) -> Double.compare(t2.getSuma(), t1.getSuma()));
                    System.out.println("Top " + topN + ":");
                    int limita = Math.min(topN, copieTop.size());
                    for (Tranzactie t : copieTop.subList(0, limita)) {
                        System.out.println(t);
                    }
                    break;
            }
        }
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON
    }

    private static Tranzactie citesteTranzactie(Scanner sc) {
        int id = Integer.parseInt(sc.next());
        double suma = Double.parseDouble(sc.next());
        String data = sc.next();
        TipTranzactie tip = TipTranzactie.valueOf(sc.next());
        return new Tranzactie(id, suma, data, tip);
    }
}

package com.pao.laboratory03.enums;

public class Main {
    public static void main(String[] args) {

        // a) Parcurge toate valorile
        System.out.println("=== Toate prioritățile ===");
        for (Priority p : Priority.values()) {
            System.out.println(p.getEmoji() + " " + p.name() +
                    " (level=" + p.getLevel() + ", color=" + p.getColor() + ")");
        }

        // b) Switch pe un Priority
        System.out.println("\n=== Switch pe prioritate ===");
        Priority current = Priority.HIGH;
        switch (current) {
            case LOW, MEDIUM -> System.out.println("Totul este sub control.");
            case HIGH, CRITICAL -> System.out.println("⚠️ Atenție! Prioritate ridicată!");
        }

        // c) Convertește String în Priority
        System.out.println("\n=== valueOf ===");
        Priority highPriority = Priority.valueOf("HIGH");
        System.out.println("Priority.valueOf(\"HIGH\") = " + highPriority);

        // d) Demonstrează compararea (folosind ==)
        System.out.println("\n=== Comparare enum ===");
        System.out.println("HIGH == HIGH? " + (highPriority == Priority.HIGH));
        System.out.println("HIGH == LOW? " + (highPriority == Priority.LOW));

        // e) name() și ordinal()
        System.out.println("\n=== name() și ordinal() ===");
        for (Priority p : Priority.values()) {
            System.out.println(p.name() + ": name=" + p.name() + ", ordinal=" + p.ordinal());
        }
    }
}
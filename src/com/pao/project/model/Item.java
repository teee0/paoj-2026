package com.pao.project.model;
import java.util.Objects;

public abstract class Item implements Comparable<Item> {
    protected String id;
    protected String titlu;
    protected int anAparitie;

    public Item(String id, String titlu, int anAparitie) {
        this.id = id;
        this.titlu = titlu;
        this.anAparitie = anAparitie;
    }

    public String getId() { return id; }
    public String getTitlu() { return titlu; }
    public int getAnAparitie() { return anAparitie; }

    @Override
    public int compareTo(Item altItem) {
        // Sortare naturala dupa titlu
        return this.titlu.compareToIgnoreCase(altItem.getTitlu());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
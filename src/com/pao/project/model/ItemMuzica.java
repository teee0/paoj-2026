package com.pao.project.model;

public class ItemMuzica extends ItemAudiovizual {
    public ItemMuzica(String id, String titlu, int anAparitie, int durataMinute, Autor autor) {
        super(id, titlu, anAparitie, durataMinute, autor);
    }

    @Override
    public String toString() {
        return "Muzica: " + titlu + " - " + autor.getNume() + " " + autor.getPrenume() + " (" + anAparitie + ")";
    }
}
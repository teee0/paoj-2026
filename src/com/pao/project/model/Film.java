package com.pao.project.model;

public class Film extends ItemAudiovizual {
    public Film(String id, String titlu, int anAparitie, int durataMinute, Autor autor) {
        super(id, titlu, anAparitie, durataMinute, autor);
    }

    @Override
    public String toString() {
        return "Film: " + titlu + " (" + anAparitie + "), Regizor: " + autor.getNume() + " " + autor.getPrenume();
    }
}
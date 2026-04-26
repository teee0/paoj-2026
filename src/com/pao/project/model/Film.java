package com.pao.project.model;

public class Film extends ItemAudiovizual {
    private String regizor;

    public Film(String id, String titlu, int anAparitie, int durataMinute, String regizor) {
        super(id, titlu, anAparitie, durataMinute);
        this.regizor = regizor;
    }

    @Override
    public String toString() {
        return "Film: " + titlu + " (" + anAparitie + "), Regizor: " + regizor;
    }
}
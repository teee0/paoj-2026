package com.pao.project.model;

public class ItemMuzica extends ItemAudiovizual {
    private String artist;

    public ItemMuzica(String id, String titlu, int anAparitie, int durataMinute, String artist) {
        super(id, titlu, anAparitie, durataMinute);
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Muzica: " + titlu + " - " + artist + " (" + anAparitie + ")";
    }
}
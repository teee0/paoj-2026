package com.pao.project.model;

public abstract class ItemAudiovizual extends Item {
    protected int durataMinute;

    public ItemAudiovizual(String id, String titlu, int anAparitie, int durataMinute, Autor autor) {
        super(id, titlu, anAparitie, autor);
        this.durataMinute = durataMinute;
    }

    public int getDurataMinute() { return durataMinute; }
}
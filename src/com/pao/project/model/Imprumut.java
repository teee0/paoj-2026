package com.pao.project.model;
import java.time.LocalDate;

public final class Imprumut {
    private final int id;
    private final Cititor cititor;
    private final Item item;
    private final LocalDate dataImprumut;

    public Imprumut(Cititor cititor, Item item) {
        this(0, cititor, item, LocalDate.now());
    }

    public Imprumut(int id, Cititor cititor, Item item, LocalDate dataImprumut) {
        this.id = id;
        this.cititor = cititor;
        this.item = item;
        this.dataImprumut = dataImprumut;
    }

    public int getId() { return id; }
    public Cititor getCititor() { return cititor; }
    public Item getItem() { return item; }
    public LocalDate getDataImprumut() { return dataImprumut; }

    @Override
    public String toString() {
        return "Imprumut: [" + item.getTitlu() + "] de catre [" + cititor.getNume() + "] la data " + dataImprumut;
    }
}
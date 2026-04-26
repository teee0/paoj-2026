package com.pao.project.model;
import java.time.LocalDate;

public final class Imprumut {
    private final Cititor cititor;
    private final Item item;
    private final LocalDate dataImprumut;

    public Imprumut(Cititor cititor, Item item) {
        this.cititor = cititor;
        this.item = item;
        this.dataImprumut = LocalDate.now();
    }

    public Cititor getCititor() { return cititor; }
    public Item getItem() { return item; }
    public LocalDate getDataImprumut() { return dataImprumut; }

    @Override
    public String toString() {
        return "Imprumut: [" + item.getTitlu() + "] de catre [" + cititor.getNume() + "] la data " + dataImprumut;
    }
}
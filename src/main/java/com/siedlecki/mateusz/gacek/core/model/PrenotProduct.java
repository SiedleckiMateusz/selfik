package com.siedlecki.mateusz.gacek.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class PrenotProduct {
    private String id;
    private int qtySales;
    private int qtyBuffer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrenotProduct that = (PrenotProduct) o;
        return Objects.equals(id, that.id);
    }

    public void addQtyBuffer(int addingQty){
        this.qtyBuffer += addingQty;
    }

    public void addQtySales(int addingQty){
        this.qtySales += addingQty;
    }

    @Override
    public String toString() {
        return "PrenotProduct{" +
                "numberId='" + id + '\'' +
                ", qtySales=" + qtySales +
                ", qtyBuffor=" + qtyBuffer +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

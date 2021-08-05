package com.siedlecki.mateusz.gacek.core.model;

import com.siedlecki.mateusz.gacek.core.model.opq.PickingInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.TreeSet;

@Builder
@Getter
public class IkeaProduct {
    private final String specshop;
    private final String rangeGroup;
    private final String numberId;
    private final String name;
    private TreeSet<String> locations;
    private final Integer fcst;
    private Integer assq;
    private final Double avgSales;
    private final Integer palQty;
    private Integer availableStock;
    private final Integer sgf;
    private final Double volume;
    private int prenotSales;
    private int prenotBuffer;
    @Setter
    private PickingInfo pickingInfo;

    public int onSalePlaces() {
        return availableStock + (pickingInfo != null ? pickingInfo.getQtyAfterDay() : 0) - sgf;
    }

    public int freeSpace() {
        return (assq - onSalePlaces() - prenotSales);
    }

    public int bufferAndSgf() {
        return sgf + prenotBuffer;
    }

    public int toL23Order() {
        int min = Math.min(freeSpace(), bufferAndSgf());
        return Math.max(min, 0);
    }

    public int toL23OrderPQ() {
        return toL23Order() / palQty;
    }

    public int freeSpaceAfterOrder() {
        return freeSpace() - toL23Order();
    }

    public int freeSpaceBeforePrenot() {
        return (assq - onSalePlaces());
    }

    public double freeSpaceBeforePrenotPQ() {
        return ((double) freeSpaceBeforePrenot() / palQty);
    }

    public double prenotSalesPQ() {
        return (double) prenotSales / palQty;
    }

    public double bufferAndSgfPQ() {
        return (double) bufferAndSgf() / palQty;
    }

    public void addAssq(int ssqLocation) {
        this.assq += ssqLocation;
    }

    public void addPrenotSalesQty(int qty) {
        prenotSales += qty;
    }

    public void addPrenotBufferQty(int qty) {
        prenotBuffer += qty;
    }

    @Override
    public String toString() {
        return "numId: " + numberId + ", " + name + ", locations=" + locations + ", assq: "
                + assq + ", refill level: " + onSalePlaces() + " free space: "
                + freeSpace() + "/" + freeSpace() / palQty + "pallets";
    }
}

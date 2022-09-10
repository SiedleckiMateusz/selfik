package com.siedlecki.mateusz.gacek.core.model;

import com.siedlecki.mateusz.gacek.core.Constants;
import com.siedlecki.mateusz.gacek.core.model.opq.PickingInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
public class IkeaProduct {
    private final String id;
    private final String name;
    private Set<Location> locations;
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
    @Setter
    private ProductStatus status;

    public int onSalePlaces() {
        return availableStock + (pickingInfo != null ? pickingInfo.getQtyAfterDay() : 0) - sgf;
    }

    public int freeSpace() {
        return (assq - onSalePlaces() - prenotSales);
    }

    public int bufferAndSgf() {
        return sgf + prenotBuffer;
    }

    public int l23Order() {
        int min = Math.min(freeSpace(), bufferAndSgf());
        return Math.max(min, 0);
    }

    public float l23OrderPQ() {
        return (float) l23Order() / palQty;
    }

    public int l23OrderToFullPal() {
        return l23Order() / palQty;
    }

    public int freeSpaceAfterOrder() {
        return freeSpace() - l23Order();
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

    public String prenotSalesPQString() {
        return Constants.convertNumber(prenotSalesPQ());
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

    public Location getMainLocation(){
        return locations.stream()
                .filter(Location::isMain)
                .findFirst()
                .orElse(locations.iterator().next());
    }

    public String locationsToString(){
        return locations.stream()
                .sorted((o1, o2) -> {
                    if (o1.isMain()) {
                        return -1;
                    }
                    if (o2.isMain()) {
                        return 1;
                    }
                    return o1.getName().compareTo(o2.getName());})
                .map(Location::getName)
                .collect(Collectors.toList()).toString();
    }

    @Override
    public String toString() {
        return "numId: " + id + ", " + name + ", locations=" + locations + ", assq: "
                + assq + ", refill level: " + onSalePlaces() + " free space: "
                + freeSpace() + "/" + freeSpace() / palQty + "pallets";
    }


}

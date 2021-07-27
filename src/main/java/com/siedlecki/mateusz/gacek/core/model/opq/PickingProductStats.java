package com.siedlecki.mateusz.gacek.core.model.opq;

import lombok.Getter;

@Getter
public class PickingProductStats {
    private int productId;
    private int qtyToOrder;
    private int qtyAfterDay;
    private int qtyReservationInCps;
    private int qtyReservationInFps;

    public PickingProductStats(PickingProduct product) {
        calculate(product);
    }

    private void calculate(PickingProduct product) {

    }
}

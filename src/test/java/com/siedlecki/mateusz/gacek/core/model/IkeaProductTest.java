package com.siedlecki.mateusz.gacek.core.model;

import com.siedlecki.mateusz.gacek.core.model.opq.Pick;
import com.siedlecki.mateusz.gacek.core.model.opq.PickingInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IkeaProductTest {

    @Test
    @DisplayName("should return 1 palet to L23 order")
    public void test1(){
        //given
        IkeaProduct product = IkeaProduct.builder()
                .assq(100)
                .availableStock(300)
                .palQty(50)
                .sgf(250)
                .prenotBuffer(0)
                .prenotSales(0)
                .pickingInfo(new PickingInfo(new Pick(5, LocalDate.now().plusDays(2), null), 1))
                .build();
        //then
        assertEquals(1,product.l23OrderToFullPal());
    }

    @Test
    @DisplayName("should return 0 palet to L23 order")
    public void test2(){
        //given
        IkeaProduct product = IkeaProduct.builder()
                .assq(100)
                .availableStock(300)
                .palQty(50)
                .sgf(230)
                .prenotBuffer(0)
                .prenotSales(0)
                .pickingInfo(new PickingInfo(new Pick(5, LocalDate.now().plusDays(2), null), 1))
                .build();
        //then
        assertEquals(0,product.l23OrderToFullPal());
    }

    @Test
    @DisplayName("should return 2 palet to L23 order")
    public void test3(){
        //given
        IkeaProduct product = IkeaProduct.builder()
                .assq(100)
                .availableStock(300)
                .palQty(50)
                .sgf(300)
                .prenotBuffer(0)
                .prenotSales(0)
                .pickingInfo(new PickingInfo(new Pick(5, LocalDate.now().plusDays(2), null), 1))
                .build();
        //then
        assertEquals(2,product.l23OrderToFullPal());
    }

    @Test
    @DisplayName("should return 1 palet to L23 order")
    public void test4(){
        //given
        IkeaProduct product = IkeaProduct.builder()
                .assq(100)
                .availableStock(300)
                .palQty(50)
                .sgf(300)
                .prenotBuffer(0)
                .prenotSales(49)
                .pickingInfo(new PickingInfo(new Pick(5, LocalDate.now().plusDays(2), null), 1))
                .build();
        //then
        assertEquals(1,product.l23OrderToFullPal());
    }
}
package com.siedlecki.mateusz.gacek.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .build();
        //then
        assertEquals(1,product.l23OrderToFullPal());
    }

    @Test
    @DisplayName("should return 16 pieces in l23 and prenot")
    public void test5(){
        IkeaProduct product = IkeaProduct.builder()
                .prenotSales(8)
                .palQty(8)
                .assq(18)
                .availableStock(30)
                .sgf(30)
                .build();
        int result = product.l23AndPrenotOrderPalToQty();
        assertEquals(16,result);
    }

}
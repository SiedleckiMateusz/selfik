package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IkeaProductProcessorTest {

    private static IkeaProductProcessor cut;

    @BeforeAll
    public static void initialize() {
        cut = new IkeaProductProcessor();
    }

    @Test
    @DisplayName("Should return one element in productsToOrder list")
    public void test1() {
        //given
        List<IkeaProduct> products = new ArrayList<>();
        products.add(IkeaProduct.builder()
                .assq(200)
                .palQty(50)
                .availableStock(300)
                .sgf(200)
                .prenotSales(50)
                .build());

        products.add(IkeaProduct.builder()
                .assq(200)
                .palQty(50)
                .availableStock(300)
                .sgf(200)
                .prenotSales(100)
                .build());
        //when
        List<IkeaProduct> result = cut.getProductsToOrder(products);
        //then
        assertEquals(2, products.size());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return one element in productsToPrepare list")
    public void test2() {
        //given
        List<IkeaProduct> products = new ArrayList<>();
        products.add(IkeaProduct.builder()
                .assq(200)
                .palQty(50)
                .availableStock(300)
                .sgf(200)
                .locations(new TreeSet<>(Arrays.asList("101000","101110")))
                .prenotSales(50)
                .build());

        products.add(IkeaProduct.builder()
                .assq(70)
                .palQty(40)
                .availableStock(300)
                .locations(new TreeSet<String>(Collections.singleton("101000")))
                .sgf(280)
                .prenotSales(0)
                .build());
        //when
        List<IkeaProduct> productsToOrder = cut.getProductsToOrder(products);
        List<IkeaProduct> result = cut.getProductListToPrepare(productsToOrder);
        //then
        assertEquals(2, products.size());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }


}
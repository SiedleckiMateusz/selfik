package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.IkeaProductService;
import com.siedlecki.mateusz.gacek.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.model.Location;
import com.siedlecki.mateusz.gacek.model.ProductStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IkeaProductServiceTest {

    private static IkeaProductService cut;

    @BeforeAll
    public static void initialize() {
        cut = new IkeaProductService();
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
        List<IkeaProduct> result = cut.getProductsL23ToOrder(products);
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
                .locations(new TreeSet<>(
                        Arrays.asList(
                                Location.builder().main(true).name("101000").build(),
                                Location.builder().name("101110").build()
                        )))
                .prenotSales(50)
                .build());

        products.add(IkeaProduct.builder()
                .assq(70)
                .palQty(40)
                .availableStock(300)
                .locations(new TreeSet<>(Collections.singleton(Location.builder().name("101000").main(true).build())))
                .sgf(280)
                .prenotSales(0)
                .build());
        //when
        List<IkeaProduct> productsToOrder = cut.getProductsL23ToOrder(products);
        cut.findProductsToPreparePlaces(productsToOrder, ProductStatus.DO_PRZYGOTOWANIA);
        //then
        assertEquals(2, products.size());
        assertFalse(productsToOrder.isEmpty());
        assertEquals(1, productsToOrder.size());
    }


}
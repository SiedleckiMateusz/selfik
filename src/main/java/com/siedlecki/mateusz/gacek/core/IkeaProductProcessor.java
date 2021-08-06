package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class IkeaProductProcessor {

    public Map<String, List<IkeaProduct>> getProductsToPrepareAndExtraOrder(List<IkeaProduct> ikeaProducts, List<PrenotProduct> prenotProducts) {

        prenotProducts.forEach(prenotProduct -> {
            Optional<IkeaProduct> ikeaProductOpt = ikeaProducts.stream()
                    .filter(ikeaProduct -> ikeaProduct.getNumberId().equals(prenotProduct.getNumberId()))
                    .findAny();
            if (ikeaProductOpt.isPresent()) {
                IkeaProduct ikeaProduct = ikeaProductOpt.get();
                ikeaProduct.addPrenotSalesQty(prenotProduct.getQtySales());
                ikeaProduct.addPrenotBufferQty(prenotProduct.getQtyBuffer());
            }
        });

        List<IkeaProduct> inPrenotProducts = ikeaProducts.stream()
                .filter(p -> p.getPrenotSales() > 0)
                .collect(Collectors.toList());

//        inPrenotProducts.forEach(p -> p.setType(OrderType.PRENOT));

        Map<String, List<IkeaProduct>> listsMap = new HashMap<>();
        List<IkeaProduct> toPrepareFromPrenot = getProductListToPrepare(inPrenotProducts);
        Map<String, List<IkeaProduct>> productsToOrderAndPrepare = getProductsToOrderAndPrepare(ikeaProducts);

        List<IkeaProduct> toOrder = productsToOrderAndPrepare.get("toOrder");
        List<IkeaProduct> toPrepareFromOrder = productsToOrderAndPrepare.get("toPrepareFromOrder");

        List<IkeaProduct> toPrepare = new ArrayList<>();
        toPrepare.addAll(toPrepareFromPrenot);
        toPrepare.addAll(toPrepareFromOrder);

        toPrepare = toPrepare.stream().distinct().sorted(new FirstLocationComparator()).collect(Collectors.toList());

        listsMap.put("toPrepare", toPrepare);
        listsMap.put("toOrder", toOrder);

        return listsMap;
    }

    public Map<String, List<IkeaProduct>> getProductsToOrderAndPrepare(List<IkeaProduct> ikeaProducts) {
        List<IkeaProduct> productsToOrder = getProductsToOrder(ikeaProducts);
//        productsToOrder.forEach(p -> p.setType(OrderType.L23));
        List<IkeaProduct> productListToPrepare = getProductListToPrepare(productsToOrder);

        Map<String, List<IkeaProduct>> listsMap = new HashMap<>();
        listsMap.put("toOrder", productsToOrder);
        listsMap.put("toPrepare", productListToPrepare);

        return listsMap;
    }

    private List<IkeaProduct> getProductsToOrder(List<IkeaProduct> ikeaProducts) {
        List<IkeaProduct> resultProducts = new ArrayList<>();
        for (IkeaProduct product : ikeaProducts) {
            if (product.toL23OrderPQ() >= 1) {
                if (product.getSgf() > 0) {
                    resultProducts.add(product);
                }
            }
        }
        return resultProducts;
    }

    public List<IkeaProduct> getProductListToPrepare(List<IkeaProduct> products) {
        List<IkeaProduct> resultProducts = new ArrayList<>();

        for (IkeaProduct product : products) {
            if (product.getLocations().size() > 1) {
                if (!allLocationsOn10Level(product.getLocations())) {
                    resultProducts.add(product);
                }
            } else {
                double pallets = (double) product.getAssq() / product.getPalQty();
                if (pallets >= 2) {
                    resultProducts.add(product);
                }
            }
        }
        return resultProducts;
    }

    private boolean allLocationsOn10Level(TreeSet<String> locations) {
        for (String location : locations) {
            if (!location.endsWith("10")) {
                return false;
            }
        }
        return true;
    }
}

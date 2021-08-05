package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.IkeaProductToWrite;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.OrderType;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
import com.siedlecki.mateusz.gacek.core.model.opq.PickingInfo;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class IkeaProductProcessor {

    public Map<String, List<IkeaProductToWrite>> getProductsToPrepareAndExtraOrder(List<IkeaProduct> ikeaProducts, List<PrenotProduct> prenotProducts) {

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

        List<IkeaProductToWrite> inPrenotProducts = ikeaProducts.stream()
                .filter(p -> p.getPrenotSales() > 0)
                .map(p->new IkeaProductToWrite(p, OrderType.PRENOT))
                .collect(Collectors.toList());

        Map<String, List<IkeaProductToWrite>> listsMap = new HashMap<>();
        List<IkeaProductToWrite> toPrepareFromPrenot = getProductListToPrepare(inPrenotProducts);
        Map<String, List<IkeaProductToWrite>> productsToOrderAndPrepare = getProductsToOrderAndPrepare(ikeaProducts);

        List<IkeaProductToWrite> toOrder = productsToOrderAndPrepare.get("toOrder");
        List<IkeaProductToWrite> toPrepareFromOrder = productsToOrderAndPrepare.get("toPrepareFromOrder");

        List<IkeaProductToWrite> toPrepare = new ArrayList<>();
        toPrepare.addAll(toPrepareFromPrenot);
        toPrepare.addAll(toPrepareFromOrder);

        toPrepare = toPrepare.stream().sorted(new FirstLocationComparator()).collect(Collectors.toList());

        listsMap.put("toPrepare", toPrepare);
        listsMap.put("toOrder", toOrder);

        return listsMap;
    }

    public Map<String, List<IkeaProductToWrite>> getProductsToOrderAndPrepareWithOpq(List<IkeaProduct> ikeaProducts, Map<String, PickingInfo> pickingProductMap) {
        List<IkeaProduct> ikeaProductsModified = modifyIkeaProductsByPickingProducts(ikeaProducts, pickingProductMap);

        return getProductsToOrderAndPrepare(ikeaProductsModified);
    }

    private List<IkeaProduct> modifyIkeaProductsByPickingProducts(List<IkeaProduct> ikeaProducts, Map<String, PickingInfo> pickingProductMap) {
        for (IkeaProduct p : ikeaProducts){
            if (pickingProductMap.containsKey(p.getNumberId())){
                PickingInfo pickingInfo = pickingProductMap.get(p.getNumberId());
                System.out.println(pickingInfo.toString());
//                p.addReservationAfterDay(pickingInfo.getQtyAfterDay());
            }
        }
        return ikeaProducts;
    }

    public Map<String, List<IkeaProductToWrite>> getProductsToOrderAndPrepare(List<IkeaProduct> ikeaProducts) {
        List<IkeaProductToWrite> productsToOrder = getProductsToOrder(ikeaProducts).stream()
                .map(p -> new IkeaProductToWrite(p, OrderType.L23))
                .collect(Collectors.toList());
        List<IkeaProductToWrite> productListToPrepare = getProductListToPrepare(productsToOrder);

        Map<String, List<IkeaProductToWrite>> listsMap = new HashMap<>();
        listsMap.put("toOrder", productsToOrder);
        listsMap.put("toPrepareFromOrder", productListToPrepare);

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

    public List<IkeaProductToWrite> getProductListToPrepare(List<IkeaProductToWrite> products) {
        List<IkeaProductToWrite> resultProducts = new ArrayList<>();

        for (IkeaProductToWrite product : products) {
            if (product.getProduct().getLocations().size() > 1) {
                if (!allLocationsOn10Level(product.getProduct().getLocations())) {
                    resultProducts.add(product);
                }
            } else {
                double pallets = (double) product.getProduct().getAssq() / product.getProduct().getPalQty();
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

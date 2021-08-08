package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
import com.siedlecki.mateusz.gacek.core.model.Result;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class IkeaProductProcessor {

    public Result getProductsToPrepareAndExtraOrder(List<IkeaProduct> ikeaProducts, List<PrenotProduct> prenotProducts) {

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

        List<IkeaProduct> toPrepareFromPrenot = getProductListToPrepare(inPrenotProducts);
        Result result = getProductsToOrderAndPrepare(ikeaProducts);

        result.addToPrepare(toPrepareFromPrenot);

        return result;
    }

    public Result getProductsToOrderAndPrepare(List<IkeaProduct> ikeaProducts) {
        List<IkeaProduct> productsToOrder = getProductsToOrder(ikeaProducts);
        List<IkeaProduct> productListToPrepare = getProductListToPrepare(productsToOrder);

        return new Result(ikeaProducts,productsToOrder,productListToPrepare);
    }

    private List<IkeaProduct> getProductsToOrder(List<IkeaProduct> ikeaProducts) {
        List<IkeaProduct> resultProducts = new ArrayList<>();
        for (IkeaProduct product : ikeaProducts) {
            if (product.l23OrderToFullPal() >= 1) {
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

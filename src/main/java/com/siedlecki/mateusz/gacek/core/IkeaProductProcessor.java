package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class IkeaProductProcessor {

    public Result getProductsToPrepareAndExtraOrder(ProductsContainer container) {
        List<IkeaProduct> ikeaProducts = new ArrayList<>(container.getIkeaProductMap().values());
        List<PrenotProduct> prenotProducts = new ArrayList<>(container.getPrenotProductMap().values());

        setParametersFromPrenotToProducts(ikeaProducts,prenotProducts);

        List<IkeaProduct> inPrenotProducts = getPrenotProducts(ikeaProducts);

        List<IkeaProduct> toPrepareFromPrenot = getProductsToPreparePlaces(inPrenotProducts);
        List<IkeaProduct> toCheckComingBackProducts = getComingBackProductsToCheck(inPrenotProducts,container.getLocations());
        toPrepareFromPrenot.addAll(toCheckComingBackProducts);

        Result result = getProductsToOrderAndPrepare(ikeaProducts);
        result.addToPrepare(toPrepareFromPrenot);

        return result;
    }

    private List<IkeaProduct> getComingBackProductsToCheck(List<IkeaProduct> inPrenotProducts, Set<LocationsWithProducts> locations) {
        Set<String> multiLocations = locations.stream().filter(lwp ->
                        !lwp.getLocationName().endsWith("10") &&
                                !lwp.getLocationName().startsWith("A") &&
                                lwp.getArtilceIds().size() > 1)
                .map(LocationsWithProducts::getLocationName).collect(Collectors.toSet());

        List<IkeaProduct> result = inPrenotProducts.stream()
                .filter(ip -> ip.getAvailableStock() == 0)
                .filter(ip -> {
                    Set<Location> ikeaProductLocations = ip.getLocations();
                    for (Location l : ikeaProductLocations) {
                        if (multiLocations.contains(l.getName()))
                            return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
        result.forEach(ip->ip.setStatus(ProductStatus.WRACA_PO_NIEDOSTEPNOSCI));
        return result;
    }

    private List<IkeaProduct> getPrenotProducts(List<IkeaProduct> ikeaProducts) {
        return ikeaProducts.stream()
                .filter(p -> p.getPrenotSales() > 0)
                .collect(Collectors.toList());
    }

    private void setParametersFromPrenotToProducts(List<IkeaProduct> ikeaProducts, List<PrenotProduct> prenotProducts) {
        prenotProducts.forEach(prenotProduct -> {
            Optional<IkeaProduct> ikeaProductOpt = ikeaProducts.stream()
                    .filter(ikeaProduct -> ikeaProduct.getId().equals(prenotProduct.getId()))
                    .findAny();
            ikeaProductOpt.ifPresent(ikeaProduct -> {
                ikeaProduct.addPrenotSalesQty(prenotProduct.getQtySales());
                ikeaProduct.addPrenotBufferQty(prenotProduct.getQtyBuffer());
            });
        });
    }

    public Result getProductsToOrderAndPrepare(List<IkeaProduct> ikeaProducts) {
        List<IkeaProduct> productsToOrder = getProductsToOrder(ikeaProducts);
        List<IkeaProduct> productListToPrepare = getProductsToPreparePlaces(productsToOrder);

        return new Result(ikeaProducts,productsToOrder,productListToPrepare);
    }

    public List<IkeaProduct> getProductsToOrder(List<IkeaProduct> ikeaProducts) {
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

    public List<IkeaProduct> getProductsToPreparePlaces(List<IkeaProduct> inPrenotProducts) {
        List<IkeaProduct> resultProducts = new ArrayList<>();

        for (IkeaProduct product : inPrenotProducts) {
            if (product.getLocations().size() > 1) {
                if (!allLocationsHave10Level(product.getLocations())) {
                    product.setStatus(ProductStatus.DO_PRZERZUCENIA);
                    resultProducts.add(product);
                }
            } else {
                double pallets = (double) product.getAssq() / product.getPalQty();
                if (pallets >= 2) {
                    product.setStatus(ProductStatus.DO_PRZERZUCENIA);
                    resultProducts.add(product);
                }
            }
        }
        return resultProducts;
    }

    private boolean allLocationsHave10Level(Set<Location> locations) {
        for (Location location : locations) {
            if (!location.getName().endsWith("10")) {
                return false;
            }
        }
        return true;
    }
}

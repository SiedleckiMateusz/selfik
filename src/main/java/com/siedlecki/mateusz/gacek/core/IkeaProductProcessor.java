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

    public Result getL23AndPrenotProducts(ProductsContainer container) {
        List<IkeaProduct> ikeaProducts = new ArrayList<>(container.getIkeaProductMap().values());
        List<PrenotProduct> prenotProducts = new ArrayList<>(container.getPrenotProductMap().values());

        setParametersFromPrenotToProducts(ikeaProducts,prenotProducts);

        List<IkeaProduct> inPrenotProducts = getPrenotProducts(ikeaProducts);

        List<IkeaProduct> toPrepareFromPrenot = findProductsToPreparePlaces(inPrenotProducts);
        List<IkeaProduct> toCheckComingBackProducts = getComingBackProductsToCheck(inPrenotProducts,container.getLocations());
        toPrepareFromPrenot.addAll(toCheckComingBackProducts);

        Result result = getL23Products(ikeaProducts);
        result.addToPrepare(toPrepareFromPrenot);

        return result;
    }

    private List<IkeaProduct> getComingBackProductsToCheck(List<IkeaProduct> inPrenotProducts, Set<LocationsWithProducts> locations) {
        Set<String> multiLocations = locations.stream().filter(lwp ->
                        !endsWith10(lwp.getLocationName()) && !startWithA(lwp) && moreThanOneAtLocation(lwp))
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

    private boolean moreThanOneAtLocation(LocationsWithProducts lwp) {
        return lwp.getArtilceIds().size() > 1;
    }

    private boolean startWithA(LocationsWithProducts lwp) {
        return lwp.getLocationName().startsWith("A");
    }

    private boolean endsWith10(String locationName) {
        return locationName.endsWith("10");
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

    public Result getL23Products(List<IkeaProduct> allIkeaProducts) {
        List<IkeaProduct> L23productsToOrder = getProductsl23ToOrder(allIkeaProducts);
        List<IkeaProduct> L23productsToPrepare = findProductsToPreparePlaces(L23productsToOrder);
        return new Result(allIkeaProducts,L23productsToOrder,L23productsToPrepare);
    }

    public List<IkeaProduct> getProductsl23ToOrder(List<IkeaProduct> ikeaProducts) {
        List<IkeaProduct> resultProducts = new ArrayList<>();
        for (IkeaProduct product : ikeaProducts) {
            if (product.l23OrderToFullPal() >= 1) {
                if (product.getSgf() > 0) {
//                    resultProducts.add(product);
                }
            }
        }
        return resultProducts;
    }

    public List<IkeaProduct> findProductsToPreparePlaces(List<IkeaProduct> inPrenotProducts) {
        List<IkeaProduct> resultProducts = new ArrayList<>();

        for (IkeaProduct product : inPrenotProducts) {
            if (product.getLocations().size() > 1) {
                if (isAnyZeroLevel(product.getLocations())) {
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

    private boolean isAnyZeroLevel(Set<Location> locations) {
        for (Location location : locations) {
            if (location.getName().endsWith("00")) {
                return true;
            }
        }
        return false;
    }
}

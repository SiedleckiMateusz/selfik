package com.siedlecki.mateusz.gacek.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ProductsContainer {
    private Map<String, IkeaProduct> ikeaProductMap;
    private Map<String, PrenotProduct> prenotProductMap;
    private Set<LocationsWithProducts> locations;
}

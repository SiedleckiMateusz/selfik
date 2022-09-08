package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.LocationsWithProducts;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
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

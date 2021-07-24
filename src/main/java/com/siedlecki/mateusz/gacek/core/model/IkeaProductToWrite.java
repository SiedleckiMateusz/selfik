package com.siedlecki.mateusz.gacek.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class IkeaProductToWrite {
    private final IkeaProduct product;
    @Setter
    private  OrderType type;

}

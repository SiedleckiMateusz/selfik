package com.siedlecki.mateusz.gacek.core.model;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Location {
    private boolean main;
    private String name;
    private Specshop specshop;
    private String rangeGroup;
}

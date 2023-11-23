package com.siedlecki.mateusz.gacek.core.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IgnoredSlm003Value {
    private Slm003Column column;
    private String value;
}

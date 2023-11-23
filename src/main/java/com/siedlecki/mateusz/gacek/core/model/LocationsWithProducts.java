package com.siedlecki.mateusz.gacek.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class LocationsWithProducts {
    private String locationName;
    private Set<String> artilceIds;
}

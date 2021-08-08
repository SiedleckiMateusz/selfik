package com.siedlecki.mateusz.gacek.core.model;

import com.siedlecki.mateusz.gacek.core.FirstLocationComparator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class Result {
    private List<IkeaProduct> all;
    private List<IkeaProduct> toOrder;
    private List<IkeaProduct> toPrepare;

    public void addToPrepare(List<IkeaProduct> toPrepare) {
        if (toPrepare != null) {
            this.toPrepare.addAll(toPrepare);
            this.toPrepare = this.toPrepare.stream()
                    .distinct()
                    .sorted(new FirstLocationComparator())
                    .collect(Collectors.toList());
        }
    }
}

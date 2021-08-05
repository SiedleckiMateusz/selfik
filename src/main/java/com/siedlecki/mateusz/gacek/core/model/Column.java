package com.siedlecki.mateusz.gacek.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
public class Column {
    private Integer index;
    private String name;

    @Override
    public String toString() {
        return "ColumnInFile{" +
                "index=" + index +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(index, column.index) && Objects.equals(name, column.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, name);
    }
}

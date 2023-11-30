package com.siedlecki.mateusz.gacek.model.entity;

import com.siedlecki.mateusz.gacek.model.ValueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parameter {
    @Id
    private String key;
    private String name;
    private String value;
    private String description;
    private ValueType valueType;
}

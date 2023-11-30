package com.siedlecki.mateusz.gacek.repository;

import com.siedlecki.mateusz.gacek.model.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<Parameter,String> {
}

package com.siedlecki.mateusz.gacek;

import com.siedlecki.mateusz.gacek.model.entity.Parameter;
import com.siedlecki.mateusz.gacek.repository.ParameterRepository;
import com.siedlecki.mateusz.gacek.settings.Settings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParameterService {

    private final ParameterRepository repository;

    public void setupSettings() {
        List<Parameter> parametersFromDatabase = repository.findAll();

        for (Parameter parameter : parametersFromDatabase) {
            Settings.setValueIfExist(parameter);
        }

        List<Parameter> parametersToSave = Arrays.stream(Settings.values())
                .filter(s -> Objects.nonNull(s.getValue()))
                .map(settings -> Parameter.builder()
                        .key(settings.getKey())
                        .value(settings.getValue())
                        .valueType(settings.getValueType())
                        .name(settings.name())
                        .description(settings.getDescription())
                        .build()).collect(Collectors.toList());

        repository.saveAll(parametersToSave);

    }
}

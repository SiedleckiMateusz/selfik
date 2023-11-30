package com.siedlecki.mateusz.gacek;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SettingsConfig implements CommandLineRunner {

    private final ParameterService parameterService;

    @Override
    public void run(String... args) {
        parameterService.setupSettings();
    }
}

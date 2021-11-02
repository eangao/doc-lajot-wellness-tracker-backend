package com.doclajotwellnesstrackerbackend.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HealthConcernMapperTest {

    private HealthConcernMapper healthConcernMapper;

    @BeforeEach
    public void setUp() {
        healthConcernMapper = new HealthConcernMapperImpl();
    }
}

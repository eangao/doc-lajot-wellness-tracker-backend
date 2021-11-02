package com.doclajotwellnesstrackerbackend.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VitalSignMapperTest {

    private VitalSignMapper vitalSignMapper;

    @BeforeEach
    public void setUp() {
        vitalSignMapper = new VitalSignMapperImpl();
    }
}

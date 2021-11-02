package com.doclajotwellnesstrackerbackend.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.doclajotwellnesstrackerbackend.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HealthConcernTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HealthConcern.class);
        HealthConcern healthConcern1 = new HealthConcern();
        healthConcern1.setId(1L);
        HealthConcern healthConcern2 = new HealthConcern();
        healthConcern2.setId(healthConcern1.getId());
        assertThat(healthConcern1).isEqualTo(healthConcern2);
        healthConcern2.setId(2L);
        assertThat(healthConcern1).isNotEqualTo(healthConcern2);
        healthConcern1.setId(null);
        assertThat(healthConcern1).isNotEqualTo(healthConcern2);
    }
}

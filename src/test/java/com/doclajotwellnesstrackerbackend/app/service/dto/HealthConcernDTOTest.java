package com.doclajotwellnesstrackerbackend.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.doclajotwellnesstrackerbackend.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HealthConcernDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HealthConcernDTO.class);
        HealthConcernDTO healthConcernDTO1 = new HealthConcernDTO();
        healthConcernDTO1.setId(1L);
        HealthConcernDTO healthConcernDTO2 = new HealthConcernDTO();
        assertThat(healthConcernDTO1).isNotEqualTo(healthConcernDTO2);
        healthConcernDTO2.setId(healthConcernDTO1.getId());
        assertThat(healthConcernDTO1).isEqualTo(healthConcernDTO2);
        healthConcernDTO2.setId(2L);
        assertThat(healthConcernDTO1).isNotEqualTo(healthConcernDTO2);
        healthConcernDTO1.setId(null);
        assertThat(healthConcernDTO1).isNotEqualTo(healthConcernDTO2);
    }
}

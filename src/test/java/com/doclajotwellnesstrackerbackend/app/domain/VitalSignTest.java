package com.doclajotwellnesstrackerbackend.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.doclajotwellnesstrackerbackend.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VitalSignTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VitalSign.class);
        VitalSign vitalSign1 = new VitalSign();
        vitalSign1.setId(1L);
        VitalSign vitalSign2 = new VitalSign();
        vitalSign2.setId(vitalSign1.getId());
        assertThat(vitalSign1).isEqualTo(vitalSign2);
        vitalSign2.setId(2L);
        assertThat(vitalSign1).isNotEqualTo(vitalSign2);
        vitalSign1.setId(null);
        assertThat(vitalSign1).isNotEqualTo(vitalSign2);
    }
}

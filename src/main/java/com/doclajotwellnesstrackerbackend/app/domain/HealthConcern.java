package com.doclajotwellnesstrackerbackend.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A HealthConcern.
 */
@Entity
@Table(name = "health_concern")
public class HealthConcern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "healthConcerns")
    @JsonIgnoreProperties(value = { "appUser", "healthConcerns" }, allowSetters = true)
    private Set<VitalSign> vitalSigns = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HealthConcern id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HealthConcern name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<VitalSign> getVitalSigns() {
        return this.vitalSigns;
    }

    public void setVitalSigns(Set<VitalSign> vitalSigns) {
        if (this.vitalSigns != null) {
            this.vitalSigns.forEach(i -> i.removeHealthConcern(this));
        }
        if (vitalSigns != null) {
            vitalSigns.forEach(i -> i.addHealthConcern(this));
        }
        this.vitalSigns = vitalSigns;
    }

    public HealthConcern vitalSigns(Set<VitalSign> vitalSigns) {
        this.setVitalSigns(vitalSigns);
        return this;
    }

    public HealthConcern addVitalSign(VitalSign vitalSign) {
        this.vitalSigns.add(vitalSign);
        vitalSign.getHealthConcerns().add(this);
        return this;
    }

    public HealthConcern removeVitalSign(VitalSign vitalSign) {
        this.vitalSigns.remove(vitalSign);
        vitalSign.getHealthConcerns().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HealthConcern)) {
            return false;
        }
        return id != null && id.equals(((HealthConcern) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HealthConcern{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

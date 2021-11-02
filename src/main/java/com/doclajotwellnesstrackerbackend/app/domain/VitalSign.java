package com.doclajotwellnesstrackerbackend.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A VitalSign.
 */
@Entity
@Table(name = "vital_sign")
public class VitalSign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "weight_in_pounds")
    private Integer weightInPounds;

    @Column(name = "height_in_inches")
    private Integer heightInInches;

    @Column(name = "bmi")
    private Double bmi;

    @Column(name = "glass_of_water")
    private Integer glassOfWater;

    @Column(name = "systolic")
    private Integer systolic;

    @Column(name = "diastolic")
    private Integer diastolic;

    @Column(name = "current_blood_sugar")
    private Double currentBloodSugar;

    @Column(name = "lipid_profile")
    private Double lipidProfile;

    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private AppUser appUser;

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_vital_sign__health_concern",
        joinColumns = @JoinColumn(name = "vital_sign_id"),
        inverseJoinColumns = @JoinColumn(name = "health_concern_id")
    )
    @JsonIgnoreProperties(value = { "vitalSigns" }, allowSetters = true)
    private Set<HealthConcern> healthConcerns = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VitalSign id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeightInPounds() {
        return this.weightInPounds;
    }

    public VitalSign weightInPounds(Integer weightInPounds) {
        this.setWeightInPounds(weightInPounds);
        return this;
    }

    public void setWeightInPounds(Integer weightInPounds) {
        this.weightInPounds = weightInPounds;
    }

    public Integer getHeightInInches() {
        return this.heightInInches;
    }

    public VitalSign heightInInches(Integer heightInInches) {
        this.setHeightInInches(heightInInches);
        return this;
    }

    public void setHeightInInches(Integer heightInInches) {
        this.heightInInches = heightInInches;
    }

    public Double getBmi() {
        return this.bmi;
    }

    public VitalSign bmi(Double bmi) {
        this.setBmi(bmi);
        return this;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Integer getGlassOfWater() {
        return this.glassOfWater;
    }

    public VitalSign glassOfWater(Integer glassOfWater) {
        this.setGlassOfWater(glassOfWater);
        return this;
    }

    public void setGlassOfWater(Integer glassOfWater) {
        this.glassOfWater = glassOfWater;
    }

    public Integer getSystolic() {
        return this.systolic;
    }

    public VitalSign systolic(Integer systolic) {
        this.setSystolic(systolic);
        return this;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public Integer getDiastolic() {
        return this.diastolic;
    }

    public VitalSign diastolic(Integer diastolic) {
        this.setDiastolic(diastolic);
        return this;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

    public Double getCurrentBloodSugar() {
        return this.currentBloodSugar;
    }

    public VitalSign currentBloodSugar(Double currentBloodSugar) {
        this.setCurrentBloodSugar(currentBloodSugar);
        return this;
    }

    public void setCurrentBloodSugar(Double currentBloodSugar) {
        this.currentBloodSugar = currentBloodSugar;
    }

    public Double getLipidProfile() {
        return this.lipidProfile;
    }

    public VitalSign lipidProfile(Double lipidProfile) {
        this.setLipidProfile(lipidProfile);
        return this;
    }

    public void setLipidProfile(Double lipidProfile) {
        this.lipidProfile = lipidProfile;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public VitalSign appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Set<HealthConcern> getHealthConcerns() {
        return this.healthConcerns;
    }

    public void setHealthConcerns(Set<HealthConcern> healthConcerns) {
        this.healthConcerns = healthConcerns;
    }

    public VitalSign healthConcerns(Set<HealthConcern> healthConcerns) {
        this.setHealthConcerns(healthConcerns);
        return this;
    }

    public VitalSign addHealthConcern(HealthConcern healthConcern) {
        this.healthConcerns.add(healthConcern);
        healthConcern.getVitalSigns().add(this);
        return this;
    }

    public VitalSign removeHealthConcern(HealthConcern healthConcern) {
        this.healthConcerns.remove(healthConcern);
        healthConcern.getVitalSigns().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VitalSign)) {
            return false;
        }
        return id != null && id.equals(((VitalSign) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VitalSign{" +
            "id=" + getId() +
            ", weightInPounds=" + getWeightInPounds() +
            ", heightInInches=" + getHeightInInches() +
            ", bmi=" + getBmi() +
            ", glassOfWater=" + getGlassOfWater() +
            ", systolic=" + getSystolic() +
            ", diastolic=" + getDiastolic() +
            ", currentBloodSugar=" + getCurrentBloodSugar() +
            ", lipidProfile=" + getLipidProfile() +
            "}";
    }
}

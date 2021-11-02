package com.doclajotwellnesstrackerbackend.app.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.doclajotwellnesstrackerbackend.app.domain.VitalSign} entity.
 */
public class VitalSignDTO implements Serializable {

    private Long id;

    private Integer weightInPounds;

    private Integer heightInInches;

    private Double bmi;

    private Integer glassOfWater;

    private Integer systolic;

    private Integer diastolic;

    private Double currentBloodSugar;

    private Double lipidProfile;

    private AppUserDTO appUser;

    private Set<HealthConcernDTO> healthConcerns = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeightInPounds() {
        return weightInPounds;
    }

    public void setWeightInPounds(Integer weightInPounds) {
        this.weightInPounds = weightInPounds;
    }

    public Integer getHeightInInches() {
        return heightInInches;
    }

    public void setHeightInInches(Integer heightInInches) {
        this.heightInInches = heightInInches;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Integer getGlassOfWater() {
        return glassOfWater;
    }

    public void setGlassOfWater(Integer glassOfWater) {
        this.glassOfWater = glassOfWater;
    }

    public Integer getSystolic() {
        return systolic;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

    public Double getCurrentBloodSugar() {
        return currentBloodSugar;
    }

    public void setCurrentBloodSugar(Double currentBloodSugar) {
        this.currentBloodSugar = currentBloodSugar;
    }

    public Double getLipidProfile() {
        return lipidProfile;
    }

    public void setLipidProfile(Double lipidProfile) {
        this.lipidProfile = lipidProfile;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public Set<HealthConcernDTO> getHealthConcerns() {
        return healthConcerns;
    }

    public void setHealthConcerns(Set<HealthConcernDTO> healthConcerns) {
        this.healthConcerns = healthConcerns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VitalSignDTO)) {
            return false;
        }

        VitalSignDTO vitalSignDTO = (VitalSignDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vitalSignDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VitalSignDTO{" +
            "id=" + getId() +
            ", weightInPounds=" + getWeightInPounds() +
            ", heightInInches=" + getHeightInInches() +
            ", bmi=" + getBmi() +
            ", glassOfWater=" + getGlassOfWater() +
            ", systolic=" + getSystolic() +
            ", diastolic=" + getDiastolic() +
            ", currentBloodSugar=" + getCurrentBloodSugar() +
            ", lipidProfile=" + getLipidProfile() +
            ", appUser=" + getAppUser() +
            ", healthConcerns=" + getHealthConcerns() +
            "}";
    }
}

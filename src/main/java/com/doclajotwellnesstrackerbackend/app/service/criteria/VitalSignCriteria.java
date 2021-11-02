package com.doclajotwellnesstrackerbackend.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.doclajotwellnesstrackerbackend.app.domain.VitalSign} entity. This class is used
 * in {@link com.doclajotwellnesstrackerbackend.app.web.rest.VitalSignResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vital-signs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VitalSignCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter weightInPounds;

    private IntegerFilter heightInInches;

    private DoubleFilter bmi;

    private IntegerFilter glassOfWater;

    private IntegerFilter systolic;

    private IntegerFilter diastolic;

    private DoubleFilter currentBloodSugar;

    private DoubleFilter lipidProfile;

    private LongFilter appUserId;

    private LongFilter healthConcernId;

    private Boolean distinct;

    public VitalSignCriteria() {}

    public VitalSignCriteria(VitalSignCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.weightInPounds = other.weightInPounds == null ? null : other.weightInPounds.copy();
        this.heightInInches = other.heightInInches == null ? null : other.heightInInches.copy();
        this.bmi = other.bmi == null ? null : other.bmi.copy();
        this.glassOfWater = other.glassOfWater == null ? null : other.glassOfWater.copy();
        this.systolic = other.systolic == null ? null : other.systolic.copy();
        this.diastolic = other.diastolic == null ? null : other.diastolic.copy();
        this.currentBloodSugar = other.currentBloodSugar == null ? null : other.currentBloodSugar.copy();
        this.lipidProfile = other.lipidProfile == null ? null : other.lipidProfile.copy();
        this.appUserId = other.appUserId == null ? null : other.appUserId.copy();
        this.healthConcernId = other.healthConcernId == null ? null : other.healthConcernId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VitalSignCriteria copy() {
        return new VitalSignCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getWeightInPounds() {
        return weightInPounds;
    }

    public IntegerFilter weightInPounds() {
        if (weightInPounds == null) {
            weightInPounds = new IntegerFilter();
        }
        return weightInPounds;
    }

    public void setWeightInPounds(IntegerFilter weightInPounds) {
        this.weightInPounds = weightInPounds;
    }

    public IntegerFilter getHeightInInches() {
        return heightInInches;
    }

    public IntegerFilter heightInInches() {
        if (heightInInches == null) {
            heightInInches = new IntegerFilter();
        }
        return heightInInches;
    }

    public void setHeightInInches(IntegerFilter heightInInches) {
        this.heightInInches = heightInInches;
    }

    public DoubleFilter getBmi() {
        return bmi;
    }

    public DoubleFilter bmi() {
        if (bmi == null) {
            bmi = new DoubleFilter();
        }
        return bmi;
    }

    public void setBmi(DoubleFilter bmi) {
        this.bmi = bmi;
    }

    public IntegerFilter getGlassOfWater() {
        return glassOfWater;
    }

    public IntegerFilter glassOfWater() {
        if (glassOfWater == null) {
            glassOfWater = new IntegerFilter();
        }
        return glassOfWater;
    }

    public void setGlassOfWater(IntegerFilter glassOfWater) {
        this.glassOfWater = glassOfWater;
    }

    public IntegerFilter getSystolic() {
        return systolic;
    }

    public IntegerFilter systolic() {
        if (systolic == null) {
            systolic = new IntegerFilter();
        }
        return systolic;
    }

    public void setSystolic(IntegerFilter systolic) {
        this.systolic = systolic;
    }

    public IntegerFilter getDiastolic() {
        return diastolic;
    }

    public IntegerFilter diastolic() {
        if (diastolic == null) {
            diastolic = new IntegerFilter();
        }
        return diastolic;
    }

    public void setDiastolic(IntegerFilter diastolic) {
        this.diastolic = diastolic;
    }

    public DoubleFilter getCurrentBloodSugar() {
        return currentBloodSugar;
    }

    public DoubleFilter currentBloodSugar() {
        if (currentBloodSugar == null) {
            currentBloodSugar = new DoubleFilter();
        }
        return currentBloodSugar;
    }

    public void setCurrentBloodSugar(DoubleFilter currentBloodSugar) {
        this.currentBloodSugar = currentBloodSugar;
    }

    public DoubleFilter getLipidProfile() {
        return lipidProfile;
    }

    public DoubleFilter lipidProfile() {
        if (lipidProfile == null) {
            lipidProfile = new DoubleFilter();
        }
        return lipidProfile;
    }

    public void setLipidProfile(DoubleFilter lipidProfile) {
        this.lipidProfile = lipidProfile;
    }

    public LongFilter getAppUserId() {
        return appUserId;
    }

    public LongFilter appUserId() {
        if (appUserId == null) {
            appUserId = new LongFilter();
        }
        return appUserId;
    }

    public void setAppUserId(LongFilter appUserId) {
        this.appUserId = appUserId;
    }

    public LongFilter getHealthConcernId() {
        return healthConcernId;
    }

    public LongFilter healthConcernId() {
        if (healthConcernId == null) {
            healthConcernId = new LongFilter();
        }
        return healthConcernId;
    }

    public void setHealthConcernId(LongFilter healthConcernId) {
        this.healthConcernId = healthConcernId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VitalSignCriteria that = (VitalSignCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(weightInPounds, that.weightInPounds) &&
            Objects.equals(heightInInches, that.heightInInches) &&
            Objects.equals(bmi, that.bmi) &&
            Objects.equals(glassOfWater, that.glassOfWater) &&
            Objects.equals(systolic, that.systolic) &&
            Objects.equals(diastolic, that.diastolic) &&
            Objects.equals(currentBloodSugar, that.currentBloodSugar) &&
            Objects.equals(lipidProfile, that.lipidProfile) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(healthConcernId, that.healthConcernId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            weightInPounds,
            heightInInches,
            bmi,
            glassOfWater,
            systolic,
            diastolic,
            currentBloodSugar,
            lipidProfile,
            appUserId,
            healthConcernId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VitalSignCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (weightInPounds != null ? "weightInPounds=" + weightInPounds + ", " : "") +
            (heightInInches != null ? "heightInInches=" + heightInInches + ", " : "") +
            (bmi != null ? "bmi=" + bmi + ", " : "") +
            (glassOfWater != null ? "glassOfWater=" + glassOfWater + ", " : "") +
            (systolic != null ? "systolic=" + systolic + ", " : "") +
            (diastolic != null ? "diastolic=" + diastolic + ", " : "") +
            (currentBloodSugar != null ? "currentBloodSugar=" + currentBloodSugar + ", " : "") +
            (lipidProfile != null ? "lipidProfile=" + lipidProfile + ", " : "") +
            (appUserId != null ? "appUserId=" + appUserId + ", " : "") +
            (healthConcernId != null ? "healthConcernId=" + healthConcernId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

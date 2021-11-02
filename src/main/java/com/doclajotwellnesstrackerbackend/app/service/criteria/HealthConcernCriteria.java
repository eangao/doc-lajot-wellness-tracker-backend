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
 * Criteria class for the {@link com.doclajotwellnesstrackerbackend.app.domain.HealthConcern} entity. This class is used
 * in {@link com.doclajotwellnesstrackerbackend.app.web.rest.HealthConcernResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /health-concerns?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HealthConcernCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter vitalSignId;

    private Boolean distinct;

    public HealthConcernCriteria() {}

    public HealthConcernCriteria(HealthConcernCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.vitalSignId = other.vitalSignId == null ? null : other.vitalSignId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public HealthConcernCriteria copy() {
        return new HealthConcernCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getVitalSignId() {
        return vitalSignId;
    }

    public LongFilter vitalSignId() {
        if (vitalSignId == null) {
            vitalSignId = new LongFilter();
        }
        return vitalSignId;
    }

    public void setVitalSignId(LongFilter vitalSignId) {
        this.vitalSignId = vitalSignId;
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
        final HealthConcernCriteria that = (HealthConcernCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(vitalSignId, that.vitalSignId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, vitalSignId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HealthConcernCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (vitalSignId != null ? "vitalSignId=" + vitalSignId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

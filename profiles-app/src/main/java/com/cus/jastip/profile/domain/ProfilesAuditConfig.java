package com.cus.jastip.profile.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProfilesAuditConfig.
 */
@Entity
@Table(name = "profiles_audit_config")
@Document(indexName = "profilesauditconfig")
public class ProfilesAuditConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "active_status")
    private Boolean activeStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public ProfilesAuditConfig entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Boolean isActiveStatus() {
        return activeStatus;
    }

    public ProfilesAuditConfig activeStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
        return this;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProfilesAuditConfig profilesAuditConfig = (ProfilesAuditConfig) o;
        if (profilesAuditConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profilesAuditConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfilesAuditConfig{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", activeStatus='" + isActiveStatus() + "'" +
            "}";
    }
}

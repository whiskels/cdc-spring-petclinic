package org.springframework.samples.petclinic.audit;

import org.hibernate.envers.RevisionType;

import java.util.HashMap;
import java.util.Map;

public class RevisionDto {

	Class<?> entityClass;

	RevisionType revisionType;

	Object entityId;

	Object currentRevisionEntity;

	Object previousRevisionEntity;

	Map<String, Object> context = new HashMap<>();

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(final Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public RevisionType getRevisionType() {
		return revisionType;
	}

	public void setRevisionType(final RevisionType revisionType) {
		this.revisionType = revisionType;
	}

	public Object getCurrentRevisionEntity() {
		return currentRevisionEntity;
	}

	public void setCurrentRevisionEntity(final Object currentRevisionEntity) {
		this.currentRevisionEntity = currentRevisionEntity;
	}

	public Object getPreviousRevisionEntity() {
		return previousRevisionEntity;
	}

	public void setPreviousRevisionEntity(final Object previousRevisionEntity) {
		this.previousRevisionEntity = previousRevisionEntity;
	}

	public Object getEntityId() {
		return entityId;
	}

	public void setEntityId(final Object entityId) {
		this.entityId = entityId;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(final Map<String, Object> context) {
		this.context = context;
	}

}

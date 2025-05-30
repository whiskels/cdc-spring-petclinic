package org.springframework.samples.petclinic.audit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.io.Serializable;

@Entity
@RevisionEntity(PreviousRevisionAwareListener.class)
@Table(name = "revinfo")
class CustomAuditRevisionEntity implements Serializable {

	@Id
	@RevisionNumber
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@RevisionTimestamp
	private long timestamp;

	private String acceptLanguage;

	private String userAgent;

	public CustomAuditRevisionEntity() {
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(final String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(final String userAgent) {
		this.userAgent = userAgent;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

}

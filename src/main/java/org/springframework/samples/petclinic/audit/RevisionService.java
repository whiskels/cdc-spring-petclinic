package org.springframework.samples.petclinic.audit;

import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;

@Component
public class RevisionService {

	private final EntityManager entityManager;

	public RevisionService(final @Lazy EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Optional<RevisionDto> findRevisionDto(final Class<?> entityClass, final Object entityId,
			final RevisionType revisionType, final Object revisionEntity) {
		AuditReader auditReader = AuditReaderFactory.get(entityManager);
		CustomAuditRevisionEntity currentRevisionEntity = (CustomAuditRevisionEntity) revisionEntity;

		var latestRevisions = auditReader.createQuery()
			.forRevisionsOfEntity(entityClass, true, true)
			.add(AuditEntity.revisionNumber().le(currentRevisionEntity.getId()))
			.add(AuditEntity.id().eq(entityId))
			.addOrder(AuditEntity.revisionNumber().desc())
			.setMaxResults(2)
			.getResultList();
		if (CollectionUtils.isEmpty(latestRevisions)) {
			return Optional.empty();
		}

		var revisionDto = new RevisionDto();
		revisionDto.setEntityClass(entityClass);
		revisionDto.setRevisionType(revisionType);
		revisionDto.setCurrentRevisionEntity(latestRevisions.get(0));
		revisionDto.setEntityId(entityId);
		revisionDto.setContext(Map.of("acceptLanguage", currentRevisionEntity.getAcceptLanguage(), "userAgent",
				currentRevisionEntity.getUserAgent()));
		if (revisionType != RevisionType.ADD) {
			revisionDto.setPreviousRevisionEntity(latestRevisions.get(1));
		}
		return Optional.of(revisionDto);
	}

}

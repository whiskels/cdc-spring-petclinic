package org.springframework.samples.petclinic.audit;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
class PreviousRevisionAwareListener implements EntityTrackingRevisionListener {

	private static final Logger LOG = LoggerFactory.getLogger(PreviousRevisionAwareListener.class);

	private final RevisionService revisionService;

	private final RevisionAnalyser revisionAnalyser;

	public PreviousRevisionAwareListener(final RevisionService revisionService,
			final RevisionAnalyser revisionAnalyser) {
		this.revisionService = revisionService;
		this.revisionAnalyser = revisionAnalyser;
	}

	@Override
	public void newRevision(final Object revisionEntity) {
		RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
		if (attribs != null) {
			CustomAuditRevisionEntity exampleRevEntity = (CustomAuditRevisionEntity) revisionEntity;
			HttpServletRequest request = ((ServletRequestAttributes) attribs).getRequest();
			exampleRevEntity.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
			exampleRevEntity.setAcceptLanguage(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
			LOG.debug("Setting user agent: {} and accept language: {} for revision entity: {}",
					exampleRevEntity.getUserAgent(), exampleRevEntity.getAcceptLanguage(), exampleRevEntity);
		}
	}

	@Override
	public void entityChanged(final Class entityClass, final String entityName, final Object entityId,
			final RevisionType revisionType, final Object revisionEntity) {
		revisionService.findRevisionDto(entityClass, entityId, revisionType, revisionEntity)
			.ifPresent(revisionAnalyser::analyse);
	}

}

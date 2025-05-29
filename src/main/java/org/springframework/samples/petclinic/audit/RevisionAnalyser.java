package org.springframework.samples.petclinic.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Map.entry;

@Component
class RevisionAnalyser {
	private static final Logger LOG = LoggerFactory.getLogger(RevisionAnalyser.class);

	void analyse(RevisionDto revisionDto) {
		var currentRevision = revisionDto.getCurrentRevisionEntity();
		var previousRevision = revisionDto.getPreviousRevisionEntity();

		List<RevisionComparisonDto> result = new ArrayList<>();
		try {
			var currentClass = revisionDto.getEntityClass();
			while (currentClass != null && currentClass != Object.class) {
				var fields = currentClass.getDeclaredFields();
				for (Field field : fields) {
					if (!field.isAnnotationPresent(AuditContext.class)) {
						 continue;
					}
					field.setAccessible(true);
					Object current = field.get(currentRevision);
					Object previous = previousRevision != null ? field.get(previousRevision) : null;
					if (Objects.equals(current, previous)) {
						continue;
					}
					RevisionComparisonDto revisionComparisonDto = new RevisionComparisonDto();
					revisionComparisonDto.name = field.getAnnotation(AuditContext.class).name();
					revisionComparisonDto.previous = previous;
					revisionComparisonDto.current = current;
					result.add(revisionComparisonDto);
				}
				currentClass = currentClass.getSuperclass();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		if (CollectionUtils.isEmpty(result)) {
			return;
		}

		LOG.info("Entity {}, context {}, changed: {}",
				revisionDto.getEntityId(),
				revisionDto.getContext(),
				result.stream()
						.map(RevisionComparisonDto::toString)
						.collect(Collectors.joining()));
	}

	static class RevisionComparisonDto {
		String name;
		Object previous;
		Object current;

		@Override
		public String toString() {
			return String.format("%n%s: %s -> %s", name, previous, current);
		}
	}
}

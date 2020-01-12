package ro.unibuc.master.groupexpensetracker.common.utils;

import org.springframework.data.jpa.domain.Specification;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EntitySpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 1L;
    private final List<SearchCriteria> searchCriteriaList;

    public EntitySpecification(List<SearchCriteria> searchCriteriaList) {
        this.searchCriteriaList = searchCriteriaList;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        Stream<Predicate> predicatesStream = this.searchCriteriaList.stream()
                .map(searchCriteria -> this.createPredicateFromSearchParam(root, criteriaBuilder, searchCriteria))
                .filter(Objects::nonNull);
        Predicate[] predicates = predicatesStream.toArray(Predicate[]::new);

        return criteriaBuilder.and(predicates);
    }

    private Predicate createPredicateFromSearchParam(Root<T> root, CriteriaBuilder cb, SearchCriteria searchParam) {
        Class fieldClass = this.getPath(root, searchParam).getJavaType();
        if (LocalDateTime.class.isAssignableFrom(fieldClass)) {
            LocalDateTime dateValue;
            switch (searchParam.getOperation()) {
                case ">":
                    dateValue = StringUtils.convertStringToDate(searchParam.getValue()).atTime(0, 0);
                    return cb.greaterThanOrEqualTo((Expression<LocalDateTime>) this.getPath(root, searchParam), dateValue);
                case "<":
                    dateValue = StringUtils.convertStringToDate(searchParam.getValue()).atTime(0, 0).plusDays(1);
                    return cb.lessThanOrEqualTo((Expression<LocalDateTime>) this.getPath(root, searchParam), dateValue);
                case ":":
                    String[] localDateTimes = searchParam.getValue().split("<<");
                    LocalDateTime startDate = StringUtils.convertStringToDate(localDateTimes[0]).atTime(0, 0);
                    LocalDateTime endDate = StringUtils.convertStringToDate(localDateTimes[1]).atTime(0, 0).plusDays(1);
                    return cb.between((Expression<LocalDateTime>) this.getPath(root, searchParam), startDate, endDate);
            }
        } else if (String.class.isAssignableFrom(fieldClass)) {
            return cb.like(cb.lower((Expression<String>) this.getPath(root, searchParam)),
                    "%" + searchParam.getValue().toLowerCase() + "%");
        } else if (Long.class.isAssignableFrom(fieldClass)) {
            switch (searchParam.getOperation()) {
                case ":":
                    return cb.equal((Expression<Long>) this.getPath(root, searchParam), searchParam.getValue());
                case "!":
                    return cb.notEqual((Expression<Long>) this.getPath(root, searchParam), searchParam.getValue());
            }
        } else if (Boolean.class.isAssignableFrom(fieldClass)) {
            return cb.equal(root.get(searchParam.getKey()), Boolean.parseBoolean(searchParam.getValue()));
        } else if (List.class.isAssignableFrom(fieldClass)) {
            Join join = root.join(searchParam.getKey());
            return cb.equal(join.get("id"), Long.parseLong(searchParam.getValue()));
        } else if (UserProfile.class.isAssignableFrom(fieldClass)) {
            Join join = root.join(searchParam.getKey());
            return cb.equal(join.get("id"), Long.parseLong(searchParam.getValue()));
        } else if (Trip.class.isAssignableFrom(fieldClass)) {
            Join join = root.join(searchParam.getKey());
            return cb.equal(join.get("id"), Long.parseLong(searchParam.getValue()));
        }

        return null;
    }

    private Path<T> getPath(Root<T> root, SearchCriteria searchParam) {
        Path<T> path;
        if (searchParam.getKey().contains(".")) {
            String[] split = searchParam.getKey().split("\\.");
            int keyPosition = 0;
            path = root.get(split[keyPosition]);
            for (String criteriaKeys : split) {
                if (keyPosition > 0) {
                    path = path.get(criteriaKeys);
                }
                keyPosition++;
            }
        } else {
            path = root.get(searchParam.getKey());
        }
        return path;
    }
}

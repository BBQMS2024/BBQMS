package ba.unsa.etf.si.bbqms.repository.specification;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TicketSpecifications {
    public static Specification<Ticket> fieldsSpecification(final Long id,
                                                            final Long number,
                                                            final Set<Service> services,
                                                            final Branch branch) {
        return (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(
                        criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id))
                );
            }

            if (number != null) {
                predicates.add(
                        criteriaBuilder.and(criteriaBuilder.equal(root.get("number"), number))
                );
            }

            if (services != null) {
                final List<Long> ids = services.stream().map(Service::getId).toList();
                predicates.add(
                        criteriaBuilder.and(root.get("service").get("id").in(ids))
                );
            }

            if (branch != null) {
                predicates.add(
                        criteriaBuilder.and(criteriaBuilder.equal(root.get("branch").get("id"), branch.getId()))
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Ticket> createdBefore(final Instant instant) {
        return (root, query, criteriaBuilder) -> {
            if (instant != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.lessThan(root.get("createdAt"), instant)
                );
            } else {
                return criteriaBuilder.and();
            }
        };
    }

    public static Specification<Ticket> createdAfter(final Instant instant) {
        return (root, query, criteriaBuilder) -> {
            if (instant != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThan(root.get("createdAt"), instant)
                );
            } else {
                return criteriaBuilder.and();
            }
        };
    }
}

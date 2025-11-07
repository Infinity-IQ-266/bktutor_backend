package com.bktutor.specification;

import com.bktutor.common.entity.Booking;
import com.bktutor.common.entity.Student;
import com.bktutor.common.enums.BookingStatus;
import com.bktutor.common.enums.BookingType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

@Component
public class BookingSpecification {

    public static Specification<Booking> hasSubjectLike(String subjectName) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(subjectName)) {
                return null;
            }
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("student", JoinType.LEFT);
                root.fetch("slot", JoinType.LEFT);
            }
            return cb.like(cb.lower(root.get("subject")), "%" + subjectName.toLowerCase() + "%");
        };
    }

    public static Specification<Booking> hasStatus(BookingStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("student", JoinType.LEFT);
                root.fetch("slot", JoinType.LEFT);
            }

            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Booking> hasType(BookingType type) {
        return (root, query, cb) -> {
            if (type == null) {
                return null;
            }

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("student", JoinType.LEFT);
                root.fetch("slot", JoinType.LEFT);
            }

            return cb.equal(root.get("type"), type);
        };
    }

    public static Specification<Booking> belongsToStudent(String username) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(username)) {
                return null;
            }

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("student", JoinType.LEFT);
            }

            Join<Booking, Student> studentJoin = root.join("student", JoinType.LEFT);
            return cb.equal(studentJoin.get("username"), username);
        };
    }
}

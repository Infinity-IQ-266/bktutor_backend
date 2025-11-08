package com.bktutor.specification;

import com.bktutor.common.entity.Booking;
import com.bktutor.common.entity.Student;
import com.bktutor.common.entity.Tutor;
import com.bktutor.common.enums.BookingStatus;
import com.bktutor.common.enums.BookingType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    // ✅ Student only
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

    // ✅ Tutor only
    public static Specification<Booking> belongsToTutor(String username) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(username)) {
                return null;
            }

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("slot", JoinType.LEFT);
            }

            Join<Object, Object> slotJoin = root.join("slot", JoinType.LEFT);
            Join<Object, Object> tutorJoin = slotJoin.join("tutor", JoinType.LEFT);

            return cb.equal(tutorJoin.get("username"), username);
        };
    }

    public static Specification<Booking> belongsToUser(String username) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(username)) {
                return null;
            }

            if (!query.getResultType().equals(Long.class) && !query.getResultType().equals(long.class)) {
                if (root.getJoins().isEmpty()) { // chỉ fetch khi chưa join
                    root.fetch("student", JoinType.LEFT);
                    root.fetch("slot", JoinType.LEFT);
                }
            }

            // Join cả hai hướng
            Join<Booking, Student> studentJoin = root.join("student", JoinType.LEFT);
            Join<Object, Object> slotJoin = root.join("slot", JoinType.LEFT);
            Join<Object, Object> tutorJoin = slotJoin.join("tutor", JoinType.LEFT);

            return cb.or(
                    cb.equal(studentJoin.get("username"), username),
                    cb.equal(tutorJoin.get("username"), username)
            );
        };
    }
}

package com.bktutor.specification;

import com.bktutor.common.entity.Department;
import com.bktutor.common.entity.Tutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;

@Component
public class TutorSpecification {

    public static Specification<Tutor> hasNameLike(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) {
                return null;
            }
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("department", jakarta.persistence.criteria.JoinType.LEFT);
            }
            return cb.like(cb.lower(root.get("fullName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Tutor> inDepartment(String departmentName) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(departmentName)) {
                return null;
            }
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("department", jakarta.persistence.criteria.JoinType.LEFT);
            }
            Join<Tutor, Department> departmentJoin = root.join("department");
            return cb.equal(departmentJoin.get("name"), departmentName);
        };
    }
}

package com.bktutor.specification;

import com.bktutor.common.entity.Department;
import com.bktutor.common.entity.Tutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

@Component
public class TutorSpecification {

    public static Specification<Tutor> hasNameLike(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) {
                return null;
            }
            return cb.like(cb.lower(root.get("fullName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Tutor> inDepartment(String departmentName) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(departmentName)) {
                return null;
            }
            // Chỉ join để filter
            Join<Tutor, Department> departmentJoin = root.join("department", JoinType.LEFT);
            return cb.equal(departmentJoin.get("name"), departmentName);
        };
    }
}
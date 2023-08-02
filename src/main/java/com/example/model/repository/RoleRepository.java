package com.example.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;
import com.example.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}

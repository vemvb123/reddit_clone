package com.example.Reddit.clone.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {



    Role findByName(String name);


    @Query(value = "INSERT INTO user_has_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    void addRoleToUser(@Param("userId") Long userId, @Param("roleId") Long roleId);
}

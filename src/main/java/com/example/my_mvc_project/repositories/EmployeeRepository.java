package com.example.my_mvc_project.repositories;

import com.example.my_mvc_project.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @Transactional
    @Modifying
    @Query("update Employee e set e.password = ?1 where e.password = ?2 and e.id = ?3")
    void updatePasswordByPasswordAndId(String newPassword, String oldPassword, Long id);
    boolean existsByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "update Employee e set e.username=?1,e.firstName=?2,e.lastName=?3 where e.id=?4")
    void updateEmployee(String username,String firstName,String lastName,Long id);

    @Query("""
            select e from Employee e
            where e.firstName like concat('%', ?1, '%') or e.lastName like concat('%', ?1, '%') or e.username like concat('%', ?1, '%')""")
    Page<Employee> findAllByName(@NonNull String name, Pageable pageable);

    @Query("from Employee e where e.username = ?1 and e.accountNonLocked=true")
    Optional<Employee> findByUsername(@NonNull String username);

    @Modifying
    @Query(value = "update Employee e set e.accountNonLocked=?1 where e.id=?2")
    int updateAccountLockedFalseById(boolean accountLocked,Long id);

    Optional<Employee> findByIdAndAccountNonLockedFalse(Long id);
}
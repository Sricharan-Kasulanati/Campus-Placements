package com.example.campus_placements.student.repository;

import com.example.campus_placements.student.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByStudentIdAndCompanyId(Long studentId, Long companyId);
    Optional<Registration> findByStudentIdAndCompanyId(Long studentId, Long companyId);

    @Query("select r.company.id from Registration r where r.student.id = :studentId")
    List<Long> findCompanyIdsByStudent(@Param("studentId") Long studentId);

    List<Registration> findByStudentIdOrderByRegisteredAtDesc(Long studentId);

    @Query("""
           select count(distinct r.student.id)
           from Registration r
           where r.company.id = :companyId
           """)
    long countDistinctStudentIdByCompanyId(@Param("companyId") Long companyId);

    List<Registration> findByCompanyId(Long companyId);

    @Query("""
           select r.student.email
           from Registration r
           where r.company.id = :companyId
           """)
    List<String> findEmailsByCompanyId(@Param("companyId") Long companyId);

}

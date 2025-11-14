package com.example.campus_placements.company.repository;

import com.example.campus_placements.company.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("""
        select c from Company c
        where (:kw is null or lower(c.name) like lower(concat('%', :kw, '%'))
               or lower(c.description) like lower(concat('%', :kw, '%')))
          and (:cat is null or lower(c.category) = lower(:cat))
          and (:loc is null or lower(c.location) like lower(concat('%', :loc, '%')))
    """)
    Page<Company> search(String kw, String cat, String loc, Pageable pageable);
}

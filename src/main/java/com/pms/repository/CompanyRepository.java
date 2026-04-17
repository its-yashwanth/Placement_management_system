package com.pms.repository;

import com.pms.model.Company;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByApproved(boolean approved);
    Optional<Company> findByUserAccountEmail(String email);
}

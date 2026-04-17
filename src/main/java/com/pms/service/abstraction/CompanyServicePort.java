package com.pms.service.abstraction;

import com.pms.model.Company;
import java.util.List;

public interface CompanyServicePort {
    List<Company> getAllCompanies();
    List<Company> getPendingCompanies();
    List<Company> getApprovedCompanies();
    Company getCompanyById(Long id);
    Company save(Company company);
    Company getByEmail(String email);
    void approveCompany(Long companyId);
    void deleteCompany(Long companyId);
    void rejectCompany(Long companyId);
}

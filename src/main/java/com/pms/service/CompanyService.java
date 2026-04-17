package com.pms.service;

import com.pms.model.Company;
import com.pms.repository.ApplicationRepository;
import com.pms.repository.CompanyRepository;
import com.pms.repository.PlacementDriveRepository;
import com.pms.service.abstraction.CompanyServicePort;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CompanyService implements CompanyServicePort {

    private final CompanyRepository companyRepository;
    private final PlacementDriveRepository placementDriveRepository;
    private final ApplicationRepository applicationRepository;

    public CompanyService(CompanyRepository companyRepository,
                          PlacementDriveRepository placementDriveRepository,
                          ApplicationRepository applicationRepository) {
        this.companyRepository = companyRepository;
        this.placementDriveRepository = placementDriveRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> getPendingCompanies() {
        return companyRepository.findByApproved(false);
    }

    @Override
    public List<Company> getApprovedCompanies() {
        return companyRepository.findByApproved(true);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + id));
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company getByEmail(String email) {
        return companyRepository.findByUserAccountEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Company not found for email: " + email));
    }

    @Override
    public void approveCompany(Long companyId) {
        Company company = getCompanyById(companyId);
        company.setApproved(true);
        companyRepository.save(company);
    }

    @Override
    @Transactional
    public void deleteCompany(Long companyId) {
        placementDriveRepository.findByCompanyId(companyId)
                .forEach(drive -> applicationRepository.deleteByPlacementDriveId(drive.getId()));
        placementDriveRepository.deleteByCompanyId(companyId);
        companyRepository.deleteById(companyId);
    }

    @Override
    public void rejectCompany(Long companyId) {
        deleteCompany(companyId);
    }
}

package com.nj.websystem.service;

import com.nj.websystem.model.PatientScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PatientScanServise extends PagingAndSortingRepository<PatientScan, Long> {
    PatientScan findAllById(Long id);

    Page<PatientScan> findAll(Pageable pageable);

    @Query("From PatientScan ORDER BY dateCreated DESC")
    public List<PatientScan> getAllByPatientId(String patientId);

    public List<PatientScan> getAllByBillingNumber(String billingNumber);
}

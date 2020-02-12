package com.nj.websystem.service;

import com.nj.websystem.enums.TestType;
import com.nj.websystem.model.MedicalTest;
import com.nj.websystem.model.PatientMedicalTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalTestService extends JpaRepository<MedicalTest, Long> {

    List<MedicalTest> findById(String id);

/*    @Query("Select MedicalTest from MedicalTest  where name like :name%")
    Page<MedicalTest> findByNameContaining(String name, Pageable page);*/

/*    @Query("Select t from MedicalTest t where t.name like :name%")
    Page<MedicalTest> findByNameContaining(String name, Pageable page);*/

    @Query("Select t from MedicalTest t where t.name like :strName%")
    List<MedicalTest> findByNameContaining(String strName);

    @Query("From MedicalTest ORDER BY testNumber ASC")
    List<MedicalTest> findAll();

    List<MedicalTest> findAllByTestType(TestType testType);

    List<MedicalTest>  findFirst15ByOrderByDateCreatedDesc();

    List<MedicalTest> findAllByName(String testName);

}

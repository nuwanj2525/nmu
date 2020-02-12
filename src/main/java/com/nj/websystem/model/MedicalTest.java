package com.nj.websystem.model;

import com.nj.websystem.enums.LabType;
import com.nj.websystem.enums.Status;
import com.nj.websystem.enums.TestType;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "TBL_MEDICAL_TEST")
public class MedicalTest {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "TBL_MEDICAL_TEST_SEQ", sequenceName = "TBL_MEDICAL_TEST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_MEDICAL_TEST_SEQ")
    private Long id;
    private TestType testType;
    private String testNumber;
    private String name;
    @Column(precision = 2, scale = 0)
    private Double price;
    private String actionBy;
    private LabType labType;
    private Date dateCreated;
    private Date lastModified;
    private Status status;
    @Transient
    private String tmpid;
    private String optTests;
    private String remarks;

    public MedicalTest() {
    }

    public MedicalTest(TestType testType, String name) {
        this.testType = testType;
        this.name = name;
    }

    public MedicalTest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public LabType getLabType() {
        return labType;
    }

    public void setLabType(LabType labType) {
        this.labType = labType;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTmpid() {
        return tmpid;
    }

    public void setTmpid(String tmpid) {
        this.tmpid = tmpid;
    }

    public String getOptTests() {
        return optTests;
    }

    public void setOptTests(String optTests) {
        this.optTests = optTests;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalTest)) return false;
        MedicalTest that = (MedicalTest) o;
        return Objects.equals(getTestType(), that.getTestType()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getName());
    }

}

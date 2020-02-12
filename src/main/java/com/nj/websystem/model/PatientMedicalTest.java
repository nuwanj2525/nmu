package com.nj.websystem.model;

import com.nj.websystem.enums.LabType;
import com.nj.websystem.enums.Status;
import com.nj.websystem.enums.TestType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_PATIENT_MEDICAL_TEST")
public class PatientMedicalTest {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "TBL_PATIENT_MEDICAL_TEST_SEQ", sequenceName = "TBL_PATIENT_MEDICAL_TEST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_PATIENT_MEDICAL_TEST_SEQ")
    private Long id;
    private String testNumber;
    private String patientId;
    private TestType testType;
    private String billingNumber;
    private String name;
    @Column(precision = 2, scale = 0)
    private Double price;
    private String reference;
    private String units;
    private String results;
    private String seenBy;
    private LabType labType;
    private String actionBy;
    private Date dateCreated;
    private Date lastModified;
    private Status status;
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public String getBillingNumber() {
        return billingNumber;
    }

    public void setBillingNumber(String billingNumber) {
        this.billingNumber = billingNumber;
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

    public String getSeenBy() {
        return seenBy;
    }

    public void setSeenBy(String seenBy) {
        this.seenBy = seenBy;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public LabType getLabType() {
        return labType;
    }

    public void setLabType(LabType labType) {
        this.labType = labType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "PatientMedicalTest{" +
                "id=" + id +
                ", testNumber='" + testNumber + '\'' +
                ", patientId='" + patientId + '\'' +
                ", testType=" + testType +
                ", billingNumber='" + billingNumber + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", reference='" + reference + '\'' +
                ", units='" + units + '\'' +
                ", results='" + results + '\'' +
                ", seenBy='" + seenBy + '\'' +
                ", labType=" + labType +
                ", actionBy='" + actionBy + '\'' +
                ", dateCreated=" + dateCreated +
                ", lastModified=" + lastModified +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

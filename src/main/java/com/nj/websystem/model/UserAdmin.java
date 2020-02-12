package com.nj.websystem.model;

import com.nj.websystem.enums.Status;
import com.nj.websystem.enums.UserRoles;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_USER_ADMIN")
public class UserAdmin {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "TBL_USER_ADMIN_SEQ", sequenceName = "TBL_USER_ADMIN_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_USER_ADMIN_SEQ")
    private Long id;
    private String userId;
    private String fistName;
    private String middleName;
    private String lastName;
    private String passWord;
    private UserRoles role;
    private String userEmail;
    private String userPFNumber;
    private String telNumber;
    private String mobNumber;
    private Status active;
    private Date dateCreated;
    private String actionBy;
    private Date lastDateModified;

    public UserAdmin() {
    }

    public UserAdmin(Long id, String userId, String fistName, String lastName, String passWord, UserRoles role, String userPFNumber, Date dateCreated, String actionBy) {
        this.id = id;
        this.userId = userId;
        this.fistName = fistName;
        this.lastName = lastName;
        this.passWord = passWord;
        this.role = role;
        this.userPFNumber = userPFNumber;
        this.dateCreated = dateCreated;
        this.actionBy = actionBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPFNumber() {
        return userPFNumber;
    }

    public void setUserPFNumber(String userPFNumber) {
        this.userPFNumber = userPFNumber;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public Status getActive() {
        return active;
    }

    public void setActive(Status active) {
        this.active = active;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public Date getLastDateModified() {
        return lastDateModified;
    }

    public void setLastDateModified(Date lastDateModified) {
        this.lastDateModified = lastDateModified;
    }
}

package com.nj.websystem.service;

import com.nj.websystem.model.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAdminService extends JpaRepository<UserAdmin, Long> {

    // List<UserAdmin> findUserAdminByStatus(Status status);

    //@Query("From UserAdmin ORDER BY lastDateModified DESC")
    //List<UserAdmin> findAll();

    List<UserAdmin> findByUserId(String userId);

}

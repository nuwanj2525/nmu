package com.nj.websystem.controller;

import com.nj.websystem.enums.Gender;
import com.nj.websystem.enums.Status;
import com.nj.websystem.model.Patient;
import com.nj.websystem.rest.HttpResponse;
import com.nj.websystem.service.PatientService;
import com.nj.websystem.util.CSVUtils;
import com.nj.websystem.util.DateUtility;
import com.nj.websystem.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/patient")
public class PatientController {

    static Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientService services;

    private static Patient logError(List<String> l, Patient p, String error) {
        StringBuilder _sb = new StringBuilder(l.get(2));
        _sb.append("/n/r");
        //_sb.append("Error in DOB Years :"+ l.get(7) +"/" + l.get(8) +"/" + l.get(9) );
        _sb.append(error);
        p.setRemarks(_sb.toString());
        return p;
    }

/*    @GetMapping
    public ResponseEntity<List<Patient>> getAllEmployees(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<Patient> list = services.getAllEmployees(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<Patient>>(list, new HttpHeaders(), HttpStatus.OK);
    }*/

    private static boolean get(String val) {
        if (val != null)
            if (!val.isEmpty() && !val.equals("?")) {
                return true;
            } else {
                return false;
            }
        else
            return false;
    }

    @RequestMapping(value = "/getNextPatientId", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse getNextPatientId() {

        HttpResponse res = new HttpResponse();
        // logger.info("getFistDayOfMonth > " +DateUtility.getFistDayOfMonth() +" | DateUtility.getMonthEnd() > " + DateUtility.getMonthEnd());
        int yearlyRecordCount = services.getAllByDateCreatedBetween(DateUtility.getFistDayOfMonth(),DateUtility.getMonthEnd()).size();
        String nextPatientId = StringUtility.getCustDateByPatten(StringUtility.YY) + StringUtility.getCustDateByPatten(StringUtility.MM) + StringUtility.getCustDateByPatten(StringUtility.DD)+StringUtility.getCustDateByPatten(String.format("%03d", (yearlyRecordCount + 1)));
        logger.info("Patient - NextPatientId : {} " + nextPatientId);
        res.setResponse(nextPatientId);
        res.setSuccess(true);
        res.setRecCount(1);
        return res;
    }

    @RequestMapping(value = "/getList", method = RequestMethod.GET, headers = "Accept=application/json")
    public Page<Patient> getList() {
        Pageable paging = PageRequest.of(0, 10, Sort.by("id"));
        Page<Patient> list = services.findAll(paging);
        logger.info("Count of UserAdmin : {} " + list.getTotalElements());
        return list;
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse getById(@RequestParam(value = "id", required = false) long id) {
        logger.info("Request Patient getById : {} " + id);
        HttpResponse res = new HttpResponse();
        Optional<Patient> patientList = services.findById(id);
        if (patientList != null) {
            res.setResponse(patientList.get());
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Invalid Patient !");
        }
        return res;
    }

    @RequestMapping(value = "/findByPatientId", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse findByPatientId(@RequestParam(value = "patientId", required = true) String patientId) {
        logger.info("Request Patient findByPatientId : {} " + patientId);
        HttpResponse res = new HttpResponse();
        List<Patient> patientList = services.findByPatientId(patientId);
        if (patientList != null && !patientList.isEmpty()) {
            res.setResponse(patientList.get(0));
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Invalid Patient !");
        }
        return res;
    }

    @RequestMapping(value = "/findByPatientListById", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse findByPatientListById(@RequestParam(value = "patientId", required = false) String patientId) {
        logger.info("Request Patient findByPatientListById : {} " + patientId);

/*
        final PageRequest page1 = new PageRequest(
                0, 20, Sort.Direction.ASC, "lastName", "salary"
        );
*/

        // Pageable paging = PageRequest.of(1, 10000, Sort.Direction.DESC, "patientId");
        HttpResponse res = new HttpResponse();
        List<String> patientList = services.findByPatientIdContaining(patientId);
        if (patientList != null && !patientList.isEmpty()) {
            res.setResponse(patientList);
            res.setSuccess(true);
            res.setRecCount(patientList.size());
        } else {
            res.setSuccess(false);
            res.setException("Invalid Patient ID !");
        }
        return res;
    }

    @RequestMapping(value = "/findByPatientIdContainingPages", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse findByPatientIdContainingAndStatus(@RequestParam(value = "patientId", required = false) String patientId) {
        logger.info("Request Patient findByPatientListById : {} " + patientId);
        Pageable paging = PageRequest.of(1, 100, Sort.by("patientId"));
        HttpResponse res = new HttpResponse();
        List<Patient> patientList = services.findByPatientIdContainingAndStatus(patientId, Status.ACTIVE);
        if (patientList != null && !patientList.isEmpty()) {
            res.setResponse(patientList);
            res.setSuccess(true);
            res.setRecCount(patientList.size());
        } else {
            res.setSuccess(false);
            res.setException("Invalid Patient ID !");
        }
        return res;
    }

    @RequestMapping(value = "/findByNicNumber", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse findByNicNumber(@RequestParam(value = "id", required = false) String nicNumber) {
        logger.info("Request Patient findByNicNumber : {} " + nicNumber);
        HttpResponse res = new HttpResponse();
        Pageable paging = PageRequest.of(1, 12, Sort.by("id"));
        List<Patient> patientList = services.findByNicNumber(nicNumber, paging);
        if (patientList != null && !patientList.isEmpty()) {
            res.setResponse(patientList.get(0));
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Invalid Patient !");
        }
        return res;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public Patient save(@RequestBody Patient obj) {
        logger.info("save Patient Name : {} " + obj.getNicNumber());
        return services.save(obj);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public HttpResponse delete(@RequestParam(value = "id", required = false) Long id) {
        logger.info("Delete Patient id : {} " + id);
        HttpResponse response = new HttpResponse();
        Patient item = services.findAllById(id);
        if (item != null) {
            services.delete(item);
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            logger.info("Record has been already deleted : {} " + id);
            response.setException("Record has been already deleted");
        }
        return response;
    }
}

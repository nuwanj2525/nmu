package com.nj.websystem.controller;

import com.nj.websystem.enums.Status;
import com.nj.websystem.enums.TestType;
import com.nj.websystem.model.Patient;
import com.nj.websystem.model.PatientMedicalTest;
import com.nj.websystem.rest.HttpResponse;
import com.nj.websystem.service.PatientMedicalTestService;
import com.nj.websystem.service.PatientService;
import com.nj.websystem.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/patientmedicaltest")
public class PatientMedicalTestController {

    static Logger logger = LoggerFactory.getLogger(PatientMedicalTestController.class);

    @Autowired
    private PatientMedicalTestService services;

    @Autowired
    private PatientService patientService;

    @RequestMapping(value = "/getList", method = RequestMethod.GET, headers = "Accept=application/json")
    public List getList() {
        List list = services.findFirst15ByOrderByDateCreatedDesc();
        logger.info("Count of UserAdmin : " + list.size());
        return list;
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse getById(@RequestParam(value = "id", required = false) long id) {
        logger.info("Request MedicalTest Id : {} " + id);
        HttpResponse res = new HttpResponse();
        List<PatientMedicalTest> patientList = services.findAll();
        if (patientList != null && !patientList.isEmpty()) {
            res.setResponse(patientList);
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Invalid MedicalTest !");
        }
        return res;
    }

    @RequestMapping(value = "/findAllByPatientIdAndType", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse findAllByPatientIdAndType(@RequestParam(value = "patientid", required = false) String patientid, @RequestParam(value = "type", required = false) TestType type) {
        logger.info("Request findAllByPatientIdAndType Id : {patientId, type} " + patientid + " | " + type);
        HttpResponse res = new HttpResponse();
        List<PatientMedicalTest> patientList = services.findAllByPatientIdAndTestType(patientid, type);
        if (patientList != null && !patientList.isEmpty()) {
            res.setResponse(patientList);
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Invalid Patient !");
        }
        return res;
    }

    @RequestMapping(value = "/getAllByTestTypeOrderByIdDesc", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse getAllByTestTypeOrderByIdDesc(@RequestParam(value = "type", required = false) TestType type) {
        logger.info("Request findAllByPatientIdAndType Id : {type} " + " | " + type);
        HttpResponse res = new HttpResponse();
        List<PatientMedicalTest> patientList = services.getAllByTestTypeOrderByIdDesc(type);
        PatientMedicalTest medicalTest = null;

        if (patientList.size() > 0) {
            medicalTest = patientList.get(0);
        }

        String nextNumber = "";
        if (medicalTest != null) {
            String exitsingNumber = medicalTest.getBillingNumber().substring(3, medicalTest.getBillingNumber().length());
            logger.info("Patient - NextPatientId : {} " + exitsingNumber);
            nextNumber = StringUtility.getCustDateByPatten(StringUtility.YY) + type + StringUtility.getFilledNumber((Integer.parseInt(exitsingNumber) + 1), 4L);
        } else {
            nextNumber = StringUtility.getCustDateByPatten(StringUtility.YY) + type + StringUtility.getFilledNumber((1), 4L);
        }

        if (!StringUtility.isEmpty(nextNumber)) {
            res.setResponse(nextNumber);
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Fail to read next sequence !");
        }

        return res;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponse save(@RequestBody PatientMedicalTest obj) {
        HttpResponse res = new HttpResponse();
        logger.info("Saving TestName : " + obj.getName());
        List<PatientMedicalTest> testsList = services.getAllByTestType(obj.getTestType());
        String testNumber = StringUtility.getCustDateByPatten(StringUtility.YY) + obj.getTestType() + String.format("%05d", (testsList.size() + 1));
        obj.setTestNumber(testNumber.toUpperCase());
        PatientMedicalTest savedMedicalTest = services.save(obj);

        if (savedMedicalTest != null) {
            //res.setResponse();
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Fail to save Medical Test : " + obj.getTestNumber());
        }
        return res;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public HttpResponse delete(@RequestParam(value = "id", required = false) Long id) {
        logger.info("Delete OfficeRoom Name : {} " + id);
        HttpResponse response = new HttpResponse();
        PatientMedicalTest item = services.getOne(id);
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

/*    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public HttpResponse delete(@RequestParam(value = "id", required = false) Long id) {
        logger.info("Delete UserAdmin Name : {} " + id);
        HttpResponse response = new HttpResponse();
        UserAdmin item = services.getOne(id);
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
*/

    @RequestMapping(value = "/findAllByPatientIdAndBillingNumber", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse findAllByPatientIdAndBillingNumber(@RequestParam(value = "patientid", required = false) String patientid, @RequestParam(value = "billingNumber", required = false) String billingNumber) {
        logger.info("Delete OfficeRoom Name : {} " + billingNumber);
        HttpResponse response = new HttpResponse();
        List<PatientMedicalTest> itemList = services.findAllByPatientIdAndBillingNumber(patientid, billingNumber);
        if (itemList != null && itemList.size() > 0) {
            response.setResponse(itemList);
            response.setRecCount(itemList.size());
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            logger.info("Record not found  : {} " + billingNumber);
            response.setException("Record not found  : {} " + billingNumber);
        }
        return response;
    }

    @RequestMapping(value = "/findAllByBillingNumber", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse findAllByBillingNumber(@RequestParam(value = "billingNumber", required = false) String billingNumber) {
        logger.info("findAllByBillingNumber : {} " + billingNumber);
        HttpResponse response = new HttpResponse();
        List<PatientMedicalTest> itemList = services.findAllByBillingNumberAndStatus(billingNumber, Status.OPEN);
        if (itemList != null && itemList.size() > 0) {
            if (itemList.size() > 0) {
                Patient patient = patientService.findByPatientId(itemList.get(0).getPatientId()).get(0);
                List test = new ArrayList();
                test.add(patient);
                test.add(itemList);
                response.setResponse(test);
            }
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            logger.info("Record not found  : {} " + billingNumber);
            response.setException("Record not found  : {} " + billingNumber);
        }
        return response;
    }

    @RequestMapping(value = "/findAllActiveByBillingNumber", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse findAllActiveByBillingNumber(@RequestParam(value = "billingNumber", required = false) String billingNumber) {
        logger.info("findAllActiveByBillingNumber : {} " + billingNumber);
        HttpResponse response = new HttpResponse();
        List<PatientMedicalTest> itemList = services.findAllByBillingNumber(billingNumber);
        if (itemList != null && itemList.size() > 0) {
            if (itemList.size() > 0) {
                Patient patient = patientService.findByPatientId(itemList.get(0).getPatientId()).get(0);
                List test = new ArrayList();
                test.add(patient);
                test.add(itemList);
                response.setResponse(test);
            }
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            logger.info("Record not found  : {} " + billingNumber);
            response.setException("Record not found  : {} " + billingNumber);
        }
        return response;
    }

    @RequestMapping(value = "/saveList", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponse bulkInsert(@RequestBody List<PatientMedicalTest> items) {
        logger.info("PatientMedicalTest count : {} " + items.size());
        HttpResponse response = new HttpResponse();
        items.forEach(i ->
                i.setId(null)
        );
        List result = services.saveAll(items);
        if (result != null && result.size() > 0) {
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            response.setException("Record not saved");
        }
        return response;

    }

}
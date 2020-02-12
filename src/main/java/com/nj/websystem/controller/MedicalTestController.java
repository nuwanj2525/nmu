package com.nj.websystem.controller;

import com.nj.websystem.enums.Status;
import com.nj.websystem.enums.TestType;
import com.nj.websystem.model.MedicalTest;
import com.nj.websystem.rest.HttpResponse;
import com.nj.websystem.service.MedicalTestService;
import com.nj.websystem.util.CSVUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/medicaltest")
public class MedicalTestController {

    static Logger logger = LoggerFactory.getLogger(MedicalTestController.class);

    @Autowired
    private MedicalTestService services;

    @RequestMapping(value = "/getList", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse getList() {
        HttpResponse res = new HttpResponse();
        //Pageable paging = PageRequest.of(1, 10, Sort.by("id"));
        List<MedicalTest> list = services.findFirst15ByOrderByDateCreatedDesc();
        logger.info("Count of MedicalTest : " + list.size());
        if (list != null && !list.isEmpty()) {
            res.setResponse(list);
            res.setSuccess(true);
            res.setRecCount(list.size());
        } else {
            res.setSuccess(false);
            res.setException("Invalid MedicalTest !");
        }
        return res;
    }

    @RequestMapping(value = "/getIdNameList", method = RequestMethod.GET, headers = "Accept=application/json")
    public Map getListName() {
        HttpResponse res = new HttpResponse();
        Map rex = new HashMap();
        List<MedicalTest> list = services.findAll();
        for(MedicalTest m:list){
            rex.put(m.getTestNumber(),m.getName());
        }
        return rex;
    }

    @RequestMapping(value = "/getListByName", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse getListByName(@RequestParam(value = "name", required = false) String name) {
        HttpResponse res = new HttpResponse();
        Pageable paging = PageRequest.of(1, 10, Sort.by("name"));
        List<MedicalTest> listOfPages = services.findByNameContaining(name);
        logger.info("Count of MedicalTest : " + listOfPages.size());
        if (listOfPages != null && !listOfPages.isEmpty()) {
            res.setResponse(listOfPages);
            res.setSuccess(true);
            res.setRecCount(listOfPages.size());
        } else {
            res.setSuccess(false);
            res.setException("Invalid MedicalTest !");
        }
        return res;
    }

    @RequestMapping(value = "/findAllByType", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse findAllByType(@RequestParam(value = "testType", required = false) TestType testType) {
        logger.info("Request MedicalTest type : {} ", testType);
        HttpResponse res = new HttpResponse();
        List<MedicalTest> patientList = services.findAllByTestType(testType);
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

    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public MedicalTest save(@RequestBody MedicalTest obj) {
        logger.info("TestName : " + obj.getName());
        return services.save(obj);
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

    @RequestMapping(value = "/bulkInsert", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<UserAdmin> bulkInsert(@RequestBody List<UserAdmin> items) {
        logger.info("UserAdmin countt : {} " + items.size());
        items.forEach(item -> {
            item.setId(null);
            item.setDateCreated(new Date());
        });
        return services.saveAll(items);
    }*/

    @RequestMapping(value = "/loadBulk", method = RequestMethod.GET, headers = "Accept=application/json")
    public List loadPatient() {
        List list = loadBulk();
        services.saveAll(list);
        logger.info("Count of loadBulk : {} " + list.size());
        return list;
    }


    private List loadBulk() {
        List<List> lineList = null;
        String csvFile = "/Users/sirimewanranathunga/Desktop/PatientData/juwan-2019/Hospital Test.csv";
        try {
            lineList = CSVUtils.LoadFile(csvFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<MedicalTest> listOfPatients = new ArrayList<>();
        MedicalTest p;
        int count = 0;
        for (List<String> l : lineList) {
            if (l.get(0).equals("ID Test")) {
                continue;
            }
            int j = 0;
            for (String s : l) {
                System.out.print("[" + j + "]=" + s + ", ");
                j++;
            }

            p = new MedicalTest();

            try {
                String data = l.get(0);
                if (CSVUtils.get(data)) {
                    p.setTestNumber(data);
                } else {
                    //CSVUtils.logError( l, p,"Error in Remarks :"+ data);
                }
            } catch (Exception e) {
                //CSVUtils.logError( l, p,"Error in Remarks :"+ e.getMessage());
            }
            p.setDateCreated(new Date());
            try {
                String data = l.get(2);
                if (CSVUtils.get(data)) {
                    p.setName(data);
                } else {
                    //CSVUtils.logError( l, p,"Error in DateCreated :"+ data);
                }
            } catch (Exception e) {
                //CSVUtils.logError( l, p,"Error in DateCreated :"+ e.getMessage());
            }

            try {
                String data = l.get(3);
                if (CSVUtils.get(data)) {
                    p.setPrice(new Double(data));
                } else {
                    //CSVUtils.logError( l, p,"Error in PatientName :"+ data);
                }
            } catch (Exception e) {
                //CSVUtils.logError( l, p,"Error in PatientName :"+ e.getMessage());
            }


            p.setActionBy("admin");
            p.setStatus(Status.ACTIVE);
            System.out.println(p.toString());
            listOfPatients.add(p);
            count++;
            // break;
        }
        return listOfPatients;
    }
}

class Compo{
    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


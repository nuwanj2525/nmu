package com.nj.websystem.controller.utils;

import com.nj.websystem.enums.Gender;
import com.nj.websystem.enums.LabType;
import com.nj.websystem.enums.Status;
import com.nj.websystem.enums.TestType;
import com.nj.websystem.model.MedicalTest;
import com.nj.websystem.model.Patient;
import com.nj.websystem.service.MedicalReferenceService;
import com.nj.websystem.service.MedicalTestService;
import com.nj.websystem.service.PatientService;
import com.nj.websystem.util.CSVUtils;
import com.nj.websystem.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestLoader {

    static Logger logger = LoggerFactory.getLogger(TestLoader.class);

    @Autowired
    private MedicalTestService services;

    private static MedicalTest logError(List<String> l, MedicalTest p, String error) {
        StringBuilder _sb = new StringBuilder(l.get(2));
        _sb.append("/n/r");
        //_sb.append("Error in DOB Years :"+ l.get(7) +"/" + l.get(8) +"/" + l.get(9) );
        _sb.append(error);
        p.setRemarks(_sb.toString());
        return p;
    }

    public void executeLoader(MedicalTestService medicalTestService) {

        services = medicalTestService;

        List<List> lineList = null;
        String csvFile = "/Users/sirimewanranathunga/Desktop/projects/PatientData/juwan-2019/cvs/nuwan_test_2019.csv";
        try {
            lineList = CSVUtils.LoadFile(csvFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        run(lineList);
    }

    private void run(List<List> lineList){
        List<MedicalTest> listOfPatients = new ArrayList<>();
        MedicalTest p;
        int count = 0;
        for (List<String> l : lineList) {
            if (l.get(0).equals("Group")) {
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
                if (StringUtility.get(data)) {
                    p.setTestNumber(data);
                } else {
                    logError(l, p, "Error in Remarks :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in Remarks :" + e.getMessage());
            }

            try {
                String data = l.get(1);
                if (StringUtility.get(data)) {
                    p.setTestType(TestType.valueOf(data.trim()));
                } else {
                    logError(l, p, "Error in setTestType :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in setTestType :" + e.getMessage());
            }

            try {
                String data = l.get(2);
                if (StringUtility.get(data)) {
                    p.setLabType(LabType.valueOf(data.trim()));
                } else {
                    logError(l, p, "Error in setLabType :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in setLabType :" + e.getMessage());
            }

            try {
                String data = l.get(3);
                if (StringUtility.get(data)) {
                    p.setName(data);
                } else {
                    logError(l, p, "Error in setName :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in setName :" + e.getMessage());
            }

            try {
                String data = l.get(4);
                if (StringUtility.get(data)) {
                    p.setPrice(new Double(data));
                } else {
                    logError(l, p, "Error in setPrice :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in setPrice :" + e.getMessage());
            }


            //p.setCityName();
            // p.setDistrictName();

            p.setActionBy("admin");
            p.setStatus(Status.ACTIVE);
            p.setDateCreated(new Date());
            System.out.println(p.toString());
            listOfPatients.add(p);
            count++;
            // break;
        }

        services.saveAll(listOfPatients);
        logger.info("Count of loadBulk : {} " + listOfPatients.size());
    }
}

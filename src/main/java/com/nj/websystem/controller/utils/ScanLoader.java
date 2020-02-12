package com.nj.websystem.controller.utils;

import com.nj.websystem.enums.Status;
import com.nj.websystem.enums.TestType;
import com.nj.websystem.model.MedicalTest;
import com.nj.websystem.service.MedicalTestService;
import com.nj.websystem.util.CSVUtils;
import com.nj.websystem.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ScanLoader {

    static Logger logger = LoggerFactory.getLogger(ScanLoader.class);

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
        String csvFile = "/Users/sirimewanranathunga/Desktop/projects/PatientData/juwan-2019/cvs/AllListedTests.csv";
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
                    p.setName(data);
                } else {
                    logError(l, p, "Error in DateCreated :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in DateCreated :" + e.getMessage());
            }

            try {
                String data = l.get(2);
                if (StringUtility.get(data)) {
                    p.setPrice(new Double(data));
                } else {
                    logError(l, p, "Error in PatientName :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in PatientName :" + e.getMessage());
            }

            try {
                String data = l.get(3);
                if (StringUtility.get(data)) {
                    p.setTestType(TestType.valueOf(data));
                } else {
                    logError(l, p, "Error in PatientName :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in PatientName :" + e.getMessage());
            }
            //p.setCityName();
            // p.setDistrictName();

            p.setActionBy("admin");
            p.setStatus(Status.ACTIVE);
            System.out.println(p.toString());
            listOfPatients.add(p);
            count++;
            // break;
        }

        services.saveAll(listOfPatients);
        logger.info("Count of loadBulk : {} " + listOfPatients.size());
    }
}

package com.nj.websystem.controller.utils;

import com.nj.websystem.enums.Status;
import com.nj.websystem.model.MedicalTest;
import com.nj.websystem.model.Patient;
import com.nj.websystem.model.PatientMedicalTest;
import com.nj.websystem.service.MedicalTestService;
import com.nj.websystem.service.PatientMedicalTestService;
import com.nj.websystem.service.PatientService;
import com.nj.websystem.util.CSVUtils;
import com.nj.websystem.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class PatientResultsLoader {

    static Logger logger = LoggerFactory.getLogger(PatientResultsLoader.class);

    @Autowired
    private PatientMedicalTestService myservices;

    private static PatientMedicalTest logError(List<String> l, PatientMedicalTest p, String error) {
        StringBuilder _sb = new StringBuilder(l.get(2));
        _sb.append("/n/r");
        //_sb.append("Error in DOB Years :"+ l.get(7) +"/" + l.get(8) +"/" + l.get(9) );
        _sb.append(error);
        p.setRemarks(_sb.toString());
        return p;
    }

    public void executeLoader(PatientMedicalTestService services, PatientService servicesp, MedicalTestService medicalTestService) {

        myservices = services;

        List<List> lineList = null;
        String csvFile = "/Users/sirimewanranathunga/Desktop/projects/PatientData/juwan-2019/cvs/PatientResults.csv";
        try {
            lineList = CSVUtils.LoadFile(csvFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        run(lineList, servicesp, medicalTestService);
    }

    private void run(List<List> lineList, PatientService servicesp, MedicalTestService medicalTestService){
        List<PatientMedicalTest> listOfPatients = new ArrayList<>();
        PatientMedicalTest p;
        int count = 0;
        for (List<String> l : lineList) {
            if (l.get(0).equals("ID")) {
                continue;
            }
            int j = 0;
            for (String s : l) {
                System.out.print("[" + j + "]=" + s + ", ");
                j++;
            }
            p = new PatientMedicalTest();


            try {
                String data = l.get(1);
                if (StringUtility.get(data)) {
                    p.setResults(data);
                } else {
                    logError(l, p, "Error in Remarks :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in Remarks :" + e.getMessage());
            }
            Patient patient = null;
            try {
                String data = l.get(2);
                if (StringUtility.get(data)) {
                    p.setBillingNumber(data);
                    List<Patient> lst = servicesp.findAllByBillingNumber(data);
                    if(lst.size() > 0){
                        patient = lst.get(0);
                    }
                } else {
                    logError(l, p, "Error in DateCreated :" + data);
                }

                p = updateFromPatient(p, patient);


            } catch (Exception e) {
                logError(l, p, "Error in DateCreated :" + e.getMessage());
            }

/*            try {
                String data = l.get(3);
                if (StringUtility.get(data)) {
                    p.setName(data);
                    List<MedicalTest> lst = medicalTestService.findAllByName(data);
                    if(lst.size() > 0){
                        p.setTestNumber(lst.get(0).getTestNumber());
                    }

                } else {
                    logError(l, p, "Error in PatientName :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in PatientName :" + e.getMessage());
            }*/

            try {
                String data = l.get(4);
                if (StringUtility.get(data)) {
                    StringTokenizer _st =new StringTokenizer(data,"/");
                    String date = _st.nextToken();
                    String month = _st.nextToken();
                    String year = _st.nextToken();
                    String appDate = year+ "-" + month + "-" + date ;
                    SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
                    p.setDateCreated(formatter.parse(appDate));
                } else {
                    logError(l, p, "Error in DateCreated :" + data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logError(l, p, "Error in DateCreated :" + e.getMessage());
            }

            try {
                String data = l.get(5);
                if (StringUtility.get(data)) {
                    p.setUnits(data);
                } else {
                    logError(l, p, "Error in DateCreated :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in DateCreated :" + e.getMessage());
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

        myservices.saveAll(listOfPatients);
        logger.info("Count of loadBulk : {} " + listOfPatients.size());
    }

    private PatientMedicalTest updateFromPatient(PatientMedicalTest p, Patient patient) {
        if(patient  != null){
            p.setPatientId(patient.getPatientId());
            Properties prop = new Properties();
            try {
                StringReader reader = new StringReader(patient.getOldValues());
                prop.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // {R21=FALSE, R20=FALSE, R23=FALSE, R22=FALSE, R25=FALSE, R24=FALSE, R27=FALSE, R26=FALSE, R29=FALSE, R28=FALSE, R0=, R1=1/1/19, R2=19H00025, R3=W.B.N.S.WIJEKON, R4=, R5=?, R6=, R7=34, R8=, R9=, R30=FALSE, R10=Female, R32=0, R31=, R12=Hospital, R34=FALSE, R11=, R33=000000000V, R14="TSH , FT4 , R36=, R13=T.H.P, R35=, R16=07-Jan-19, R15="Dr.D.Nanayakkara, MBBS, M Phil. (U.K)PhD,FANMB, R18=TRUE, R17=TRUE, R19=FALSE}
            p.setSeenBy(prop.getProperty("R15"));
            p.setName(prop.getProperty("R14"));







        }
        return p;
    }


}

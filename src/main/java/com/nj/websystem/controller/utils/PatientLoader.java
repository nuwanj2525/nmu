package com.nj.websystem.controller.utils;

import com.nj.websystem.enums.Gender;
import com.nj.websystem.enums.Status;
import com.nj.websystem.model.Patient;
import com.nj.websystem.service.PatientService;
import com.nj.websystem.util.CSVUtils;
import com.nj.websystem.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class PatientLoader {

    static Logger logger = LoggerFactory.getLogger(PatientLoader.class);

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

    public void executeLoader(PatientService services) throws Exception {

        if(services == null){
            throw new Exception("Null PatientService ");
        }

        List<List> lineList = null;
        String csvFile = "/Users/sirimewanranathunga/Desktop/projects/PatientData/juwan-2019/cvs/PatientTable1.csv";
        try {
            lineList = CSVUtils.LoadFile(csvFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Patient> listOfPatients = new ArrayList<>();
        Patient p;
        int count = 0;
        for (List<String> l : lineList) {

            if (l.get(0).equals("Scan Registration No")) {
                continue;
            }
            int j = 0;
            Map patientMap =new HashMap();
            for (String s : l) {
                String key = "R"+j;
                patientMap.put(String.valueOf(key).trim(),  String.valueOf(s).trim());
                j++;
            }
            p = new Patient();
            System.out.print(patientMap);
            p.setOldValues(patientMap.toString());
            String nextPatientId = StringUtility.getCustDateByPatten(String.format("%05d", (count + 1)));
            Date d = new Date();
            d.setYear(2019);
            p.setPatientId(StringUtility.getCustDateByPatten(StringUtility.YY, d) + nextPatientId);

            try {
                String data = l.get(2);
                if (StringUtility.get(data)) {
                    p.setBillingNumber(data);
                } else {
                    logError(l, p, "Error in Remarks :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in Remarks :" + e.getMessage());
            }

            try {
                String data = l.get(1);
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
                String data = l.get(3);
                if (StringUtility.get(data)) {
                    p.setPatientName(data);
                } else {
                    logError(l, p, "Error in PatientName :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in PatientName :" + e.getMessage());
            }


            try {
                String gender = l.get(10);
                if (StringUtility.get(gender)) {
                    if (gender.toUpperCase().equals("FEMALE")) {
                        p.setGender(Gender.FEMALE);
                    } else {
                        p.setGender(Gender.MALE);
                    }
                } else {
                    logError(l, p, "Error in GENDER :" + gender);
                }
            } catch (Exception e) {
                logError(l, p, "Error in GENDER :" + e.getMessage());
            }

            try {
                String years = l.get(7);
                String months = l.get(8);
                String days = l.get(9);

                if (StringUtility.get(years)) {
                    Date dob = new Date();
                    int year = Integer.parseInt(l.get(7));
                    dob.setYear(dob.getYear() - year);
                    p.setDateOfBirth(dob);
                } else {
                    logError(l, p, "Error in DOB Years :" + years + "/" + months + "/" + days);
                }
            } catch (Exception e) {
                logError(l, p, "Error in GENDER :" + e.getMessage());
            }

            try {
                String data = l.get(33);
                if (StringUtility.get(data)) {
                    p.setNicNumber(data);
                } else {
                    logError(l, p, "Error in NicNumber :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in GENDER :" + e.getMessage());
            }

            try {
                String data = l.get(4);
                if (StringUtility.get(data)) {
                    p.setOther(data);
                } else {
                    logError(l, p, "Error in Other :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in Other :" + e.getMessage());
            }

            try {
                String data = l.get(6);
                if (StringUtility.get(data)) {
                    p.setTelNumber(data);
                } else {
                    logError(l, p, "Error in TelNumber :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in TelNumber :" + e.getMessage());
            }

            try {
                String data = l.get(5);
                if (StringUtility.get(data)) {
                    p.setPatientAddress(data);
                } else {
                    logError(l, p, "Error in PatientAddress :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in PatientAddress :" + e.getMessage());
            }

            try {
                String data = l.get(11);
                if (StringUtility.get(data)) {
                    p.setBht(data);
                } else {
                    logError(l, p, "Error in Bht :" + data);
                }
            } catch (Exception e) {
                logError(l, p, "Error in Bht :" + e.getMessage());
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

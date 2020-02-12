package com.nj.websystem.controller.utils;


import com.nj.websystem.service.MedicalTestService;
import com.nj.websystem.service.PatientMedicalTestService;
import com.nj.websystem.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/loader")
public class LoaderController {

    static Logger logger = LoggerFactory.getLogger(LoaderController.class);

    @Autowired
    private PatientService services;

    @Autowired
    private MedicalTestService medicalTestService;

    @Autowired
    private PatientMedicalTestService patientMedicalTestService;

    @RequestMapping(value = "/loadPatient", method = RequestMethod.GET, headers = "Accept=application/json")
    public void loadPatient() throws Exception {
        PatientLoader loader = new PatientLoader();
        loader.executeLoader(services);
    }

    @RequestMapping(value = "/testLoader", method = RequestMethod.GET, headers = "Accept=application/json")
    public void testLoader() throws Exception {
        TestLoader loader = new TestLoader();
        loader.executeLoader(medicalTestService);
    }

    @RequestMapping(value = "/resultsLoader", method = RequestMethod.GET, headers = "Accept=application/json")
    public void resultsLoader() throws Exception {
        PatientResultsLoader loader = new PatientResultsLoader();
        loader.executeLoader(patientMedicalTestService, services , medicalTestService);
    }

    @RequestMapping(value = "/scanLoader", method = RequestMethod.GET, headers = "Accept=application/json")
    public void scanLoader() throws Exception {
        //loader = new PatientLoader();
        //loader.executeLoader();
    }
}

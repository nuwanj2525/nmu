package com.nj.websystem.controller;

import com.nj.websystem.enums.Status;
import com.nj.websystem.model.MedicalReference;
import com.nj.websystem.model.MedicalTest;
import com.nj.websystem.rest.HttpResponse;
import com.nj.websystem.service.MedicalReferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/reference")
public class ReferenceController {

    static Logger logger = LoggerFactory.getLogger(MedicalTestController.class);

    @Autowired
    private MedicalReferenceService services;

    @RequestMapping(value = "/getRefList", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse getRefList(@RequestParam(value = "testId", required = false) String testId) {
        HttpResponse res = new HttpResponse();
        //Pageable paging = PageRequest.of(1, 10, Sort.by("id"));
        MedicalTest mt = new MedicalTest(Long.parseLong(testId));
        List<MedicalReference> list = services.findAllByMedicalTestAndStatus(mt, Status.ACTIVE);
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

    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public void saveList(@RequestBody List<MedicalReference> refList) {
        if (refList.size() > 0) {
            MedicalReference reference = refList.get(0);
            MedicalTest mt = new MedicalTest(reference.getMedicalTest().getId());
            refList.forEach(t -> {
                t.setMedicalTest(mt);
                t.setDateCreated(new Date());
                t.setStatus(Status.ACTIVE);
            });
            services.saveAll(refList);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public void delete(@RequestParam(value = "id", required = false) Long id) {
        services.deleteById(id);
    }
}

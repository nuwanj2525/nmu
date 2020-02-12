package com.nj.websystem.controller;

import com.nj.websystem.model.UserScreen;
import com.nj.websystem.rest.HttpResponse;
import com.nj.websystem.service.UserScreenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/userscreen")
public class UserScreenController {

    static Logger logger = LoggerFactory.getLogger(UserScreenController.class);

    @Autowired
    private UserScreenService services;

    @RequestMapping(value = "/getList", method = RequestMethod.GET, headers = "Accept=application/json")
    public List getList() {
        List list = services.findAll();
        logger.info("Count of Screen Id : {} " + list.size());
        return list;
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse getById(@RequestParam(value = "id", required = false) String id) {
        logger.info("Request Screen Id Id : {} " + id);
        HttpResponse res = new HttpResponse();
        UserScreen screen = services.getOne(Long.parseLong(id));
        if (screen != null) {
            res.setResponse(screen);
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Invalid Screen Id !");
        }
        return res;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public UserScreen save(@RequestBody UserScreen obj) {
        logger.info("Save / Screen Id : {} " + obj.getId());
        HttpResponse res = new HttpResponse();
        UserScreen result = services.save(obj);
        if (result == null) {
            res.setResponse(result);
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Invalid Screen Id !");
        }
        return services.save(obj);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public HttpResponse delete(@RequestParam(value = "id", required = false) Long id) {
        logger.info("Delete Screen Id Name : {} " + id);
        HttpResponse response = new HttpResponse();
        UserScreen item = services.getOne(id);
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
    public List<UserScreen> bulkInsert(@RequestBody List<UserScreen> items) {
        logger.info("Screen Id count : {} " + items.size());
        return services.saveAll(items);
    }

}

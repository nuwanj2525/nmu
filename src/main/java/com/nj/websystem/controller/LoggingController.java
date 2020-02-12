package com.nj.websystem.controller;

import com.nj.websystem.model.UserAdmin;
import com.nj.websystem.rest.AuthRequest;
import com.nj.websystem.rest.HttpResponse;
import com.nj.websystem.service.UserAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class LoggingController {

    static Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @Autowired
    private UserAdminService services;

    @RequestMapping(value = "/getList", method = RequestMethod.GET, headers = "Accept=application/json")
    public List getList() {
        List list = services.findAll(Sort.by(Sort.Direction.ASC, "id"));
        logger.info("Count of UserAdmin : {} " + list.size());
        return list;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpResponse authenticate(@RequestBody AuthRequest request) {
        logger.info("authenticate Name : {} " + request.getUsername());
        List<UserAdmin> userAdminList = services.findByUserId(request.getUsername());
        HttpResponse res;
        if (userAdminList.size() > 0) {
            UserAdmin userAdmin = userAdminList.get(0);
            if (userAdmin.getPassWord().trim().equals(request.getPassword().trim())) {
                res = new HttpResponse();
                res.setResponse(userAdmin);
                res.setSuccess(true);
                res.setRecCount(1);
                return res;
            } else {
                res = new HttpResponse();
                res.setResponse(null);
                res.setSuccess(false);
                res.setRecCount(0);
                res.setException("Password incorrect!");
                return res;
            }
        } else {
            res = new HttpResponse();
            res.setResponse(null);
            res.setSuccess(false);
            res.setRecCount(1);
            res.setException("User not found in the system!");
            return res;
        }

    }


    @RequestMapping(value = "/getById", method = RequestMethod.GET, headers = "Accept=application/json")
    public HttpResponse getById(@RequestParam(value = "id", required = false) String id) {
        logger.info("Request UserAdmin Id : {} " + id);
        HttpResponse res = new HttpResponse();
        List<UserAdmin> userList = services.findByUserId(id);
        if (userList != null && userList.size() > 0) {
            res.setResponse(userList.get(0));
            res.setSuccess(true);
            res.setRecCount(1);
        } else {
            res.setSuccess(false);
            res.setException("Invalid User !");
        }
        return res;
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public HttpResponse findByUserId(@PathVariable(value = "username", required = true) String username) {
        logger.info("Request UserAdmin username : {} " + username);
        HttpResponse res = new HttpResponse();
        List result = services.findByUserId(username);
        if (result.size() > 0) {
            res.setResponse(result);
            res.setSuccess(true);
            res.setRecCount(result.size());
        } else {
            res.setSuccess(false);
            res.setException("Invalid User !");
        }

        return res;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public UserAdmin save(@RequestBody UserAdmin obj) {
        logger.info("UserAdmin Name : {} " + obj.getUserId());
        return services.save(obj);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, headers = "Accept=application/json")
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
    }

}

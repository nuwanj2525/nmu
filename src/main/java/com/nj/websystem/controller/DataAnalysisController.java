package com.nj.websystem.controller;

import com.nj.websystem.enums.Gender;
import com.nj.websystem.util.JDBCUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/analysis")
public class DataAnalysisController {

    static Logger logger = LoggerFactory.getLogger(MedicalTestController.class);

    @Autowired
    JDBCUtility jdbcUtility;

    @RequestMapping(value = "/genderStatics", method = RequestMethod.GET, headers = "Accept=application/json")
    public List genderStatics() {

        StringBuilder _sb=new StringBuilder("select  p.gender,count(p.gender) as recCount ");
        _sb.append("from tbl_patient_medical_test t, tbl_patient p ");
        _sb.append("where t.patient_id = p.patient_id ");
        _sb.append("group by p.gender");
        SqlRowSet rowSet = jdbcUtility.run(_sb.toString());
        List list = new ArrayList();
        int rowCount = 0;
        List resLst = null;
        while (rowSet.next()) {
            int gender = rowSet.getInt("gender");
            int recCount = rowSet.getInt("recCount");
            if(Gender.FEMALE.ordinal() == gender){
                resLst = new ArrayList();
                resLst.add(Gender.FEMALE.name());
                resLst.add(recCount);
                list.add(resLst);
            }else{
                resLst = new ArrayList();
                resLst.add(Gender.MALE.name());
                resLst.add(recCount);
                list.add(resLst);
            }
        }
        return list;
    }

    @RequestMapping(value = "/genderStaticsByTest", method = RequestMethod.GET, headers = "Accept=application/json")
    public List genderStaticsByTest(@RequestParam(value = "testid", required = false) String testid) {
        StringBuilder _sb=new StringBuilder("select  p.gender,count(p.gender) as recCount ");
        _sb.append(" from tbl_patient_medical_test t, tbl_patient p ");
        _sb.append(" where t.patient_id = p.patient_id ");
        _sb.append(" and t.test_number = '" + testid + "'");
        _sb.append(" group by p.gender");
        SqlRowSet rowSet = jdbcUtility.run(_sb.toString());
        List list = new ArrayList();
        int rowCount = 0;
        List resLst = null;
        while (rowSet.next()) {
            int gender = rowSet.getInt("gender");
            int recCount = rowSet.getInt("recCount");
            if(Gender.FEMALE.ordinal() == gender){
                resLst = new ArrayList();
                resLst.add(Gender.FEMALE.name());
                resLst.add(recCount);
                list.add(resLst);
            }else{
                resLst = new ArrayList();
                resLst.add(Gender.MALE.name());
                resLst.add(recCount);
                list.add(resLst);
            }
        }
        return list;
    }

    @RequestMapping(value = "/genderStaticsByPatient", method = RequestMethod.GET, headers = "Accept=application/json")
    public List genderStaticsByPatient(@RequestParam(value = "testid", required = false) long testid) {

        StringBuilder _sb=new StringBuilder("select  p.gender,count(p.gender) as recCount ");
        _sb.append("from tbl_patient p ");
        _sb.append("group by p.gender");

        SqlRowSet rowSet = jdbcUtility.run(_sb.toString());
        List list = new ArrayList();
        int rowCount = 0;
        List resLst = null;
        while (rowSet.next()) {
            int gender = rowSet.getInt("gender");
            int recCount = rowSet.getInt("recCount");
            if(Gender.FEMALE.ordinal() == gender){
                resLst = new ArrayList();
                resLst.add(Gender.FEMALE.name());
                resLst.add(recCount);
                list.add(resLst);
            }else{
                resLst = new ArrayList();
                resLst.add(Gender.MALE.name());
                resLst.add(recCount);
                list.add(resLst);
            }
        }


        return list;
    }

}

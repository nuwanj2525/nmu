package com.nj.websystem.util;

import com.nj.websystem.rest.QueryResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JDBCUtility {

    //@Value("spring.datasource.driver-class-name")
    private String driverClassName = "org.postgresql.Driver";

    //@Value("spring.datasource.url")
    private String dbUrl= "jdbc:postgresql://localhost:5432/njsysdb";

    //@Value("spring.datasource.url")
    private String dbUsername= "postgres";

    //@Value("spring.datasource.url")
    private String dbPassword= "123456";

    private static DataSource dataSource;
    // @Autowired
    private JdbcTemplate jdbcTemplate;

    public SqlRowSet run(String sqlStr) {
        List<Map<String, Object>> resultLst = null;
        jdbcTemplate = new JdbcTemplate(dataSource());
        return jdbcTemplate.queryForRowSet(sqlStr);
    }

    public void runQuary(String sqlStr) {
        List<Map<String, Object>> resultLst = null;
        resultLst = jdbcTemplate.queryForList(sqlStr);
        if (!commonUtility.isObjEmpty(resultLst)) {
            if (!resultLst.isEmpty()) {
                List<String> headreNameList = new ArrayList<>();
                for (Map.Entry e : resultLst.get(0).entrySet()) {
                    headreNameList.add(e.getKey().toString());
                }

                List<QueryResponseData> dataList = new ArrayList<>();
                resultLst.stream().forEach(s -> {
                    QueryResponseData resultNode = new QueryResponseData();
                    for (Map.Entry entry : s.entrySet()) {
                        resultNode.getData().add(entry.getValue() == null ? StringUtils.EMPTY : entry.getValue().toString());
                    }
                    dataList.add(resultNode);
                });

            } else {
                //results not found
            }
        }

    }

    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }
}

package com.nj.websystem.model;

import javax.persistence.*;

@Entity
@Table(name = "TBL_SYSTEM_SCREEN")
public class SystemScreen {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "TBL_SYSTEM_SCREEN_SEQ", sequenceName = "TBL_SYSTEM_SCREEN_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_SYSTEM_SCREEN_SEQ")
    private Long id;
    private String screenName;
    private String urlString;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }
}

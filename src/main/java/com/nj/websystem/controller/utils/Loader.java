package com.nj.websystem.controller.utils;

import org.hibernate.boot.jaxb.hbm.spi.JaxbHbmLoaderType;
import org.springframework.stereotype.Component;

@Component
public interface Loader {
    public void executeLoader() throws Exception;
}

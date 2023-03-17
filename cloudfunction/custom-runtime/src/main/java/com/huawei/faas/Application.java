package com.huawei.faas;

import com.huawei.faas.utils.ClasspathLoader;
import com.huawei.faas.utils.MyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOG.info("start spring server now!");
        try {
            ClasspathLoader.loadClassPath();
        } catch (MyException e) {
            LOG.error("failed to load layer " + e.getMessage());
        }
        SpringApplication.run(Application.class, args);

    }

}
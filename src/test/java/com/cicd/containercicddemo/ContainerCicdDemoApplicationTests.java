package com.cicd.containercicddemo;

import com.cicd.containercicddemo.libs.Sha;
import com.cicd.containercicddemo.libs.ShaImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

@SpringBootTest
class ContainerCicdDemoApplicationTests {
    @Autowired
    private ApplicationContext ac;

    @Test
    void contextLoads() {
        Sha sha = ac.getBean(Sha.class);
        Assert.isTrue(sha instanceof ShaImpl, "Sha should be an instance of the ShaImpl in the spring context");
    }

}

package com.cicd.containercicddemo.controllers;

import com.cicd.containercicddemo.libs.Sha;
import com.cicd.containercicddemo.models.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin
@RequestMapping("/version")
public class VersionController {
    @Autowired
    private Sha sha;

    @Autowired
    private BuildProperties buildProp;

    @Value("${build.description}")
    private String description;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Version getVersion() throws NoSuchAlgorithmException {
        String buildSha = sha.getShaString(buildProp.getTime().toString());
        return new Version(buildProp.getVersion(), buildSha, description);
    }
}

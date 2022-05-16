package com.cicd.containercicddemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;

@Getter
@Setter
@NoArgsConstructor
public class Version {
    private String version;
    @JsonProperty("build_sha")
    private String buildSha;
    private String description;

    public Version(String version, String buildSha, String description) {
        this.version = version;
        this.buildSha = buildSha;
        this.description = description;
    }
}

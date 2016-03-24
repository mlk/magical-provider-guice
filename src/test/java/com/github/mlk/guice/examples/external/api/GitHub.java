package com.github.mlk.guice.examples.external.api;

import com.github.mlk.guice.examples.external.dto.Contributor;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface GitHub {
    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);
}

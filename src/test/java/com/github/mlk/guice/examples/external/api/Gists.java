package com.github.mlk.guice.examples.external.api;

import com.github.mlk.guice.examples.external.dto.Gist;
import feign.RequestLine;

import java.util.List;

public interface Gists {
    @RequestLine("GET /gists/public")
    List<Gist> getPublicGists();
}

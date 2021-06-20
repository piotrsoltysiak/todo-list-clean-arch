package com.piotrsoltysiak.todoclean.adapters.inbound.rest;

import java.net.URI;

import org.springframework.web.util.UriComponentsBuilder;

class UriResolver {
    private String path;

    private UriResolver(String path) {
        this.path = path;
    }

    public static UriResolver operateOnPath(String path) {
        return new UriResolver(path);
    }

    public URI andResolveWithIds(String... uriVariableValues) {
        return UriComponentsBuilder.fromPath(path)
                .buildAndExpand((Object[]) uriVariableValues)
                .toUri();
    }

}


package com.todoclean.adapters.inbound.rest;

import java.net.URI;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class UriResolver {
    private final String path;

    static UriResolver operateOnPath(String path) {
        return new UriResolver(path);
    }

    URI andResolveWithIds(String... uriVariableValues) {
        return UriComponentsBuilder.fromPath(path)
                .buildAndExpand((Object[]) uriVariableValues)
                .toUri();
    }

}


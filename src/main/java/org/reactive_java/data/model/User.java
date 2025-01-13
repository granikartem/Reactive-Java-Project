package org.reactive_java.data.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@RequiredArgsConstructor
@JsonSerialize
public class User {

    private final String login;

    private final Set<Group> groups;

    @Override
    public String toString() {
        Set<String> groupNames = groups.stream().map(Group::getName).collect(Collectors.toSet());
        return "User [login=" + login + ", groups=" + groupNames + "]";
    }
}

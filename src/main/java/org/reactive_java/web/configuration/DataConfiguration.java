package org.reactive_java.web.configuration;

import org.reactive_java.data.generator.UserGenerator;
import org.reactive_java.data.model.Group;
import org.reactive_java.data.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

import static org.reactive_java.data.util.Constants.GROUP_NAMES;
import static org.reactive_java.data.util.Constants.USER_AMOUNT;

@Configuration
public class DataConfiguration {
    @Bean
    public Map<String, Group> groupMap() {
        Map<String, Group> groups = new HashMap<>();
        for (String groupName: GROUP_NAMES) {
            groups.put(groupName, new Group(groupName, new HashSet<>()));
        }

        System.out.println("groups init");
        return groups;
    }

    @Bean
    public List<User> userList(Map<String, Group> groupMap) {
        System.out.println("users init");
        return UserGenerator.generateUsers(USER_AMOUNT, groupMap.values().stream().toList());
    }
}

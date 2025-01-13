package org.reactive_java.web.configuration;

import org.reactive_java.data.generator.UserGenerator;
import org.reactive_java.data.model.Group;
import org.reactive_java.data.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.reactive_java.data.util.Constants.GROUP_NAMES;
import static org.reactive_java.data.util.Constants.USER_AMOUNT;

@Configuration
public class DataConfiguration {
    @Bean
    public List<Group> groupList() {
        List<Group> groups = new ArrayList<>();
        for (String groupName: GROUP_NAMES) {
            groups.add(new Group(groupName, new HashSet<>()));
        }

        System.out.println("groups init");
        return groups;
    }

    @Bean
    public List<User> userList(List<Group> groupList) {
        System.out.println("users init");
        return UserGenerator.generateUsers(USER_AMOUNT, groupList);
    }
}

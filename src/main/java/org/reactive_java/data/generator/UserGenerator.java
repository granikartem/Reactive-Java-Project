package org.reactive_java.data.generator;

import org.reactive_java.data.model.Group;
import org.reactive_java.data.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.reactive_java.data.util.Constants.*;
import static org.reactive_java.data.util.Utils.pickRandomGroups;

public class UserGenerator {
    public static List<User> generateUsers(int userAmount, List<Group> groups) {
        List<User> users = new ArrayList<>(userAmount);

        for (int i = 0; i < USER_AMOUNT; i++) {
            var pickedGroups = pickRandomGroups(groups);
            User user = new User(
                    generateUserName(),
                    pickedGroups.stream()
                            .map(Group::getName)
                            .collect(Collectors.toSet())
            );
            users.add(user);
            for (Group group : pickedGroups) {
                group.getMembers().add(user);
            }
        }

        return users;
    }

    private static String generateUserName() {
        return USER_NAMES.get(ThreadLocalRandom.current().nextInt(USER_NAMES.size())) +
                "_" +
                USER_SURNAMES.get(ThreadLocalRandom.current().nextInt(USER_SURNAMES.size()));
    }
}

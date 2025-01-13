package org.reactive_java.data.generator;

import org.reactive_java.data.model.Group;
import org.reactive_java.data.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.reactive_java.data.util.Constants.*;

public class UserGenerator {
    public static List<User> generateUsers(int userAmount, List<Group> groups) {
        List<User> users = new ArrayList<>(userAmount);

        for (int i = 0; i < USER_AMOUNT; i++) {
            var pickedGroups = pickRandomGroups(groups);
            User user = new User(generateUserName(), pickedGroups);
            users.add(user);
            for (Group group : pickedGroups) {
                group.getMembers().add(user);
            }
        }

        return users;
    }

    private static String generateUserName() {
        return USER_NAMES.get(ThreadLocalRandom.current().nextInt(USER_NAMES.size())) +
                " " +
                USER_SURNAMES.get(ThreadLocalRandom.current().nextInt(USER_SURNAMES.size()));
    }

    private static Set<Group> pickRandomGroups(List<Group> groups) {
        int groupAmount = ThreadLocalRandom.current().nextInt(MAX_GROUPS_PER_USER);
        Set<Group> userGroups = new HashSet<>(groupAmount);
        for (int i = 0; i < groupAmount; i++) {
            userGroups.add(groups.get(ThreadLocalRandom.current().nextInt(groups.size())));
        }
        return userGroups;
    }

    public static User generateUserFromUserName(String userName) {
        var pickedGroups = pickRandomGroups(groups);
        User user = new User(userName, pickedGroups);
    }
}

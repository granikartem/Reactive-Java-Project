package org.reactive_java.data.util;

import org.reactive_java.data.model.Group;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.reactive_java.data.util.Constants.MAX_GROUPS_PER_USER;

public class Utils {
    public static Set<Group> pickRandomGroups(List<Group> groups) {
        int groupAmount = ThreadLocalRandom.current().nextInt(MAX_GROUPS_PER_USER);
        Set<Group> userGroups = new HashSet<>(groupAmount);
        for (int i = 0; i < groupAmount; i++) {
            userGroups.add(groups.get(ThreadLocalRandom.current().nextInt(groups.size())));
        }
        return userGroups;
    }
}

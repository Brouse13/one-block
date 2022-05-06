package es.noobcraft.oneblock.api.permission;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.permission.Group;

import java.util.Set;

public class OneBlockGroups {
    /**
     * Group of all the roles that can access to admin features
     */
    public static final Set<Group> ADMIN = Sets.newHashSet(Group.CO_CREATOR, Group.CREATOR, Group.DEVELOPER, Group.ADMINISTRATOR);

    /**
     * Group of all the roles that can access to staff features
     */
    public static final Set<Group> STAFF = Sets.newHashSet(Group.CO_CREATOR, Group.CREATOR, Group.DEVELOPER, Group.ADMINISTRATOR,
            Group.MODERATOR, Group.HELPER);
}

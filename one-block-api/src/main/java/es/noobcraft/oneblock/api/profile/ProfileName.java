package es.noobcraft.oneblock.api.profile;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public enum ProfileName {
    CREEPER,
    ZOMBIE,
    PURPLE,
    GREEN,
    ANOTHER_NAME,
    MY_PROFILE;

    public static ProfileName randomName(Set<OneBlockProfile> profiles) {
        while (true) {
            int randValue = new Random().nextInt(ProfileName.values().length);
            if (profiles.isEmpty()) return ProfileName.values()[randValue];

            Set<String> names = profiles.stream().map(OneBlockProfile::getProfileName).collect(Collectors.toSet());
            if (!names.contains(ProfileName.values()[randValue]))
                return ProfileName.values()[randValue];
        }
    }
}

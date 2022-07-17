package es.noobcraft.oneblock.profile;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileCache;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MapProfileCache implements ProfileCache {
    private final Cache<OneBlockProfile, Object> profiles = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build();

    @Override
    public Set<OneBlockProfile> getProfiles() {
        return ImmutableSet.copyOf(profiles.asMap().keySet());
    }

    @Override
    public boolean addProfile(OneBlockProfile profile) {
        if (!profiles.asMap().containsValue(profile)) {
            profiles.put(profile, new Object());//2 minutes
            return true;
        }
        return false;
    }

    @Override
    public boolean removeProfile(OneBlockProfile profile) {
        if (!profiles.asMap().containsValue(profile)) {
            profiles.asMap().remove(profile);
            return true;
        }
        return false;
    }
}

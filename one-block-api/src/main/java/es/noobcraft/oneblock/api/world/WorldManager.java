package es.noobcraft.oneblock.api.world;

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;

import java.io.IOException;

public interface WorldManager {

    boolean createWorld(OneBlockPlayer player, String name) throws IOException, WorldAlreadyExistsException;

    boolean loadWorld(String name, boolean readOnly) throws CorruptedWorldException, UnknownWorldException;

    boolean unloadWorld(String name);
}

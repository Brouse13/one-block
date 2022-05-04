package es.noobcraft.oneblock.server;

import es.noobcraft.core.api.redis.RedisSubscriber;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import lombok.NonNull;

public class RedisServerSubscriber implements RedisSubscriber {
    @Override
    public void onMessage(@NonNull String message) {
        String[] args = message.split("/");

        /*
        1 -> Register world (Server, World)
        2 -> Unregister world (Server, World)
         */
        try {
            switch (Integer.parseInt(args[0])) {
                case 1:
                    OneBlockAPI.getServerCache().addWorld(args[1], args[2]);
                    break;
                case 2:
                    OneBlockAPI.getServerCache().removeWorld(args[1], args[2]);
                    break;
                default:
                    Logger.log(LoggerType.ERROR, "Unable to parse message: "+ message);
                    break;
            }
        }catch (ArrayIndexOutOfBoundsException exception) {
            Logger.log(LoggerType.ERROR, "Args length couldn't be parsed: "+ message);
        }catch (NumberFormatException exception) {
            Logger.log(LoggerType.ERROR, "Couldn't not parse number: "+ args[0]);
        }
    }
}

package dk.sdu.mmmi.cbse.wrap;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import javafx.scene.input.KeyCode;

/**
 *
 * @author jcs
 */
public class WrapControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(EntityType.PLAYER, EntityType.ENEMY, EntityType.ASTEROIDS, EntityType.BULLET)) {
            if (entity.getX() < 0) {
                if(entity.getWrap())
                    entity.setX(gameData.getDisplayWidth());
                else
                    world.removeEntity(entity);
            }

            if (entity.getX() > gameData.getDisplayWidth()) {
                if(entity.getWrap())
                    entity.setX(0);
                else
                    world.removeEntity(entity);
            }

            if (entity.getY() < 0) {
                if(entity.getWrap())
                    entity.setY(gameData.getDisplayHeight());
                else
                    world.removeEntity(entity);
            }

            if (entity.getY() > gameData.getDisplayHeight()) {
                if(entity.getWrap())
                    entity.setY(0);
                else
                    world.removeEntity(entity);
            }
        }
    }
}

package dk.sdu.mmmi.cbse.hitprocessing;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import javafx.scene.input.KeyCode;

/**
 *
 * @author jcs
 */
public class HitProcessingSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        
        // TODO: Implement entity processor
        
            for(Entity entity: world.getEntities(EntityType.PLAYER, EntityType.ENEMY))
            {
                if(entity.getIsHit())
                {
                    entity.setLife(entity.getLife() - 20);
                    entity.setIsHit(false);
                }
                
                if(entity.getLife() <= 0)
                    world.removeEntity(entity);
            }
        }
}

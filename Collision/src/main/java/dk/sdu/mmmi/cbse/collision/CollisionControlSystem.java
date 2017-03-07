package dk.sdu.mmmi.cbse.collision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Area;
import javafx.scene.input.KeyCode;

/**
 *
 * @author jcs
 */
public class CollisionControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        // TODO: Implement entity processor
        for (Entity collider : world.getEntities(EntityType.BULLET))
            for (Entity entity : world.getEntities(EntityType.ASTEROIDS))
                if (Collide(collider, entity)) {
                    world.removeEntity(collider);
                    gameData.addEvent(new Event(EventType.ASTEROID_SPLIT, entity.getID()));
                }

        for (Entity entity : world.getEntities(EntityType.ENEMY, EntityType.PLAYER)) {
            for (Entity collider : world.getEntities(EntityType.BULLET))
                if (Collide(entity, collider))
                {
                    world.removeEntity(collider);
                    entity.setIsHit(true);
                }

            for (Entity collider : world.getEntities(EntityType.ASTEROIDS))
                if (Collide(entity, collider))
                    world.removeEntity(entity);
        }
    }

    private boolean Collide(Entity firstE, Entity otherE) {
        double scaling = 10;
        
        Polygon pFirst = new Polygon();
        Polygon pSecond = new Polygon();

        for (int i = 0; i < firstE.getShapeX().length; i++)
            pFirst.addPoint((int) (firstE.getShapeX()[i] * scaling), (int) (firstE.getShapeY()[i] * scaling));

        for (int i = 0; i < otherE.getShapeX().length; i++)
            pSecond.addPoint((int) (otherE.getShapeX()[i] * scaling), (int) (otherE.getShapeY()[i] * scaling));
        
        Area area = new Area((Shape)pFirst);
        area.intersect(new Area((Shape)pSecond));
    
        return !area.isEmpty();
    }
}

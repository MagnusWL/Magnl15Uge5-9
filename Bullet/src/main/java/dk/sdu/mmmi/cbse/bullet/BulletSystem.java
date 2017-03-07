package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.ArrayList;
import java.util.List;

public class BulletSystem implements IGamePluginService, IEntityProcessingService {

    private List<Entity> bullets = new ArrayList<Entity>();

    @Override
    public void process(GameData gameData, World world) {

        for (Event e : gameData.getEvents()) {
            if (e.getType() == EventType.ENEMY_SHOOT || e.getType() == EventType.PLAYER_SHOOT) {
                Entity entity = world.getEntity(e.getEntityID());
                if (entity != null) {
                    Entity bullet = createBullet(gameData, (float) (entity.getX() + Math.cos(entity.getRadians()) * 12), (float) (entity.getY() + Math.sin(entity.getRadians()) * 12), entity.getRadians());
                    world.addEntity(bullet);
                }
                
                gameData.removeEvent(e);
            }
        }

        // TODO: Implement entity processor
        for (Entity bullet : world.getEntities(EntityType.BULLET)) {

            bullet.setX(bullet.getX() + (float) Math.cos(bullet.getRadians()) * gameData.getDelta() * bullet.getSpeed());
            bullet.setY(bullet.getY() + (float) Math.sin(bullet.getRadians()) * gameData.getDelta() * bullet.getSpeed());

            bullet.setShapeX(new float[]{bullet.getX() + (float) Math.cos(bullet.getRadians() + Math.PI * 1.15) * 3,
                bullet.getX() + (float) Math.cos(bullet.getRadians() + Math.PI * 0.85) * 3,
                bullet.getX() + (float) Math.cos(bullet.getRadians()) * 3});

            bullet.setShapeY(new float[]{bullet.getY() + (float) Math.sin(bullet.getRadians() + Math.PI * 1.15) * 3,
                bullet.getY() + (float) Math.sin(bullet.getRadians() + Math.PI * 0.85) * 3,
                bullet.getY() + (float) Math.sin(bullet.getRadians()) * 3});
        }
    }

    public BulletSystem() {
    }

    @Override
    public void start(GameData gameData, World world) {
    }

    private Entity createBullet(GameData gameData, float x, float y, float radians) {
        Entity bullet = new Entity();
        bullet.setWrap(false);
        bullet.setType(BULLET);
        bullet.setPosition(x, y);
        bullet.setSpeed(350);
        bullet.setRadians(radians);
        bullets.add(bullet);
        return bullet;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity e : bullets) {
            world.removeEntity(e);
        }
    }

}

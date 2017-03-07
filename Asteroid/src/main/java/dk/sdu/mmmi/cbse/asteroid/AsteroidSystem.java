package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ASTEROIDS;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyCode;

/**
 *
 * @author jcs
 */
public class AsteroidSystem implements IEntityProcessingService, IGamePluginService {

    public AsteroidSystem() {
    }

    List<Entity> asteroids = new ArrayList<>();

    @Override
    public void process(GameData gameData, World world) {

        // TODO: Implement entity processor
        for (Entity asteroid : world.getEntities(EntityType.ASTEROIDS)) {
            asteroid.setSpeed(30);
            asteroid.setX((float) (asteroid.getX() + asteroid.getSpeed() * Math.cos(asteroid.getRadians()) * gameData.getDelta()));
            asteroid.setY((float) (asteroid.getY() + asteroid.getSpeed() * Math.sin(asteroid.getRadians()) * gameData.getDelta()));

            int corners = 8;
            float[] xvalues = new float[corners];
            float[] yvalues = new float[corners];

            for (int i = 0; i < corners; i++) {
                xvalues[i] = asteroid.getX() + (float) Math.cos(asteroid.getRadians() + Math.PI * (i / 3.0)) * asteroid.getRadius();
                yvalues[i] = asteroid.getY() + (float) Math.sin(asteroid.getRadians() + Math.PI * (i / 3.0)) * asteroid.getRadius();
            }

            asteroid.setShapeX(xvalues);
            asteroid.setShapeY(yvalues);
        }

        for (Event e : gameData.getEvents()) {
            if (e.getType() == EventType.ASTEROID_SPLIT) {
                Entity entity = world.getEntity(e.getEntityID());

                if (entity != null) {
                    if (entity.getRadius() > 6) {
                        Entity e1 = createAsteroidShip(gameData, entity.getX(), entity.getY(), entity.getRadius() * 0.618f);
                        Entity e2 = createAsteroidShip(gameData, entity.getX(), entity.getY(), entity.getRadius() * 0.618f);
                        world.addEntity(e1);
                        world.addEntity(e2);
                    }
                }

                world.removeEntity(entity);
                asteroids.remove(entity);
                gameData.removeEvent(e);
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {

        // Add entities to the world
        for (int i = 0; i < 10; i++) {
            Entity e = createAsteroidShip(gameData, (float) (Math.random() * gameData.getDisplayWidth()), (float) (Math.random() * gameData.getDisplayWidth()), 12f);
            world.addEntity(e);
        }
    }

    private Entity createAsteroidShip(GameData gameData, float x, float y, float radius) {

        Entity asteroid = new Entity();
        asteroid.setType(ASTEROIDS);

        asteroid.setPosition((float) (Math.random() * gameData.getDisplayWidth()), (float) (Math.random() * gameData.getDisplayHeight()));
        if (x != 0) {
            asteroid.setX(x);
            asteroid.setY(y);
        }

        asteroid.setSpeed(300);
        asteroid.setRadius(radius);
        asteroid.setRadians((float) (Math.random() * Math.PI * 2));
        asteroids.add(asteroid);
        return asteroid;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (int i = 0; i < asteroids.size(); i++) {
            world.removeEntity(asteroids.get(i));
        }
    }
}

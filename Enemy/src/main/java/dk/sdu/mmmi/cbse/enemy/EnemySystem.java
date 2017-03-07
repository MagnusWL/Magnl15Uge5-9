package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value={
        @ServiceProvider(service=IEntityProcessingService.class),
        @ServiceProvider(service=IGamePluginService.class)})

public class EnemySystem implements IEntityProcessingService, IGamePluginService {

    int cooldown = 200, maxCooldown = cooldown;
    private List<Entity> enemies = new ArrayList<>();
    
    @Override
    public void process(GameData gameData, World world) {            
            // TODO: Implement entity processor
            for (Entity enemy : world.getEntities(EntityType.ENEMY)) {

                cooldown -= 1;
                if (cooldown <= 0) {
                    gameData.addEvent(new Event(EventType.ENEMY_SHOOT, enemy.getID()));
                    cooldown = maxCooldown;
                }

                for (Entity player : world.getEntities(EntityType.PLAYER)) {

                    enemy.setRadians((float) Math.atan2(player.getY() - enemy.getY(), player.getX() - enemy.getX()));

                    if (Math.sqrt((enemy.getX() - player.getX()) * (enemy.getX() - player.getX()) + (enemy.getY() - player.getY()) * (enemy.getY() - player.getY())) > 100) {
                        if (enemy.getSpeed() < enemy.getMaxSpeed()) {
                            enemy.setSpeed((float) (enemy.getSpeed() + gameData.getDelta() * enemy.getAcceleration()));
                        }
                    } else if (enemy.getSpeed() > 0) {
                        enemy.setSpeed((float) (enemy.getSpeed() - gameData.getDelta() * enemy.getAcceleration()));
                    }

                    break;
                }

                enemy.setX(enemy.getX() + enemy.getSpeed() * (float) Math.cos(enemy.getRadians()) * gameData.getDelta());
                enemy.setY(enemy.getY() + enemy.getSpeed() * (float) Math.sin(enemy.getRadians()) * gameData.getDelta());

                enemy.setShapeX(new float[]{
                    enemy.getX() + (float) Math.cos(enemy.getRadians() + Math.PI * 0.85) * 12,
                    enemy.getX() + (float) Math.cos(enemy.getRadians() + Math.PI) * 8,
                    enemy.getX() + (float) Math.cos(enemy.getRadians() + Math.PI * 1.15) * 12,
                    enemy.getX() + (float) Math.cos(enemy.getRadians()) * 10});

                enemy.setShapeY(new float[]{
                    enemy.getY() + (float) Math.sin(enemy.getRadians() + Math.PI * 0.85) * 12,
                    enemy.getY() + (float) Math.sin(enemy.getRadians() + Math.PI) * 8,
                    enemy.getY() + (float) Math.sin(enemy.getRadians() + Math.PI * 1.15) * 12,
                    enemy.getY() + (float) Math.sin(enemy.getRadians()) * 10});

            }
        }
    

    

    public EnemySystem() {
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        Entity enemy = createPlayerShip(gameData);
        enemies.add(enemy);
        world.addEntity(enemy);
    }

    private Entity createPlayerShip(GameData gameData) {

        Entity enemyShip = new Entity();
        enemyShip.setType(ENEMY);

        enemyShip.setPosition((float) (Math.random() * gameData.getDisplayWidth()), (float) (Math.random() * gameData.getDisplayHeight()));

        enemyShip.setMaxSpeed(100);
        enemyShip.setAcceleration(75);
        enemyShip.setDeacceleration(75);
        enemyShip.setLife(100);

        enemyShip.setRadians(3.1415f / 2);
        enemyShip.setRotationSpeed(3);

        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity e : enemies) {
            world.removeEntity(e);
        }
    }
}

package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.bullet.BulletSystem;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
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
public class PlayerSystem implements IEntityProcessingService, IGamePluginService {

    int cooldown = 20, maxCooldown = cooldown;
    private Entity player;

    public PlayerSystem() {
    }

    @Override
    public void process(GameData gameData, World world) {

        // TODO: Implement entity processor
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            if (player.getSpeed() > 0) {
                player.setSpeed(player.getSpeed() - player.getDeacceleration() * gameData.getDelta());
            }

            if (gameData.getKeys().isDown(GameKeys.UP) && player.getSpeed() < player.getMaxSpeed()) {
                player.setSpeed((float) (player.getSpeed() + gameData.getDelta() * player.getAcceleration()));
            }

            if (gameData.getKeys().isDown(GameKeys.DOWN) && player.getSpeed() > 0) {
                player.setSpeed((float) (player.getSpeed() - gameData.getDelta() * player.getAcceleration()));
            }

            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.setRadians(player.getRadians() + player.getRotationSpeed() * gameData.getDelta());
            }

            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.setRadians(player.getRadians() - player.getRotationSpeed() * gameData.getDelta());
            }

            cooldown -= 1;
            if (gameData.getKeys().isDown(GameKeys.SPACE) && cooldown <= 0) {
                gameData.addEvent(new Event(EventType.PLAYER_SHOOT, player.getID()));
                cooldown = maxCooldown;
            }

            player.setX(player.getX() + player.getSpeed() * (float) Math.cos(player.getRadians()) * gameData.getDelta());
            player.setY(player.getY() + player.getSpeed() * (float) Math.sin(player.getRadians()) * gameData.getDelta());

            player.setShapeX(new float[]{
                player.getX() + (float) Math.cos(player.getRadians() + Math.PI * 0.85) * 12,
                player.getX() + (float) Math.cos(player.getRadians() + Math.PI) * 8,
                player.getX() + (float) Math.cos(player.getRadians() + Math.PI * 1.15) * 12,
                player.getX() + (float) Math.cos(player.getRadians()) * 10});

            player.setShapeY(new float[]{
                player.getY() + (float) Math.sin(player.getRadians() + Math.PI * 0.85) * 12,
                player.getY() + (float) Math.sin(player.getRadians() + Math.PI) * 8,
                player.getY() + (float) Math.sin(player.getRadians() + Math.PI * 1.15) * 12,
                player.getY() + (float) Math.sin(player.getRadians()) * 10});
        }
    }

    @Override
    public void start(GameData gameData, World world) {

        // Add entities to the world
        player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    private Entity createPlayerShip(GameData gameData) {

        Entity playerShip = new Entity();
        playerShip.setType(PLAYER);

        playerShip.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);

        playerShip.setMaxSpeed(250);
        playerShip.setAcceleration(400);
        playerShip.setDeacceleration(250);
        playerShip.setLife(100);

        playerShip.setRadians(3.1415f / 2);
        playerShip.setRotationSpeed(3);

        playerShip.setShapeX(new float[]{playerShip.getX(), playerShip.getX() + 10, playerShip.getX() + 10, playerShip.getX()});
        playerShip.setShapeY(new float[]{playerShip.getY() + 10, playerShip.getY() + 10, playerShip.getY(), playerShip.getY()});

        return playerShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(player);
    }
}

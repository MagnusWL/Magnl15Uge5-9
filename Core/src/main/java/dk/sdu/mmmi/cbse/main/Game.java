package dk.sdu.mmmi.cbse.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.managers.GameInputProcessor;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.util.SPILocator;
import java.util.ArrayList;
import java.util.List;

public class Game implements ApplicationListener {

    private static OrthographicCamera cam;
    private ShapeRenderer sr;

    private final GameData gameData = new GameData();
    private List<IEntityProcessingService> entityProcessors = new ArrayList<>();
    private World world = new World();
    private IGamePluginService playerPlugin, enemyPlugin, asteroidPlugin;

    @Override
    public void create() {

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(
                new GameInputProcessor(gameData)
        );

        for(IGamePluginService e: SPILocator.locateAll(IGamePluginService.class))
            e.start(gameData, world);        
    }

    @Override
    public void render() {

        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        update();

        draw();

        gameData.getKeys().update();
    }

    private void update() {
        for(IEntityProcessingService e: SPILocator.locateAll(IEntityProcessingService.class))
            e.process(gameData, world);
/*
        for (Event e : gameData.getEvents()) {
            if (e.getType() == EventType.ENEMY_SHOOT || e.getType() == EventType.PLAYER_SHOOT) {
                Entity entity = world.getEntity(e.getEntityID());
                if(entity != null)
                {
                    IGamePluginService b = new BulletEntityPlugin((float) (entity.getX() + Math.cos(entity.getRadians()) * 12), (float) (entity.getY() + Math.sin(entity.getRadians()) * 12), entity.getRadians());
                    b.start(gameData, world);
                }
            } else if (e.getType() == EventType.ASTEROID_SPLIT) {
                Entity entity = world.getEntity(e.getEntityID());

                if(entity != null)
                {
                    if (entity.getRadius() > 6) {
                        IGamePluginService a = new AsteroidEntityPlugin(entity.getRadius() * 0.618f, entity.getX(), entity.getY());
                        a.start(gameData, world);

                        IGamePluginService b = new AsteroidEntityPlugin(entity.getRadius() * 0.618f, entity.getX(), entity.getY());
                        b.start(gameData, world);
                    }
                }

                world.removeEntity(entity);

            }

            gameData.removeEvent(e);
        }*/
    }

    private void draw() {

        for (Entity entity : world.getEntities()) {
            float[] shapex = entity.getShapeX();
            float[] shapey = entity.getShapeY();
            if (shapex != null && shapey != null) {

                sr.setColor(1, 1, 1, 1);

                if (entity.isType(EntityType.ENEMY)) {
                    sr.setColor(1, 0, 0, 1);
                }

                if (entity.isType(EntityType.ASTEROIDS)) {
                    sr.setColor(0, 0, 1, 1);
                }

                sr.begin(ShapeRenderer.ShapeType.Line);
                

                for (int i = 0, j = shapex.length - 1;
                        i < shapex.length;
                        j = i++) {
                    sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
                }

                sr.end();
            }
        }

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}

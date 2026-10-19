package com.unicamp.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unicamp.model.entity.Player;
import com.unicamp.view.EntityRenderer;
import com.unicamp.model.entity.EntityManager;

public class PlayerRenderer implements EntityRenderer<Player> {

    private final Texture idleTexture;
    private final TextureRegion idleRegion;

    private final Animation<TextureRegion> walkingAnimation;
    private final Texture walkingSheet;

    private static final float RENDER_WIDTH = 128f; 
    private static final float RENDER_HEIGHT = 128f;

    private float stateTime;

    public PlayerRenderer() {
        this.idleTexture = new Texture("entities/player/Player_idle.png");
        this.idleRegion = new TextureRegion(idleTexture); 

        walkingSheet = new Texture(Gdx.files.internal("entities/player/Player_walking.png"));
        this.walkingAnimation = createAnimation(walkingSheet, 8, 1, 0.25f);
    }

    @Override
    public void render(SpriteBatch batch, Player entity) {
        float stateTime = EntityManager.getGlobalTime();
        
        TextureRegion regionToDraw;

        if (entity.getySpeed() != 0 || entity.getxSpeed() != 0) {
            regionToDraw = walkingAnimation.getKeyFrame(stateTime, true);
        } else {
            regionToDraw = idleRegion;
        }

        if ((entity.isFlipped() && !regionToDraw.isFlipX()) || (!entity.isFlipped() && regionToDraw.isFlipX()))
            regionToDraw.flip(true, false); 

        batch.draw(regionToDraw, 
            entity.getX() - RENDER_WIDTH / 2f,  
            entity.getY() - RENDER_HEIGHT / 2f, 
            RENDER_WIDTH,                      
            RENDER_HEIGHT           
        );
    }

    @Override
    public void dispose() {
        idleTexture.dispose();
        walkingSheet.dispose();
    }
}
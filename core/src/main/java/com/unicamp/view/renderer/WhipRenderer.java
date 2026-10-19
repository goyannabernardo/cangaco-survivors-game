package com.unicamp.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unicamp.model.entity.EntityManager;
import com.unicamp.model.entity.projectile.WhipProjectile;
import com.unicamp.view.EntityRenderer;
import com.unicamp.model.entity.EntityManager;

public class WhipRenderer implements EntityRenderer<WhipProjectile> {

    private final Animation<TextureRegion> idleAnimation;
    private final Texture idleSheet;

    private static final float RENDER_WIDTH = 128f; 
    private static final float RENDER_HEIGHT = 128f;

    private Animation<TextureRegion> currentAnimation;
    private float stateTime;

    public WhipRenderer() {
        this.idleSheet = new Texture(Gdx.files.internal("weapons/Slash_Merge.png"));
        this.idleAnimation = createAnimation(idleSheet, 6, 1, 0.08f); // Ajustado para bater rápido conforme o lifetime de 0.5s do projétil
        this.currentAnimation = this.idleAnimation;
    }

    @Override
    public void render(SpriteBatch batch, WhipProjectile entity) {
        float stateTime = EntityManager.getGlobalTime();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        
        float scaleX = entity.isFlipped() ? -1f : 1f;

        batch.draw(
            currentFrame,
            entity.getX() - RENDER_WIDTH,
            entity.getY() - RENDER_HEIGHT / 2f,
            RENDER_WIDTH,
            RENDER_HEIGHT / 2f,
            2 * RENDER_WIDTH,
            RENDER_HEIGHT,
            scaleX,
            1f,
            0f
        );
    }

    public Animation<TextureRegion> createAnimation(Texture sheet, int cols, int rows, float frameDuration) {
		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
		TextureRegion[] frames = new TextureRegion[cols * rows];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		return new Animation<>(frameDuration, frames);
	}

    @Override
    public void dispose() {
        idleSheet.dispose();
    }
}
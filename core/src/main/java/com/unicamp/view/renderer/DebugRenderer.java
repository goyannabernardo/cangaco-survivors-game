package com.unicamp.view.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unicamp.model.entity.Entity;
import com.unicamp.view.EntityRenderer;

public class DebugRenderer implements EntityRenderer<Entity> {

	private final Texture whitePixel;

	public DebugRenderer() {
		this.whitePixel = new Texture("ui/white_pixel.png");
	}

	@Override
	public void render(SpriteBatch batch, Entity entity) {
		float x = entity.getX();
		float y = entity.getY();
		float w  = entity.getHitW();
		float h = entity.getHitH();

		batch.setColor(0f, 0f, 1f, 0.4f);
		batch.draw(whitePixel, x - w/2, y - h/2, w, h);
		batch.setColor(Color.WHITE); 
	}

	@Override
	public void dispose() {
		whitePixel.dispose();
	}
	
}

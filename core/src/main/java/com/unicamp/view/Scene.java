package com.unicamp.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unicamp.model.entity.Entity;
import com.unicamp.model.entity.EntityManager;

public abstract class Scene {
    protected final EntityManager entityManager;
	protected final RendererFactory rendererFactory;

    public Scene() {
        this.entityManager = new EntityManager();
		this.rendererFactory = RendererFactory.getInstance();
	}

    public void addEntity(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Não é possível adicionar uma entidade nula.");
        }
        entityManager.spawnEntity(entity);
    }

    public void updateState(float deltaTime) {
        entityManager.handleUpdate(deltaTime);
        entityManager.handleSpawn();
        entityManager.handleColissions();
        onUpdate(deltaTime);
    }

    protected abstract void onUpdate(float deltaTime);

	public void render(SpriteBatch batch, float delta) {
        entityManager.render(batch);
	}

    public boolean isGameOver() { return false; }
    public void resize(int width, int height) {}

    public void dispose() {
        entityManager.dispose();
    }
}
package com.unicamp.model.entity.itemdrop;

import com.unicamp.model.entity.Entity;
import com.unicamp.model.entity.EntityManager;

public abstract class ItemDrop extends Entity {

	public ItemDrop(float x, float y) {
		super(x, y, 25, 25);
	}

	@Override
	public void update(float deltaTime, EntityManager entitySpawner) {}
}
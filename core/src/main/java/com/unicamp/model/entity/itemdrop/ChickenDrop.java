package com.unicamp.model.entity.itemdrop;

import com.unicamp.model.entity.Player;

public class ChickenDrop extends ItemDrop {

	public ChickenDrop(float x, float y) {
		super(x, y);
		onCollideWith(Player.class, player -> {
			player.restoreHealth(25);
			destroy();
		});
	}

}

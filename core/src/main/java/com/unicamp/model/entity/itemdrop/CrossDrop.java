package com.unicamp.model.entity.itemdrop;

import com.unicamp.model.entity.Player;
import com.unicamp.model.entity.weapon.Cross;

public class CrossDrop extends ItemDrop {

	public CrossDrop(float x, float y) {
		super(x, y);
		onCollideWith(Player.class, player -> {
			player.addWeapon(new Cross());
			destroy();
		});
	}
	
}

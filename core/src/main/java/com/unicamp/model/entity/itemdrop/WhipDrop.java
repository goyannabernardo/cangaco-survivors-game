package com.unicamp.model.entity.itemdrop;

import com.unicamp.model.entity.Player;
import com.unicamp.model.entity.weapon.Whip;

public class WhipDrop extends ItemDrop {

	public WhipDrop(float x, float y) {
		super(x, y);
		onCollideWith(Player.class, player -> {
			player.addWeapon(new Whip());
			destroy();
		});
	}
}

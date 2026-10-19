package com.unicamp.model.entity.itemdrop;

import com.unicamp.model.entity.Player;
import com.unicamp.model.entity.weapon.Pistol;

public class PistolDrop extends ItemDrop {

	public PistolDrop(float x, float y) {
		super(x, y);
		onCollideWith(Player.class, player -> {
			player.addWeapon(new Pistol());
			destroy();
		});
	}
}

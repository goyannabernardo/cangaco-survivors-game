package com.unicamp.model.entity.projectile;

import com.unicamp.model.entity.EntityManager;
import com.unicamp.model.entity.Player;
import com.unicamp.model.valueobject.PositionVO;


public class CrossProjectile extends Projectile {

    private static final float LIFE_TIME = 4.0f;      // quanto tempo a cruz fica girando
    private static final float ORBIT_RADIUS = 110f;   // distância até o jogador 
    private static final float ANGULAR_SPEED = 3.5f;  // velocidade da órbita

    private float timer = 0.0f;
    private float angle;

    public CrossProjectile(float x, float y, float finalDamage, float startingAngle) {
        super(x + (float) Math.cos(startingAngle) * ORBIT_RADIUS,
              y + (float) Math.sin(startingAngle) * ORBIT_RADIUS,
              40f, 40f, finalDamage);
        this.angle = startingAngle;
    }

    @Override
    public void update(float deltaTime, EntityManager entityManager) {
        timer += deltaTime;
        if (timer >= LIFE_TIME) {
            destroy();
            return;
        }

        PositionVO playerPos = entityManager.findFirst(Player.class);
        // jogador morreu
        if (playerPos == null) {
            destroy();
            return;
        }

        angle += ANGULAR_SPEED * deltaTime;
        setX(playerPos.x() + (float) Math.cos(angle) * ORBIT_RADIUS);
        setY(playerPos.y() + (float) Math.sin(angle) * ORBIT_RADIUS);
    }
}
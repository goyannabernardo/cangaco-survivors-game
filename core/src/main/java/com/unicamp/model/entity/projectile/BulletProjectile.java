package com.unicamp.model.entity.projectile;

import java.util.HashSet;
import java.util.Set;

import com.unicamp.exception.IllegalEntityStateException;
import com.unicamp.model.entity.EntityManager;
import com.unicamp.model.entity.enemy.Enemy;
import com.unicamp.model.valueobject.PositionVO;

public class BulletProjectile extends Projectile {

    private float timer = 0.0f;
    private final float lifeTime = 2.0f;
    private final float speed = 550f;
    
    private float dirX = 0f;
    private float dirY = 0f;
    private boolean initialized = false;
    private int piercingLeft;

    private final Set<Enemy> alreadyHit = new HashSet<>();

    public BulletProjectile(float x, float y, float finalDamage, int maxPiercing) {
        super(x, y, 30f, 15f, finalDamage);
        this.piercingLeft = maxPiercing;

        onCollideWith(Enemy.class, enemy -> {
            if (alreadyHit.add(enemy)) {
                piercingLeft--;
                if (piercingLeft <= 0) destroy();
            }
        });
    }

    @Override
    public void update(float deltaTime, EntityManager entityManager) {
        timer += deltaTime;
        if (timer >= lifeTime) {
            destroy();
            return;
        }

        if (!initialized) {
            PositionVO nearest = entityManager.findNearestAssignable(new PositionVO(getX(), getY()), Enemy.class);
            if (nearest != null) {
                float dx = nearest.x() - getX();
                float dy = nearest.y() - getY();
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist > 0) {
                    dirX = dx / dist;
                    dirY = dy / dist;
                } else {
                    dirX = 1f;
                }
            } else {
                dirX = 1f;
            }
            initialized = true;
        }

        setxSpeed(dirX * speed * deltaTime);
        setySpeed(dirY * speed * deltaTime);

        try {
            move();
        } catch (IllegalEntityStateException e) {
            System.err.println("Erro no movimento da Bullet: " + e.getMessage());
        }
    }
}
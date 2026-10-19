package com.unicamp.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.unicamp.model.entity.EntityManager;
import com.unicamp.model.entity.enemy.CorpoSeco;
import com.unicamp.model.entity.enemy.Diabo;
import com.unicamp.model.entity.enemy.Enemy;
import com.unicamp.model.entity.enemy.Lobisomem;

public class EnemySpawner {

    // Distância além da borda da tela onde os inimigos nascem (maior que o sprite de 40px)
    private static final float OFFSCREEN_MARGIN = 64f;
    private static final float SPAWN_INTERVAL = 1.5f;

    private final OrthographicCamera camera;
    private final EntityManager entityManager;
    private final List<Enemy> enemies = new ArrayList<>();
    private final Random random = new Random();
    private float spawnTimer = 0f;

    public EnemySpawner(EntityManager entityManager, OrthographicCamera camera) {
        this.entityManager = entityManager;
        this.camera = camera;
    }

    public void update(float delta) {
        if (entityManager.getIsPaused()) return;

        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnEnemy();
            spawnTimer = 0f;
        }

        // Remove inimigos mortos para a lista não crescer para sempre
        enemies.removeIf(enemy -> !enemy.isActive());

        for (Enemy enemy : enemies) {
            wrapAroundCamera(enemy);
        }
    }

    private void spawnEnemy() {
        // Sorteia um ponto no perímetro de um retângulo logo fora da tela
        float halfW = camera.viewportWidth / 2f + OFFSCREEN_MARGIN;
        float halfH = camera.viewportHeight / 2f + OFFSCREEN_MARGIN;
        float width = halfW * 2f;
        float height = halfH * 2f;

        float t = random.nextFloat() * 2f * (width + height);
        float offsetX, offsetY;

        if (t < width) {                    // lado de cima
            offsetX = -halfW + t;
            offsetY = halfH;
        } else if (t < 2f * width) {        // lado de baixo
            offsetX = -halfW + (t - width);
            offsetY = -halfH;
        } else if (t < 2f * width + height) { // lado esquerdo
            offsetX = -halfW;
            offsetY = -halfH + (t - 2f * width);
        } else {                            // lado direito
            offsetX = halfW;
            offsetY = -halfH + (t - 2f * width - height);
        }

        Enemy newEnemy = createRandomEnemy(camera.position.x + offsetX, camera.position.y + offsetY);

        enemies.add(newEnemy);
        entityManager.spawnEntity(newEnemy);
    }

    private Enemy createRandomEnemy(float x, float y) {
        int chance = random.nextInt(100);

        if (chance < 10) {
            // 10% de chance: Lobisomem (tanque)
            return new Lobisomem(x, y, entityManager);
        } else if (chance < 30) {
            // 20% de chance: Diabo (rápido)
            return new Diabo(x, y, entityManager);
        } else {
            // 70% de chance: CorpoSeco (médio)
            return new CorpoSeco(x, y, entityManager);
        }
    }

    // Inimigos que ficam para trás reaparecem do outro lado, ainda fora da tela
    private void wrapAroundCamera(Enemy enemy) {
        float leftBound = camera.position.x - camera.viewportWidth / 2f - OFFSCREEN_MARGIN;
        float rightBound = camera.position.x + camera.viewportWidth / 2f + OFFSCREEN_MARGIN;
        float bottomBound = camera.position.y - camera.viewportHeight / 2f - OFFSCREEN_MARGIN;
        float topBound = camera.position.y + camera.viewportHeight / 2f + OFFSCREEN_MARGIN;

        if (enemy.getX() > rightBound) {
            enemy.setX(leftBound);
        } else if (enemy.getX() < leftBound) {
            enemy.setX(rightBound);
        }

        if (enemy.getY() > topBound) {
            enemy.setY(bottomBound);
        } else if (enemy.getY() < bottomBound) {
            enemy.setY(topBound);
        }
    }
}
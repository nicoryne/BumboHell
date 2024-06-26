package game.entities.player;

import game.entities.EntityType;
import game.entities.CharacterBuilder;
import game.equips.weapons.Weapon;
import game.util.Directions;
import game.util.controllers.KeyboardController;
import game.util.controllers.MouseController;
import game.util.managers.SpritesManager;

import java.awt.*;

public class PlayerBuilder<W extends Weapon> implements CharacterBuilder {

    private Player<W> player;

    @Override
    public void reset() {
        this.player = new Player<>();
    }

    @Override
    public void setEntityType(EntityType type) {
        this.player.setEntityType(type);
    }

    @Override
    public void setScreenPositionX(double screenPositionX) {
        this.player.getPositionComponent().setScreenPositionX(screenPositionX);
    }

    @Override
    public void setScreenPositionY(double screenPositionY) {
        this.player.getPositionComponent().setScreenPositionY(screenPositionY);
    }

    @Override
    public void setAlive() {
        this.player.getRenderComponent().setAlive(true);
    }

    @Override
    public void setHitbox(Rectangle rectangle) {
        this.player.getRenderComponent().setHitbox(rectangle);
    }

    @Override
    public void setEntitySpeed(int entitySpeed) {
        this.player.getMovementComponent().setEntitySpeed(entitySpeed);
    }

    @Override
    public void setDirection(Directions direction) {
        this.player.getMovementComponent().setDirection(direction);
    }

    @Override
    public void setHitPoints(int hitPoints) {
        this.player.getStatComponent().setMaxHitPoints(hitPoints);
        this.player.getStatComponent().setCurrentHitPoints(hitPoints);
    }


    @Override
    public void setSpeed(int speed) {
        this.player.getStatComponent().setSpeed(speed);
    }

    public void setKeyboardController(KeyboardController keyboardController) {
        this.player.setKeyboardController(keyboardController);
    }

    public void setExp(double exp) {
        this.player.setExp(exp);
    }

    public void setLevel(int level) {
        this.player.setLevel(level);
    }

    public void setExpToLevelUp(double expToLevelUp) {
        this.player.setExpToLevelUp(expToLevelUp);
    }

    public void setMovementSpritesManager(SpritesManager spritesManager) {
        this.player.setMovementSpritesManager(spritesManager);
    }

    public void setIdleSpritesManager(SpritesManager spritesManager) {
        this.player.setIdleSpritesManager(spritesManager);
    }

    public void setDamagedSpritesManager(SpritesManager spritesManager) {
        this.player.setDamagedSpritesManager(spritesManager);
    }

    public void setDyingSpritesManager(SpritesManager spritesManager) {
        this.player.setDyingSpritesManager(spritesManager);
    }

    public Player<W> build() {
        return this.player;
    }
}

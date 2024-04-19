package game.entities.player;

import game.Game;
import game.entities.CharacterEntity;
import game.entities.util.ControllableEntity;
import game.equips.weapons.Weapon;
import game.util.Directions;
import game.util.handlers.CollisionHandler;
import game.util.controllers.KeyboardController;
import game.util.controllers.MouseController;
import game.util.managers.SpritesManager;
import game.util.handlers.SoundHandler;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Player<T extends Weapon> extends CharacterEntity implements ControllableEntity {
    private KeyboardController keyboardController;
    private MouseController mouseController;
    private SpritesManager spritesManager;
    private double exp;
    private int level;
    private double expToLevelUp;
    private T weapon;

    @Override
    public void update() {
        CollisionHandler.checkEnemyCollision(this);
        CollisionHandler.checkDropCollision(this);
        move();
        look();
        attack();
        weapon.incrementReloadCooldown();
        incrementTakeDamageCounter();
    }

    @Override
    public void render(Graphics2D g2) {
        BufferedImage sprite = spritesManager.getCurrentSprite(getMovementComponent().getDirection());

        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();

        g2.drawImage(sprite, screenPositionX, screenPositionY,null);
        showHPBar(g2);
        showXPBar(g2);
    }

    private void move() {
        boolean validKey = keyboardController.isUpPressed()
                || keyboardController.isDownPressed()
                || keyboardController.isLeftPressed()
                || keyboardController.isRightPressed();


        if (validKey && getRenderComponent().isAlive()) {
            handleDirections();
            int speed = calculateSpeed();
            int diagonalSpeed = (int) (speed / Math.sqrt(2));
            getMovementComponent().setColliding(false);
            CollisionHandler.checkTileCollision(this, speed);

            if(!getMovementComponent().isColliding()) {
                int worldPositionY = getPositionComponent().getWorldPositionY().intValue();
                int worldPositionX = getPositionComponent().getWorldPositionX().intValue();

                handleMovement(worldPositionX, worldPositionY, speed, diagonalSpeed);
            } else {
                SoundHandler.playAudio("bump-1", 0, 1.0f);
            }

            spritesManager.updateSprite();
        }
    }

    private void look() {
        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();

        int dx = screenPositionX - mouseController.getMousePositionX();
        int dy = screenPositionY - mouseController.getMousePositionY();

        double angle = (Math.atan2(dy, dx)) - Math.PI / 2.0;

        getMovementComponent().setAngle(angle);
    }

    private void attack() {
        boolean validKey = keyboardController.isAttacking();
        double screenX = getPositionComponent().getScreenPositionX().doubleValue() + getRenderComponent().getHitbox().getWidth();
        double screenY = getPositionComponent().getScreenPositionY().doubleValue() + getRenderComponent().getHitbox().getHeight();
        double angle = getMovementComponent().getAngle();
        double worldPositionX = getPositionComponent().getWorldPositionX().doubleValue();
        double worldPositionY = getPositionComponent().getWorldPositionY().doubleValue();
        Directions direction = getMovementComponent().getDirection();

        if (validKey) {
            weapon.attack(angle, screenX, screenY, worldPositionX, worldPositionY, direction);
        }
    }

    @Override
    public int dealDamage() {
        return weapon.getDamage();
    }

    private void handleDirections() {
        boolean up = keyboardController.isUpPressed();
        boolean down = keyboardController.isDownPressed();
        boolean left = keyboardController.isLeftPressed();
        boolean right = keyboardController.isRightPressed();

        int directionCode = (up ? 1 : 0) | (down ? 2 : 0) | (left ? 4 : 0) | (right ? 8 : 0);

        switch (directionCode) {
            case 1:
                getMovementComponent().setDirection(Directions.NORTH);
                break;
            case 2:
                getMovementComponent().setDirection(Directions.SOUTH);
                break;
            case 4:
                getMovementComponent().setDirection(Directions.WEST);
                break;
            case 8:
                getMovementComponent().setDirection(Directions.EAST);
                break;
            case 9:
                getMovementComponent().setDirection(Directions.NORTH_EAST);
                break;
            case 5:
                getMovementComponent().setDirection(Directions.NORTH_WEST);
                break;
            case 10:
                getMovementComponent().setDirection(Directions.SOUTH_EAST);
                break;
            case 6:
                getMovementComponent().setDirection(Directions.SOUTH_WEST);
                break;
            default:
                getMovementComponent().setDirection(Directions.NONE);
                break;
        }
    }

    private void handleMovement(int worldPositionX, int worldPositionY, int speed, int diagonalSpeed) {
        switch (getMovementComponent().getDirection()) {
            case Directions.NORTH_EAST:
                getPositionComponent().setWorldPositionY(worldPositionY - diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX + diagonalSpeed);
                break;
            case Directions.NORTH_WEST:
                getPositionComponent().setWorldPositionY(worldPositionY - diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX - diagonalSpeed);
                break;
            case Directions.SOUTH_EAST:
                getPositionComponent().setWorldPositionY(worldPositionY + diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX + diagonalSpeed);
                break;
            case Directions.SOUTH_WEST:
                getPositionComponent().setWorldPositionY(worldPositionY + diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX - diagonalSpeed);
                break;
            case Directions.NORTH:
                getPositionComponent().setWorldPositionY(worldPositionY - speed);
                break;
            case Directions.SOUTH:
                getPositionComponent().setWorldPositionY(worldPositionY + speed);
                break;
            case Directions.WEST:
                getPositionComponent().setWorldPositionX(worldPositionX - speed);
                break;
            case Directions.EAST:
                getPositionComponent().setWorldPositionX(worldPositionX + speed);
                break;
        }
    }

    private void showHitbox(Graphics2D g2, int screenPositionX, int screenPositionY) {
        Rectangle hitbox = getRenderComponent().getHitbox();
        g2.setColor(Color.RED); // Set the color of the outline
        g2.drawRect((int) (screenPositionX + hitbox.getX()), (int) (screenPositionY + hitbox.getY()), (int) hitbox.getWidth(), (int) hitbox.getHeight());
    }

    private void showXPBar(Graphics2D g2) {
        int screenWidth = Game.getInstance().getScreenWidth();

        int expWidth = (int) (screenWidth - exp);

        g2.setColor(Color.WHITE);
        g2.drawRect(0, 0, screenWidth, 30);
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 0, expWidth, 30);
    }

    public void setSpritesManager(SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
    }

    @Override
    public void setKeyboardController(KeyboardController keyboardController) {
        this.keyboardController = keyboardController;
    }

    @Override
    public void setMouseController(MouseController mouseController) {
        this.mouseController = mouseController;
    }

    public T getWeapon() {
        return weapon;
    }

    public void setWeapon(T weapon) {
        this.weapon = weapon;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void takeExp(double exp) {
        this.exp += exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void incrementLevel() {
        this.level++;
    }

    public double getExpToLevelUp() {
        return expToLevelUp;
    }

    public void setExpToLevelUp(double expToLevelUp) {
        this.expToLevelUp = expToLevelUp;
    }


}
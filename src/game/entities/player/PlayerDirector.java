package game.entities.player;

import game.Game;
import game.entities.EntityType;
import game.equips.weapons.Weapon;
import game.util.Directions;
import game.util.controllers.KeyboardController;
import game.util.controllers.MouseController;
import game.util.managers.SpritesManager;

import java.awt.*;

public class PlayerDirector {

    public <T extends Weapon> void constructPlayer(PlayerBuilder<T> builder, KeyboardController keyboardController, MouseController mouseController, T weapon) {
        Game game = Game.getInstance();
        int tileSize = Integer.parseInt(game.getProperty("TILE_SIZE"));

        builder.reset();
        builder.setEntityType(EntityType.PLAYER);
        builder.setAlive();
        builder.setScreenPositionX(game.getScreenWidth() / 2.0 - (tileSize / 2.0));
        builder.setScreenPositionY(game.getScreenHeight() / 2.0 - (tileSize / 2.0));
        builder.setHitbox(new Rectangle(8, 8, 32, 32));
        builder.setEntitySpeed(1);
        builder.setDirection(Directions.SOUTH);
        builder.setExp(0.0);
        builder.setLevel(1);
        builder.setExpToLevelUp(45.0);
        builder.setSpritesManager(new SpritesManager("player", 3, 1));
        builder.setKeyboardController(keyboardController);
        builder.setMouseController(mouseController);
        builder.setWeapon(weapon);
        builder.setHitPoints(100);
        builder.setSpeed(10);
    }

}

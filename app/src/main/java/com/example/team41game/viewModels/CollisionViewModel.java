package com.example.team41game.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.team41game.MoveDown;
import com.example.team41game.MoveLeft;
import com.example.team41game.MovePattern;
import com.example.team41game.MoveRight;
import com.example.team41game.MoveUp;
import com.example.team41game.Position;
import com.example.team41game.enemyFactoryDesign.Enemy;
import com.example.team41game.interactiveObjFactoryDesign.Box;
import com.example.team41game.interactiveObjFactoryDesign.Chest;
import com.example.team41game.interactiveObjFactoryDesign.InteractiveObj;
import com.example.team41game.itemFactoryDesign.Item;
import com.example.team41game.models.Player;
import com.example.team41game.models.Room;

import java.util.HashMap;
import java.util.List;

public class CollisionViewModel extends ViewModel {
    private HashMap<String, List<InteractiveObj>> interactiveObjsMap;
    private final Player player;
    private Room room;

    public CollisionViewModel() {
        this.player = Player.getPlayer();
    }

    public void setInteractiveObjsMap(HashMap<String, List<InteractiveObj>> interactiveObjsMap) {
        this.interactiveObjsMap = interactiveObjsMap;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean inScreenLimit() {
        //check that player does not move off game screen, based on game bitmap sizes
        if (player.getMovePattern() instanceof MoveLeft) {
            return player.getPosition().getX() - 1 >= 0;
        } else if (player.getMovePattern() instanceof MoveRight) {
            return player.getPosition().getX() + 1 < room.getFloorLayout()[0].length;
        } else if (player.getMovePattern() instanceof MoveUp) {
            return player.getPosition().getY() - 1 >= 0;
        } else if (player.getMovePattern() instanceof MoveDown) {
            return player.getPosition().getY() + 1 < room.getFloorLayout().length;
        }
        return false;
    }

    // Returns true if the player will not move off screen, collide with a chest, or collide
    // with a wall
    public boolean isValidMove() {
        Position pos = player.getPosition();
        MovePattern movePattern = player.getMovePattern();
        return (inScreenLimit()
                && !isChestCollision(pos, movePattern)
                && !isWallCollision(pos, movePattern)
                && !isBoxCollision(pos, movePattern)
                && !isDoorCollision(pos, movePattern));
    }

    // return the new Position resulting from moving from pos in the movePattern direction
    public Position getNextPosition(Position pos, MovePattern movePattern) {
        int nextX = pos.getX();
        int nextY = pos.getY();
        if (movePattern instanceof MoveLeft) {
            nextX--;
        } else if (movePattern instanceof MoveRight) {
            nextX++;
        } else if (movePattern instanceof MoveUp) {
            nextY--;
        } else if (movePattern instanceof MoveDown) {
            nextY++;
        }
        return new Position(nextX, nextY);
    }

    // if the pos position collides with an object of objType (i.e. chests, boxes, doors)
    // return the object it collides with
    public InteractiveObj getCollision(Position pos, String objType) {
        if (interactiveObjsMap.containsKey(objType) && interactiveObjsMap.get(objType) != null) {
            for (InteractiveObj interactiveObj : interactiveObjsMap.get(objType)) {
                if (pos.equals(interactiveObj.getPosition())) {
                    return interactiveObj;
                }
            }
        }
        return null;
    }

    // if a movement from pos in the movePat direction would result
    // in a collision with an object of objType (i.e. chests, boxes, doors)
    // return the object it would collide with
    public InteractiveObj getFutureCollision(Position pos, MovePattern movePat, String objType) {
        Position nextPosition = getNextPosition(pos, movePat);
        return getCollision(nextPosition, objType);
    }

    // Returns true if a movement from pos in the movePattern direction
    // would result in a wall collision
    public boolean isWallCollision(Position pos, MovePattern movePattern) {
        Position nextPosition = getNextPosition(pos, movePattern);
        int nextTile = room.getFloorLayout()[nextPosition.getY()][nextPosition.getX()];
        // the numbers 1, 3, 4, and 5 are the different tileId's of walls
        return (nextTile == 1) || (nextTile == 3) || (nextTile == 4) || (nextTile == 5);
    }

    // returns true if moving from pos in the movePattern direction results in a chest collision
    public boolean isChestCollision(Position pos, MovePattern movePattern) {
        return (getFutureCollision(pos, movePattern, "chests") != null);
    }

    // returns true if moving from pos in the movePattern direction
    // results in an open exit door collision
    public boolean isExitCollision() {
        InteractiveObj door = getCollision(player.getPosition(), "doors");
        return (door != null && door.getType().equals("exit") && door.isOpen());
    }

    // returns true if moving from pos in the movePattern direction
    // results in a closed door collision
    public boolean isDoorCollision(Position pos, MovePattern movePattern) {
        InteractiveObj door = getFutureCollision(pos, movePattern, "doors");
        return (door != null && !door.isOpen());
    }

    // returns true if moving from pos in the movePattern direction results in a box collision
    public boolean isBoxCollision(Position pos, MovePattern movePattern) {
        return (getFutureCollision(pos, movePattern, "boxes") != null);
    }

    public boolean isAnyCollision(Position pos, MovePattern movePattern) {
        return (isWallCollision(pos, movePattern)
                || isChestCollision(pos, movePattern)
                || isDoorCollision(pos, movePattern)
                || isBoxCollision(pos, movePattern));
    }

    public void handleBoxCollision() {
        Position playerPos = player.getPosition();
        MovePattern movePattern = player.getMovePattern();

        InteractiveObj box = getFutureCollision(playerPos, movePattern, "boxes");
        if (box != null) {
            // only move the box if it won't collide with anything
            if (!isAnyCollision(box.getPosition(), player.getMovePattern())) {
                ((Box) box).setMovePattern(player.getMovePattern());
                ((Box) box).doMove();
            }
        }
    }

    // returns the chest that the player is adjacent to
    // If player isn't adjacent to a chest, returns null
    public InteractiveObj getAdjChest() {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();

        // player is near a chest if the chest is 1 block away vertically or horizontally
        Position pos1 = new Position(x + 1, y);
        Position pos2 = new Position(x - 1, y);
        Position pos3 = new Position(x, y + 1);
        Position pos4 = new Position(x, y - 1);

        if (interactiveObjsMap.containsKey("chests") && interactiveObjsMap.get("chests") != null) {
            for (InteractiveObj chest : interactiveObjsMap.get("chests")) {
                Position chestPos = chest.getPosition();
                if (chestPos.equals(pos1) || chestPos.equals(pos2)
                        || chestPos.equals(pos3) || chestPos.equals(pos4)) {
                    return chest;
                }
            }
        }
        return null;
    }

    // returns the door that the player is adjacent to
    // If player isn't adjacent to a door, returns null
    public InteractiveObj getAdjDoor() {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();

        // player is near a door if the door is 1 block away vertically or horizontally
        Position pos1 = new Position(x + 1, y);
        Position pos2 = new Position(x - 1, y);
        Position pos3 = new Position(x, y + 1);
        Position pos4 = new Position(x, y - 1);

        if (interactiveObjsMap.containsKey("doors") && interactiveObjsMap.get("doors") != null) {
            for (InteractiveObj door : interactiveObjsMap.get("doors")) {
                Position doorPos = door.getPosition();
                if (doorPos.equals(pos1) || doorPos.equals(pos2)
                        || doorPos.equals(pos3) || doorPos.equals(pos4)) {
                    return door;
                }
            }
        }
        return null;
    }

    public InteractiveObj handleChestInteraction() {
        InteractiveObj chest = getAdjChest();
        if (chest != null && !chest.isOpen()) {
            chest.open();
            Item item = ((Chest) chest).getContents();
            if (item != null) {
                item.discover();
            }
        }
        return chest;
    }

    public InteractiveObj handleDoorInteraction() {
        InteractiveObj door = getAdjDoor();
        if (door != null && !door.isOpen() && player.getInventory() != null) {
            Item key = player.getInventory().get("Key");
            if (key != null) {
                door.open();
                player.getInventory().put("Key", null); // remove key from player inventory
            }
        }
        return door;
    }

    public Enemy handlePlayerAttack(HashMap<String, List<Enemy>> enemiesMap) {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();

        // player is near an enemy if the enemy is 1 block away vertically or horizontally
        Position pos1 = new Position(x + 1, y);
        Position pos2 = new Position(x - 1, y);
        Position pos3 = new Position(x, y + 1);
        Position pos4 = new Position(x, y - 1);

        for (String enemyType : enemiesMap.keySet()) {
            List<Enemy> enemyList = enemiesMap.get(enemyType);
            for (Enemy enemy : enemyList) {
                Position enemyPos = enemy.getPosition();
                if (enemyPos.equals(pos1) || enemyPos.equals(pos2)
                        || enemyPos.equals(pos3) || enemyPos.equals(pos4)) {

                    enemy.die();

                    // Check if the enemy is defeated (no longer alive).
                    if (!enemy.isAlive()) {
                        // Increase the player's score by 5 points.
                        int currentScore = player.getScore();
                        player.setScore(currentScore + 5);
                    }

                    // Remove the defeated enemy from the list to prevent further processing.
                    enemyList.remove(enemy);

                    // Return the enemy that was attacked.
                    return enemy;
                }
            }
        }
        return null;
    }

    public void handleItemAcquisition() {
        Chest chest = (Chest) getAdjChest();
        if (chest != null) {
            Item item = chest.getContents();
            // check if the item inside the chest has just been discovered (i.e. not yet acquired)
            if (item != null && item.justDiscovered()) {
                item.acquire();
                player.addToInventory(item.getClass().getSimpleName(), item);
            }
        }
    }
}

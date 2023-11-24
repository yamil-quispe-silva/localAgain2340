package com.example.team41game;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.example.team41game.interactiveObjFactoryDesign.BoxCreator;
import com.example.team41game.interactiveObjFactoryDesign.Chest;
import com.example.team41game.interactiveObjFactoryDesign.ChestCreator;
import com.example.team41game.interactiveObjFactoryDesign.DoorCreator;
import com.example.team41game.interactiveObjFactoryDesign.InteractiveObj;
import com.example.team41game.interactiveObjFactoryDesign.InteractiveObjCreator;
import com.example.team41game.itemFactoryDesign.Item;
import com.example.team41game.itemFactoryDesign.ItemCreator;
import com.example.team41game.itemFactoryDesign.KeyCreator;
import com.example.team41game.models.Player;
import com.example.team41game.models.Room;
import com.example.team41game.viewModels.CollisionViewModel;

import com.example.team41game.itemFactoryDesign.Key;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CollisionUnitTests {
    private CollisionViewModel collisionViewModel;
    private Player player;



    @Before
    public void setup() {
        collisionViewModel = new CollisionViewModel();
        player = Player.getPlayer();
    }

    /**
     * Test that getAdjChest correctly returns the chest
     * that the player is one tile away from (Sprint 4)
     */
    @Test
    public void testGetAdjChest() {
        InteractiveObjCreator chestCreator = new ChestCreator();
        InteractiveObj chest = chestCreator.createInteractiveObj(0, 1, "gold");
        List<InteractiveObj> chests = new ArrayList<>();
        chests.add(chest);
        HashMap<String, List<InteractiveObj>> interactiveObjsMap = new HashMap<>();
        interactiveObjsMap.put("chests", chests);
        collisionViewModel.setInteractiveObjsMap(interactiveObjsMap);

        // player is one tile above the chest
        player.setPosition(new Position(0, 0));
        assertEquals(chest, collisionViewModel.getAdjChest());

        // player is far from the chest
        player.setPosition(new Position(10, 10));
        assertNull(collisionViewModel.getAdjChest());

        // player is diagonal to the chest
        player.setPosition(new Position(1, 0));
        assertNull(collisionViewModel.getAdjChest());
    }

    /**
     * Test that handleBoxCollision correctly moves the box that the player collides with (Sprint 4)
     */
    @Test
    public void testHandleBoxCollision() {
        InteractiveObjCreator boxCreator = new BoxCreator();
        InteractiveObj box = boxCreator.createInteractiveObj(1, 0, "unstacked");
        List<InteractiveObj> boxes = new ArrayList<>();
        boxes.add(box);
        HashMap<String, List<InteractiveObj>> interactiveObjsMap = new HashMap<>();
        interactiveObjsMap.put("boxes", boxes);
        collisionViewModel.setInteractiveObjsMap(interactiveObjsMap);

        int[][] roomLayout = {{0, 0, 0}, {0, 0, 0}};
        Room room = new Room(roomLayout);
        collisionViewModel.setRoom(room);

        // player is one tile to the left of the box, so moving right will push the box to (2, 0)
        player.setPosition(new Position(0, 0));
        player.setMovePattern(new MoveRight());
        collisionViewModel.handleBoxCollision();
        assertEquals(new Position(2, 0), box.getPosition());

        // player is one tile to the left of the box, but moving left will not collide with the box
        player.setPosition(new Position(0, 0));
        player.setMovePattern(new MoveLeft());
        collisionViewModel.handleBoxCollision();
        assertEquals(new Position(2, 0), box.getPosition());
    }

    /**
     * Test that the isAnyCollision method in CollisionViewModel responds
     * correctly to different types of collisions
     */
    @Test
    public void testIsAnyCollision() {
        CollisionViewModel collisionViewModel = new CollisionViewModel();

        // Set up interactive objects map with a box at position (1, 0),
        // a chest at position (2,0), and a door at position (3, 0)
        InteractiveObjCreator boxCreator = new BoxCreator();
        InteractiveObjCreator chestCreator = new ChestCreator();
        InteractiveObjCreator doorCreator = new DoorCreator();
        InteractiveObj box = boxCreator.createInteractiveObj(1, 0, "unstacked");
        InteractiveObj chest = chestCreator.createInteractiveObj(2, 0, "gold");
        InteractiveObj door = doorCreator.createInteractiveObj(3, 0, "exit");
        List<InteractiveObj> chests = new ArrayList<>();
        List<InteractiveObj> boxes = new ArrayList<>();
        List<InteractiveObj> doors = new ArrayList<>();
        boxes.add(box);
        chests.add(chest);
        doors.add(door);
        HashMap<String, List<InteractiveObj>> interactiveObjsMap = new HashMap<>();
        interactiveObjsMap.put("chests", chests);
        interactiveObjsMap.put("boxes", boxes);
        interactiveObjsMap.put("doors", doors);
        collisionViewModel.setInteractiveObjsMap(interactiveObjsMap);

        // Set up room layout
        int[][] roomLayout = {{0, 0, 0, 0}, {0, 0, 0, 0}};
        Room room = new Room(roomLayout);
        collisionViewModel.setRoom(room);

        // when the player is one tile to the left of the box, moving right will cause a collison
        Position pos1 = new Position(0, 0);
        MovePattern movePattern1 = new MoveRight();
        assertTrue(collisionViewModel.isAnyCollision(pos1, movePattern1));

        // when the player is one tile below the chest, moving up will cause a collsion
        Position pos2 = new Position(2, 1);
        MovePattern movePattern2 = new MoveUp();
        assertTrue(collisionViewModel.isAnyCollision(pos2, movePattern2));

        // when the player is one tile to the right of the door, moving left will cause a collsion
        Position pos3 = new Position(4, 0);
        MovePattern movePattern3 = new MoveLeft();
        assertTrue(collisionViewModel.isAnyCollision(pos3, movePattern3));

        // when the player is one tile below the chest, moving left will not result in a collision
        Position pos4 = new Position(2, 1);
        MovePattern movePattern4 = new MoveLeft();
        assertFalse(collisionViewModel.isAnyCollision(pos4, movePattern4));
    }

    /**
     * Test the isChestCollision method in CollisionViewModel responds correctly to chest collisions
     */
    @Test
    public void testIsChestCollision() {
        CollisionViewModel collisionViewModel = new CollisionViewModel();

        InteractiveObjCreator chestCreator = new ChestCreator();
        InteractiveObj chest = chestCreator.createInteractiveObj(1, 0, "gold");
        List<InteractiveObj> chests = new ArrayList<>();
        chests.add(chest);
        HashMap<String, List<InteractiveObj>> interactiveObjsMap = new HashMap<>();
        interactiveObjsMap.put("chests", chests);
        collisionViewModel.setInteractiveObjsMap(interactiveObjsMap);

        // Set up room layout
        int[][] roomLayout = {{0, 0, 0}, {0, 0, 0}};
        Room room = new Room(roomLayout);
        collisionViewModel.setRoom(room);

        // Test when the player is one tile to the left of the chest and moving right
        Position pos1 = new Position(0, 0);
        MovePattern movePattern1 = new MoveRight();
        assertTrue(collisionViewModel.isChestCollision(pos1, movePattern1));

        // Test when the player is one tile to the right of the chest and moving left
        Position pos2 = new Position(2, 0);
        MovePattern movePattern2 = new MoveLeft();
        assertTrue(collisionViewModel.isChestCollision(pos2, movePattern2));

        // Test when the player is one tile above the chest and moving up
        Position pos3 = new Position(1, 1);
        MovePattern movePattern3 = new MoveUp();
        assertTrue(collisionViewModel.isChestCollision(pos3, movePattern3));

        // Test when the player is not colliding with the chest
        Position pos4 = new Position(2, 1);
        MovePattern movePattern4 = new MoveUp();
        assertFalse(collisionViewModel.isChestCollision(pos4, movePattern4));
    }
  
    /** Test that handleChestInteraction opens adjacent chest and discovers any of its contents.
     * (Sprint 4)
     */
    @Test
    public void testHandleChestInteraction() {
        HashMap<String, List<InteractiveObj>> interactiveObjsMap = new HashMap<>();
        List<InteractiveObj> chests = new ArrayList<>();
        InteractiveObjCreator chestCreator = new ChestCreator();
        chests.add(chestCreator.createInteractiveObj(0, 0, "gold"));
        chests.add(chestCreator.createInteractiveObj(0, 1, "gold"));
        chests.add(chestCreator.createInteractiveObj(5, 7, "silver"));
        interactiveObjsMap.put("chests", chests);
        collisionViewModel.setInteractiveObjsMap(interactiveObjsMap);

        //non-adjacent chest
        player.setPosition(new Position(4, 4));
        collisionViewModel.handleChestInteraction();
        for (InteractiveObj chest : chests) {
            assertFalse(chest.isOpen());
            if (((Chest) chest).getContents() != null) {
                assertFalse(((Chest) chest).getContents().justDiscovered());
            }
        }

        //horizontally adjacent key chest
        player.setPosition(new Position(1, 0));
        Chest testChest1 = (Chest) chests.get(0);
        ItemCreator keyCreator = new KeyCreator();
        Item key1 = keyCreator.createItem();
        testChest1.setContents(key1);
        key1.setContainer(testChest1);
        assertFalse(key1.justDiscovered());
        collisionViewModel.handleChestInteraction();
        assertTrue(testChest1.isOpen());
        assertTrue(key1.justDiscovered());

        //vertically adjacent key chest
        player.setPosition(new Position(0, 2));
        Chest testChest2 = (Chest) chests.get(1);
        Item key2 = keyCreator.createItem();
        testChest2.setContents(key2);
        key2.setContainer(testChest2);
        assertFalse(key2.justDiscovered());
        collisionViewModel.handleChestInteraction();
        assertTrue(testChest2.isOpen());
        assertTrue(key2.justDiscovered());

        //adjacent non-key chest
        player.setPosition(new Position(5, 6));
        InteractiveObj testChest3 = chests.get(2);
        collisionViewModel.handleChestInteraction();
        assertTrue(testChest3.isOpen());
    }

    /**
     * Test that handleDoorInteraction opens adjacent door when player has key. (Sprint 4)
     */
    @Test
    public void testHandleDoorInteraction() {
        HashMap<String, List<InteractiveObj>> interactiveObjsMap = new HashMap<>();
        List<InteractiveObj> doors = new ArrayList<>();
        InteractiveObjCreator doorCreator = new DoorCreator();
        doors.add(doorCreator.createInteractiveObj(3, 1, "exit"));
        interactiveObjsMap.put("doors", doors);
        collisionViewModel.setInteractiveObjsMap(interactiveObjsMap);

        player.setPosition(new Position(3, 0));
        collisionViewModel.handleDoorInteraction();
        assertNull(player.getInventory().get("Key"));
        assertFalse(doors.get(0).isOpen());

        ItemCreator keyCreator = new KeyCreator();
        Item key = keyCreator.createItem();
        player.addToInventory("Key", key);
        assertNotNull(player.getInventory().get("Key"));
        collisionViewModel.handleDoorInteraction();
        assertTrue(doors.get(0).isOpen());
        assertNull(player.getInventory().get("Key"));
    }

    /**
     * Test that handleItemAcquisition correctly acquires a key from a chest and puts it in
     * the player's inventory
     */
    @Test
    public void testHandleItemAcquisition() {
        // create key an place in chest
        Key key = new Key();
        Chest chest = new Chest(6, 5, "silver");
        chest.setContents(key);
        key.setContainer(chest);

        // set player position 1 tile to the left of the chest
        player.setPosition(new Position(5, 5));

        // Add the chest to the interactive objects map in CollisionViewModel
        HashMap<String, List<InteractiveObj>> interactiveObjsMap = new HashMap<>();
        List<InteractiveObj> chests = new ArrayList<>();
        chests.add(chest);
        interactiveObjsMap.put("chests", chests);
        collisionViewModel.setInteractiveObjsMap(interactiveObjsMap);

        // Simulate the chest being opened and the item being discovered but not yet acquired
        chest.open();
        key.discover();
        assertTrue(key.justDiscovered());

        // Acquire the key from the chest and place in player's inventory
        collisionViewModel.handleItemAcquisition();
        assertNotNull(player.getInventory().get("Key"));
    }

    /**
     * Test that getCollision correctly identifies a collision with an object of a specified type
     * at a given position (Sprint 4)
     */
    @Test
    public void testGetCollision() {
        HashMap<String, List<InteractiveObj>> interactiveObjsMap = new HashMap<>();
        List<InteractiveObj> chests = new ArrayList<>();
        InteractiveObjCreator chestCreator = new ChestCreator();
        chests.add(chestCreator.createInteractiveObj(0, 0, "gold"));
        interactiveObjsMap.put("chests", chests);
        collisionViewModel.setInteractiveObjsMap(interactiveObjsMap);

        // if pos matches position of chest, then returns chest
        Position pos = new Position(0, 0);
        assertTrue(collisionViewModel.getCollision(pos, "chests") != null);

        // if pos2 does not match the location of chest, then it returns null
        Position pos2 = new Position(0, 1);
        assertNull(collisionViewModel.getCollision(pos2, "chests"));

        // if object type is not in the InteractiveObjMap, then it returns null
        assertNull(collisionViewModel.getCollision(pos, "house"));
    }
}

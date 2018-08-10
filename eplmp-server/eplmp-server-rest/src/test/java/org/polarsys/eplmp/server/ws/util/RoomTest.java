/*******************************************************************************
  * Copyright (c) 2017 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/
package org.polarsys.eplmp.server.ws.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.server.ws.chat.Room;

import javax.websocket.Session;
import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Asmae CHADID
 */
@RunWith(MockitoJUnitRunner.class)
public class RoomTest {

    private static Room room = Mockito.mock(Room.class);

    // Safe cast
    @SuppressWarnings("unchecked")
    private static ConcurrentMap<String, Room> DB = Mockito.mock(ConcurrentHashMap.class);

    private static Session userSession1 = Mockito.mock(Session.class);
    private static Session userSession2 = Mockito.mock(Session.class);
    private static Principal principal1 = Mockito.mock(Principal.class);
    private static Principal principal2 = Mockito.mock(Principal.class);
    private static Principal principal3 = Mockito.mock(Principal.class);
    private static Room secondRoom = Mockito.spy(new Room("PLMRoom"));
    private static Room thirdRoom = Mockito.spy(new Room("ChatRoom"));
    private static Session userSession3 = Mockito.mock(Session.class);

    @BeforeClass
    public static void setUp() {

        Mockito.when(room.addUserSession(userSession1, "user1")).thenCallRealMethod();
        Mockito.when(room.addUserSession(userSession2, "user2")).thenCallRealMethod();
        Mockito.when(room.key()).thenReturn("plm");

        Mockito.when(userSession1.getUserPrincipal()).thenReturn(principal1);
        Mockito.when(principal1.getName()).thenReturn("user1");
        Mockito.when(room.getUser1Login()).thenCallRealMethod();

        Mockito.when(userSession2.getUserPrincipal()).thenReturn(principal2);
        Mockito.when(principal2.getName()).thenReturn("user2");
        Mockito.when(room.getUser2Login()).thenCallRealMethod();

        Mockito.when(principal3.getName()).thenReturn("user3");
        Mockito.when(userSession3.getUserPrincipal()).thenReturn(principal3);

        Mockito.when(room.getUserSession(Matchers.anyString())).thenCallRealMethod();
        Mockito.when(room.getOtherUserSession(Matchers.any(Session.class))).thenCallRealMethod();
        Mockito.when(RoomTest.DB.get(Matchers.anyString())).thenReturn(room);
        Mockito.when(RoomTest.DB.put(Matchers.anyString(), Matchers.any(Room.class))).thenReturn(room);
        Mockito.when(RoomTest.DB.get("plm").getSessionForUserLogin(Matchers.anyString())).thenCallRealMethod();
        Mockito.when(RoomTest.DB.get("plm").getRoomSessionForUserLogin(Matchers.anyString())).thenCallRealMethod();
        Mockito.when(room.getOccupancy()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(room).put();

        Mockito.when(room.hasUser(Matchers.anyString())).thenCallRealMethod();

        room.addUserSession(userSession1, "user1");
        room.addUserSession(userSession2, "user2");

        secondRoom.put();
        thirdRoom.put();

        Mockito.when(room.key()).thenReturn("plm");

    }

    @Test
    public void testGetByKeyName() {
        Assert.assertEquals("plm", DB.get(" ").key());
    }

    @Test
    public void testGetRoom() {
        Assert.assertEquals(DB.get("null"), room);
    }

    @Test
    public void testAddUserSession() {
        Assert.assertTrue(room.addUserSession(userSession1, "user1"));
        Assert.assertTrue(room.addUserSession(userSession2, "user2"));

    }

    @Test
    public void testGetUser1Login() {
        Assert.assertEquals("user1", DB.get("plm").getUser1Login());

    }

    @Test
    public void testGetUser2Login() {
        Assert.assertEquals("user2", DB.get("plm").getUser2Login());

    }

    @Test
    public void testGetSessionForUserLogin() {
        Assert.assertEquals(room.getSessionForUserLogin("user1"), userSession1);
        Assert.assertNull(room.getSessionForUserLogin("user4"));
    }

    @Test
    public void testGetOccupancy() {
        Assert.assertEquals(2, DB.get("plm").getOccupancy());
    }

    @Test
    public void testGetOtherUserSession() {
        Session userSession3 = Mockito.mock(Session.class);
        Assert.assertEquals(DB.get("plm").getOtherUserSession(userSession1), userSession2);
        Assert.assertEquals(DB.get("plm").getOtherUserSession(userSession2), userSession1);
        Assert.assertNull(DB.get("plm").getOtherUserSession(userSession3));
    }

    @Test
    public void testHasUser() {
        Assert.assertTrue(DB.get("plm").hasUser("user1"));
    }

    @Test
    public void testGetUserSession() {
        Assert.assertEquals(userSession1, DB.get("plm").getUserSession("user1"));
        Assert.assertEquals(userSession2, DB.get("plm").getUserSession("user2"));
    }


    @Test
    public void testAddUserSessionSecondRoom() {
        //When
        secondRoom.addUserSession(userSession1, "user1");
        secondRoom.addUserSession(userSession2, "user2");

        //Then
        Mockito.verify(secondRoom, Mockito.times(1)).addUserSession(userSession1, "user1");
        Mockito.verify(secondRoom, Mockito.times(1)).addUserSession(userSession2, "user2");
        Mockito.verify(secondRoom, Mockito.times(1)).put();
    }

    @Test
    public void testRemoveUser() {
        //Given
        thirdRoom.addUserSession(userSession1, "user1");
        thirdRoom.addUserSession(userSession2, "user2");

        secondRoom.addUserSession(userSession1, "user1");
        secondRoom.addUserSession(userSession2, "user2");
        //When
        secondRoom.removeSession(userSession1);
        thirdRoom.removeSession(userSession2);
        //Then
        Assert.assertFalse(secondRoom.hasUser("user1"));
        Assert.assertTrue(room.hasUser("user1"));

        Assert.assertTrue(secondRoom.hasUser("user2"));
        Assert.assertTrue(!thirdRoom.hasUser("user2"));
        Assert.assertTrue(room.hasUser("user2"));
    }

    @Test
    public void testRemoveUserFromAllRooms() {
        //Given
        thirdRoom.addUserSession(userSession3, "user3");
        thirdRoom.addUserSession(userSession2, "user2");

        secondRoom.addUserSession(userSession1, "user1");
        secondRoom.addUserSession(userSession2, "user2");
        //When
        Room.removeUserFromAllRoom("user1");
        //Then
        Assert.assertTrue(!thirdRoom.hasUser("user1"));

    }
}

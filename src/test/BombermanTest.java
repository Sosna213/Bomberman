package test;


import Bomberman.Player;
import Bomberman.client.GameClient;
import Bomberman.exceptions.GameException;
import Bomberman.server.GameServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;


public class BombermanTest {

    @Test
    public void addPlayer_TooManyPlayers_throwsGameException()
    {
        //given
        Player p1 =new Player("p1");
        Player p2 =new Player("p2");
        Player p3 =new Player("p3");
        Player p4 =new Player("p4");
        Player p5 =new Player("p5");

        GameServer gameServer = new GameServer(10,10);
        gameServer.playerList.add(p1);
        gameServer.playerList.add(p2);
        gameServer.playerList.add(p3);
        gameServer.playerList.add(p4);

        //when
        //then
        Assertions.assertThrows(GameException.class,()-> gameServer.addPlayer(p5));
    }

    @Test
    public void connectServer_serverNotAvailable_throwsNotBoundException()
    {
        //given
        GameClient client = new GameClient();

        //when
        //then
        Assertions.assertThrows(NotBoundException.class,()-> client.connectServer());
    }

}

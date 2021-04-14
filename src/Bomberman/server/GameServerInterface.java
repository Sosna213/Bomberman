package Bomberman.server;

import Bomberman.Grid;
import Bomberman.client.GameClientInterface;
import Bomberman.Player;
import Bomberman.exceptions.GameException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameServerInterface extends Remote {

    void addClient(GameClientInterface client, String playerName) throws RemoteException, NotBoundException;
    void addPlayer(Player player) throws RemoteException, NotBoundException, GameException, AlreadyBoundException;
    Grid getGrid() throws RemoteException, NotBoundException;
    ArrayList<Player> getPlayerList() throws RemoteException, NotBoundException;
    Player getPlayer(String name) throws RemoteException, NotBoundException;
    void playerLeave(String playerName) throws RemoteException, NotBoundException;
    void move(String playerName, int keyIndex) throws RemoteException, NotBoundException;
    void placeBomb(String playerNam) throws RemoteException, NotBoundException;
    void restartGame() throws RemoteException, NotBoundException;
}
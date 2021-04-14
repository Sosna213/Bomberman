package Bomberman.client;

import Bomberman.Explosion;
import Bomberman.Grid;
import Bomberman.Player;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameClientInterface extends Remote {
    void updateGrid(Grid gameGrid, ArrayList<Player> players) throws RemoteException, NotBoundException;
    void die() throws  RemoteException, NotBoundException;
    void drawExplosion(Explosion explosion) throws  RemoteException, NotBoundException;
    void win() throws  RemoteException, NotBoundException;
}

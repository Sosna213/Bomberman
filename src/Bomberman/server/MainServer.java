package Bomberman.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

public class MainServer {
  public static void main(String[] args) throws RemoteException {

    LocateRegistry.createRegistry(1243);
    GameServer gameServer = new GameServer(15, 13);
    GameServerInterface stub = (GameServerInterface) UnicastRemoteObject.exportObject(gameServer, 1243);
    RemoteServer.setLog(System.out);
    Registry registry = LocateRegistry.getRegistry("localhost", 1243);
    registry.rebind("client", stub);
    System.out.println("Server is available");
  }
}
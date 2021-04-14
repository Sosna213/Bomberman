package Bomberman.client;

import Bomberman.*;
import Bomberman.exceptions.GameException;
import Bomberman.server.GameServerInterface;
import processing.core.PApplet;
import processing.core.PImage;


import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class GameClient extends PApplet implements GameClientInterface, Serializable {
  String serverHost = "127.0.0.1";
  int serverPort = 1243;
  String ownIP = "localhost";
  Random r = new Random();
  int ownPort = r.nextInt(1000)+9000;
  int rColor = r.nextInt(256);
  int gColor = r.nextInt(256);
  int bColor = r.nextInt(256);
  private int playerSize = 0;
  private String playerName = "";
  private Player player;
  private boolean win = false;
  private int fieldSize = 70;
  private Grid gameGrid;
  private GameServerInterface gameServer;
  private ArrayList<Player> players;
  private ArrayList<Explosion> explosions = new ArrayList<>();
  private int flag = 0;
  private int menuPos = 0;
  private  ArrayList<Character> playerN = new ArrayList<Character>();
  private int playerNCount = 0;
  private PImage menuBack;

  public GameClient()  {

  }

  public void connectServer() throws RemoteException, NotBoundException
  {
      LocateRegistry.createRegistry(ownPort);
      GameClientInterface clientStub = (GameClientInterface) UnicastRemoteObject.exportObject(this, ownPort);


      Registry localRegistry = LocateRegistry.getRegistry(ownIP, ownPort);
      localRegistry.rebind("client", clientStub);

      Registry serverRegistry = LocateRegistry.getRegistry(serverHost, serverPort);
      try {
          this.gameServer = (GameServerInterface) serverRegistry.lookup("client");
      } catch (ConnectException e) {
          System.out.println("Server not available");
          throw new NotBoundException("Server not available");
      }

      this.gameServer.addClient(this, this.playerName);
      this.gameGrid = gameServer.getGrid();
      this.players = gameServer.getPlayerList();
      this.playerSize = players.size();

      player = new Player(playerName);
      try {
          this.gameServer.addPlayer(player);
      } catch (GameException e) {
          System.out.println("Game is full already");
          e.printStackTrace();
      } catch (AlreadyBoundException e) {
          System.out.println("Player name already in use");
          e.printStackTrace();
      }
  }

  public void settings() {
      size(15 * fieldSize, 13 * fieldSize);
  }

  public void setup() {
      menuBack = loadImage("assets/backmenu.jpg");
    background(menuBack);
  }

  public void draw() {
      if (flag == 0) {
          background(menuBack);
          fill(0,0,240);
          circle(380,430 + menuPos*100,30);
          rect(425, 400, 200, 60,7);
          rect(425, 500, 200, 60,7);
          fill(0,0,240);
          textAlign(CENTER);
          textSize(60);
          fill(230);
          textSize(20);
          text("Multiplayer",525,430);
          text("Wyjscie",525,530);
      }
      else if (flag == 1)
      {
          if(playerNCount==0)
          {
              background(menuBack);
              fill(0,0,102);
              text("Wpisz nazwe:",300,500);
          }
          else
          {
              background(menuBack);
              textSize(20);
              fill(0,0,102);
              text("Wpisz nazwe:",300,500);
              for(int i=0;i<playerN.size();i++) {
                  text(playerN.get(i), 400 + i * 15, 500);
              }
          }

      }
      else
      {
          background(200);
      color(0);
      int rowNumber = 0;
      int playerCount = 0;
      for (Field[] row : gameGrid.getGrid()) {
          int fieldNumber = 0;
          for (Field field : row) {
              if (field.type.equals("wall")) {
                  fill(0);
              }
              if (field.type.equals("rock")) {
                  fill(150, 100, 50);
              }
              // Field
              rect(fieldNumber * fieldSize, rowNumber * fieldSize, fieldSize, fieldSize);
              noFill();
              if (field.bomb != null) {
                  // Bomb
                  fill(r.nextInt(100));
                  ellipse(fieldNumber * fieldSize + 35, rowNumber * fieldSize + 35, 40, 40);
                  noFill();
              }
              fieldNumber++;
          }
          rowNumber++;
      }

      // player
      color(rColor, gColor, bColor);
      fill(0, 180, 180);
      strokeWeight(0);
      for (Player player : players) {
          if (player == this.player) {
              fill(0, 180, 0);
          }
          if (player.alive) {
              ellipse(player.posX * fieldSize, player.posY * fieldSize, 30, 30);
              fill(0);
              textAlign(CENTER);
              text(players.get(playerCount).name, player.posX * fieldSize, player.posY * fieldSize);
              playerCount++;
              fill(0, 180, 0);
          }
          if (player == this.player) {
              //fill(0, 0, 200);
          }
      }

      // explosions
      fill(255, 80, 0);
      synchronized (explosions) {
          Explosion toDelete = null;
          for (Explosion explosion : this.explosions) {
              for (Coordinates cood : explosion.coordinatesList) {
                  rect(cood.x * fieldSize, cood.y * fieldSize, fieldSize, fieldSize);
              }
              explosion.increaseFrame();
              if (explosion.over()) {
                  toDelete = explosion;
              }
          }
          explosions.remove(toDelete);
      }
      if (win) {
          background(50);
          textAlign(CENTER);
          textSize(40);
          fill(0);
          text("You won !!!", this.gameGrid.getSizeX() * fieldSize / 2, this.gameGrid.getSizeY() * fieldSize / 2 - 100);
          text("Wcisnij ESC aby wyjsc", this.gameGrid.getSizeX() * fieldSize / 2, this.gameGrid.getSizeY() * fieldSize / 2 );
//
      } else if (!this.player.alive) {
          background(50);
          textAlign(CENTER);
          textSize(40);
          fill(0);
          text("Game Over", this.gameGrid.getSizeX() * fieldSize / 2, this.gameGrid.getSizeY() * fieldSize / 2 - 100);
          text("Wcisnij ESC aby wyjsc", this.gameGrid.getSizeX() * fieldSize / 2, this.gameGrid.getSizeY() * fieldSize / 2 );
      }
  }
  }

  public void keyPressed() {
      if(flag == 0)
      {
          if (key == 'w') {
              if(menuPos>0)
              {
                  menuPos--;
              }

          } else if (key == 's') {
              if(menuPos<1)
              {
                  menuPos++;
              }

          }
          else if(key == 32 || key == ENTER)
          {
              if (menuPos == 0)
              {
                  flag = 1;
              }

              if(menuPos == 1)
              {
                  this.exit();
              }

          }
      }
      else if(flag == 1)
      {
          if(key >=30 && key <=122)
          {
              playerNCount++;
              playerN.add((Character) key);
          }
          else if(key == BACKSPACE && playerNCount > 0)
          {
              playerN.remove(playerN.size()-1);
              playerNCount--;
          }
          else if(key == ENTER)
          {
              StringBuilder sb = new StringBuilder();
              for(Character ch: playerN){
                  sb.append(ch);
              }
              this.playerName = sb.toString();
              flag = 3;
              try{
                  connectServer();
              }
              catch (RemoteException | NotBoundException e){
                  System.out.println(e);
              }

          }

      }
      else {
          if (!this.player.alive) {
              if(key == ESC)
              {
                  exit();
              }
          }
          int keyIndex = -1;
          if (key == ' ') {
              keyIndex = 1;
          } else if (key == 'w') {
              keyIndex = key - 'w';
          } else if (key == 'a') {
              keyIndex = key - 'a';
          } else if (key == 's') {
              keyIndex = key - 's';
          } else if (key == 'd') {
              keyIndex = key - 'd';
          }
          if (keyIndex == -1) {
          } else if (keyIndex == 0) {
              try {
                  gameServer.move(playerName, key);
              } catch (RemoteException e) {
                  e.printStackTrace();
              } catch (NotBoundException e) {
                  e.printStackTrace();
              }
          } else if (keyIndex == 1) {
              try {
                  gameServer.placeBomb(playerName);
              } catch (RemoteException e) {
                  e.printStackTrace();
              } catch (NotBoundException e) {
                  e.printStackTrace();
              }
          }
      }
  }

  public void exit() {
      if (flag != 0&& flag!=1) {
          try {
              gameServer.playerLeave(playerName);
          } catch (RemoteException e) {
              e.printStackTrace();
          } catch (NotBoundException e) {
              e.printStackTrace();
          }
          super.exit();
      }
      else
          super.exit();
  }

  @Override
  public void updateGrid(Grid gameGrid, ArrayList<Player> players){
    this.gameGrid = gameGrid;
    this.players = players;
    System.out.println("Client updated");
  }

  @Override
  public void die(){
    this.player.alive = false;
  }

  @Override
  public void drawExplosion(Explosion explosion){
    synchronized (explosions) {
      this.explosions.add(explosion);
    }
  }

  @Override
  public void win(){
    this.win = true;
  }
}

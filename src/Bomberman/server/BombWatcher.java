package Bomberman.server;

import java.util.concurrent.DelayQueue;

public class BombWatcher implements Runnable {
    DelayQueue<DelayedBomb> delayedBombs;
    GameServer gameServerReference;

    public BombWatcher(DelayQueue<DelayedBomb> delayedBombs, GameServer gameServer) {
        this.delayedBombs = delayedBombs;
        this.gameServerReference = gameServer;
    }

    @Override
    public void run() {
        DelayedBomb bomb = null;
        while (true) {
            try {
                bomb = delayedBombs.take();
                gameServerReference.explodeBomb(bomb);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
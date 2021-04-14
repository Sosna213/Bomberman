package Bomberman.server;

import Bomberman.Bomb;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedBomb implements Delayed {
  private final long delayTime;
  private long expireTimeMillis;
  public final Bomb bomb;
  public int x;
  public int y;

  public DelayedBomb(float x, float y, long delayTime, Bomb bomb){
    this.x = (int)x;
    this.y = (int)y;
    this.delayTime=delayTime;
    this.expireTimeMillis = System.currentTimeMillis()+delayTime;
    this.bomb = bomb;
  }

  @Override
  public long getDelay(TimeUnit unit) {
    long delayMillis = expireTimeMillis-System.currentTimeMillis();
    return unit.convert(delayMillis,TimeUnit.MILLISECONDS);
  }

  @Override
  public int compareTo(Delayed o) {
    long diffMillis = getDelay(TimeUnit.MILLISECONDS)-o.getDelay(TimeUnit.MILLISECONDS);
    diffMillis = Math.min(diffMillis,1);
    diffMillis = Math.max(diffMillis,-1);
    return (int) diffMillis;
  }

  public void explodeNow() {
    this.expireTimeMillis = System.currentTimeMillis();
  }
}


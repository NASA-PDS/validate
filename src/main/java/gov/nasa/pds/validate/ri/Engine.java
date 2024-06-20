package gov.nasa.pds.validate.ri;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Engine implements CamShaft {
  private final int cylinders;
  private long broken, total = 0;
  private final AuthInformation registry;
  private final AuthInformation search;
  private final Queue<String> queue = new ArrayDeque<String>();
  private final ArrayList<Cylinder> workers = new ArrayList<Cylinder>();
  private final Logger log = LogManager.getLogger(Engine.class);

  public Engine(int cylinders, List<String> lidvids, AuthInformation registry,
      AuthInformation search) {
    this.cylinders = cylinders;
    this.queue.addAll(lidvids);
    this.registry = registry;
    this.search = search;
    this.total = this.queue.size();
  }

  @Override
  public void addAll(List<String> lidvids) {
    this.log.info("Adding " + lidvids.size() + " references to be processed.");
    synchronized (this.queue) {
      this.total += lidvids.size();
      this.queue.addAll(lidvids);
      this.queue.notifyAll();
    }
  }

  public long getBroken() {
    return this.broken;
  }

  public long getTotal() {
    return this.total;
  }

  @Override
  public void replace(Cylinder cylinder) {
    synchronized (this.workers) {
      this.broken += cylinder.getBroken();
      this.workers.remove(cylinder);
      this.workers.notifyAll();
    }
  }

  public void processQueueUntilEmpty() {
    int inQueue = this.queue.size(), atWork = this.workers.size();
    ArrayList<Cylinder> applicants = new ArrayList<Cylinder>();
    String lidvid;

    while (inQueue > 0 || atWork > 0) {
      this.log.info(
          "Have " + inQueue + " lidvids to check with " + atWork + " threads to get them done.");
      for (int i = 0; i < inQueue && i + atWork < this.cylinders; i++) {
        synchronized (this.queue) {
          lidvid = this.queue.remove();
        }
        applicants.add(new Cylinder(lidvid, this.registry, this.search, this));
      }

      if (this.cylinders == 1) {
        for (Cylinder cylinder : applicants)
          cylinder.run();
      } else {
        synchronized (this.workers) {
          for (Cylinder cylinder : applicants) {
            Thread t = new Thread(cylinder);
            this.workers.add(cylinder);
            t.start();
          }
        }
      }
      applicants.clear();

      synchronized (this.workers) {
        if (!this.workers.isEmpty())
          try {
            this.workers.wait();
          } catch (InterruptedException e) {
            log.trace(
                "This should never happen and means there is one or more workers stuck in the abyss",
                e);
          }
      }
      synchronized (this.queue) {
        inQueue = this.queue.size();
      }
      synchronized (this.workers) {
        atWork = this.workers.size();
      }
    }
  }
}

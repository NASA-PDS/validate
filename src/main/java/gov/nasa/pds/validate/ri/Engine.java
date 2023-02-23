package gov.nasa.pds.validate.ri;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Engine implements CamShaft {
  private final int cylinders;
  private final AuthInformation registry;
  private final AuthInformation search;
  private final Queue<String> queue = new LinkedList<String>();
  private final ArrayList<Cylinder> workers = new ArrayList<Cylinder>();
  private final Logger log = LogManager.getLogger(Engine.class);

  public Engine (int cylinders, List<String> lidvids, AuthInformation registry, AuthInformation search) {
    this.cylinders = cylinders;
    this.queue.addAll(lidvids);
    this.registry = registry;
    this.search = search;
  }

  @Override
  public void addAll (List<String> lidvids) {
    synchronized (this.queue) {
      this.queue.addAll(lidvids);
    }
  }
  
  @Override
  public void replace (Cylinder cylinder) {
    synchronized (this.workers) {
      this.workers.remove(cylinder);
    }
  }

  public void processQueueUntilEmpty() {
    int inQueue = this.queue.size(), atWork = this.workers.size();
    ArrayList<Cylinder> applicants = new ArrayList<Cylinder>();
    String lidvid;

    while (inQueue > 0 || atWork > 0) {
      for (int i=0 ; i < inQueue && i + atWork < this.cylinders ; i++) {
        synchronized (this.queue) { lidvid = this.queue.remove(); }
        applicants.add(new Cylinder(lidvid, this.registry, this.search, this));
      }

      if (this.cylinders == 1) {
        for (Cylinder cylinder : applicants) cylinder.run();
      } else {
        synchronized (this.workers) {
          for (Cylinder cylinder : applicants) {
            Thread t = new Thread(cylinder);
            this.workers.add(cylinder);
            t.start();
          }
        }
      }

      synchronized (this.workers) {
        if (!this.workers.isEmpty())
          try {
            this.workers.wait();
          } catch (InterruptedException e) {
            log.trace("This should never happen and means there is one or more workers stuck in teh abyss", e);
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

/* Semaphore.java */

package eduni.simjava;

/**
 * A counting semaphore class.
 * 用Java实现的一个信号量机制。信号量，pv操作的那个。
 * p一个资源，资源减少，到0就阻塞。v一个资源，资源增加，同时唤醒其他阻塞的线程。
 * <p>
 * This class is used internally by <code>Sim_system</code> to
 * synchronize the simulation's entities. Semaphores should not be
 * needed in user simulations.
 * @see		eduni.simjava.Sim_system
 * @version     1.0, 4 September 1996
 * @author      Ross McNab
 */

public class Semaphore {
  private int count;

  // Constructors
  /**
   * Allocate a new semaphore object with an initial count of zero.
   */
  public Semaphore() {
    count = 1;
  }

  /**
   * Allocates a new semaphore object with a given initial count.
   * @param count	The initial count of the semaphore.
   */
  public Semaphore(int count) {
    this.count = count;
  }

  /**
   * Try to obtain the semaphore. If the count is above zero, the
   * function decrements it, then return so the calling thread can
   * continue. If the count is zero then the calling thread is suspended
   * until it becomes non-zero.
   */
  public synchronized void p() {
    while (count == 0) {
      try { wait(); } catch (InterruptedException e) { }
    }
    count--;
  }

  /**
   * Free the semaphore, by incrementing the internal count.
   */
  public synchronized void v() {
    count++;
    notify();
  }
}

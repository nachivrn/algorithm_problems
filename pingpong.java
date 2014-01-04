import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PingPongLock {
	private final int MAX = 10;
	private volatile int counter = 0;
	private Lock lock = new ReentrantLock();
	private Condition pinged;
	private Condition ponged;

	public PingPongLock() {
		pinged = lock.newCondition();
		ponged = lock.newCondition();
	}

	public void ping() {
		try {
			lock.lock();
			counter++;
			System.out.println("Ping");
			pinged.signalAll();
			if (counter < MAX)
				ponged.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void pong() {
		try {
			lock.lock();
			counter++;
			System.out.println("Pong");
			ponged.signalAll();
			if (counter < MAX)
				pinged.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		final PingPongLock pingpong = new PingPongLock();
		new Thread() {
			public void run() {
				while (pingpong.counter < pingpong.MAX)
					pingpong.ping();
			}
		}.start();
		new Thread() {
			public void run() {
				while (pingpong.counter < pingpong.MAX)
					pingpong.pong();
			}
		}.start();
	}
}


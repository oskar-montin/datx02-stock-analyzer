package program;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is a class representing progress of a processess in our collector. This class 
 * is a monitor and can be used for progress bars in the gui. Whenever a thread tries to 
 * get info from the class it have to wait untill the monitor is updated.
 * @author oskar-montin
 *
 */
public class ProgressInfo {
	private String message;
	private int value;
	private Lock lock;
	private Condition upToDate;
	private boolean updated;
	
	public ProgressInfo() {
		this.setMessage(new String(""));
		this.setValue(0);
		lock = new ReentrantLock();
		upToDate = lock.newCondition();
	}

	public int getValue() {
		try {
			this.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public void setValue(int value) {
		lock.lock();
		try{
			this.value = value;
			upToDate.signal();
		}finally{
			lock.unlock();
		}
	}

	public String getMessage() {
		try {
			this.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	private void await() throws InterruptedException {
		lock.lock();
		try{
			while(!updated) {
				upToDate.await();
			}
		} finally {
			lock.unlock();
		}
	}

	public void setMessage(String message) {
		lock.lock();
		try{
			this.message = message;
			upToDate.signal();
		} finally {
			lock.unlock();
		}
	}
}
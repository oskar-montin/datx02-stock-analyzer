package program;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is a class representing progress of a processess in our collector. This class 
 * is a monitor and can be used for progress bars in the gui. Whenever a thread tries to 
 * get info from the class it have to wait untill the monitor is updated.
 * 
 * This class can easily be changed to use an observer pattern instead of the lock pattern 
 * if we want that behaviour in the gui.
 * @author oskar-montin
 *
 */
public class ProgressInfo {
	private String message;
	private int value;
	private Lock lock;
	private Condition valueChange, messageChange;
	private boolean valueUpdated,messageUpdated;
	
	public ProgressInfo() {
		this.setMessage(new String(""));
		this.setValue(0);
		lock = new ReentrantLock();
		valueUpdated = true;
		messageUpdated = true;
		valueChange = lock.newCondition();
		messageChange = lock.newCondition();
	}

	/**
	 * 
	 * @return the current value of the progress.
	 */
	public int getValue() {
		lock.lock();
		try {
			while(!valueUpdated) {
				valueChange.await();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.valueUpdated = false;
		lock.unlock();
		return value;
	}

	/**
	 * 
	 * @param value the new value to be set
	 */
	public void setValue(int value) {
		lock.lock();
		try{
			this.value = value;
			this.valueUpdated = true;
			valueChange.signal();
		}finally{
			lock.unlock();
		}
	}


	/**
	 * 
	 * @return the current message of the progress
	 */
	public String getMessage() {
		lock.lock();
		try {
			while(!messageUpdated) {
				messageChange.await();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.messageUpdated = false;
		lock.unlock();
		return message;
	}

	/**
	 * 
	 * @param message the new message to be set
	 */
	public void setMessage(String message) {
		lock.lock();
		try{
			this.message = message;
			this.messageUpdated = true;
			messageChange.signal();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Updates the progress with the given value and message
	 * @param value
	 * @param message
	 */
	public void update(int value, String message) {
		lock.lock();
		try{
			this.value = value;
			this.message = message;
			this.valueUpdated = true;
			this.messageUpdated = true;
			valueChange.signal();
			messageChange.signal();
		} finally {
			lock.unlock();
		}
	}
}
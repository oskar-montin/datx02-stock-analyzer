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
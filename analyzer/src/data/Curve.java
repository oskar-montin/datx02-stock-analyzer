package data;

import java.util.PriorityQueue;

public class Curve {
	private final PriorityQueue<SimpleData> queue;
	private final String name;
	
	public Curve(PriorityQueue<SimpleData> queue, String name){
		this.queue = queue;
		this.name = name;
	}

	public PriorityQueue<SimpleData> getQueue() {
		return queue;
	}

	public String getName() {
		return name;
	}
}

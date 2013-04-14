package data;

import java.util.PriorityQueue;

public class Curve {

	private final PriorityQueue<SimpleData> queue;
	private final String name;
	
	// Constructor with SimpleData array input
	public Curve(SimpleData[] data, String name) {
		this.name = name;
		queue = new PriorityQueue<SimpleData>();
		for (int i = 0; i < data.length; i++)
			queue.add(new SimpleData(data[i]));
	}
	
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
	
	@Override
	public String toString() {
		return "Curve [queue=" + queue + ", name=" + name + "]";
	}
}

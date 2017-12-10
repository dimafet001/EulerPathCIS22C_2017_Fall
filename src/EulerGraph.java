import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

/**
 * @author Dongbo Liu, Dolgopolov Dmitry
 *
 * @param <E>
 */

public class EulerGraph<E> extends Graph<E> {

	// undo
	// here we have a Queue of the steps that we can reverse any time
	private Stack<Step<E>> steps = new Stack<>();
	// for not repeating the steps and looping forever
	private boolean justUndid = false;

	// Function to add an edge into the graph
	@Override
	public void addEdge(E source, E dest, double cost) {
		super.addEdge(source, dest, cost);

		// added the new Edge, so later we can undo it
		if (!justUndid) 
			steps.add(new Step<E>(source, dest, cost, Operation.ADDED));
		justUndid = false;
	}

	@Override
	public void addEdge(E source, E dest, int cost) {
		addEdge(source, dest, (int) cost);
	}

	// Function to remove an edge into the graph
	@Override
	public boolean remove(E src, E dest) {
		// removed the new Edge, so later we can undo it
		// adjList.get(dst.data).second;
		
		// in case they are not there, we do not even delete
		if (!vertexSet.containsKey(src) || !vertexSet.containsKey(dest))
			return false;
		
		
		// finding the weight
		double weight = 0;

		Iterator<Entry<E, Pair<Vertex<E>, Double>>> iter = vertexSet.get(src).iterator();

		// iterating thru the adjList of the Vertex
		// so that we can get the weight (the only way)
		while (iter.hasNext()) {
			Entry<E, Pair<Vertex<E>, Double>> entry = iter.next();
			if (entry.getValue().first.data.equals(dest)) {
				weight = entry.getValue().second;
				break;
			}
		}

		if (!justUndid) 
			steps.add(new Step<E>(src, dest, weight, Operation.REMOVED));
		justUndid = false;
		
		
		return super.remove(src, dest);
	}

	// just can undo the last step. Nothing more;
	public boolean undo() {
		// we delete the last step from the Queue
		// at the same time we save it into the object
		// so we can still use it
		if (steps.isEmpty()) return false;
		
		justUndid = true;
		
		Step<E> lastStep = steps.pop();
		System.out.println(lastStep.getSrc().toString() + lastStep.getDst().toString() + lastStep.getWeight());
		
		// if we added we remove and viceversa
		if (lastStep.getOperation().equals(Operation.ADDED))
			return remove(lastStep.getSrc(), lastStep.getDst());
		else if (lastStep.getOperation().equals(Operation.REMOVED)) {
			addEdge(lastStep.getSrc(), lastStep.getDst(), lastStep.getWeight());
		}
		
		return true;

	}

	public void findEulerPath() { // find Euler path using fleury algorithm
		// ToDo: actually put the algorithm in here
	}

	/*
	 * Iterator<Entry<E, Pair<Vertex<E>, Double>>> iter ; Entry<E,
	 * Pair<Vertex<E>, Double>> entry; Pair<Vertex<E>, Double> pair;
	 * 
	 * System.out.print( "Adj List for " + data + ": "); iter =
	 * adjList.entrySet().iterator(); while( iter.hasNext() ) { entry =
	 * iter.next(); pair = entry.getValue(); System.out.print( pair.first.data +
	 * "(" + String.format("%3.1f", pair.second) + ") " ); }
	 * System.out.println();
	 */

	private Map<Integer, String> indexToName;

	public void setIndexToName(Map<Integer, String> map) {
		indexToName = map;
	}

	enum Operation {
		ADDED, REMOVED
	}

	// a struct to save the steps to the Queue and reverse them
	class Step<E> {

		private E src, dst;
		private double weight;
		// // when we are in the start and there was no operation
		// // when we added
		// // when we removed

		private Operation oper;

		Step(E src, E dst, double weight, Operation oper) {
			this.src = src;
			this.dst = dst;
			this.weight = weight;
			this.oper = oper;
		}

		public E getSrc() {
			return src;
		}

		public void setSrc(E src) {
			this.src = src;
		}

		public E getDst() {
			return dst;
		}

		public void setDst(E dst) {
			this.dst = dst;
		}

		public double getWeight() {
			return weight;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		public Operation getOperation() {
			return oper;
		}

		public void setOperation(Operation oper) {
			this.oper = oper;
		}

		@Override
		public String toString() {
			return "the previous step: [src=" + src + ", dst=" + dst + ", weight=" + weight + ", oper=" + oper + "]";
		}

	}

}

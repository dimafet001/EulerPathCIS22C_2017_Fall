import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import graphFiles.Graph;
import graphFiles.LinkedQueue;

// author Dongbo Liu + Dolgopolov Dmitry 

public class EulerGraph<E> extends Graph {
	// ToDo: instead, use the parent class

	// undo part -----------------

	// here we have a Queue of the steps that we can reverse any time
	LinkedQueue<Step> steps;

	

	// private operation lastOperation = operation.NONE;
	// private int lastV, lastW; // last vertices we worked with
	// undo part end -------------

	public EulerGraph() {
		super();
		
		steps = new LinkedQueue<>();
	}

	// Function to add an edge into the graph
	@Override
	public void addEdge(Object source, Object dest, int cost) {
		super.addEdge(source, dest, cost);

		// added the new Edge, so later we can undo it
		steps.enqueue(new Step(source, dest, Operation.ADDED));
		
	}

	// Function to remove an edge into the graph
	@Override
	public boolean remove(Object src, Object dest) {
		// removed the new Edge, so later we can undo it
		steps.enqueue(new Step(src, dest, Operation.ADDED));

		return super.remove(src, dest);
	}


	// Check grpah is an Eulerian path
	boolean isEulerian() {
		// Check if all non-zero degree vertices are connected
		if (isConnected() == false) {
			return false;
		}
		// Count vertices with odd degree
		int oddDegree = 0;
		for (int i = 0; i < numberOfVertices; i++) {
			if (adjacencyMatrix[i].size() % 2 != 0) {
				oddDegree++;
			}
		}
		// If count is more than 2, then graph is not Eulerian
		if (oddDegree > 2) {
			return false;
		} else {
			return true;
		}
	}

	public void findEulerPath() { // find Euler path using fleury algorithm

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

	public void printIndexToName() {

		for (int i = 0; i < numberOfVertices; i++) {
			System.out.println("Index " + i + ": " + indexToName.get(i));
		}
	}

	enum Operation {
		 ADDED, REMOVED
	}
	
	// a struct to save the steps to the Queue and reverse them
	class Step<E> {

		private E src, dst;
		
		// // when we are in the start and there was no operation
		// // when we added
		// // when we removed
		 
		 
		 private Operation oper;

		Step(E src, E dst, Operation oper) {
			this.src = src;
			this.dst = dst;
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

		public Operation getOperation() {
			return oper;
		}

		public void setOperation(Operation oper) {
			this.oper = oper;
		}

		@Override
		public String toString() {
			return "the previous step: [src=" + src + ", dst=" + dst + ", oper=" + oper + "]";
		}
		
	}
}

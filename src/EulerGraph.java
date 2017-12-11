import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;

/**
 * @author Dolgopolov Dmitry, Mher Torjyan, Shiyu Zhang (explaination of method and algorithms)
 * 	Dongbo Liu (the original file for the presentation was made by him)
 *
 * @param <E extends Comparable<E>>
 */

public class EulerGraph<E extends Comparable<E>> extends Graph<E> {

	// undo
	// here we have a Queue of the steps that we can reverse any time
	private Stack<Step<E>> steps = new Stack<>();
	// for not repeating the steps and looping forever
	private boolean justUndid = false;

	/** @author Dmitry Dolgopolov
	* Method Name:addEdge
	 * Parameter:source(E), dest(E), cost(double)
	 * Description: This method adds a edge to the Euler path.
	 * Return Value: none
	 */
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

	/** @author Dmitry Dolgopolov 
	* Method Name:addEdge
	 * Parameter:src(E), dest(E)
	 * Description: This method removes a edge to the Euler path.
	 * Return Value: boolean
	 */
	@Override
	public boolean remove(E src, E dest) {

		// in case they are not there, we do not even delete
		if (!vertexSet.containsKey(src) || !vertexSet.containsKey(dest))
			return false;

		// finding the weight
		double weight = 0;

		Iterator<Entry<E, Pair<Vertex<E>, Double>>> iter = vertexSet.get(src).iterator();

		// iterating thru the adjList of the Vertex
		// so that we can get the weight (the only way)
		while (iter.hasNext()) {
			//Save the current entry 
			Entry<E, Pair<Vertex<E>, Double>> entry = iter.next();
			//If the current Entry is equal save weight of edge.
			if (entry.getValue().first.data.equals(dest)) {
				weight = entry.getValue().second;
				break;
			}
		}
		//Add to steps for Undo
		if (!justUndid)
			steps.add(new Step<E>(src, dest, weight, Operation.REMOVED));
		justUndid = false;
		//Call remove in Remove class
		return super.remove(src, dest);
	}

	/** @author Dmitry Dolgopolov
	 * Method Name: undo
	 * Parameter:none
	 * Description: This method undo the previous action.
	 * Return Value: boolean
	 */
	public boolean undo() {
		// we delete the last step from the Queue
		// at the same time we save it into the object
		// so we can still use it
		if (steps.isEmpty())
			return false;

		justUndid = true;

		Step<E> lastStep = steps.pop();

		// if we added we remove and viceversa
		//if previous mode was add
		if (lastStep.getOperation().equals(Operation.ADDED))
			return remove(lastStep.getSrc(), lastStep.getDst());
		//if previous move was remove
		else if (lastStep.getOperation().equals(Operation.REMOVED)) {
			addEdge(lastStep.getSrc(), lastStep.getDst(), lastStep.getWeight());
		}

		return true;

	}

	/** @author Mher Torjyan
	 * This method returns the Euler path that goes through all
	 * 	edges in form of a String in format "A-B-C-D-B" 
	 *  */
	public String findEulerPath() { // find Euler path using fleury algorithm

		//Count number of odd degree vertecies
		int numOddVertexes = 0;
		//Iterator for Vertexes
		Iterator<Entry<E, Vertex<E>>> iter;
		//To save the start Vertex which HAS to be of odd degree if one exists
		Entry<E, Vertex<E>> startVertex = null;
		iter = vertexSet.entrySet().iterator();
		while (iter.hasNext()) { //While there is another vertex
			//To iterate over the adjency list of the vertex
			Entry<E, Vertex<E>> a = iter.next(); 
			if (a.getValue().adjList.size() % 2 != 0) { //To check if odd degree. 
				numOddVertexes++;
				//set starting vertex
				startVertex = a; 
			}
		}

		//EULERIAN PATH MUST HAVE 0 OR 2 ODD DEGREE VERTECIES TO BE SOLVABLE
		if (numOddVertexes != 0 && numOddVertexes != 2)
			return null;

		//To save Edge
		LList<Pair> adjList = new LList();

		Iterator<Entry<E, Vertex<E>>> newIter = vertexSet.entrySet().iterator();
		while (newIter.hasNext()) {
			
			Entry<E, Vertex<E>> vertex = newIter.next();
			// System.out.println(vertex.getKey());
			Iterator<Entry<E, Pair<Vertex<E>, Double>>> newIter2 = vertex.getValue().iterator();
			while (newIter2.hasNext()) {
				Entry<E, Pair<Vertex<E>, Double>> entry = newIter2.next();
				// System.out.println(entry.getKey());
				//To save only one of the edge from one vertex to another
				if (vertex.getKey().compareTo(entry.getKey()) < 0) {
					//Save as Pair
					Pair toAdd = new Pair(vertex.getKey(), entry.getKey());
					//Add to List
					adjList.add(toAdd);
				}
			}

		}

		//Stack to store the final path
		Stack<E> finalPath = new Stack<>();
		
		return findPath(adjList, startVertex.getKey(), finalPath, adjList.getLength(), false);
	}
	
	/**
	 * @author Mher Torjyan, Dolgopolov Dmitry (20%)
	 * 
	 * This method is recursive Method which computes Euler Path.
	*/
	private String findPath(LList<Pair> input, E curVertex, Stack<E> path, int totalEdgeNumber, boolean isBackTracking) {
		//Final return string
		String resString = "";
		//Temp String to store in between calls before overriding curVertex
		String realCurVertex = "";
		
		//base case
		if (input.getLength() == 0 && path.size() == totalEdgeNumber - 1 ) {
			return curVertex.toString();
		} else {
			for (int i = 1; i < input.getLength() + 1; i++) {
				//If equal to first element of Pair
				if (curVertex.equals(input.getEntry(i).first)) {
					//if backtracking DO NOT update realCurVertex
					if(!isBackTracking) {
						//Save before override
						realCurVertex = curVertex.toString();
					}
					//Update curVertex to new position
					curVertex = (E) input.getEntry(i).second;
					//Add curVertex to the path
					path.push(curVertex);
					//Remove edge
					input.remove(i);
					//String to return 
					resString = realCurVertex + (!isBackTracking ? "-" : "") + findPath(input, curVertex, path, totalEdgeNumber, false);
					return resString;
				} //If equal to second element of Pair
				else if (curVertex.equals(input.getEntry(i).second)) {
					//if backtracking DO NOT update realCurVertex
					if(!isBackTracking) {	
						realCurVertex = curVertex.toString();
						
					}else {
						realCurVertex = "";
					}
					curVertex = (E) input.getEntry(i).first;
					path.push(curVertex);
					input.remove(i);
					resString = realCurVertex + (!isBackTracking ? "-" : "") + findPath(input, curVertex, path, totalEdgeNumber, false);
					return resString;
				}
			}
			// here we hit a dead end
			path.pop();
			//Add back to input list
			input.add(new Pair(curVertex, path.peek()));
			//Set curVertex to Previous
			curVertex = path.pop();
			//Recussive Step
			return findPath(input, curVertex, path, totalEdgeNumber, true) ;
			//return resString;
		}
	}

	enum Operation {
		ADDED, REMOVED
	}

	// a struct to save the steps to the Queue and reverse them
	class Step<E> {

		private E src, dst;
		private double weight;
		 // when we are in the start and there was no operation
		 // when we added
		 // when we removed

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

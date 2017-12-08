import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

import graphFiles.Graph;


// author Dongbo Liu + Dolgopolov Dmitry 

public class EulerGraph extends Graph {
	private int numberOfVertices;
	private LinkedList<Integer> adjacencyMatrix[];
	
	
	// undo part -----------------
	
	// when we are in the start and there was no operation
	// when we added
	// when we removed
	private enum operation {
		NONE, ADDED, REMOVED 
	}
	
	private operation lastOperation = operation.NONE;
	private int lastV, lastW; // last vertices we worked with
	// undo part end -------------
	
	
		
	EulerGraph(int v) {
		numberOfVertices = v;
		adjacencyMatrix = new LinkedList[v];
		for (int i = 0; i < v; ++i) {
			adjacencyMatrix[i] = new LinkedList();
		}
	}

	// Function to add an edge into the graph
	void addEdge(int v, int w) {
		adjacencyMatrix[v].add(w);// Add w to v's list.
		adjacencyMatrix[w].add(v); // The graph is undirected

		// for undoing the last step
		lastV = v;
		lastW = w;
		lastOperation = operation.ADDED;
	}

	// Function to remove an edge into the graph
	void removeEdge(int v, int w) {
		adjacencyMatrix[v].remove(w);
		adjacencyMatrix[w].remove(v);
		
		// for undoing the last step
		lastV = v;
		lastW = w;
		lastOperation = operation.REMOVED;
	}
	
	// just can undo the last step. Nothing more;
	void undo() {
// TODO: implement the stack
		// if we added we remove and viceversa
		if (lastOperation == operation.ADDED) {
			adjacencyMatrix[lastV].remove(lastW-1);
			adjacencyMatrix[lastW].remove(lastV-3);
		} else if (lastOperation == operation.REMOVED) {
			adjacencyMatrix[lastV].add(lastW-1);
			adjacencyMatrix[lastW].add(lastV-3);
		}
		
		lastOperation = operation.NONE; // because we cannot undo more than one step
	}

	// A function used by DFS
	void DFSUtil(int v, boolean visited[]) {
		// Mark the current node as visited
		visited[v] = true;
		// TODO: display the solution and have an option of saving it to the file
		// Recur for all the vertices adjacent to this vertex
		Iterator<Integer> i = adjacencyMatrix[v].listIterator();
		while (i.hasNext()) {
			int n = i.next();
			if (!visited[n])
				DFSUtil(n, visited);
		}
	}

	// For checking if all non-zero degree vertices are
	// connected. It does DFS traversal starting from
	boolean isConnected() {
		// Set all the vertices to be non-visited
		int i;
		boolean visited[] = new boolean[numberOfVertices];
		for (i = 0; i < numberOfVertices; i++) {
			visited[i] = false;
		}
		// Find a vertex with non-zero degree
		for (i = 0; i < numberOfVertices; i++) {
			if (adjacencyMatrix[i].size() != 0) {
				break;
			}
		}
		// If there are no edges in the graph, return true
		if (i == numberOfVertices) {
			return true;
		}
		// Start traversal from a non-zero degree vertex
		DFSUtil(i, visited);

		// Check if all non-zero degree vertices are visited
		for (i = 0; i < numberOfVertices; i++) {
			if (visited[i] == false && adjacencyMatrix[i].size() > 0) {
				return false;
			}
		}// TODO: store the solution to a Stack
		return true;
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
	
	public void findEulerPath() { //find Euler path using fleury algorithm
		
		
	}
	
	// TODO: be able to call it from menu
	public void outputToFile(PrintWriter pw) {
		
		for (int i = 0; i < numberOfVertices; i++) {
			
			pw.println( "Adj List for " + indexToName.get(i) + ": ");
			
			for (int j = 0; j < adjacencyMatrix[i].size(); j++) {
				pw.print( indexToName.get("\t" + adjacencyMatrix[i].get(j)));
			}
		}
	}
	
	public void showAdjTable() {
		for (int i = 0; i < numberOfVertices; i++) {
			
			System.out.println( "Adj List for " + indexToName.get(i) + ": ");
//			System.out.println( "Adj List for " + i + ": ");
			
			for (int j = 0; j < adjacencyMatrix[i].size(); j++) {
				System.out.print( "\n\t" + indexToName.get(adjacencyMatrix[i].get(j)));
//				System.out.print( "\n\t" + adjacencyMatrix[i].get(j));
			} // Bad coz it should be generic
			
			System.out.println();
		}
	}
	
	/* Iterator<Entry<E, Pair<Vertex<E>, Double>>> iter ;
	   Entry<E, Pair<Vertex<E>, Double>> entry;
	   Pair<Vertex<E>, Double> pair;
	
	   System.out.print( "Adj List for " + data + ": ");
	   iter = adjList.entrySet().iterator();
	   while( iter.hasNext() )
	   {
	      entry = iter.next();
	      pair = entry.getValue();
	      System.out.print( pair.first.data + "("
	         + String.format("%3.1f", pair.second)
	         + ") " );
	   }
	   System.out.println();*/
	
	
	
	
	private Map<Integer, String> indexToName;
	
	public void setIndexToName(Map<Integer, String> map) {
		indexToName = map;
	}
	
	public void printIndexToName() {
		
		for (int i = 0; i < numberOfVertices; i++) {
			System.out.println("Index " + i + ": " + indexToName.get(i));	
		}
	}
}

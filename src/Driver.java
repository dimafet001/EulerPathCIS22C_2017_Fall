import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import graphFiles.Graph;

public class Driver {
	public static Scanner userScanner = new Scanner(System.in);
	//Shiyu, Zhang
	public static void main(String[] args)
	 {
		 Scanner fileScanner = openInputFile();
		 EulerGraph/*<String>*/ flightPath;
		 if(fileScanner==null){
			System.out.println("Unable to open the input file, ending program");
			return;
		 } else{
			flightPath = new EulerGraph();//<String>
			fillPath(fileScanner,flightPath); 
		 }
	 }
	 //Shiyu, Zhang
	public static void fillPath(Scanner s,EulerGraph<String> path) {
		while(s.hasNext()){
			String line = s.nextLine();//get next line
			String[] temp = line.split(",");
			path.addEdges(temp[0], temp[1]);
			path.numberOfVertices ++;
		}
		
	}
	//Shiyu, Zhang
	public static Scanner openInputFile() {
		String filename = null;
		Scanner temp = null;
		System.out.print("\nPlease enter the input file name: ");
		filename = userScanner.nextLine();
		File inputFile = new File(filename);
		try{
			temp = new Scanner(inputFile);
		}
		catch(FileNotFoundException e){
			System.out.println(e.getMessage());
			return null;
		}
		return temp;
	}

	public static void main(String[] args) {
		Graph<String> myGraph1 = new Graph<String>();
		
		// read the data and get index map of values
		
		
		myGraph1.addEdge("A", "B");
		myGraph1.addEdge("A", "C", 0);
		myGraph1.addEdge("A", "D", 0);
		myGraph1.addEdge("B", "E", 0);
		myGraph1.addEdge("B", "F", 0);
		myGraph1.addEdge("C", "G", 0);
		myGraph1.addEdge("D", "H", 0);
		myGraph1.addEdge("D", "I", 0);
		myGraph1.addEdge("F", "J", 0);
		myGraph1.addEdge("G", "K", 0);
		myGraph1.addEdge("G", "L", 0);
		myGraph1.addEdge("H", "M", 0);
		myGraph1.addEdge("H", "N", 0);
		myGraph1.addEdge("I", "N", 0);

		myGraph1.showAdjTable();
	}

	public static void readMethod(Scanner s) {
		
		
		
	}
	
}

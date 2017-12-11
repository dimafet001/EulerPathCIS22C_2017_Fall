import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**

 *  -Name of program : Final Project. Euler Path with the Flights Data

      -Programmer's name: Dmitry Dolgopolov (main and EulerGraph work, output), Shiyu Zhang (input), 
 * 			Mher Torjyan (menu and Graph work , output), Dongbo Liu(miscellaneous help)
 * 
      -Current Date: 12/08/17

      -Computer operating system and compiler you are using : MAC OSX

      -Brief description of the program (1-5 sentences) : The program stores the flights in the Graph.
  		It can parse it from a file and output it to the file, too.
  		Also, we can find a EulerPath (if exists) that passes thru
 		all the edges once.
 */

public class Driver {
	

	// this file is the practical EulerGraph usage
	// so we use Strings everywhere here
	public static Scanner userScanner = new Scanner(System.in);
	public static EulerGraph<String> curGraph;
	
	public static void main(String[] args) {
		
		// setting up the Graph object
		curGraph = new EulerGraph<String>();
		fillPath(openInputFile(), curGraph);
		mainMenu(curGraph);
		
	}

	/**
	 * @author Mher Torjyan, Dmitry Dolgopolov
	 * 
	 * This method provided users with a user interface and deal with their commands.
	 */
	public static void mainMenu(EulerGraph a) { // GRAPH IS PLACEHOLDER
		
		int choice = -1;
	
		do {
			System.out.println("Welcome to Euler's Algorithm \n" 
						+ "-1 : Exit.\n" 
						+ "1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)\n"
						+ "2 : Delete Airport from graph.\n" 
						+ "3 : Undo last move.\n" 
						+ "4 : Output.\n"
						+ "5 : Show Eulers Solution.\n" 
						+ "6 : Input Another File.");

			System.out.print("\n\nPlease enter your choice:");

			try {
				choice = Integer.parseInt(userScanner.next()); // try catch
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
				continue; // starting the loop once again
			}
			switch (choice) {
			case 1: // add
				System.out.println("Where is the flight from?");
				String from = userScanner.next();
				System.out.println("Where is the flight to?");
				String to = userScanner.next();
				System.out.println("How far are the two airports?");
				double weight = userScanner.nextDouble();
				
				if (from.equals(to)) {
					System.out.println("You cannot fly from-to one airport.");
					break;
				}
				
				a.addEdge(from, to, weight);
				break;
			case 2: 
				System.out.println("Where are you removing from?");
				String remFrom = userScanner.next();
				System.out.println("Where was the flight to?");
				String remTo = userScanner.next();
				
				if (remFrom.equals(remTo)) {
					System.out.println("Failed. Non-existent edge");
					break;
				}
				if (a.remove(remFrom, remTo)) 
					System.out.println("Successfully removed");
				else
					System.out.println("Failed removing. You probably used non-existent edge");
				break;
			case 3:
				
				if(a.undo())
					System.out.println("Successfully undid last move");
				else
					System.out.println("Could not undo.");
				break;
				
			case 4:
				
				System.out.println("1 : Display to console \n "
								+ "2 : Output the Adjacency List to a File");
				int sc = userScanner.nextInt();
				
				switch (sc) {
				case 1:
					switch (presentOutputChoices(userScanner)) {
					case 1:
						String startVertex = chooseStartingVertex(userScanner);
						if (startVertex == null) {
							System.out.println("This vertex is not in the Graph. Cancelling\n");
							continue;
						}
						a.depthFirstTraversal(startVertex, new Visitor() {
							public void visit(Object obj) {
								System.out.println(obj.toString());
							}
						});
						break;
					case 2:
						startVertex = chooseStartingVertex(userScanner);
						if (startVertex == null) {
							System.out.println("This vertex is not in the Graph. Cancelling\n");
							continue;
						}
						a.breadthFirstTraversal(startVertex, new Visitor() {
							public void visit(Object obj) {
								System.out.println(obj.toString());
							}
						});
						break;
					case 3:
						a.showAdjTable();
						break;
					}
					break;
				case 2:
					// if false, restart it. it didn't work
					if (outputToFile()) 
						System.out.println("Successfully outputted to a file");
					else
						continue;
					break;
				default:
					System.out.println("Not valid input. Canceling");
					continue;
				}

				break;
			case 5:
				System.out.println();
				
				String result = curGraph.findEulerPath();
				if (result == null)
					System.out.println("Graph is not solvable.\n");
				else {
					System.out.println(result);
					System.out.println("Do you want to output the Euler path to a file as well? (Y/N)");
					
					String response = userScanner.next();
					if (response.equals("Y") || response.equals("y")) {
						if (outputStrToFile(result)) 
							System.out.println("See in the file");
						else 
							System.out.println("Couldn't output");
					} else {
						System.out.println("As you wish");
					}
					
				}
				break;
			case 6:
				fillPath(openInputFile(), curGraph); 
				System.out.println("Changed the input file");
				break;
			case -1:
				System.out.println("Exiting");
				System.out.println("Goodbye.");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Input. Try again");
				continue;//we just ask once again
			}
			
		} while (choice != -1);

	}
	
	/** @author Mher Torjyan
	 * Method Name: presentOutputChoice
	 * Parameter: in (Scanner)
	 * Description: This method reads user's choice of how to display the graph.
	 * Return Value: int
	 */
	public static int presentOutputChoices(Scanner in) {
		System.out.println("1 : Output using Depth-First Traversal\n"
				+ "2 : Output using Breadth-First Traversal\n"
						  + "3 : Output Adjacency List");
		
		return in.nextInt();
	}
	
	/** @author Dmitry Dolgopolov
	 *  This method gets the object of a starting vertex and checks if it exists
	 */
	public static String chooseStartingVertex(Scanner in) {

		System.out.print("\nFrom where: ");
		String ret = in.next();
		
		// checking if the vertex is real and exists in the Graph
		if (!curGraph.vertexSet.containsKey(ret))
			return null;
		
		return ret;
	}
	
	/** @author Shiyu Zhang, Dongbo Liu 
	 *	This method parses the input files into Graph instances
	 */
	static void fillPath(Scanner s, EulerGraph path) {
		
		// clearing the variables before using (always do ;) )
		path.vertexSet.clear();
		
		// filling it up
		while(s.hasNext()) {
			String[] ar = s.nextLine().split(",");
			path.addEdge(ar[0], ar[1], Double.parseDouble(ar[2]));
		}
	}

	/** @author Shiyu Zhang, Dmitry Dolgopolov
	* Method Name: openInputFile
	 * Parameter: none
	 * Description: This method asks users to enter a input filename and check whether open the file successfully.
	 * Return Value: Scanner
	*/
	public static Scanner openInputFile() {

		String filename = null;
		Scanner temp = null;

		System.out.println("\nPlease enter the input file name: (Ex. input1.txt)");

		filename = userScanner.next();
		
		File inputFile = new File(filename);
		try {
			temp = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return temp;
	}
	
	/** @author Dmitry Dolgopolov
	 * 	
	 * 	This method outputs to an existing file or creates one and outputs there
	 * 
	 *  @return True if the output is successful. Otherwise False
	 */ 	
	public static boolean outputToFile(){
		//output adjTable to a file (new or old)
		
		// Trying to open the file
		PrintWriter pw = null;
		try {
			//"/Users/dimafet/Documents/workspace/EulerPathCIS22C_2017_Fall/output.txt"
			System.out.println("Where do you want to output it? (Ex. output.txt [is a file in the project])");
			pw = new PrintWriter(userScanner.next());
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found. Canceling");
			return false;
		}
		
		if (pw == null) return false; 
		
		// outputting to file
		curGraph.outputToFile(pw);
		
		return true;
	}
	
	/** author Dmitry Dolgopolov */
	public static boolean outputStrToFile(String str){
		
		// Trying to open the file
		PrintWriter pw = null;
		try {
			System.out.println("Where do you want to output it? (Ex. output.txt [is a file in the project])");
			pw = new PrintWriter(userScanner.next());
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found. Canceling");
			return false;
		}
		
		if (pw == null) return false; 
		
		// outputting to file
		pw.println(str);
		pw.close();
		
		return true;
	}

}

/*

Please enter the input file name: (Ex. input1.txt)
input1.txt
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
3
------------------------ 
Adj List for PHX: SLC(12.0) MSP(24.0) 
Adj List for LAX: SJC(8.0) SFO(12.0) 
Adj List for DCA: DTW(9.0) ATL(12.0) 
Adj List for DTW: DCA(9.0) YYZ(5.0) 
Adj List for BOS: PBI(30.0) SFO(36.0) 
Adj List for SAT: ATL(13.0) MSP(23.0) 
Adj List for SLC: PHX(12.0) JFK(30.0) 
Adj List for MSP: PHX(24.0) SAT(23.0) 
Adj List for SJC: LAX(8.0) 
Adj List for JFK: PBI(35.0) SLC(30.0) 
Adj List for CVG: YYZ(13.0) LAS(29.0) 
Adj List for PBI: BOS(30.0) JFK(35.0) 
Adj List for ATL: DCA(12.0) SAT(13.0) 
Adj List for YYZ: CVG(13.0) DTW(5.0) 
Adj List for SFO: LAX(12.0) BOS(36.0) 
Adj List for LAS: CVG(29.0) 

Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:1
Where is the flight from?
SFO
Where is the flight to?
POR
How far are the two airports?
16
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
3
------------------------ 
Adj List for PHX: SLC(12.0) MSP(24.0) 
Adj List for LAX: SJC(8.0) SFO(12.0) 
Adj List for DCA: DTW(9.0) ATL(12.0) 
Adj List for DTW: DCA(9.0) YYZ(5.0) 
Adj List for BOS: PBI(30.0) SFO(36.0) 
Adj List for SAT: ATL(13.0) MSP(23.0) 
Adj List for SLC: PHX(12.0) JFK(30.0) 
Adj List for MSP: PHX(24.0) SAT(23.0) 
Adj List for SJC: LAX(8.0) 
Adj List for JFK: PBI(35.0) SLC(30.0) 
Adj List for POR: SFO(16.0) 
Adj List for CVG: YYZ(13.0) LAS(29.0) 
Adj List for PBI: BOS(30.0) JFK(35.0) 
Adj List for ATL: DCA(12.0) SAT(13.0) 
Adj List for YYZ: CVG(13.0) DTW(5.0) 
Adj List for SFO: LAX(12.0) POR(16.0) BOS(36.0) 
Adj List for LAS: CVG(29.0) 

Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:2
Where are you removing from?
YYZ
Where was the flight to?
CVG
Successfully removed
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
3
------------------------ 
Adj List for PHX: SLC(12.0) MSP(24.0) 
Adj List for LAX: SJC(8.0) SFO(12.0) 
Adj List for DCA: DTW(9.0) ATL(12.0) 
Adj List for DTW: DCA(9.0) YYZ(5.0) 
Adj List for BOS: PBI(30.0) SFO(36.0) 
Adj List for SAT: ATL(13.0) MSP(23.0) 
Adj List for SLC: PHX(12.0) JFK(30.0) 
Adj List for MSP: PHX(24.0) SAT(23.0) 
Adj List for SJC: LAX(8.0) 
Adj List for JFK: PBI(35.0) SLC(30.0) 
Adj List for POR: SFO(16.0) 
Adj List for CVG: LAS(29.0) 
Adj List for PBI: BOS(30.0) JFK(35.0) 
Adj List for ATL: DCA(12.0) SAT(13.0) 
Adj List for YYZ: DTW(5.0) 
Adj List for SFO: LAX(12.0) POR(16.0) BOS(36.0) 
Adj List for LAS: CVG(29.0) 

Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:3
Successfully undid last move
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
3
------------------------ 
Adj List for PHX: SLC(12.0) MSP(24.0) 
Adj List for LAX: SJC(8.0) SFO(12.0) 
Adj List for DCA: DTW(9.0) ATL(12.0) 
Adj List for DTW: DCA(9.0) YYZ(5.0) 
Adj List for BOS: PBI(30.0) SFO(36.0) 
Adj List for SAT: ATL(13.0) MSP(23.0) 
Adj List for SLC: PHX(12.0) JFK(30.0) 
Adj List for MSP: PHX(24.0) SAT(23.0) 
Adj List for SJC: LAX(8.0) 
Adj List for JFK: PBI(35.0) SLC(30.0) 
Adj List for POR: SFO(16.0) 
Adj List for CVG: YYZ(13.0) LAS(29.0) 
Adj List for PBI: BOS(30.0) JFK(35.0) 
Adj List for ATL: DCA(12.0) SAT(13.0) 
Adj List for YYZ: CVG(13.0) DTW(5.0) 
Adj List for SFO: LAX(12.0) POR(16.0) BOS(36.0) 
Adj List for LAS: CVG(29.0) 

Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:3
Successfully undid last move
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
3
------------------------ 
Adj List for PHX: SLC(12.0) MSP(24.0) 
Adj List for LAX: SJC(8.0) SFO(12.0) 
Adj List for DCA: DTW(9.0) ATL(12.0) 
Adj List for DTW: DCA(9.0) YYZ(5.0) 
Adj List for BOS: PBI(30.0) SFO(36.0) 
Adj List for SAT: ATL(13.0) MSP(23.0) 
Adj List for SLC: PHX(12.0) JFK(30.0) 
Adj List for MSP: PHX(24.0) SAT(23.0) 
Adj List for SJC: LAX(8.0) 
Adj List for JFK: PBI(35.0) SLC(30.0) 
Adj List for POR: 
Adj List for CVG: YYZ(13.0) LAS(29.0) 
Adj List for PBI: BOS(30.0) JFK(35.0) 
Adj List for ATL: DCA(12.0) SAT(13.0) 
Adj List for YYZ: CVG(13.0) DTW(5.0) 
Adj List for SFO: LAX(12.0) BOS(36.0) 
Adj List for LAS: CVG(29.0) 

Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:1
Where is the flight from?
LAS
Where is the flight to?
AXY
How far are the two airports?
90
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
1

From where: SFO
Data: SFO
SFO
LAX
SJC
BOS
PBI
JFK
SLC
PHX
MSP
SAT
ATL
DCA
DTW
YYZ
CVG
LAS
AXY
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:3
Successfully undid last move
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
2

From where: SFO
SFO
LAX
BOS
SJC
PBI
JFK
SLC
PHX
MSP
SAT
ATL
DCA
DTW
YYZ
CVG
LAS
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:5

LAS-CVG-YYZ-DTW-DCA-ATL-SAT-MSP-PHX-SLC-JFK-PBI-BOS-SFO-LAX-SJC
Do you want to output the Euler path to a file as well? (Y/N)
Y
Where do you want to output it? (Ex. output.txt [is a file in the project])
input1OUTPUT.txt
See in the file
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
2
Where do you want to output it? (Ex. output.txt [is a file in the project])
adjInput1OUTPUT.txt
Successfully outputted to a file
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:6

Please enter the input file name: (Ex. input1.txt)
input2.txt
Changed the input file
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
3
------------------------ 
Adj List for New York(JFK): Phoenix(PHX)(18.0) Boston(BOS)(8.0) West Palm Beach(PBI)(5.0) 
Adj List for Seattle-Tacoma (SEA): Salt Lake City(SLC)(11.0) San Jose (SJC)(12.0) 
Adj List for San Jose (SJC): Las Vegas(LAX)(8.0) Seattle-Tacoma (SEA)(12.0) 
Adj List for Phoenix(PHX): Salt Lake City(SLC)(20.0) San Francisco(SFO)(14.0) New York(JFK)(18.0) West Palm Beach(PBI)(16.0) 
Adj List for Las Vegas(LAX): Salt Lake City(SLC)(10.0) San Francisco(SFO)(8.0) San Jose (SJC)(8.0) 
Adj List for Boston(BOS): Salt Lake City(SLC)(32.0) New York(JFK)(8.0) 
Adj List for Salt Lake City(SLC): Phoenix(PHX)(20.0) Las Vegas(LAX)(10.0) Boston(BOS)(32.0) Seattle-Tacoma (SEA)(11.0) 
Adj List for San Francisco(SFO): Phoenix(PHX)(14.0) Las Vegas(LAX)(8.0) 
Adj List for West Palm Beach(PBI): Phoenix(PHX)(16.0) New York(JFK)(5.0) 

Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:6

Please enter the input file name: (Ex. input1.txt)
input3.txt
Changed the input file
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
3
------------------------ 
Adj List for 1: 2(0.0) 
Adj List for 2: 1(0.0) 3(0.0) 4(0.0) 8(0.0) 
Adj List for 3: 2(0.0) 4(0.0) 
Adj List for 4: 2(0.0) 3(0.0) 
Adj List for 8: 2(0.0) 

Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:5

8-2-3-4-2-1
Do you want to output the Euler path to a file as well? (Y/N)
N
As you wish
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:-1
Exiting
Goodbye.


Please enter the input file name: (Ex. input1.txt)
input4.txt
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:4
1 : Display to console 
 2 : Output the Adjacency List to a File
1
1 : Output using Depth-First Traversal
2 : Output using Breadth-First Traversal
3 : Output Adjacency List
3
------------------------ 
Adj List for 1: 2(0.0) 3(0.0) 9(0.0) 10(0.0) 
Adj List for 2: 1(0.0) 7(0.0) 9(0.0) 
Adj List for 3: 1(0.0) 4(0.0) 
Adj List for 4: 3(0.0) 5(0.0) 7(0.0) 
Adj List for 5: 4(0.0) 6(0.0) 
Adj List for 6: 5(0.0) 7(0.0) 
Adj List for 7: 2(0.0) 4(0.0) 6(0.0) 8(0.0) 
Adj List for 8: 7(0.0) 
Adj List for 9: 1(0.0) 2(0.0) 10(0.0) 
Adj List for 10: 1(0.0) 9(0.0) 

Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:5

Graph is not solvable.
Welcome to Euler's Algorithm 
-1 : Exit.
1 : Add a New Flight to the Graph. (You are not allowed to enter existing connections)
2 : Delete Airport from graph.
3 : Undo last move.
4 : Output.
5 : Show Eulers Solution.
6 : Input Another File.


Please enter your choice:-1
Exiting
Goodbye.


Please enter the input file name: (Ex. input1.txt)
DOES_NOT_EXIST.txt
DOES_NOT_EXIST.txt (No such file or directory)

*/
 
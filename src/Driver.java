import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Dmitry Dolgopolov (main and EulerGraph work), Shiyu Zhang (data, input, output), 
 * 			Mher Torjyan (menu and Graph work)
 *
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
	 * This method shows the meny and calls the methods on different 
	 * manipulations of the user
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
					System.out.println("Graph is not solvable.");
				else {
					System.out.println("Do you want to output the Euler path to a file as well?");
					
					String response = userScanner.next();
					if (response.equals("Y") || response.equals("y")) {
						outputStrToFile(result);
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
	
	/** @author Mher Torjyan */
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

		System.out.println("From where?");
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

	/** @author Shiyu Zhang, Dmitry Dolgopolov*/
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
	
	/** @author Shiyu Zhang, Dmitry Dolgopolov
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
			System.out.println("Where do you want to output it? (Ex. output.txt [a file in the project])");
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
	
	public static boolean outputStrToFile(String str){
		
		// Trying to open the file
		PrintWriter pw = null;
		try {
			//"/Users/dimafet/Documents/workspace/EulerPathCIS22C_2017_Fall/output.txt"
			System.out.println("Where do you want to output it? (Ex. output.txt [a file in the project])");
			pw = new PrintWriter(userScanner.next());
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found. Canceling");
			return false;
		}
		
		if (pw == null) return false; 
		
		// outputting to file
		pw.println(str);
		
		return true;
	}

}

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author Dmitry Dolgopolov (main and EulerGraph work), Shiyu Zhang (data part and files), 
 * 			Mher Torjyan (menu and Graph work)
 *
 */
public class Driver {
	

	// this file is the practical EulerGraph usage
	// so we use Strings everywhere here
	public static Scanner userScanner = new Scanner(System.in);
	public static EulerGraph<String> curGraph;
	
	public static void main(String[] args) {
		

		curGraph = new EulerGraph<String>();
		System.out.println();
		fillPath(openInputFile(), curGraph);
		curGraph.showAdjTable();
		mainMenu(curGraph);
//		g1.findEulerPath();
		
	}

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
			// TODO read another file in the same run
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
				if (!a.remove(remFrom, remTo)) 
					System.out.println("Failed removing. You probably used non-existent edge");
				else
					System.out.println("Successfully removed");
				break;
			case 3:
				if(a.undo()) {
					System.out.println("Successfully undid last move");
				}else {
					System.out.println("Could not undo.");
				}
				break;
			case 4:
				System.out.println("1 : Display to console \n "
								+ "2 : Display to File");
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
					// TODO: make everything work
					System.out.println("Coming Soon");
					
					// Trying to open the file
					PrintWriter pw = null;
					try {
						pw = new PrintWriter("/Users/dimafet/Documents/workspace/EulerPathCIS22C_2017_Fall/output.txt");
					} catch (FileNotFoundException e) {
						System.out.println("File Not Found.\nDo you want to create a file? (Y/N)");
						
						String response = userScanner.next();
						if (response.equals("Y") || response.equals("y")) {

							System.out.println("Input the file name");
							File file = new File(userScanner.next());

							// Create the file
							try {
								if (file.createNewFile()) {
									System.out.println("File is created!");
								} else {
									System.out.println("File already exists.");
								}
							} catch (IOException e2) {
								System.out.println("Couldn't read file well. Aborting");
								continue;
							}
							// Write Content
							PrintWriter writer;
							try {
								writer = new PrintWriter(file);
							} catch (FileNotFoundException e1) {
								System.out.println("File not found. Canceling");
								continue;
							}
							writer.write("Test data");
							writer.close();
						} else {
							System.out.println("File not found. Canceling");
							continue;
						}
					}
					
					a.outputToFile(pw);
					break;
				default:
					System.out.println("Not valid input. Canceling");
					continue;
				}

				break;
			case 5:
				// TODO: actually call the solution
				System.out.println("Coming soon");
				break;
			case 6:
				fillPath(openInputFile(), curGraph); 
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
			
			// TODO: Traversals [DFT, BFT]
			// fix Remove
			// delete the flight
			
			a.showAdjTable();
		} while (choice != -1);

	}
	
	public static int presentOutputChoices(Scanner in) {
		System.out.println("1 : Output using Depth-First Traversal\n"
						  +"2 : Output using Breadth-First Traversal\n"
						  + "3 : Output Adjacency List");
		
		return in.nextInt();
	}
	public static String chooseStartingVertex(Scanner in) {
//		System.out.println(curGraph.vertexSet.toString());
		System.out.println("From where?");
		String ret = in.next();
		
		// checking if the vertex is real and exists in the Graph
		if (!curGraph.vertexSet.containsKey(ret)) {
			return null;
		}
		
		return ret;
	}
	
	static void fillPath(Scanner s, EulerGraph path) {
		
		path.vertexSet.clear();
		while(s.hasNext()) {
			String[] ar = s.nextLine().split(",");
			path.addEdge(ar[0], ar[1], Double.parseDouble(ar[2]));
			
		}
	}

	// Shiyu, Zhang
	public static Scanner openInputFile() {

		String filename = null;
		Scanner temp = null;

		System.out.print("\nPlease enter the input file name: ");
//		filename = "/Users/m_torjyan/Documents/EulerPathCIS22C_2017_Fall/input3.txt";
//		filename = "/Users/dimafet/Documents/workspace/EulerPathCIS22C_2017_Fall/input3.txt";
		
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

}

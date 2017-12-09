import java.io.File;
import java.io.FileNotFoundException;
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
	public static Scanner userScanner = new Scanner(System.in);

	public static void main(String[] args) {
		
		EulerGraph<String> g1 = new EulerGraph<String>();
		userScanner = openInputFile();
		fillPath(userScanner, g1);
		g1.showAdjTable();
		mainMenu(g1);
		
	}

	public static void mainMenu(EulerGraph a) { // GRAPH IS PLACEHOLDER
		
		int choice = -1;
	
		do {
			System.out.println("Welcome to Euler's Algorithm \n" 
						+ "-1 : Exit.\n" 
						+ "1 : Add a New Flight to the Graph.\n"
						+ "2 : Delete Airport from graph.\n" 
						+ "3 : Undo last move.\n" 
						+ "4 : Output.\n"
						+ "5 : Show Eulers Solution.\n" 
						+ "7 : Input Another File.");

			System.out.print("\n\nPlease enter your choice:");

			try {
				choice = userScanner.nextInt(); // try catch
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid number.");
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
				a.addEdge(from, to, weight);
				break;
			case 2: 
				System.out.println("Where are you removing from?");
				String remFrom = userScanner.next();
				System.out.println("Where was the flight to?");
				String remTo = userScanner.next();
				a.remove(remFrom, remTo);
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
				
				presentOutputChoices(userScanner);
				switch(sc) {
				case 1: 
					switch(presentOutputChoices(userScanner)) {
						case 1: 
							a.depthFirstTraversal(chooseStartingVertex(userScanner), new Visitor() {
								 public void visit(Object obj) {
									System.out.println(obj.toString()); 
								 }
							});
							break;
						case 2: 
							a.breadthFirstTraversal(chooseStartingVertex(userScanner);, new Visitor() {
								 public void visit(Object obj) {
									System.out.println(obj.toString()); 
								 }
							});
							break;
						case 3:
							//ToDo: change it later
							a.outputToFile(null);
							break;
					}
				}
				
				break;
			case 5:

				break;
			case 6:// exit
				System.out.println("Exiting");
				System.exit(0);
				break;
			case -1:
				System.out.println("Goodbye.");
				break;
			default:
				break;
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
	public static int chooseStartingVertex(Scanner in) {
		System.out.println("From where?");
		return in.nextInt();
	}
	
	static void fillPath(Scanner s, EulerGraph path) {
		NumberFormat format = NumberFormat.getInstance(Locale.US);
		while(s.hasNext()) {
			String[] ar = s.nextLine().split(",");
			try {
				path.addEdge(ar[0], ar[1], format.parse(ar[2].trim()).doubleValue());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// Shiyu, Zhang
	public static Scanner openInputFile() {

		String filename = null;
		Scanner temp = null;

		System.out.print("\nPlease enter the input file name: ");
		filename = "/Users/dimafet/Documents/workspace/EulerPathCIS22C_2017_Fall/input3.txt";
		
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

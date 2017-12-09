import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;


//Shiyu Zhang
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
		Scanner choiceScan = new Scanner(System.in);
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
				choice = choiceScan.nextInt(); // try catch
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid number.");
			}
			// TODO read another file in the same run
			switch (choice) {
			case 1: // add
				System.out.println("Where is the flight from?");
				String from = choiceScan.next();
				System.out.println("Where is the flight to?");
				String to = choiceScan.next();
				System.out.println("How far are the two airports?");
				double weight = choiceScan.nextDouble();
				a.addEdge(from, to, weight);
				break;
			case 2: 
				System.out.println("Where are you removing from?");
				String remFrom = choiceScan.next();
				System.out.println("Where was the flight to?");
				String remTo = choiceScan.next();
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
				int sc = choiceScan.nextInt();
				String[] subChoice = presentOutputChoices(choiceScan);
				switch(sc) {
				case 1: 
					switch(Integer.parseInt(subChoice[0])) {
						case 1: 
							a.depthFirstTraversal(subChoice[1], new Visitor() {
								 public void visit(Object obj) {
									System.out.println(obj.toString()); 
								 }
							});
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
	
	public static String[] presentOutputChoices(Scanner in) {
		System.out.println("1 : Output using Depth-First Traversal\n"
						  +"2 : Output using Breadth-First Traversal\n"
						  + "3 : Output Adjacency List");
		
		String[] choice =  new String[3];
		choice[0] = in.next();
		System.out.println("From where?");
		choice[1] = in.next();
		return choice;

	}
	
	static void fillPath(Scanner s, EulerGraph path) {

		while(s.hasNext()) {
			String[] ar = s.nextLine().split(",");
			System.out.println(ar[0] + " "+ ar[1]);
			path.addEdge(ar[0], ar[1], Double.parseDouble(ar[2]));
		}
	}

	// Shiyu, Zhang
	public static Scanner openInputFile() {

		String filename = null;
		Scanner temp = null;
		//System.out.print("\nPlease enter the input file name: ");
		filename = "/Users/m_torjyan/Documents/EulerPathCIS22C_2017_Fall/input2.txt";
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

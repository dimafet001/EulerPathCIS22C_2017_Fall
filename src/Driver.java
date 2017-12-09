import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import graphFiles.Graph;

//Shiyu Zhang
public class Driver {
	public static Scanner userScanner = new Scanner(System.in);

	public static void main(String[] args) {
		
	}

	public static void mainMenu(EulerGraph a) { // GRAPH IS PLACEHOLDER

		a.printIndexToName();
		
		int choice = -1;
		do {
			System.out.println("Welcome to Euler's Algorithm \n" 
						+ "-1.Exit\n" 
						+ "1.Add a New Flight to the Graph\n"
						+ "2.Delete Airport from graph \n" 
						+ "3.Undo last move\n" 
						+ "4.Show Adjacency List\n"
						+ "5.Show Eulers Solution \n" 
						+ "6.Display Graph"
						+ "7.Input Another File");

			System.out.print("\n\nPlease enter your choice:");

			try {
				choice = userScanner.nextInt(); // try catch
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid number.");
			}
// TODO read another file in the same run
			switch (choice) {
			case 1: // add
				a.addEdge(userScanner.nextInt(), userScanner.nextInt());
				System.out.println("added an edge");
				break;
			case 2: // delete existing cellTower
				a.remove(userScanner.nextInt(), userScanner.nextInt());
				System.out.println("removed the edge");
				break;
			case 3://
				System.out.println("Undoing");
				a.undo();
				break;
			case 4:// Show adjacency list
				System.out.println("Showing the data");
				a.showAdjTable();
				break;
			case 5:
				System.out.println("Finding the path. What vertice?");
				// File outfile = new Fle()
				boolean[] visited = new boolean[9];
				a.DFSUtil(userScanner.nextInt(), visited);;
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

	static void fillPath(Scanner s, EulerGraph path) {

		s.nextLine();

		

	}

	// Shiyu, Zhang
	public static Scanner openInputFile() {

		String filename = null;
		Scanner temp = null;
		System.out.print("\nPlease enter the input file name: ");
		filename = userScanner.nextLine();
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

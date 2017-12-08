import graphFiles.Graph;
//Shiyu Zhang
public class Driver {	
	public static Scanner userScanner = new Scanner(System.in);
	
	 public static void main(String[] args)
	 {
		 Scanner fileScanner = OpenInputFile();
		 EulerGraph<String> flightPath;
		 if(fileScanner==null){
			System.out.println("Unable to open the input file, ending program");
			return;
		}
		else{
			int NumVert = fileScanner.nextInt();
			flightPath = new EulerGraph<String>(NumVert);
			fillPath(fileScanner,flightPath); 
			
		}
	 }
	 
	 static void fillPath(Scanner s,EulerGraph<String> path) {
		 s.nextLine();
		while(s.hasNext()){
			String line = s.nextLine();//get next line
			String[] temp = line.split(",");
			path.addEdge(temp[0], temp[1]);
			path.numberOfVertices ++;
		}
		
	}

	public static Scanner OpenInputFile() {
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
/*
	public static void main(String[] args) {
		Graph<String> myGraph1 = new Graph<String>();
		myGraph1.addEdge("A", "B", 0);
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
*/
}

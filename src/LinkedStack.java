
/**
   A class of stacks whose entries are stored in a chain of nodes.

   @author Frank M. Carrano
   @author Timothy M. Henry
   @version 4.0
   UPDATED by C. Lee-Klawender
*/
{
	private Node topNode; // References the first node in the chain
	private int nodeCount;
	// ADD A PRIVATE INT FOR COUNTER THAT INDICATES HOW MANY NODES ARE IN THE STACK

	public LinkedStack()
	{
		topNode = null;
		nodeCount = 0;
	} // end default constructor

	public boolean push(T newEntry)
	{
        topNode = new Node(newEntry, topNode);
        // ADD CODE SO THE COUNTER IS CORRECT
        nodeCount++;
        return true;
	} // end push

	public T peek()
	{
		if (isEmpty())
			return null;
		else
         return topNode.getData();
	} // end peek

	public T pop()
	{
	   T top = peek();
	   if(topNode != null)
	   {
           topNode = topNode.getNextNode();
           // ADD CODE SO THE COUNTER IS CORRECT
           nodeCount--;
       }

	   return top;
	} // end pop
	
	public int size() {
		return nodeCount;
		
	}
	
	public boolean isEmpty(){
		if(nodeCount == 0) {
			return true; 
		}
		return false;
		
	} // end isEmpty


	private class Node
	{
      private T    data; // Entry in stack
      private Node next; // Link to next node

      private Node(T dataPortion)
      {
         this(dataPortion, null);
      } // end constructor

      private Node(T dataPortion, Node linkPortion)
      {
         data = dataPortion;
         next = linkPortion;
      } // end constructor

      private T getData()
      {
         return data;
      } // end getData

      private void setData(T newData)
      {
         data = newData;
      } // end setData

      private Node getNextNode()
      {
         return next;
      } // end getNextNode

      private void setNextNode(Node nextNode)
      {
         next = nextNode;
      } // end setNextNode
	} // end Node
} // end LinkedStack

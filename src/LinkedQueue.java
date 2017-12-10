/**
   A class that implements the ADT queue by using a chain of nodes
   that has both head and tail references.

   @author Frank M. Carrano
   @author Timothy M. Henry
   @version 4.0
   UPDATED by C. Lee-Klawender
   NOTE: the LinkedQueue class includes the Node class as an inner class
*/

public class LinkedQueue<T> implements QueueInterface<T>
{
  private Node frontNode; // References node at front of queue
  private Node backNode;  // References node at back of queue
  private int count = 0;

	public LinkedQueue()
	{
		frontNode = null;
		backNode = null;
	} // end default constructor

	//to clear LQ
	public void clear() {
		//if there is at least 1 element
		if(frontNode != backNode) {
			//set front nex tnode to the back
			frontNode.next = backNode;
			//reset front and back
			frontNode = null;
			backNode = null;
			//reset count
			count = 0;
		}
		
	}
	/*
	// copy a linkedQueue into self
	public void copy(LinkedQueue<T> toCopy) {
		//get the top node of copy LQ
		Node curNode = toCopy.frontNode;
		//Keep adding to current LQ by creating a deepClone of each object
		do {
			enqueue(curNode.data.deepClone());
			curNode = curNode.getNextNode();
		} while (curNode != toCopy.backNode.next);
		

	}
	*/
	//enquque an object 
	public boolean enqueue(T newEntry)
	{
	// ADD CODE TO add data to linked list HERE!
	// In addition to updating the backNode, also
	//    make sure you check if the list was empty before adding this
	//    and update the correct variable if so
		
		Node cur = new Node(newEntry,null);
		
		if(isEmpty()) { //if empty set to frontNode
			frontNode = cur;
		}else {// if not empty set the next of back to the curNode
			backNode.setNextNode(cur);
		}
		//set the backNode to the curNode
		backNode = cur;
		//increment count
		++count;
		return true;
	} // end enqueue

	public T peekFront()
	{
		if (isEmpty())
			return null;
		else
            return frontNode.getData();
	} // end getFront

	//deque from LQ
	public T dequeue(){
		
	   T front = peekFront();
       if( count > 0 ) { // if is not empty
    	   	//update front node to point to the next node
    	   		frontNode = frontNode.getNextNode();
    	   		
	// ADD CODE TO remove data from linked list HERE!
	// In addition to updating the frontNode, also
	//    make sure to check if the list becomes empty and
	//    update the correct variable if so

    	   		//decrement count
          --count;
          
          // if now is null set the backNode to null
          if(count == 0) {
        	  	backNode = null;
          }
        }

        return front;
	} // end dequeue

	public boolean isEmpty()
	{
		return count == 0;
	} // end isEmpty

    public int size()
    {
        return count;

    }

	private class Node
	{
		private T    data; // Entry in queue
		private Node next; // Link to next node

		private Node(T dataPortion)
		{
			data = dataPortion;
			next = null;
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
} // end Linkedqueue
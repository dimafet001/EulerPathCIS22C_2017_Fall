/**
   An interface for the ADT queue.
   @author Frank M. Carrano
   @author Timothy M. Henry
   @version 4.0
   UPDATED by C. Lee-Klawender
*/

/*

 *  -Name of program : Final Project

      -Programmer's name: Dmitry Dolgopolov (main and EulerGraph work), Shiyu Zhang (input), 
 * 			Mher Torjyan (menu and Graph work), Dongbo Liu(miscellaneous help)
 * 
      -Current Date: 12/08/17

      -Computer operating system and compiler you are using : MAC OSX

      -Brief description of the program (1-5 sentences) : The program stores the flights in the Graph.
  		It can parse it from a file and output it to the file, too.
  		Also, we can find a EulerPath (if exists) that passes thru
 		all the edges once.
 */
public interface QueueInterface<T>
{
  /** Adds a new entry to the back of this queue.
      @param newEntry  An object to be added.
      @return  True if succesfully added the newEntry, false otherwise*/
  public boolean enqueue(T newEntry);

  /** Removes and returns the entry at the front of this queue.
      @return  The object at the front of the queue
                or null if the queue is empty before the operation. */
  public T dequeue();

  /**  Retrieves the entry at the front of this queue.
      @return  The object at the front of the queue
                or null if the queue is empty. */
  public T peekFront();

  /** Detects whether this queue is empty.
      @return  True if the queue is empty, or false otherwise. */
  public boolean isEmpty();

  /** Returns number of items in this queue
    @return: Number of items */
  public int size();
} // end QueueInterface

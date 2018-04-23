// -*- Java -*-
/*
 * <copyright>
 * 
 *  Copyright (c) 2002
 *  Institute for Information Processing and Computer Supported New Media (IICM),
 *  Graz University of Technology, Austria.
 * 
 * </copyright>
 * 
 * <file>
 * 
 *  Name:    Alphabetizer.java
 * 
 *  Purpose: Sorts a set of lines (shifts) alphabetically
 * 
 *  Created: 23 Sep 2002 
 * 
 *  $Id$
 * 
 *  Description:
 *    Sorts a set of lines (shifts) alphabetically
 * </file>
*/

package kwic.pf;

/*
 * $Log$
*/

import java.util.ArrayList;
import java.util.Iterator;
import java.io.CharArrayWriter;
import java.io.IOException;

/**
 *  Alphabetizer filter sorts lines that are passed to it. Alphabetizer reads
 *  lines from its input pipe. The sorted lines are written to the output pipe.
 *  @author  dhelic
 *  @version $Id$
*/

public class Alphabetizer extends Filter{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Constructors
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Default constructor.
 * @param input input pipe
 * @param output output pipe
 */

  public Alphabetizer(Pipe input, Pipe output){
    super(input, output);
  }

//----------------------------------------------------------------------
/**
 * Methods
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Sorts lines from a given set, read from the input pipe.
 */

  protected void transform() {
    try {
      ArrayList lines = new ArrayList();
      CharArrayWriter writer = new CharArrayWriter();

      int c = input_.read();
      while (c != -1) {
        writer.write(c);
        if (((char) c) == '\n') {
          String line = writer.toString();
          lines.add(line);
          writer.reset();
        }
        c = input_.read();
      }

      sort(lines);

      Iterator iterator = lines.iterator();
      while (iterator.hasNext()) {
        char[] chars = ((String) iterator.next()).toCharArray();
        for (int i = 0; i < chars.length; i++)
          output_.write(chars[i]);
      }

      output_.closeWriter();
    } catch (IOException exc) {
      exc.printStackTrace();
      System.err.println("KWIC Error: Could not sort circular shifts.");
      System.exit(1);
    }
  }



//----------------------------------------------------------------------
/**
 * Sorts lines from the given set of lines.
 */

  private void sort(ArrayList lines){ // heap sort
    int size = lines.size();

    for(int i = (size / 2 - 1); i >= 0; i--)
      siftDown(lines, i, size);

    for(int i = (size - 1); i >= 1; i--){
      Object tmp = lines.get(0);
      lines.set(0, lines.get(i));
      lines.set(i, tmp);
      siftDown(lines, 0, i);      
    }
  }

//----------------------------------------------------------------------
/**
 * This method builds and reconstucts the heap for the heap sort algorithm.
 * @param lines set of lines
 * @param root heap root
 * @param bottom heap bottom
 */

  private void siftDown(ArrayList lines, int root, int bottom){    
    int max_child = root * 2 + 1;

    while(max_child < bottom){
      if((max_child + 1) < bottom)
        if(((String) lines.get(max_child + 1)).compareTo((String) lines.get(max_child)) > 0)
          max_child++;

      if(((String) lines.get(root)).compareTo((String) lines.get(max_child)) < 0){
        Object tmp = lines.get(root);
        lines.set(root, lines.get(max_child));
        lines.set(max_child, tmp);
        root = max_child;
        max_child = root * 2 + 1;
      }else
        break;
    }    
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}

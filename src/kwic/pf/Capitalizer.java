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
 *  Name:    CircularShifter.java
 *
 *  Purpose: Produces circular shifts of input lines
 *
 *  Created: 23 Sep 2002
 *
 *  $Id$
 *
 *  Description:
 *    Produces circular shifts of input lines
 * </file>
*/

package kwic.pf;

/*
 * $Log$
*/

import java.io.IOException;
import java.io.CharArrayWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;


public class Capitalizer extends Filter{

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

    public Capitalizer(Pipe input, Pipe output){
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
     * Capitalize the first word of lines from the input pipe.
     */

    protected void transform(){
        try{
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

            Iterator iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = ((String) iterator.next());
                StringTokenizer tokenizer = new StringTokenizer(line);
                String[] words = new String[tokenizer.countTokens()];
                int i = 0;
                while(tokenizer.hasMoreTokens())
                    words[i++] = tokenizer.nextToken();
                words[0] = words[0].toUpperCase();

                String shift = "";
                for(int j = i; j < (words.length + i); j++){
                    shift += words[j % words.length];
                    if(j < (words.length + i - 1))
                        shift += " ";
                }
                shift += '\n';
                char[] chars = shift.toCharArray();
                for(int j = 0; j < chars.length; j++)
                    output_.write(chars[j]);

            }

            output_.closeWriter();
        }catch(IOException exc){
            exc.printStackTrace();
            System.err.println("KWIC Error: Could not make circular shifts.");
            System.exit(1);
        }
    }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}

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

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;



public class ShiftFilter extends Filter{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

    private List<String> noise = new ArrayList<>();
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
     * @param noiseFile noise file path
     */

    public ShiftFilter(Pipe input, Pipe output, String noiseFile){
        super(input, output);
        parse(noiseFile);
    }

//----------------------------------------------------------------------
/**
 * Methods
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
    /**
     * remove all noise shifts of lines from the input pipe.
     */

    protected void transform(){
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

            Iterator iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = ((String) iterator.next());
                String firstWord = line.split("\\s")[0];
                if(!noise.contains(firstWord)){
                    char[] chars = line.toCharArray();
                    for (int i = 0; i < chars.length; i++)
                        output_.write(chars[i]);
                }
            }


                output_.closeWriter();
            }catch(IOException exc){
                exc.printStackTrace();
                System.err.println("KWIC Error: Could not make circular shifts.");
                System.exit(1);
            }

    }


    public void parse(String file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();
            while(line != null){

                noise.add(line.trim().toString());
                line = reader.readLine();
            }

        }catch(FileNotFoundException exc){
            exc.printStackTrace();
            System.err.println("KWIC Error: Could not open " + file + "file.");
            System.exit(1);
        }catch(IOException exc){
            exc.printStackTrace();
            System.err.println("KWIC Error: Could not read " + file + "file.");
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

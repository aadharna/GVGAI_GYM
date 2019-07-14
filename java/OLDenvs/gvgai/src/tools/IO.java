package qmul.gvgai.engine.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class IO
{
    /**
     * Default constructor
     */
    public IO(){}

    /**
     * Reads a file and returns its content as a String[]
     * @param filename file to read
     * @return file content as String[], one line per element
     */
    public String[] readFile(String filename)
    {
        ArrayList<String> lines = new ArrayList<String>();
        try{
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            in.close();
        }catch(Exception e)
        {
            System.out.println("Error reading the file " + filename + ": " + e.toString());
            e.printStackTrace();
            return null;
        }
        return lines.toArray(new String[lines.size()]);
    }
}

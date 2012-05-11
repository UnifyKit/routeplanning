package routeplanning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;

/**
 * Class ReduceFileSize which reduces the size of the osm file.
 * @author CJC
 */
public class ReduceFileSize {

  /**
   * Path to input file.
   */
  private String pathIn;
  /**
   * Path to output file.
   */
  private String pathOut;

  /**
   * Constructor.
   */
  public ReduceFileSize(String pathIn, String pathOut) {
    this.pathIn = pathIn;
    this.pathOut = pathOut;
  }

  /**
   * Returns a line.
   */
  private String review(String lineIn) {
    String newLine;

    if (lineIn.contains("<node")) {
      newLine = new String();
      newLine = lineIn.substring(1, lineIn.indexOf("\" ver"));
      newLine = newLine + "\">";
      return newLine;
    } else if (lineIn.contains("</node")) {
      return lineIn;
    } else if (lineIn.contains("<way")) {
      newLine = lineIn.substring(1, lineIn.indexOf("\" ver") + 1);
      newLine = newLine + ">";
      return newLine;
    } else if (lineIn.contains("</way")) {
      return lineIn;
    } else if (lineIn.contains("<tag k=\"highway\"")) {
      return lineIn;
    } else if (lineIn.contains("<nd ref=")) {
      return lineIn;
    } else if (lineIn.contains("<?xml")) {
      return lineIn;
    } else if (lineIn.contains("<osm")) {
      return lineIn;
    } else if (lineIn.contains("</osm")) {
      return lineIn;
    }

    return "";
  }
  
  
  /**
   * Reduces the XML keeping lines with needed information.
   */
  public void process() {
    String lineIn = "";
    int linesReduced = 0;
    String review;
    System.out.println("Start: " + Calendar.getInstance().getTime());
    try {
      File in = new File(this.pathIn);

      BufferedReader inBuff = new BufferedReader(new FileReader(in));
      BufferedWriter outWriter = new BufferedWriter(new FileWriter(pathOut));

      while ((lineIn = inBuff.readLine()) != null) {
        review = review(lineIn);
        if (review.isEmpty()) {
          linesReduced++;
          // System.out.println(lineIn);
          continue;
        } else {
          // System.out.println(lineIn);
          // System.out.println(review);
          outWriter.newLine();
          outWriter.write(review);
        }
      }
      inBuff.close();
      outWriter.close();
      System.out.println("Lines reduced: " + linesReduced);
      System.out.println("Finish: " + Calendar.getInstance().getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}

package routeplanning;

import org.junit.Test;
import org.junit.Assert;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 *  * Test class for the CsvParser.
 *   *
 *    */
public class CsvParserTest {
  /**
   * Test the constructor.
   **/
  @Test
  public void testReadNextLine() {
    // Write some test-data to a temporary sample file.
    String filePath = "CsvParserTest.TMP.csv";
    try {
      FileWriter outFile = new FileWriter(filePath);
      PrintWriter out = new PrintWriter(outFile);
      out.println("H1,H2,H3");
      out.println("1,2,3");
      out.println("4,,   ");
      out.println(", 5 , \" 6 \" ");
      out.println(",  \"\",\"6\"");
      out.println(",,");
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Test that everything is getting parsed correctly.
    try {
      CsvParser csvParser = new CsvParser(filePath);
      Assert.assertEquals(0, csvParser.getNumColumns());
      Assert.assertTrue(csvParser.readNextLine());
      Assert.assertEquals(3, csvParser.getNumColumns());
      Assert.assertEquals("H1", csvParser.getItem(0));
      Assert.assertEquals("H2", csvParser.getItem(1));
      Assert.assertEquals("H3", csvParser.getItem(2));
      Assert.assertTrue(csvParser.readNextLine());
      Assert.assertEquals(3, csvParser.getNumColumns());
      Assert.assertEquals("1", csvParser.getItem(0));
      Assert.assertEquals("2", csvParser.getItem(1));
      Assert.assertEquals("3", csvParser.getItem(2));
      Assert.assertTrue(csvParser.readNextLine());
      Assert.assertEquals(3, csvParser.getNumColumns());
      Assert.assertEquals("4", csvParser.getItem(0));
      Assert.assertEquals("", csvParser.getItem(1));
      Assert.assertEquals("", csvParser.getItem(2));
      Assert.assertTrue(csvParser.readNextLine());
      Assert.assertEquals(3, csvParser.getNumColumns());
      Assert.assertEquals("", csvParser.getItem(0));
      Assert.assertEquals("5", csvParser.getItem(1));
      Assert.assertEquals("6", csvParser.getItem(2));
      Assert.assertTrue(csvParser.readNextLine());
      Assert.assertEquals(3, csvParser.getNumColumns());
      Assert.assertEquals("", csvParser.getItem(0));
      Assert.assertEquals("", csvParser.getItem(1));
      Assert.assertEquals("6", csvParser.getItem(2));
      Assert.assertTrue(csvParser.readNextLine());
      Assert.assertEquals(3, csvParser.getNumColumns());
      Assert.assertEquals("", csvParser.getItem(0));
      Assert.assertEquals("", csvParser.getItem(1));
      Assert.assertEquals("", csvParser.getItem(2));
      Assert.assertFalse(csvParser.readNextLine());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

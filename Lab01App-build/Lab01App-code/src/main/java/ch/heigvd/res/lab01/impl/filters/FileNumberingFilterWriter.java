package ch.heigvd.res.lab01.impl.filters;

import ch.heigvd.res.lab01.impl.Utils;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
  private int count = 1;
  private boolean endLine = false;
  private String[] line;
  
  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    String tmp = "";
    
    if(line != null && line[1].equals("")) {
        endLine = false;
    } else {
        endLine = true;
    }
    
    line = Utils.getNextLine(str.substring(off, off + len));
    
    while(!line[0].equals("")) {
        tmp += count + "\t" + line[0];
        count++;
        line = Utils.getNextLine(line[1]);
    }
    
    if(line[1] != null) {
        if(endLine) {
            tmp += count + "\t" + line[1];
            count++;
        } else {
            tmp += line[1];
        }
    }
      System.out.println("Result");
      System.out.println(tmp);
    out.write(tmp, 0, tmp.length());
    //throw new UnsupportedOperationException("The student has not implemented this method yet.");
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    write(new String(cbuf), off, len);
    //throw new UnsupportedOperationException("The student has not implemented this method yet.");
  }

  @Override
  public void write(int c) throws IOException {
      out.write(count + "\t" + c);
      count++;
    //throw new UnsupportedOperationException("The student has not implemented this method yet.");
  }

}

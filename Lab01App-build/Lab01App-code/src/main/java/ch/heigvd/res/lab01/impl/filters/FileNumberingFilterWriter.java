package ch.heigvd.res.lab01.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;
import ch.heigvd.res.lab01.impl.Utils;

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

  private int lineNumber = 0;
  private boolean firstLine = true;
  private char previousCharacter = '\0';
  
  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    String s = str.substring(off, off + len);
    String myString = "";

    if (firstLine) {
      lineNumber = 1;
      myString += lineNumber + "\t";
      firstLine = false;
    }

    String[] next = {"",""};
    do {
      next = Utils.getNextLine(s);
      if (next[0].isEmpty())
        break;
      lineNumber++;
      myString += next[0] + lineNumber + "\t";
      s = next[1];
    } while (true);

    myString += next[1];
    super.write(myString, 0, myString.length());
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    String s = new String(cbuf, off, len);
    this.write(s, 0, s.length());
  }

  @Override
  public void write(int c) throws IOException { 

    if (firstLine) {
      lineNumber = 1;
      super.write('1');
      super.write('\t');
      firstLine = false;
    }

    switch (c) {
      case '\n':
        if (previousCharacter != '\r') {
          super.write('\n');
          lineNumber++;
          String s = Integer.toString(lineNumber);
          for (int i = 0; i < s.length(); ++i)
            super.write(s.charAt(i));
          super.write('\t');
        }
        else {
          super.write('\n');
          lineNumber++;
          String s = Integer.toString(lineNumber);
          for (int i = 0; i < s.length(); ++i)
            super.write(s.charAt(i));
          super.write('\t');
        }
        break;
      default:
        if (previousCharacter == '\r') {
          lineNumber++;
          String s = Integer.toString(lineNumber);
          for (int i = 0; i < s.length(); ++i)
            super.write(s.charAt(i));
          super.write('\t');
        }
        super.write(c);
    }
    previousCharacter = (char)c;
  }

}

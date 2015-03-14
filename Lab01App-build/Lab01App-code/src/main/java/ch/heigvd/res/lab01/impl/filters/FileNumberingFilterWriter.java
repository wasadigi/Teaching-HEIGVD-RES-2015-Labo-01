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
    String nextLine[] = Utils.getNextLine(str.substring(off, off+len));
    String strF = "";

    if (lineNumber == 0)
      strF += (++lineNumber) + "\t";

    while (!nextLine[0].isEmpty()) {
      strF += nextLine[0] + (++lineNumber) + "\t";
      nextLine = Utils.getNextLine(nextLine[1]);
    }

    strF += nextLine[1];
    super.write(strF, 0, strF.length());
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    this.write(new String(cbuf), off, len);
  }

  @Override
  public void write(int c) throws IOException { 

    if (lineNumber == 0) {
      lineNumber = 1;
      super.write('1');
      super.write('\t');
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

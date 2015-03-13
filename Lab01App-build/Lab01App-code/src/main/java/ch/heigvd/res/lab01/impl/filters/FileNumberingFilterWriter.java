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
    private int count = 0;
    private boolean start = true; // Is the first line

    public FileNumberingFilterWriter(Writer out) {
        super(out);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        if (start) {
            writeLineNumber();
            start = false;
        }
        String line[] = Utils.getNextLine(str.substring(off, off + len));
        // One line
        if (line[0].isEmpty()) {
            out.write(line[1], 0, line[1].length());
            return;
        }
        // loop to line[0] is empty => there is a line
        while (!line[0].isEmpty()) {
            out.write(line[0], 0, line[0].length());
            writeLineNumber();
            line = Utils.getNextLine(line[1]);
        }
        
        // Write the last line
        if (!line[1].isEmpty()) {
            out.write(line[1], 0, line[1].length());
        }
        //throw new UnsupportedOperationException("The student has not implemented this method yet.");
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        write(new String(cbuf), off, len);
        //throw new UnsupportedOperationException("The student has not implemented this method yet.");
    }

    private boolean isNewLine = false;

    @Override
    public void write(int c) throws IOException {
        if (start) {
            writeLineNumber();
            start = false;
        }
        if (c == '\n' || c == '\r') {
            isNewLine = true;
        } else if (isNewLine) {
            writeLineNumber();
            isNewLine = false;
        }
        out.write(c);
        //throw new UnsupportedOperationException("The student has not implemented this method yet.");
    }

    private void writeLineNumber() throws IOException {
        String s = ++count + "\t";
        out.write(s, 0, s.length());
    }

}

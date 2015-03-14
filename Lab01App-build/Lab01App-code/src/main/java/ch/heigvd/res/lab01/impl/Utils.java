package ch.heigvd.res.lab01.impl;

import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

  private static final Logger LOG = Logger.getLogger(Utils.class.getName());

  /**
   * This method looks for the next new line separators (\r, \n, \r\n) to extract
   * the next line in the string passed in arguments. 
   * 
   * @param lines a string that may contain 0, 1 or more lines
   * @return an array with 2 elements; the first element is the next line with
   * the line separator, the second element is the remaining text. If the argument does not
   * contain any line separator, then the first element is an empty string.
   */
  public static String[] getNextLine(String lines) {
    /* notes separators order : "\r\n" separator should be first */
    String[] separators = {"\r\n", "\n", "\r"}; 
    
    /* looks for separators */
    for (String sep : separators) {
      int pos = lines.indexOf(sep);
      if (pos != -1)
        return new String[] {lines.substring(0, pos + sep.length()), lines.substring(pos + sep.length())};
    }
    
    /* arriving here means no separators have been found */
    return new String[] {"", lines};  
  }

}

package ebhack.error;

import ebhack.MainGUI;
import ebhack.text.TextHelper;

import javax.swing.JOptionPane;

/**
 * An error that might occur in the application. Contains public static
 * instances of known errors.
 */
public class Error {
  /**
   * A FileNotFoundException was thrown while attempting to open the user
   * preferences file.
   */
  public static final Error PREFERENCES_FILE_COULD_NOT_BE_FOUND = new Error(
      "The user preferences file (%s) could not be found. Preferences were not applied.");

  /**
   * An IOException was thrown while attempting to open the the user preferences
   * file.
   */
  public static final Error PREFERENCES_FILE_COULD_NOT_BE_OPENED = new Error(
      "The user preferences file (%s) could not be opened. Preferences were not saved.");

  private int exitStatus;
  private String message;

  /**
   * Instantiate an object represeting an error that might occur in the
   * application.
   * 
   * @param exitStatus The value to use for the status parameter if
   *                   System.exit(int status) is called. Fatal errors should us a
   *                   unique value. Default is 0.
   * @param message    A message explaining the error and its impact to the user.
   *                   Can include format string placeholders.
   */
  Error(int exitStatus, String message) {
    this.exitStatus = exitStatus;
    this.message = message;
  }

  /**
   * Instantiate an object represeting an error that might occur in the
   * application.
   * 
   * @param message A message explaining the error and its impact to the user. Can
   *                include format string placeholders.
   */
  Error(String message) {
    this.exitStatus = 0;
    this.message = message;
  }

  /**
   * Display a GUI information pop-up to the user explaining the error and its
   * impact.
   * 
   * @param relatedException  An exception related to or causing the error. Its
   *                          stack trace will be displayed.
   * @param messageFormatArgs The values to interpolate into the message provided
   *                          for this error (if any). Uses String.format().
   */
  public void showMessage(Exception relatedException, Object... messageFormatArgs) {
    String message = String.format(this.message, messageFormatArgs);
    showMessage(message, relatedException);
  }

  /**
   * Display a GUI information pop-up to the user explaining the error and its
   * impact, then invoke System.exit().
   * 
   * @param relatedException  An exception related to or causing the error. Its
   *                          stack trace will be displayed.
   * @param messageFormatArgs The values to interpolate into the message provided
   *                          for this error (if any). Uses String.format().
   */
  public void showMessageAndExit(Exception relatedException, Object... messageFormatArgs) {
    showMessage(relatedException, messageFormatArgs);
    System.exit(exitStatus);
  }

  /**
   * Display a GUI information pop-up to the user explaining an error and its
   * impact, then invoke System.exit().
   * 
   * @param exitStatus       The value to use for the status parameter when
   *                         System.exit(int status) is called.
   * @param message          A message explaining the error and its impact.
   * @param relatedException An exception related to or causing the error. Its
   *                         stack trace will be displayed.
   */
  public static void showMessageAndExit(int exitStatus, String message, Exception relatedException) {
    showMessage(message, relatedException);
    System.exit(exitStatus);
  }

  /**
   * Display a GUI information pop-up to the user explaining an error and its
   * impact.
   * 
   * @param message          A message explaining the error and its impact.
   * @param relatedException An exception related to or causing the error. Its
   *                         stack trace will be displayed.
   */
  public static void showMessage(String message, Exception relatedException) {
    StringBuilder output = new StringBuilder();
    output.append(TextHelper.wrapText(new StringBuilder(message), 80));
    output.append("\n\nDebug Information\n\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\n");
    output.append(getStackTraceAsStringBuilder(relatedException));

    MainGUI.showDynamicMessageDialog("Error", output, JOptionPane.ERROR_MESSAGE, 0);
  }

  private static StringBuilder getStackTraceAsStringBuilder(Throwable t) {
    StringBuilder output = new StringBuilder();

    output.append(t);
    for (StackTraceElement element : t.getStackTrace()) {
      output.append('\n');
      output.append("    ");
      output.append(element);
    }

    Throwable cause = t.getCause();
    if (cause != null) {
      output.append("\n\n");
      output.append(getStackTraceAsStringBuilder(cause));
    }

    return output;
  }
}

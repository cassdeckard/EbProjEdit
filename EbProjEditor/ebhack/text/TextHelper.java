package ebhack.text;

/**
 * A collection of static methods that assist with processing text.
 */
public final class TextHelper {
  /**
   * In the text provided, replace spaces with newlines as necessary to prevent
   * lines from exceeding the specified number of characters. Sequences without
   * spaces are not wrapped.
   * 
   * @param text                The text to be wrapped
   * @param preferredLineLength The greatest number of characters a line should
   *                            have. Sequences without spaces might exceed this
   *                            number.
   * @return The text with newlines added as necessary.
   */
  public static StringBuilder wrapText(StringBuilder text, int preferredLineLength) {
    text.append(' ');

    int lastPosition = text.length() - 1;
    int lineStartPosition = 0;
    int currentLineLength;
    int nextBreakPosition;

    while (lineStartPosition < lastPosition) {
      nextBreakPosition = text.indexOf("\n", lineStartPosition);
      if (nextBreakPosition == -1) {
        nextBreakPosition = lastPosition;
      }
      currentLineLength = nextBreakPosition - lineStartPosition;

      if (currentLineLength > preferredLineLength) {
        wrapLine(text, lineStartPosition, nextBreakPosition, preferredLineLength);
      }

      lineStartPosition = nextBreakPosition + 1;
    }

    text.deleteCharAt(lastPosition);

    return text;
  }

  /**
   * Return the number of lines in the text provided.
   * 
   * @param text The text for which to count lines.
   * @return The number of lines in the text provided.
   */
  public static int getLineCount(StringBuilder text) {
    int breakCount = countCharacterOccurrences(text, '\n');

    if (breakCount == 0 && text.length() > 0) {
      return 1;
    }

    return breakCount;
  }

  /**
   * Return the number of occurrences of the specified character in the provided
   * text.
   * 
   * @param text      The text for which to count occurrences.
   * @param character The character to count.
   * @return The number of occurrences of the specified character in the provided
   *         text.
   */
  public static int countCharacterOccurrences(StringBuilder text, char character) {
    int count = 0;

    for (int i = 0; i < text.length(); ++i) {
      if (text.charAt(i) == character) {
        ++count;
      }
    }

    return count;
  }

  private static void wrapLine(StringBuilder text, int lineStartPosition, int lineEndPosition,
      int preferredLineLength) {
    int previousWhitespaceCharacterPosition = lineStartPosition - 1;
    int spacePosition = 0;
    int newBreakPosition;

    while (spacePosition < lineEndPosition) {
      spacePosition = text.indexOf(" ", previousWhitespaceCharacterPosition + 1);

      if (spacePosition - lineStartPosition > preferredLineLength) {
        newBreakPosition = previousWhitespaceCharacterPosition < lineStartPosition ? spacePosition
            : previousWhitespaceCharacterPosition;

        text.deleteCharAt(newBreakPosition);
        text.insert(newBreakPosition, '\n');

        previousWhitespaceCharacterPosition = newBreakPosition;
        lineStartPosition = newBreakPosition + 1;
      } else {
        previousWhitespaceCharacterPosition = spacePosition;
      }
    }
  }
}
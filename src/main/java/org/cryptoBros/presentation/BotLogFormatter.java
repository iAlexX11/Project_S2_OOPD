package org.cryptoBros.presentation;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Custom log formatter that color-codes bot trade messages.
 */
public class BotLogFormatter extends Formatter {

    /**
     * Creates a new BotLogFormatter instance.
     */
    public BotLogFormatter() {}

    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET  = "\u001B[0m";

    @Override
    public String format(LogRecord record) {
        String message = record.getMessage();
        String color;

        if (message.contains("BUY")) {
            color = GREEN;
        } else if (message.contains("SELL")) {
            color = RED;
        } else if (record.getLevel() == Level.WARNING) {
            color = YELLOW;
        } else {
            color = RESET;
        }

        return color + record.getMessage() + RESET + "\n";
    }
}

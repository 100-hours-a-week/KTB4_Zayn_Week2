package org.example.log;

import java.io.*;
import java.util.Properties;

public class TournamentLogger {
    private static final String LOG_FILE = "tournament_times_logger.txt";

    public static void saveLog(String roundName, long elapsed) {
        Properties props = new Properties();

        try (FileReader reader = new FileReader(LOG_FILE)) {
            props.load(reader);
        } catch (IOException e) {

        }

        String existing = props.getProperty(roundName, "");
        String updated = existing.isEmpty()
                ? elapsed + "ms"
                : existing + ", " + elapsed + "ms";

        props.setProperty(roundName, updated);

        try (FileWriter writer = new FileWriter(LOG_FILE)) {
            props.store(writer, null);
        } catch (IOException e) {
            System.err.println("로그 저장 실패: " + e.getMessage());
        }
    }
}
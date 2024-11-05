package hu.nye.util;

import hu.nye.model.Board;

import java.io.*;

public class FileManager {

    public void loadBoard(Board board, String filePath) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = (inputStream != null) ? new BufferedReader(new InputStreamReader(inputStream)) : null) {

            if (reader == null) {
                System.out.println("File not found: " + filePath);
                return;
            }

            board.initializeEmptyBoard(); // Initialize board to empty state
            boolean hasData = false;

            for (int i = 0; i < board.getRows(); i++) {
                String line = reader.readLine();
                if (line != null && line.trim().length() == board.getCols()) {
                    board.setRow(i, line.trim().toCharArray());
                    hasData = true;
                }
            }

            if (!hasData) {
                System.out.println("File is empty, using empty board.");
            }

        }
    }

    // Add methods for XML saving and loading in FileManager
    public void saveToXML(Board board, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("<board>\n");
            for (char[] row : board.getBoard()) {
                writer.write("  <row>" + new String(row) + "</row>\n");
            }
            writer.write("</board>\n");
        }
    }

    public void loadFromXML(Board board, String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            board.initializeEmptyBoard();
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<row>") && line.contains("</row>")) {
                    String rowData = line.replaceAll("<.*?>", "").trim();
                    board.setRow(row, rowData.toCharArray());
                    row++;
                }
            }
        }
    }

    public void saveBoard(Board board, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (char[] row : board.getBoard()) {
                writer.write(row);
                writer.newLine();
            }
            writer.write("====\n"); // Game end indicator
        }
    }
}

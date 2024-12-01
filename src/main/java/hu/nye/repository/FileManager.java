package hu.nye.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import hu.nye.model.Board;

/**
 * Kezeli a játéktáblák mentésére és betöltésére szolgáló fájlműveleteket.
 */
public class FileManager {


    /**
     * Betölti a tábla állapotát egy szövegfájlból.
     *
     * @param board fájladatokkal frissítendő táblaobjektum.
     * @param filePath A tábla adatait tartalmazó fájl elérési útja.
     * @throws IOException ha a fájl nem olvasható.
     */
    public void loadBoard(final Board board, final String filePath) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = (inputStream != null) ? new BufferedReader(new InputStreamReader(inputStream)) : null) {

            if (reader == null) {
                System.out.println("File not found: " + filePath);
                return;
            }

            board.initializeEmptyBoard(); 
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

    /**
     * Szövegfájlba menti a tábla aktuális állapotát.
     *
     * @param board A mentendő táblaobjektum.
     * @param filePath Annak a fájlnak az elérési útja, ahová a tábla adatait menti.
     * @throws IOException ha a fájl nem írható.
     */
    public void saveBoard(final Board board, final String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (char[] row : board.getBoard()) {
                writer.write(row);
                writer.newLine();
            }
            if (!isFileEndingWith(filePath, "====")) {
                writer.write("====\n");
            }
        }
    }

    /**
     * Helper metódus a fájl végét ellenőrzi
     */
    boolean isFileEndingWith(final String filePath, final String ending) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            long length = file.length();
            if (length < ending.length()) {
                return false;
            }
            file.seek(length - ending.length());
            byte[] buffer = new byte[ending.length()];
            file.readFully(buffer);
            return new String(buffer).equals(ending);
        }
    }

}

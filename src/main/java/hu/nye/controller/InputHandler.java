package hu.nye.controller;

import java.util.Scanner;

/**
 * Kezeli a Connect 4 játék felhasználói bevitelét.
 * Ez az osztály felelős azért, hogy érvényes lépést kapjon a játékostól.
 */
public class InputHandler {

    /** A felhasználótól kapott bemenet beolvasására használt képolvasó. */
    private final Scanner inputScanner;

    /**
     * Létrehoz egy InputHandlert az adott Scanner-példánnyal.
     * Ez arra szolgál, hogy összegyűjtsük a felhasználói visszajelzéseket a játékhoz.
     *
     * @param scanner a lejátszó bemenetének olvasásához nem lehet null
     */
    public InputHandler(final Scanner scanner) {
        this.inputScanner = scanner;
    }

    /**
     * Érvényes lépés megtételére kéri a játékost
     * oszlopszám megadásával.
     * A módszer ismételten bemenetet kér
     * amíg érvényes oszlopot nem ad meg.
     *
     * @param columnRange az oszlopszámok érvényes tartománya (beleértve)
     * @return a lejátszó által kiválasztott oszlopszámot
     */
    public int getPlayerMove(final int columnRange) {
        int col;
        do {
            col = getValidColumnInput(columnRange);
        } while (!isValidColumn(col, columnRange));
        return col;
    }

    /**
     * Kéri a lejátszót, hogy adjon meg egy oszlopszámot.
     * Ez a módszer ellenőrzi, hogy a bemenet érvényes egész szám-e.
     *
     * @param columnRange az érvényes tartomány oszlopszámok száma (beleértve)
     * @return a lejátszó által megadott oszlopszámot.
     */
    private int getValidColumnInput(final int columnRange) {
        int col = -1;
        System.out.print("Enter column (0-" + columnRange + ") "
                + "to make your move: ");


        if (inputScanner.hasNextInt()) {
            col = inputScanner.nextInt();
        } else {
            inputScanner.next();
            System.out.println("Invalid input. Please enter a number.");
        }
        return col;
    }

    /**
     * Ellenőrzi, hogy az adott oszlop érvényes-e.
     * Egy érvényes oszlop a megadott tartományon belül van.
     *
     * @param col az oszlop számát ellenőrizni
     * @param columnRange az érvényes tartományoszlopszámok (beleértve)
     * @return true ha az oszlop érvényes, egyébként hamis
     */
    private boolean isValidColumn(final int col, final int columnRange) {
        if (col < 0 || col > columnRange) {
            System.out.println("Invalid column. Please "
                    + "enter a number between 0 and " + columnRange + ".");
            return false;
        }
        return true;
    }
}

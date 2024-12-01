package hu.nye.controller;

import java.util.Random;

import hu.nye.model.Board;
import hu.nye.model.Player;
import hu.nye.service.GameLogic;
import hu.nye.service.MoveValidator;

/**
 * Kezeli az AI mozdulatait a Connect 4 játékban.
 * Ez az osztály felelős a
 * érvényes oszlop és mozgás az AI lejátszó számára.
 */
public class AIMoveHandler {

    /** A mozgásérvényesítő az oszlop jogszerűségének ellenőrzéséhez. */
    private final MoveValidator moveValidator;

    /** A lépés kiválasztásához használt véletlen generátor. */
    private final Random random;

    /** Az AI lépéséhez használt játéklogika. */
    private final GameLogic gameLogic;
    
    /**
     * AIMoveHandlert hoz létre
     * az adott MoveValidatorral,
     * Véletlenszerű példány és GameLogic.
     *
     * @param moveValidatorParam a mozgásérvényesítőt az oszlop jogszerűségének ellenőrzéséhez
     * @param randomParam a véletlen generátor a mozgás kiválasztásához
     * @param gameLogicParam a játék logikája a mozgások kezeléséhez
     */
    public AIMoveHandler(final MoveValidator moveValidatorParam,
                         final Random randomParam,
                         final GameLogic gameLogicParam) {
        this.moveValidator = moveValidatorParam;
        this.random = randomParam;
        this.gameLogic = gameLogicParam;
    }

    /**
     * Mozgást tesz az AI lejátszónak az adott táblán.
     * Az AI véletlenszerűen kiválaszt egy érvényes oszlopot, amely
     * nem teljes, és helyezze a darabját ebbe az oszlopba.
     *
     * @param board játéktáblán, ahol a lépés megtörténik
     * @param aiPlayer az AI lejátszó, aki mozog
     */
    public void makeMove(final Board board, final Player aiPlayer) {
        int col;


        do {
            col = random.nextInt(board.getCols());
        } while (moveValidator.isColumnFull(board, col));


        boolean moveSuccess = gameLogic.makeMove(board, col, aiPlayer.symbol());
        if (!moveSuccess) {
            System.out.println("AI move failed unexpectedly. "
                    + "Please check the logic.");
        }
    }
}

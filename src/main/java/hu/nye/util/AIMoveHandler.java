package hu.nye.util;

import hu.nye.model.Board;
import hu.nye.model.Player;

import java.util.Random;

public class AIMoveHandler {
    private final MoveValidator moveValidator;
    private final Random random;

    public AIMoveHandler(MoveValidator moveValidator, Random random) {
        this.moveValidator = moveValidator;
        this.random = random;
    }

    public void makeMove(Board board, Player aiPlayer) {
        int col;
        do {
            col = random.nextInt(board.getCols());
        } while (moveValidator.isColumnFull(board, col));

        board.makeMove(col, aiPlayer.symbol());
    }
}

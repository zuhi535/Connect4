package hu.nye.util;

import hu.nye.model.Board;
import hu.nye.model.Player;

public class PlayerMoveHandler {
    private final InputHandler inputHandler;
    private final MoveValidator moveValidator;

    public PlayerMoveHandler(InputHandler inputHandler, MoveValidator moveValidator) {
        this.inputHandler = inputHandler;
        this.moveValidator = moveValidator;
    }

    public void makeMove(Board board, Player player) {
        int col;
        do {
            col = inputHandler.getPlayerMove(board.getCols() - 1);
        } while (moveValidator.isColumnFull(board, col));

        board.makeMove(col, player.symbol());
    }
}

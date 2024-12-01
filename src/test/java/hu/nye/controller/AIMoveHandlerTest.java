package hu.nye.controller;

import hu.nye.model.Board;
import hu.nye.model.Player;
import hu.nye.service.GameLogic;
import hu.nye.service.MoveValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.mockito.Mockito.*;

public class AIMoveHandlerTest {

    private AIMoveHandler aiMoveHandler;
    private MoveValidator moveValidatorMock;
    private Random randomMock;
    private GameLogic gameLogicMock;
    private Board boardMock;
    private Player aiPlayerMock;

    @BeforeEach
    public void setUp() {
        // Mock függőségek
        moveValidatorMock = mock(MoveValidator.class);
        randomMock = mock(Random.class);
        gameLogicMock = mock(GameLogic.class);
        boardMock = mock(Board.class);
        aiPlayerMock = mock(Player.class);

        // Hozza létre a AIMoveHandler moccolt függőségekkel
        aiMoveHandler = new AIMoveHandler(moveValidatorMock, randomMock, gameLogicMock);
    }

    @Test
    public void testMakeMove_shouldChooseValidColumn() {
        int boardCols = 7; 
        int validCol = 3;
        when(boardMock.getCols()).thenReturn(boardCols);
        when(moveValidatorMock.isColumnFull(boardMock, validCol)).thenReturn(false);
        when(randomMock.nextInt(boardCols)).thenReturn(validCol);
        when(gameLogicMock.makeMove(boardMock, validCol, aiPlayerMock.symbol())).thenReturn(true);

        aiMoveHandler.makeMove(boardMock, aiPlayerMock);

        // Ellenőrizzük, hogy a GameLogic makeMove metódusa a megfelelő oszloppal lett hívva
        verify(gameLogicMock).makeMove(boardMock, validCol, aiPlayerMock.symbol());
        // Ellenőrizzük, hogy az isColumnFull metódust meghívták-e, hogy ellenőrizzék, tele van-e az oszlop
        verify(moveValidatorMock).isColumnFull(boardMock, validCol);
    }

    @Test
    public void testMakeMove_shouldRetryUntilValidColumn() {
        int boardCols = 7;
        int firstInvalidCol = 0; 
        int secondValidCol = 2; 
        when(boardMock.getCols()).thenReturn(boardCols);
        when(moveValidatorMock.isColumnFull(boardMock, firstInvalidCol)).thenReturn(true); 
        when(moveValidatorMock.isColumnFull(boardMock, secondValidCol)).thenReturn(false); 
        when(randomMock.nextInt(boardCols)).thenReturn(firstInvalidCol, secondValidCol); 
        when(gameLogicMock.makeMove(boardMock, secondValidCol, aiPlayerMock.symbol())).thenReturn(true);

        aiMoveHandler.makeMove(boardMock, aiPlayerMock);

        // Ellenőrizzük, hogy a GameLogic makeMove metódusát a érvényes oszloppal (2) hívták-e meg
        verify(gameLogicMock).makeMove(boardMock, secondValidCol, aiPlayerMock.symbol());
        // Ellenőrizzük, hogy az isColumnFull metódust mindkét oszlopra (0 és 2) meghívták-e
        verify(moveValidatorMock).isColumnFull(boardMock, firstInvalidCol);
        verify(moveValidatorMock).isColumnFull(boardMock, secondValidCol);
    }

    @Test
    public void testMakeMove_shouldHandleMoveFailure() {
        int validCol = 3;
        when(boardMock.getCols()).thenReturn(7);
        when(moveValidatorMock.isColumnFull(boardMock, validCol)).thenReturn(false);
        when(randomMock.nextInt(7)).thenReturn(validCol);
        when(gameLogicMock.makeMove(boardMock, validCol, aiPlayerMock.symbol())).thenReturn(false);

        aiMoveHandler.makeMove(boardMock, aiPlayerMock);

        // Ellenőrizzük, hogy a metódus meghívása megtörtént
        verify(gameLogicMock).makeMove(boardMock, validCol, aiPlayerMock.symbol());
    }
}

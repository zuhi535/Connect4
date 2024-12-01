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

        // Hozza létre a AIMoveHandler with moccolt függőségekkel
        aiMoveHandler = new AIMoveHandler(moveValidatorMock, randomMock, gameLogicMock);
    }

    @Test
    public void testMakeMove_shouldChooseValidColumn() {
        // Given
        int boardCols = 7; // Feltételezve, hogy a tábla 7 oszlopból áll
        int validCol = 3; // Az oszlop, amelyet a mesterséges intelligenciának ki kell választania
        when(boardMock.getCols()).thenReturn(boardCols);
        when(moveValidatorMock.isColumnFull(boardMock, validCol)).thenReturn(false); // Az oszlop nincs tele
        when(randomMock.nextInt(boardCols)).thenReturn(validCol); // Szimuláljuk, hogy az AI a 3. oszlopot választja
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
        int firstInvalidCol = 0; // Szimuláljuk, hogy az első oszlop tele van
        int secondValidCol = 2; // A második oszlop, amelyet a mesterséges intelligenciának ki kell választania
        when(boardMock.getCols()).thenReturn(boardCols);
        when(moveValidatorMock.isColumnFull(boardMock, firstInvalidCol)).thenReturn(true); // Az 0-ás oszlop tele van
        when(moveValidatorMock.isColumnFull(boardMock, secondValidCol)).thenReturn(false); // A 2. oszlop nincs tele
        when(randomMock.nextInt(boardCols)).thenReturn(firstInvalidCol, secondValidCol); // Szimuláljuk, hogy az AI először a 0-ás oszlopot próbálja, majd a 2-es oszlopot
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
        when(gameLogicMock.makeMove(boardMock, validCol, aiPlayerMock.symbol())).thenReturn(false); // Szimuláljuk, hogy a lépés végrehajtása sikertelen

        aiMoveHandler.makeMove(boardMock, aiPlayerMock);

        // Arra számítunk, hogy az AI szépen lekezeli a hibát és naplózza az üzenetet
        // Ellenőrizzük, hogy a metódus meghívása megtörtént
        verify(gameLogicMock).makeMove(boardMock, validCol, aiPlayerMock.symbol());
        // A naplóüzenetet nem tudjuk közvetlenül ellenőrizni a tesztben, de szükség esetén naplózási keretrendszeren keresztül ellenőrizhetjük
    }
}

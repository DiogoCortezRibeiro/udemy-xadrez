package application;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import exceptions.ChessException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TesteApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        while(true) {
            try {
                for (int i = 0; i < 50; ++i) System.out.println();
                UI.printBoard(chessMatch.getPieces());

                System.out.println();

                System.out.print("Origem: ");
                ChessPosition source = UI.readChessPosition(sc);

                System.out.print("Destino: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

            }catch (ChessException e)
            {
                System.out.println(e.getMessage());
                sc.nextLine();
            }catch(InputMismatchException e)
            {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
    }
}

package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import exceptions.ChessException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces   = new ArrayList<>();

    public ChessMatch() {
        this.board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public boolean getCheck()
    {
        return check;
    }

    public boolean getCheckMate()
    {
        return checkMate;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessPiece[][] getPieces()
    {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for(int i = 0; i < board.getRows(); i++)
        {
            for(int j = 0; j < board.getColumns(); j++)
            {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }

        return mat;
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targePosition)
    {
        Position source = sourcePosition.toPosition();
        Position target = targePosition.toPosition();

        validateSourcePosition(source);

        validateTargetPosition(source, target);

        Piece capturePiece = makeMove(source, target);

        if(testCheck(currentPlayer))
        {
            undoMove(source, target, capturePiece);
            throw new ChessException("Voce nao pode se colocar em check");
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if(testeCheckMate(opponent(currentPlayer)))
        {
            checkMate = true;
        }else
        {
            nextTurn();
        }

        return (ChessPiece) capturePiece;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition)
    {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.getPiece(position).possibleMoves();
    }

    private void validateTargetPosition(Position source, Position target) {
        if(!board.getPiece(source).possibleMove(target))
        {
            throw new ChessException("Movimento invalido da peça");
        }
    }

    private void nextTurn()
    {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece captured = board.removePiece(target);
        board.placePiece(p, target);

        if(capturedPieces != null)
        {
            piecesOnTheBoard.remove(captured);
            capturedPieces.add(captured);
        }

        return captured;
    }

    private void undoMove(Position souce, Position target, Piece capturedPiece)
    {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, souce);

        if(capturedPiece != null)
        {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    private void validateSourcePosition(Position source) {
        if(!board.thereIsAPiece(source))
        {
            throw new ChessException("Não existe peça na posição de origem");
        }

        if(!board.getPiece(source).isThereAnypossibleMove())
        {
            throw new ChessException("Sem movimento possivel para a peça");
        }

        if(currentPlayer != ((ChessPiece)board.getPiece(source)).getColor())
        {
            throw new ChessException("Peça do adversario, movimento invalido");
        }
    }

    private void placeNewPiece(char column, int row, ChessPiece piece)
    {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup()
    {
        placeNewPiece('h', 7, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('b', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 8, new King(board, Color.BLACK));
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
    }

    private Color opponent(Color color)
    {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color)
    {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());

        for(Piece p : list)
        {
            if(p instanceof King)
            {
                return (ChessPiece) p;
            }
        }

        throw new IllegalStateException("Não existe o rei da cor: " + color + " no tabuleiro");
    }

    private boolean testeCheckMate(Color color)
    {
        if(!testCheck(color))
        {
            return false;
        }

        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());

        for(Piece p : list)
        {
            boolean[][] mat = p.possibleMoves();
            for(int i = 0; i < board.getRows(); i++)
            {
                for(int j = 0; j < board.getColumns(); j++)
                {
                    if(mat[i][j])
                    {
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if(!testCheck)
                        {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean testCheck(Color color)
    {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());

        for(Piece p : opponentPieces)
        {
            boolean[][] mat = p.possibleMoves();
            if(mat[kingPosition.getRow()][kingPosition.getColumn()])
            {
                return true;
            }
        }

        return false;
    }
}

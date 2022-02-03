package boardgame;

import exceptions.BoardException;

public class Board {

    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns)
    {
        if(rows < 1 || columns < 1)
        {
            throw new BoardException("Erro criando tabuleiro, é nescesario que haja ao menos uma linha e uma coluna");
        }
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Piece piece(int row, int column)
    {
        return pieces[row][column];
    }

    public Piece getPiece(Position position)
    {

        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position)
    {
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private boolean positionExists(int row, int column)
    {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean positionExists(Position position)
    {
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position)
    {
        return this.getPiece(position) != null;
    }

    public Piece removePiece(Position position)
    {
        if(!positionExists(position))
        {
            throw new BoardException("Posição not on the board");
        }

        if(getPiece(position) == null)
        {
            return null;
        }

        Piece aux = getPiece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;

        return aux;
    }
}

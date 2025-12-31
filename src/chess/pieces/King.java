package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.enums.Color;

public class King extends ChessPiece {
    public King(Board board, Color color){
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean canMove(Position position) {
        ChessPiece piece = (ChessPiece) getBoard().piece(position);
        return piece == null || piece.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        int[][] directions = {
                {-1,  0}, // up
                {-1,  1}, // up right
                { 0,  1}, // right
                { 1,  1}, // down right
                { 1,  0}, // down
                { 1, -1}, // down left
                { 0, -1}, // left
                {-1, -1}  // up left
        };

        for (int[] d : directions) {
            p.setValues(position.getRow() + d[0], position.getColumn() + d[1]);

            if (getBoard().positionExists(p) && canMove(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        return mat;
    }

}

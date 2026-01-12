package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.enums.Color;
import chess.exceptions.ChessException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ChessGUI extends JFrame {

    private ChessMatch chessMatch;
    private JButton[][] buttons = new JButton[8][8];
    private ChessPosition sourcePosition = null;
    private Map<String, ImageIcon> iconCache = new HashMap<>();
    private JPanel whiteCapturedPanel;
    private JPanel blackCapturedPanel;

    public ChessGUI() {
        chessMatch = new ChessMatch();
        initializeGUI();
    }

    private void setInitialButtonColors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    buttons[i][j].setBackground(new java.awt.Color(240, 240, 240));
                } else {
                    buttons[i][j].setBackground(new java.awt.Color(150, 150, 150));
                }
            }
        }
    }

    private void initializeGUI() {
        setTitle("Java Chess Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("SansSerif", Font.BOLD, 40));
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleButtonClick(row, col);
                    }
                });
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        whiteCapturedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        blackCapturedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        whiteCapturedPanel.setPreferredSize(new Dimension(105, 600));
        blackCapturedPanel.setPreferredSize(new Dimension(105, 600));
        whiteCapturedPanel.setBorder(BorderFactory.createTitledBorder("White Captured"));
        blackCapturedPanel.setBorder(BorderFactory.createTitledBorder("Black Captured"));

        add(whiteCapturedPanel, BorderLayout.WEST);
        add(blackCapturedPanel, BorderLayout.EAST);

        setInitialButtonColors();
        updateBoard();
        setVisible(true);
    }

    private void updateBoard() {
        ChessPiece[][] pieces = chessMatch.getPieces();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] == null) {
                    buttons[i][j].setText("");
                    buttons[i][j].setIcon(null);
                } else {
                    ImageIcon icon = getPieceIcon(pieces[i][j]);
                    if (icon != null) {
                        buttons[i][j].setIcon(icon);
                        buttons[i][j].setText("");
                    } else {
                        if (pieces[i][j].getColor() == chess.enums.Color.WHITE) {
                            buttons[i][j].setForeground(new java.awt.Color(50, 50, 50));
                        } else {
                            buttons[i][j].setForeground(java.awt.Color.BLACK);
                        }
                    }
                }
            }
        }
        updateCapturedPieces();
    }

    private void updateCapturedPieces() {
        whiteCapturedPanel.removeAll();
        blackCapturedPanel.removeAll();

        for (boardgame.Piece piece : chessMatch.getCapturedPieces()) {
            ChessPiece p = (ChessPiece) piece;
            JLabel label = new JLabel();
            ImageIcon icon = getPieceIcon(p);
            if (icon != null) {
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImg));
            } else {
                label.setText(p.toString());
            }

            if (p.getColor() == Color.WHITE) {
                blackCapturedPanel.add(label);
            } else {
                whiteCapturedPanel.add(label);
            }
        }
        whiteCapturedPanel.revalidate();
        whiteCapturedPanel.repaint();
        blackCapturedPanel.revalidate();
        blackCapturedPanel.repaint();
    }

    private ImageIcon getPieceIcon(ChessPiece piece) {
        String color = piece.getColor() == chess.enums.Color.WHITE ? "white" : "black";
        String pieceName = piece.getClass().getSimpleName().toLowerCase();
        String key = color + "-" + pieceName;
        
        if (iconCache.containsKey(key)) {
            return iconCache.get(key);
        }

        String path = "/boardgame/icons/" + key + ".png";
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImg);
            iconCache.put(key, scaledIcon);
            return scaledIcon;
        }
        return null;
    }

    private void handleButtonClick(int row, int col) {
        ChessPosition clickedPosition = chess.ChessPosition.fromPosition(new boardgame.Position(row, col));

        if (sourcePosition == null) {
            try {
                boolean[][] possibleMoves = chessMatch.possibleMoves(clickedPosition);
                sourcePosition = clickedPosition;
                highlightPossibleMoves(possibleMoves);
            } catch (ChessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        } else {
            try {
                chessMatch.performChessMove(sourcePosition, clickedPosition);
                sourcePosition = null;
                clearHighlights();
                updateBoard();
                checkGameOver();
            } catch (ChessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
                sourcePosition = null;
                clearHighlights();
            }
        }
    }

    private void highlightPossibleMoves(boolean[][] possibleMoves) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (possibleMoves[i][j]) {
                    buttons[i][j].setBackground(new java.awt.Color(170, 240, 170));
                }
            }
        }
        buttons[sourcePosition.toPosition().getRow()][sourcePosition.toPosition().getColumn()].setBackground(java.awt.Color.YELLOW);
    }

    private void clearHighlights() {
        setInitialButtonColors();
    }

    private void checkGameOver() {
        if (chessMatch.getCheckMate()) {
            String winner = chessMatch.getCurrentPlayer().toString();
            winner = winner.substring(0, 1).toUpperCase() + winner.substring(1).toLowerCase();
            JOptionPane.showMessageDialog(this, winner + " wins!");
            System.exit(0);
        } else if (chessMatch.getCheck()) {
            JOptionPane.showMessageDialog(this, "CHECK!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGUI());
    }
}

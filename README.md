# Java Chess Game

A fully functional chess game implemented in Java, featuring a Graphical User Interface (GUI). This project demonstrates strong **OOP** principles and clean code architecture.

## Features:

- **Full Chess Logic**: Implementation of all chess rules, including piece-specific movements.
- **Special Moves**: 
  - **Castling**: King-side and Queen-side castling supported.
  - **En Passant**: Correct handling of the special pawn capture.
- **Interactive Board**: Modern interactive board using Java Swing.
- **Captured Pieces**: Live display of captured pieces.
- **Game Alerts**: Dynamic alerts for Check and Checkmate.
- **Move Validation**: Prevents illegal moves, including those that would put your own king in check.

- **OOP Principles**:
  - **Inheritance**: Base `Piece` and `ChessPiece` classes extended by specific piece types.
  - **Polymorphism**: Unified movement handling through abstract methods.
  - **Encapsulation**: Game state protected within the `ChessMatch` and `Board` classes.
  - **Exception Handling**: Custom exceptions for game-specific errors.

## Structure:

- `application`: Entry point (`Main`) and GUI logic (`ChessGUI`).
- `boardgame`: Generic board game engine logic (Board, Piece, Position).
- `chess`: Domain-specific chess logic (Match, Pieces, Positions).
- `chess.pieces`: Individual implementations for each chess piece.

## Personal Notes:
It´s important to say that part of this project was a module of "Nelio Alves - Java Completo POO" course on udemy, through this project I tried to not only copy and paste his code, but do it my own, with my own understanding of the program logic, personally, I didn't have that much knowledge of chess rules, so I had to do a search on each piece of the game to not miss any detail, I've faced many issues developing this, such as the logic on Check and CheckMate, source/target validation, special moves and the GUI with Swing, which wasn't on the course module, because it was only a terminal game, so I decided do challenge myself and implement it, previously I had no experience with Swing, but I'm very happy with the result.
For me, this project was a great way to learn more about OOP, Inheritance, Logic, get started with Swing and how to implement it in a real project.

## GUI Preview:
![img.png](img.png)


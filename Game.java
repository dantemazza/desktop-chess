package chess;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import javafx.scene.paint.*;
import javafx.scene.control.Label.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.scene.input.*;

public class Game extends Application{
	 final String white = "white";
	 final String black = "black";
	int clicked = -1;
	Label squares[][] = new Label[8][8];
	Board gameBoard = new Board(black,white);
	BorderPane pane = new BorderPane();
	HBox top = new HBox();
	HBox bottom = new HBox();	
	VBox left = new VBox();
	VBox right = new VBox();
	String topSide, bottomSide;
	ScrollPane moveScroller = new ScrollPane();
	Label moveLabel = new Label();
	Vector lastClicked = new Vector();
	Image leftArrow = new Image("file:resources/left_arrow.png");
	Image rightArrow = new Image("file:resources/right_arrow.png");
	ArrayList<Board> storedBoards = new ArrayList<Board>();
	int moveKey = 0, maxMoveKey = 0;
	boolean moveKeyAddOne = true;
	
	public void start(Stage primaryStage) throws Exception{
		storedBoards.add(gameBoard.clone(gameBoard));
		Button button = new Button("Change Side");
		Button exit = new Button("Exit");
		button.setOnAction(e ->{ 
			System.out.println("Hello World!");
			System.out.println("Oh Yeah!");
		});
		
		top.getChildren().addAll(button, exit);
		exit.setOnAction(e -> System.exit(0));
		pane.setStyle("-fx-background-color: #98ff98;");
		pane.setTop(top);
		this.setBoard(black,white);
		this.updateImageBoard(gameBoard);
		this.addEventsToBoard();
		
		this.setLeft();
		this.setRight();
		
		this.setBottom();
		Scene scene = new Scene(pane, 900, 695, Color.GREEN);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Yes");
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	

	 public static void main(String args[]) {
		 launch(args);}
	 
	 
	 public void setBoard(String top, String bottom) {
		 topSide = top;
		 bottomSide = bottom; 
		 Label h[][] = new Label[8][8];
		 GridPane board = new GridPane();
		 int c = 1;
		
		 for(int a=0; a<8; a++) { c*= -1; 
			 for(int b=0; b<8; b++) { 
				 Label square = new Label();
				 squares[a][b] = square;
				 squares[a][b].setMinSize(80, 80);
				 
				 
//				 squares[a][b].setText(Integer.toString(a) + " " + Integer.toString(b));
				 if(c == -1) squares[a][b].setStyle("-fx-background-color: #f5f5dc;");
				 if(c == 1)  squares[a][b].setStyle("-fx-background-color: #add8e6;");
				 board.add(squares[a][b], a, b);
				 c*= -1; 
			 }
		 }

		 pane.setCenter(board);
	 }
	 public void setLeft() {
		 if(topSide == white) {
			 for(int a=1; a<9; a++) {
				 Label num = new Label("  " + Integer.toString(a) + " ");
				 num.setMinSize(15, 80);
				 left.getChildren().add(num); 
			 }
		 }else {
			 for(int a=8; a>0; a--) {
				 Label num = new Label("  " + Integer.toString(a) + " ");
				 num.setMinSize(15, 80);
				 left.getChildren().add(num); 
			 }
		 } pane.setLeft(left);
	 }
	 public void setRight() {
		moveScroller.setMinSize(200, 500);
		
		moveLabel.setMinSize(100,20000);
		moveLabel.setAlignment(Pos.TOP_LEFT);
		moveLabel.setText("console under construction");
		moveScroller.setContent(moveLabel);
		right.getChildren().add(moveScroller);
		right.setPadding(new Insets(17,17,17,17));
		
		HBox forback = new HBox();
		Label undo = new Label(), redo = new Label();

		ImageView ivLarr = new ImageView(leftArrow);
		ivLarr.setFitHeight(100);
		ivLarr.setFitWidth(100);
		undo.setGraphic(ivLarr);
		
		undo.setOnMouseClicked(e ->{
			if(moveKey == 0) return;
			System.out.println("inleft " + moveKey);
			gameBoard = storedBoards.get(moveKey-1).clone(storedBoards.get(moveKey-1));
			moveKey--; 
			this.updateImageBoard(gameBoard);
//			moveKeyAddOne = true;
		});
		
		
		ImageView ivRarr = new ImageView(rightArrow);
		ivRarr.setFitHeight(100);
		ivRarr.setFitWidth(100);
		redo.setGraphic(ivRarr);
	
		redo.setOnMouseClicked(e ->{
			if(moveKey == maxMoveKey) return;
			System.out.println("inright " + moveKey);

//			if(moveKeyAddOne == true)moveKey++;
			moveKey++;
			gameBoard = storedBoards.get(moveKey).clone(storedBoards.get(moveKey));
			this.updateImageBoard(gameBoard);
//			moveKeyAddOne = false;
		});
		
		
		forback.getChildren().addAll(undo,redo);	
		
		
		right.getChildren().add(forback);
		pane.setRight(right);  
	 }
	 
	 
	 public void setBottom() {
		 Label blfill = new Label();
		 blfill.setMinSize(50, 9);
		 bottom.getChildren().add(blfill);
		 for(char a = 'a'; a<'i'; a++) {
			 Label let = new Label(Character.toString(a));
			 let.setMinSize(80, 15);
			 bottom.getChildren().add(let); 
	 } pane.setBottom(bottom);
}
	 
	 public void updateImageBoard(Board board) {
		 for(int j=0; j<8; j++) {
			 for(int h=0; h<8; h++) {
				 squares[j][h].setGraphic(null);
			 }
		 }
		 for(int a=0; a<8; a++) {
			 for(int d=0; d<8; d++) { 
				 if(board.isOccupied(a,d)) {
					Piece piece = board.getSquare(a,d);
					ImageView pieces;
					if(piece.side.equals(white)) {
					if(piece instanceof Pawn) {
					pieces = new ImageView("file:resources/pawn_white.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);	
					}
					if(piece instanceof King) {
				    pieces = new ImageView("file:resources/king_white.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);
					}
					if(piece instanceof Rook) {
					pieces = new ImageView("file:resources/rook_white.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);						
					squares[a][d].setGraphic(pieces);
					}
					if(piece instanceof Knight) {
				    pieces = new ImageView("file:resources/knight_white.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);
					}
					if(piece instanceof Bishop) {
					pieces = new ImageView("file:resources/bishop_white.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);
					}
					if(piece instanceof Queen) {
					pieces = new ImageView("file:resources/queen_white.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);	
					}}
					if(piece.side.equals(black)) { 
					if(piece instanceof Pawn) {
					pieces = new ImageView("file:resources/pawn_black.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);
					}
					if(piece instanceof King) {
					pieces = new ImageView("file:resources/king_black.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);
					}
					if(piece instanceof Rook) {
					pieces = new ImageView("file:resources/rook_black.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);
					}
					if(piece instanceof Knight) {
					pieces = new ImageView("file:resources/knight_black.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);
					}
					if(piece instanceof Bishop) {
					pieces = new ImageView("file:resources/bishop_black.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);
					}
					if(piece instanceof Queen) {
					pieces = new ImageView("file:resources/queen_black.png");
					pieces.setFitHeight(80);
					pieces.setFitWidth(80);
					squares[a][d].setGraphic(pieces);
					}}}}}
	 			}
	 public void addEventsToBoard() {
		 
		 for(int w=0; w<8; w++) {
			 for(int z=0; z<8; z++) { 
				 final int x = w, y = z;
				 squares[x][y].setOnMouseClicked(e ->{	 System.out.println(x + "," + y);
					 if(!gameBoard.isOccupied(x,y) && clicked == -1) return;
						if(clicked == -1) {
							lastClicked.setVector(x,y);
							
						}
						if(clicked == 1) { 
							
							gameBoard.getSquare(lastClicked).move(new Vector(x,y), gameBoard);
							
								if(!gameBoard.wasMoveInvalid) {
									if(moveKey != maxMoveKey) {
//										if(maxMoveKey == 1 ) { storedBoards.remove(1); break key;}
										System.out.println("clear " + (moveKey+1) + " to " + maxMoveKey);
										System.out.println("size " + storedBoards.size());
										for(int j=maxMoveKey; j>=moveKey+1; j--) {
											storedBoards.remove(j);
										}
										System.out.println("size " + storedBoards.size());
									}
								this.updateImageBoard(gameBoard);
								storedBoards.add(gameBoard.clone(gameBoard));
								
								this.moveKey = gameBoard.getMoveCount();
								maxMoveKey = gameBoard.getMoveCount();
								System.out.println("move " + moveKey + " max " + (maxMoveKey));

								}
							
						}
						clicked *= -1;
					}
					 
				 );
			 }
		 }
	 }
	 

 } 


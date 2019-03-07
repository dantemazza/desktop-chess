package chess;

import java.util.ArrayList;
//make isInCheck method apply to all piece
public class Board{
	private Piece board[][] = new Piece[8][8];
	char letters = 'a';
	public boolean inCheck, inDoubleCheck, isATestBoard = false, wasACheck;
	private int whiteScore, blackScore, moveCount=0;
	private String capturedSide;
	private int whoseTurn = 1; 
	public Piece lastPieceMoved, lastPieceToCheck;
	public Vector lastPieceStart = new Vector(), lastPieceEnd = new Vector();
	public Vector whiteKingPosition, blackKingPosition;
	public final String white = "white";
	public final String black = "black";
	public final int xC[] = {1,2,2,1,-1,-2,-2,-1}; 
	public final int yC[] = {2,1,-1,-2,-2,-1,1,2};
	public boolean wasMoveInvalid = false;
	Board(String topSide, String bottomSide){
		board[0][0] = new Rook(0, 0, "Ra1", topSide, "Bottom");
		board[1][0] = new Knight(1, 0, "Nb1", topSide, "Bottom");
		board[2][0] = new Bishop(2, 0, "Bc1", topSide, "Bottom");
		board[3][0] = new Queen(3, 0, "Qd1", topSide, "Bottom");			
		board[4][0] = new King(4, 0, "Ke1", topSide, "Bottom");
		board[5][0] = new Bishop(5, 0, "Bf1", topSide, "Bottom");
		board[6][0] = new Knight(6, 0, "Ng1", topSide, "Bottom");
		board[7][0] = new Rook(7, 0, "Rh1", topSide, "Bottom");
		for(int a=0; a<8; a++) {
			board[a][1] = new Pawn(a, 1, "P" + letters + 2, topSide, "Bottom");
			letters++;
		}
		
		letters = 'a';
		board[0][7] = new Rook(0, 7, "Ra8", bottomSide, "Top");
		board[1][7] = new Knight(1, 7, "Nb8", bottomSide, "Top");
		board[2][7] = new Bishop(2, 7, "Bc8", bottomSide, "Top");
		board[3][7] = new Queen(3, 7, "Qd8", bottomSide, "Top");			
		board[4][7] = new King(4, 7, "Ke8", bottomSide, "Top");
		board[5][7] = new Bishop(5, 7, "Bf8", bottomSide, "Top");
		board[6][7] = new Knight(6, 7, "Ng8", bottomSide, "Top");
		board[7][7] = new Rook(7, 7, "Rh8", bottomSide, "Top");		
		for(int a=0; a<8; a++) {
			board[a][6] = new Pawn(a, 6, "P" + letters + 7, bottomSide, "Top");
			letters++;
		}
		if(board[4][0].side.equals(white)) { 
			whiteKingPosition = new Vector(4,0);
			blackKingPosition = new Vector(4,7);
		}
		else {
			whiteKingPosition = new Vector(4,7);
			blackKingPosition = new Vector(4,0);
		}
	}
	
	Board(){
//		board[3][3] = new King(3,3, "kng", "black", "Top");
//		blackKingPosition = new Vector(3,3);
//		whiteKingPosition = new Vector(0,0);
//		board[3][7] = new Bishop(3,7, "Bsp", "black", "Bottom");
//		board[6][6] = new Queen(6,6, "Bsp", "white", "Bottom");
	}
	// conducts all end of turn procedures (variable, score update etc.)
	public void endTurn(Piece piece, Vector place) {
		if(piece.getClass().getName().equals("chess.King")) {
			if(piece.side.equals(black)) { blackKingPosition.setVector(place);
			}
			else whiteKingPosition.setVector(place);
		}	
		 lastPieceStart.setVector(piece.getPosition());	
		 lastPieceEnd.setVector(place);
		 
		 piece.setPosition(place);
		 if(this.isATestBoard == false) {
			 lastPieceMoved = piece;
			 
			 if(lastPieceMoved instanceof Pawn && (lastPieceEnd.getY() == 0 || lastPieceEnd.getY() == 7))
				 this.setSquare((Piece)new Queen(lastPieceEnd.getX(), lastPieceEnd.getY(), "Q" + moveCount, piece.side, piece.sideOfBoard), lastPieceEnd);
		 }
		 moveCount++;
		 whoseTurn *= -1; 

		 switch(whoseTurn) {
		 case(1):{
			 inCheck = this.isInCheck((King)this.getSquare(whiteKingPosition));			
			 break;
		 }
		 case(-1):{
			 inCheck = this.isInCheck((King)this.getSquare(blackKingPosition));
			 break;
		 }}	 

	}
	public Board clone(Board c) {
		Board boardClone = new Board();
		for(int a=0; a<8; a++) {
			for(int b=0; b<8; b++) {
				if(c.isOccupied(new Vector(a,b))) {
				try {
					boardClone.board[a][b] = (Piece)c.board[a][b].clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}}
			}
		}
		boardClone.inCheck = c.inCheck;
		boardClone.whiteScore = c.whiteScore;
		boardClone.blackScore = c.blackScore;
		boardClone.whoseTurn = c.whoseTurn;
		boardClone.moveCount = c.getMoveCount();
		if(moveCount !=0) {
		try {
			boardClone.lastPieceMoved = (Piece) c.lastPieceMoved.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boardClone.lastPieceStart.setVector(c.lastPieceStart);
		boardClone.lastPieceEnd.setVector(c.lastPieceEnd);
		}
		boardClone.whiteKingPosition = new Vector(c.whiteKingPosition);
		
		boardClone.blackKingPosition = new Vector(c.blackKingPosition);
		return boardClone;
	}
	
	public void updateScore() {
		 capturedSide = lastPieceMoved.side;
		 if(capturedSide.equals(white)) blackScore += lastPieceMoved.score;
		 else whiteScore += lastPieceMoved.score;
	}
	
	public int getScore() {
		return whiteScore - blackScore;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	
	
	
	
	
	public boolean isOccupied(Vector v) {
		return this.getSquare(v) != null;
	}
	public boolean isOccupied(int x, int y) {
		return this.getSquare(x,y) != null;
	}
	
	// creates a board sand box to see verify if a move is valid.
	public boolean validateMove(Piece piece, Vector place, boolean isntChecker){
		//returns true if invalid, false if valid
 		if(piece instanceof King) {
		for(int m=0; m<place.getDiag().size(); m++) {
 			if(this.isOccupied(place.getDiag().get(m))){
 				if(this.getSquare(place.getDiag().get(m)) instanceof King &&
 				   this.getSquare(place.getDiag().get(m)).side !=piece.side) return true;}
 		}
 		for(int m=0; m<place.getHorzVert().size(); m++) {
 			if(this.isOccupied(place.getHorzVert().get(m))){
 				if(this.getSquare(place.getHorzVert().get(m)) instanceof King &&
 				   this.getSquare(place.getHorzVert().get(m)).side != piece.side) return true;}
 		}}
		Board testCheck = new Board();
		 testCheck = clone(this);
		 testCheck.isATestBoard = true;
		 testCheck.getSquare(piece.getPosition()).move(place, testCheck);
		 if(testCheck.getSquare(piece.getPosition()) != null) return true;
		 /*adjusts "whoseTurn" in case it is not the players turn but we would still like to test moves for them
		  */
		 if(isntChecker)
		 testCheck.whoseTurn *= -1;
		 switch(testCheck.whoseTurn) {
		 case(1):{ 

			 return testCheck.isInCheck((King)testCheck.getSquare(testCheck.whiteKingPosition));
			 
			 
		 }
		 case(-1):{

			 return testCheck.isInCheck((King)testCheck.getSquare(testCheck.blackKingPosition));
			 
	 
			
		 }}	 
		 System.out.println("moveInvalid bypass");
		return false;
	}	
	// prints the board on the console
	public void print() {
		for(int c=7; c>-1; c--) {
			if(c!=7) System.out.println();
			for(int b=0; b<8; b++){
			try{System.out.print(board[b][c].name + " ");}
			catch(NullPointerException e){System.out.print(" 0  ");}
			}}
		System.out.println();
		
	}

	/* this method is  adjusted to apply to any piece input. In the context of a non-king parameter, 
	   the method is used to determine if a piece delivering the only check to a king can be captured 
	   by the checked side legally. */
	public boolean isInCheck(Piece piece) {
		int checks = 0;
		int y = piece.getPosition().getY();
		int x = piece.getPosition().getX();
		boolean isKing = piece instanceof King;
		if(piece.sideOfBoard == "Bottom" && y < 6) {
		try {
			if((this.getSquare(x+1,y+1).getClass().getName().equals("chess.Pawn") && 
				this.getSquare(x+1,y+1).side != piece.side)) {
				if(isKing) {
				lastPieceToCheck = this.getSquare(x+1, y+1);
				return true;
				}else {
					if(!this.validateMove(this.getSquare(x+1, y+1),piece.getPosition(), false)) return true;	
				}
			} 
			if((this.getSquare(x-1, y+1).side != piece.side &&  
				this.getSquare(x-1,y+1).getClass().getName().equals("chess.Pawn"))) {
				if(isKing) {
				lastPieceToCheck = this.getSquare(x-1, y+1);
				return true;
				}else {
					if(!this.validateMove(this.getSquare(x-1, y+1),piece.getPosition(), false)) return true;	
				}
			} 
					
		}catch(NullPointerException | ArrayIndexOutOfBoundsException e){} }
		

		if(piece.sideOfBoard == "Top" && y > 1) {
		try {
			if((this.getSquare(x+1,y-1).getClass().getName().equals("chess.Pawn") && 
					this.getSquare(x+1,y-1).side != piece.side)) {
					if(isKing){
					lastPieceToCheck = this.getSquare(x+1, y-1);
					return true;
					}else {
						if(!this.validateMove(this.getSquare(x+1, y-1),piece.getPosition(), false)) return true;	
					}
				} 
				if((this.getSquare(x-1, y-1).side != piece.side &&  
					this.getSquare(x-1,y-1).getClass().getName().equals("chess.Pawn"))) {
					if(isKing){
					lastPieceToCheck = this.getSquare(x-1, y-1);
					return true;
					}else {
						if(!this.validateMove(this.getSquare(x-1, y-1),piece.getPosition(), false)) return true;	
					}
				}	
		}catch(NullPointerException | ArrayIndexOutOfBoundsException e){}}
		for(int a=x+1; a<=7; a++) {
			if(this.isOccupied(a,y)) {
				if(this.isHostilePiece("chess.Rook", a, y, piece.side, isKing)) { checks++;
					if(!isKing && !this.validateMove(this.getSquare(a,y), piece.getPosition(), false))
						return true;			
				} 
				else break;
			}}
		for(int a=x-1; a>=0; a--) {
			if(this.isOccupied(a,y)) {
				if(this.isHostilePiece("chess.Rook", a, y, piece.side, isKing)) { checks++;
					if(!isKing && !this.validateMove(this.getSquare(a,y), piece.getPosition(), false))
						return true;
				}
				else break;
			}}

		for(int a=y+1; a<=7; a++) {
			if(this.isOccupied(x,a)) {
				if(this.isHostilePiece("chess.Rook", x, a, piece.side, isKing)) { checks++;
					if(!isKing && !this.validateMove(this.getSquare(x,a), piece.getPosition(), false))
						return true;
				}
				else break;
			}}
		for(int a=y-1; a>=0; a--) {
			if(this.isOccupied(x,a)) {
				if(this.isHostilePiece("chess.Rook", x, a, piece.side, isKing)) { checks++;
					if(!isKing && !this.validateMove(this.getSquare(x,a), piece.getPosition(), false))
						return true;
				}
				else break;
				
			}}
		
		int b=y+1;
		for(int a=x+1; a<=7; a++) {
			if(b>7) break;
			if(this.isOccupied(a,b)) {
				if(this.isHostilePiece("chess.Bishop", a, b, piece.side, isKing)) { checks++;
					if(!isKing && !this.validateMove(this.getSquare(a,b), piece.getPosition(), false))
						return true;
				}
				else break;
			}
			b++; 			
			
		}
		b=y+1;
		for(int a=x-1; a>=0; a--) {
			if(b>7) break;
			if(this.isOccupied(a,b)) {
				if(this.isHostilePiece("chess.Bishop", a, b, piece.side, isKing)) { checks++;
					if(!isKing && !this.validateMove(this.getSquare(a,b), piece.getPosition(), false))
						return true;
				}
				else break;
			}
			b++;
		}
		b=y-1;
		for(int a=x+1; a<=7; a++) {
			if(b<0) break;
			if(this.isOccupied(a,b)) {
				if(this.isHostilePiece("chess.Bishop", a, b, piece.side, isKing)) { checks++;
					if(!isKing && !this.validateMove(this.getSquare(a,b), piece.getPosition(), false))
						return true;
				}
				else break;
			}
			b--; 
		}
		b=y-1;
		for(int a=x-1; a>=0; a--) {
			if(b<0) break;
			if(this.isOccupied(a,b)) {
				if(this.isHostilePiece("chess.Bishop", a, b, piece.side, isKing)) { checks++;
					if(!isKing && !this.validateMove(this.getSquare(a,b), piece.getPosition(), false))
						return true;
				}
				else break;
			}
			b--; 
		}
		
		for(int i=0; i<8; i++) {
			if(x+xC[i] > 7 || y+yC[i] > 7 || x+xC[i] < 0 || y+yC[i] < 0) continue;
			if(isOccupied(x+xC[i],y+yC[i])) { 	
			if(this.isHostileKnight(x+xC[i],y+yC[i],piece.side, isKing)) { checks++;
				if(!isKing && !this.validateMove(this.getSquare(x+xC[i],y+yC[i]), piece.getPosition(), false))
					return true;
			} 
		}}	
		
		if(isKing) {
		if(checks == 1) return true;
		if(checks == 2) { this.inDoubleCheck = true; return true; }
		return false;}
		
		return false;
	}
	public boolean canBlock(ArrayList<Vector> squares, String sideChecked) {
		for(int a=0; a<8; a++) {
			for(int b=0; b<8; b++) {
				if(this.isOccupied(a,b)) {
				if(this.getSquare(a,b).side == sideChecked && !(this.getSquare(a,b) instanceof King)) {
					for(int c=0; c<squares.size(); c++) { 
						if(!this.validateMove(this.getSquare(a,b), squares.get(c), false)) {

							return true;}
					}
				}
				}
			}}
		
		return false;
	}
	
	
	
	
	public boolean isHostilePiece(String type, int x, int y, String side, boolean isKing) {
		if((this.getSquare(x,y).getClass().getName().equals(type) || 
				this.getSquare(x,y).getClass().getName().equals("chess.Queen")) &&
			   !this.getSquare(x,y).side.equals(side)) {
			lastPieceToCheck = this.getSquare(x,y);
			return true;
		} return false;
	}
	public Piece getSquare(Vector v) {
			return board[v.getX()][v.getY()];
		}

	public boolean isHostileKnight(int x, int y, String side, boolean isKing){
		if(this.getSquare(x,y).getClass().getName().equals("chess.Knight") &&
			   !this.getSquare(x,y).side.equals(side)) {
			lastPieceToCheck  = this.getSquare(x,y);
			return true;
		} return false;
	}
	
	
	public Piece getSquare(int x, int y) {
		return board[x][y];
	}
	
	public void setSquare(Piece piece, Vector v) {
		board[v.getX()][v.getY()] = piece;
	}
	
	public void move(Piece piece, Vector place) {     
		if((this.isOccupied(place) && this.getSquare(place).side == piece.side) ||
			(this.isATestBoard == false && (whoseTurn == 1 && piece.side == black) || (whoseTurn == -1 && piece.side == white)))
			 return;
		
		 if(!isATestBoard && moveCount >= 0) {
			 this.wasMoveInvalid = this.validateMove(piece, place, true);
			 if(wasMoveInvalid) return;
		 }
		 
		 	if(piece.getClass().getName().equals("chess.King")) { 
		 		if(inCheck && Math.abs(piece.getPosition().getX()-place.getX()) == 2) return;
		 		if((piece.getPosition().getX() - place.getX()) == -2) { 
		 			switch(piece.sideOfBoard) {
		 			case("Top"):{
		 			 if(!(this.getSquare(7,7).hasMoved == false && this.getSquare(6,7) == null &&
		 				  this.getSquare(5,7) == null)) return;
		 			 this.castle(new Vector(7,7), true);
		 			 break;
		 			}
		 			case("Bottom"):{ 
			 		 if(!(this.getSquare(7,0).hasMoved == false && this.getSquare(6,0) == null &&
				 		  this.getSquare(5,0) == null)) return;
			 		 
				 	 this.castle(new Vector(7,0), true);	
		 			 break;
		 			}
		 			
		 			
		 			}
		 		}
		 		if((piece.getPosition().getX() - place.getX()) == 2) {
		 			switch(piece.sideOfBoard) {
		 			case("Top"):{
			 		 if(!(this.getSquare(0,7).hasMoved == false && this.getSquare(1,7) == null &&
				 		   this.getSquare(2,7) == null && this.getSquare(3,7) == null)) return;
				 		   this.castle(new Vector(0,7), false);	
		 			 break;
		 			}
		 				
		 			
		 			case("Bottom"):{
				 	 if(!(this.getSquare(0,0).hasMoved == false && this.getSquare(1,0) == null &&
						   this.getSquare(2,0) == null && this.getSquare(3,0) == null)) return;
						   this.castle(new Vector(0,0), false);		
		 			 break;
		 			}
		 			
		 			
		 			}
		 		}	
		 		
		 	}
		 
		 
		 	if(piece.getClass().getName().equals("chess.Pawn")) { 
		 		if(!Piece.isPawnCapture(place, piece.getPosition(), (Pawn)piece) && isOccupied(place))
		 		{	
		 			
		 			
		 			return;
		 		}
		 		if(Piece.isPawnCapture(place, piece.getPosition(), (Pawn)piece) && !isOccupied(place) &&
		 				!Piece.isEnPassant(this, place)) return;
		
			if(Piece.isEnPassant(this, place) && place.getX() == lastPieceEnd.getX() && 
				lastPieceEnd.getY() == piece.getPosition().getY()) {	
					 this.updateScore();
					 this.setSquare(null, lastPieceEnd);
				}
		 	}
		if(isOccupied(place)){	
			 this.updateScore(); 
		}	
		
			 String sideStale = "";
			 if(moveCount>5) sideStale = lastPieceMoved.side;
		   	 this.setSquare(null, piece.getPosition());
			 this.setSquare(piece, place);
			 this.endTurn(piece, place);

			 if(inCheck==false && moveCount > 6 && !this.isATestBoard) {
				 if(this.isStalemate(sideStale)) {
					 System.out.println("Stalemate!");
					 System.exit(0);
				 }	}				
			String sideCheck = "";
//			if(lastPieceMoved.side == white) wasACheck = this.isInCheck((King)this.getSquare(blackKingPosition));
//			if(lastPieceMoved.side == black) wasACheck = this.isInCheck((King)this.getSquare(whiteKingPosition));
			
			if(!this.isATestBoard && inCheck == true) {
				if(lastPieceMoved.side == white) sideCheck = black;
				if(lastPieceMoved.side == black) sideCheck = white; 
				if(this.isCheckmate(sideCheck)) {
					System.out.println("Checkmate! " + lastPieceMoved.side + " wins !");
//					System.exit(0);
				}
			}
			
			 
	}
	public void castle(Vector rookPos, boolean kingSide){
//		System.out.println("were here" + this.isATestBoard);
		int b = -2;
		if(!kingSide)b +=5; 
		this.setSquare(this.getSquare(rookPos), new Vector(rookPos.getX()+b, rookPos.getY()));
		this.setSquare(null, rookPos);
//		this.print(); rookPos.print();
		this.getSquare(rookPos.getX()+b, rookPos.getY()).getPosition().setX(rookPos.getX() + b);	
		
	}
	public boolean isStalemate(String sideToMove) {
		
		for(int a=0; a<8; a++) {
			for(int b=0; b<8; b++) { System.out.println(a + "," + b);
				if(this.isOccupied(a,b)) { 
				if(this.getSquare(a,b).side == sideToMove) { 
					Vector pos = new Vector(a,b);
					ArrayList<Vector>moveList = this.peripheralMoves(pos, this.getSquare(a,b).getClass().getName(), this.getSquare(a,b).sideOfBoard);
					for(int c=0; c<moveList.size(); c++){ 
						if(this.isOccupied(a, b) && this.getSquare(a,b).side == sideToMove) { 
							if(!this.validateMove(this.getSquare(a,b), moveList.get(c), true)) { 
								return false;}
							}
					}
				}}
			}
		}
		return true;
	}
	public boolean isCheckmate(String sideInCheck) {
		King kingInCheck = new King(1, 1, "", "", "");
		if(sideInCheck == white) kingInCheck = (King)this.getSquare(whiteKingPosition);
		if(sideInCheck == black) kingInCheck = (King)this.getSquare(blackKingPosition);
		Vector kingPos = kingInCheck.getPosition();
		ArrayList<Vector> kingMoves = this.peripheralMoves(kingPos, "chess.King", kingInCheck.sideOfBoard);
		

		//checks if any legal king moves
		for(int a=0; a<kingMoves.size(); a++) { 
			if(this.isOccupied(kingMoves.get(a))) {
				if(this.getSquare(kingMoves.get(a)).side == sideInCheck) continue;
			}
			if(!this.validateMove(kingInCheck, kingMoves.get(a), true)) {return false;}
		} 
		//if no legal king moves and double check, checkmate
		if(this.inDoubleCheck == true) return true;
		
		if(this.isInCheck(lastPieceToCheck)) return false;
		
		//if no legal king moves and piece is knight/pawn and cannot be captured legally, checkmate
		if(lastPieceToCheck instanceof Knight || lastPieceToCheck instanceof Pawn) return true;
		
		if(this.canBlock(this.getBlockSquares(kingInCheck.getPosition(), lastPieceToCheck.getPosition()), sideInCheck) == true) return false;
		System.out.println("here");


		return true;
	}
	public ArrayList<Vector> getBlockSquares(Vector kingPos, Vector checkPos){
		int x = kingPos.getX();
		int y = kingPos.getY();
		ArrayList<Vector> squares = new ArrayList<Vector>();
		
		find:{
		for(int a=x+1; a<=7; a++) {
			squares.add(new Vector(a,y));
			if(checkPos.equals(new Vector(a,y))) break find;
		}
		squares.clear();
		for(int a=x-1; a>=0; a--) {
			squares.add(new Vector(a,y));
			if(checkPos.equals(new Vector(a,y))) break find;
		}
		squares.clear();
		for(int a=y+1; a<=7; a++) {
			squares.add(new Vector(x,a));
			if(checkPos.equals(new Vector(x,a))) break find;
		}
		squares.clear();

		for(int a=y-1; a>=0; a--) {
			squares.add(new Vector(x,a));
			if(checkPos.equals(new Vector(x,a))) break find;
		}
		squares.clear();

		int b=y+1;
		for(int a=x+1; a<=7; a++) {
			if(b>7) break;
			squares.add(new Vector(a,b));
			if(checkPos.equals(new Vector(a,b))) break find;
			b++; 			
			
		}
		squares.clear();
		
		b=y+1;
		for(int a=x-1; a>=0; a--) {
			if(b>7) break;
			squares.add(new Vector(a,b));
			if(checkPos.equals(new Vector(a,b))) break find;
			b++;
		}
		squares.clear();
		
		b=y-1;
		for(int a=x+1; a<=7; a++) {
			if(b<0) break;
			squares.add(new Vector(a,b));
			if(checkPos.equals(new Vector(a,b))) break find;
			b--; 
		}
		squares.clear();
		
		b=y-1;
		for(int a=x-1; a>=0; a--) {
			if(b<0) break;
			squares.add(new Vector(a,b));
			if(checkPos.equals(new Vector(a,b))) break find;
			b--; 
		}
	}   squares.remove(squares.size()-1);
		return squares;
	}	
	
	public ArrayList<Vector> peripheralMoves(Vector pos, String type, String sideOfBoard){
		ArrayList<Vector> moves = new ArrayList<Vector>();
		int x = pos.getX(), y = pos.getY();
		ArrayList<Vector> diagMoves = pos.getDiag();
		ArrayList<Vector> hvMoves = pos.getHorzVert();

		switch(type) {
		case("chess.King"):{
			for(int i=0; i<diagMoves.size(); i++){
				moves.add(diagMoves.get(i));
			}
			for(int i=0; i<hvMoves.size(); i++){
				moves.add(hvMoves.get(i));
			}
			break;
		}
		case("chess.Rook"):{
			for(int i=0; i<hvMoves.size(); i++){
				moves.add(hvMoves.get(i));
			}
			break;
		}
		case("chess.Bishop"):{
			for(int i=0; i<diagMoves.size(); i++){
				moves.add(diagMoves.get(i));
			}
			break;
		}
		case("chess.Knight"):{
			for(int i=0; i<8; i++) {
				if(x+xC[i] > 7 || y+yC[i] > 7 || x+xC[i] < 0 || y+yC[i] < 0) continue;
				moves.add(new Vector(x+xC[i],y+yC[i]));
			}		
			break;
		}
		case("chess.Queen"):{
			for(int i=0; i<diagMoves.size(); i++){
				moves.add(diagMoves.get(i));
			}
			for(int i=0; i<hvMoves.size(); i++){
				moves.add(hvMoves.get(i));
			}
			break;
		}
		case("chess.Pawn"):{
			if(sideOfBoard == "Bottom") {
				if(pos.getX()>0 && pos.getY()<7)
					moves.add(new Vector(pos.getX()-1, pos.getY()+1));
				if(pos.getY()<7)
					moves.add(new Vector(pos.getX(), pos.getY()+1));
				if(pos.getX()<7 && pos.getY()<7)
					moves.add(new Vector(pos.getX()+1, pos.getY()+1));}
			else {
				if(pos.getX()>0 && pos.getY()>0)
					moves.add(new Vector(pos.getX()-1, pos.getY()-1));
				if(pos.getY()>0)
					moves.add(new Vector(pos.getX(), pos.getY()-1));
				if(pos.getX()<7 && pos.getY()>0)
					moves.add(new Vector(pos.getX()+1, pos.getY()-1));}
			break;
		}
		}
		return moves;
	}
	
	public static void printList(ArrayList<Vector> list) {
		System.out.println("startlist");
		for(int i=0; i<list.size(); i++)
		System.out.println(list.get(i).getX() + "," + list.get(i).getY());
		System.out.println("endlist");
	}
	
	
	
	
	
		
}

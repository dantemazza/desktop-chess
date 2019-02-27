package chess;

import java.util.ArrayList;

public class Board{
	private Piece board[][] = new Piece[8][8];
	char letters = 'a';
	public boolean inCheck, isATestBoard = false;
	private int whiteScore, blackScore, moveCount=0;
	private String capturedSide;
	private int whoseTurn = 1; 
	public Piece lastPieceMoved;
	public Vector lastPieceStart = new Vector(), lastPieceEnd = new Vector(), whiteKingPosition, blackKingPosition;
	public final String white = "white";
	public final String black = "black";
	public final int xC[] = {1,2,2,1,-1,-2,-2,-1}; 
	public final int yC[] = {2,1,-1,-2,-2,-1,1,2};
	Board(String topSide, String bottomSide){
		board[0][0] = new Rook(0, 0, "Ra1", bottomSide, "Bottom");
		board[1][0] = new Knight(1, 0, "Nb1", bottomSide, "Bottom");
		board[2][0] = new Bishop(2, 0, "Bc1", bottomSide, "Bottom");
		board[3][0] = new Queen(3, 0, "Qd1", bottomSide, "Bottom");			
		board[4][0] = new King(4, 0, "Ke1", bottomSide, "Bottom");
		board[5][0] = new Bishop(5, 0, "Bf1", bottomSide, "Bottom");
		board[6][0] = new Knight(6, 0, "Ng1", bottomSide, "Bottom");
		board[7][0] = new Rook(7, 0, "Rh1", bottomSide, "Bottom");
		for(int a=0; a<8; a++) {
			board[a][1] = new Pawn(a, 1, "P" + letters + 2, bottomSide, "Bottom");
			letters++;
		}
		
		letters = 'a';
		board[0][7] = new Rook(0, 7, "Ra8", topSide, "Top");
		board[1][7] = new Knight(1, 7, "Nb8", topSide, "Top");
		board[2][7] = new Bishop(2, 7, "Bc8", topSide, "Top");
		board[3][7] = new Queen(3, 7, "Qd8", topSide, "Top");			
		board[4][7] = new King(4, 7, "Ke8", topSide, "Top");
		board[5][7] = new Bishop(5, 7, "Bf8", topSide, "Top");
		board[6][7] = new Knight(6, 7, "Ng8", topSide, "Top");
		board[7][7] = new Rook(7, 7, "Rh8", topSide, "Top");		
		for(int a=0; a<8; a++) {
			board[a][6] = new Pawn(a, 6, "P" + letters + 7, topSide, "Top");
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
	
	public void endTurn(Piece piece, Vector place) {
//		System.out.println(piece.getPosition().getX() + "," + piece.getPosition().getY());
		if(piece.getClass().getName().equals("chess.King")) {
			if(piece.side.equals(black)) blackKingPosition = place;
			else whiteKingPosition = place;
		}	
		 lastPieceStart.setVector(piece.getPosition());	
		 lastPieceEnd.setVector(place);
		
		 piece.setPosition(place);
		 if(this.isATestBoard == false) lastPieceMoved = piece;
		 moveCount++;
		 whoseTurn *= -1; 
//		 System.out.println(this + "," + piece.getPosition().getX() + "," + piece.getPosition().getY());

		 switch(whoseTurn) {
		 case(1):{
			 inCheck = this.isInCheck((King) this.getSquare(whiteKingPosition));			
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
		boardClone.lastPieceMoved= c.lastPieceMoved;
		boardClone.lastPieceStart = c.lastPieceStart;
		boardClone.whiteKingPosition = c.whiteKingPosition;
		boardClone.blackKingPosition = c.blackKingPosition;
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
	public boolean validateMove(Piece piece, Vector place, boolean isntChecker){
		 Board testCheck = new Board();
		 testCheck = clone(this);
//		 this.print();
		 testCheck.isATestBoard = true;
		 testCheck.move(testCheck.getSquare(piece.getPosition()), place);
//		 testCheck.print();
//		 System.out.println();
		 
//		 this.print();
//		 System.out.println();
		 
		 if(isntChecker)
		 testCheck.whoseTurn *= -1;
		 switch(testCheck.whoseTurn) {
		 case(1):{ 
			 return testCheck.isInCheck((King)testCheck.getSquare(testCheck.whiteKingPosition));
			 
			 
		 }
		 case(-1):{
//			 System.out.println(testCheck.blackKingPosition.getX() + "," + testCheck.blackKingPosition.getY());
//			 System.out.println(testCheck.board[4][7]);
			
			 return testCheck.isInCheck((King)testCheck.getSquare(testCheck.blackKingPosition));
			 
//			 System.out.println("im here");	 
			
		 }}	 
		 System.out.println("moveInvalid bypass");
		return false;
	}	
	
	public void print() {
		for(int c=7; c>-1; c--) {
			if(c!=7) System.out.println();
			for(int b=0; b<8; b++){
			try{System.out.print(board[b][c].name + " ");}
			catch(NullPointerException e){System.out.print(" 0  ");}
			}}
		System.out.println();
		
	}
	public boolean isInCheck(King king) {

		int y = king.getPosition().getY();
		int x = king.getPosition().getX();
		if(king.sideOfBoard == "Bottom" && y < 6) {
		try {
			if((this.getSquare(x+1,y+1).getClass().getName().equals("chess.Pawn") && 
				this.getSquare(x+1,y+1).side != king.side) || (this.getSquare(x-1, y+1).side != king.side
				&&  this.getSquare(x-1,y+1).getClass().getName().equals("chess.Pawn")))
					return true;  
		}catch(NullPointerException e){} }
		

		if(king.sideOfBoard == "Top" && y > 1) {
		try {
			if((this.getSquare(x+1,y-1).getClass().getName().equals("chess.Pawn") && 
				this.getSquare(x+1,y-1).side != king.side) || (this.getSquare(x-1, y-1).side != king.side
				&&  this.getSquare(x-1,y-1).getClass().getName().equals("chess.Pawn")))
					return true; 	
		}catch(NullPointerException e){}}
		for(int a=x+1; a<=7; a++) {
			if(this.isOccupied(a,y)) {
				if(this.isHostilePiece("chess.Rook", a, y, king.side)) return true;
				else break;
			}}
		for(int a=x-1; a>=0; a--) {
			if(this.isOccupied(a,y)) {
				if(this.isHostilePiece("chess.Rook", a, y, king.side)) return true;
				else break;
			}}

		for(int a=y+1; a<=7; a++) {
			if(this.isOccupied(x,a)) {
				if(this.isHostilePiece("chess.Rook", x, a, king.side)) return true;
				else break;
			}}
		for(int a=y-1; a>=0; a--) {
			if(this.isOccupied(x,a)) {
				if(this.isHostilePiece("chess.Rook", x, a, king.side)) return true;
				else break;
				
			}}
		
		int b=y+1;
		for(int a=x+1; a<=7; a++) {
			if(b>7) break;
			if(this.isOccupied(a,b)) {
				if(this.isHostilePiece("chess.Bishop", a, b, king.side)) return true;
				else break;
			}
			b++; 			
			
		}
		b=y+1;
		for(int a=x-1; a>=0; a--) {
			if(b>7) break;
			if(this.isOccupied(a,b)) {
				if(this.isHostilePiece("chess.Bishop", a, b, king.side)) return true;
				else break;
			}
			b++;
		}
		b=y-1;
		for(int a=x+1; a<=7; a++) {
			if(b<0) break;
			if(this.isOccupied(a,b)) {
				if(this.isHostilePiece("chess.Bishop", a, b, king.side)) return true;
				else break;
			}
			b--; 
		}
		b=y-1;
		for(int a=x-1; a>=0; a--) {
			if(b<0) break;
			if(this.isOccupied(a,b)) {
				if(this.isHostilePiece("chess.Bishop", a, b, king.side)) return true;
				else break;
			}
			b--; 
		}
		//check knight check
		
		
		for(int i=0; i<8; i++) {
			if(x+xC[i] > 7 || y+yC[i] > 7 || x+xC[i] < 0 || y+yC[i] < 0) continue;
			if(isOccupied(x+xC[i],y+yC[i])) { 	
			if(this.isHostileKnight(x+xC[i],y+yC[i],king.side)) return true; 
		}}		
		return false;
	}
	
	public boolean isHostilePiece(String type, int x, int y, String side) {
		return (this.getSquare(x,y).getClass().getName().equals(type) || 
				this.getSquare(x,y).getClass().getName().equals("chess.Queen")) &&
			   !this.getSquare(x,y).side.equals(side);
	}
	public Piece getSquare(Vector v) {
			return board[v.getX()][v.getY()];
		}

	public boolean isHostileKnight(int x, int y, String side){
		return this.getSquare(x,y).getClass().getName().equals("chess.Knight") &&
			   !this.getSquare(x,y).side.equals(side);
	}
	
	
	public Piece getSquare(int x, int y) {
		return board[x][y];
	}
	
	public void setSquare(Piece piece, Vector v) {
		board[v.getX()][v.getY()] = piece;
	}
	
	public void move(Piece piece, Vector place) {     
		if((this.isOccupied(place) && this.getSquare(place).side == this.getSquare(piece.getPosition()).side) ||
			(whoseTurn == 1 && piece.side == black) || (whoseTurn == -1 && piece.side == white))
			 return;
		
		 if(!isATestBoard) 
			 if(this.validateMove(piece, place, true)) return;
		
		 
		 	if(piece.getClass().getName().equals("chess.King")) { 
		 		if((piece.getPosition().getX() - place.getX()) == -2) { 
		 			switch(piece.sideOfBoard) {
		 			case("Top"):{
		 			 if(!(this.getSquare(7,7).hasMoved == false && this.getSquare(6,7) == null &&
		 				  this.getSquare(5,7) == null)) return;
		 			 this.castle((King)piece, (Rook)this.getSquare(7,7), true);
		 			 break;
		 			}
		 			case("Bottom"):{ 
			 		 if(!(this.getSquare(7,0).hasMoved == false && this.getSquare(6,0) == null &&
				 		  this.getSquare(5,0) == null)) return;
			 		 
				 	 this.castle((King)piece, (Rook)this.getSquare(7,0), true);	
		 			 break;
		 			}
		 			
		 			
		 			}
		 		}
		 		if((piece.getPosition().getX() - place.getX()) == 2) {
		 			switch(piece.sideOfBoard) {
		 			case("Top"):{
			 		 if(!(this.getSquare(0,7).hasMoved == false && this.getSquare(1,7) == null &&
				 		   this.getSquare(2,7) == null && this.getSquare(3,7) == null)) return;
				 		   this.castle((King)piece, (Rook)this.getSquare(0,7), false);	
		 			 break;
		 			}
		 				
		 			
		 			case("Bottom"):{
				 	 if(!(this.getSquare(0,0).hasMoved == false && this.getSquare(1,0) == null &&
						   this.getSquare(2,0) == null && this.getSquare(3,0) == null)) return;
						   this.castle((King)piece, (Rook)this.getSquare(0,0), false);		
		 			 break;
		 			}
		 			
		 			
		 			}
		 		}	
		 	}
		 
		 
		 	if(piece.getClass().getName().equals("chess.Pawn")) { 
		 		if(!Piece.isPawnCapture(place, piece.getPosition(), (Pawn)piece) && isOccupied(place))
		 		{	
///*HERE*/		 	System.out.println(piece.getPosition().getX() + "," + piece.getPosition().getY());
//		 			System.out.println(place.getX() + "," + place.getY());
		 			
		 			
		 			return;
		 		}
		 		if(Piece.isPawnCapture(place, piece.getPosition(), (Pawn)piece) && !isOccupied(place) &&
		 				!Piece.isEnPossant(this, place)) return;
			
//			if(Piece.isPawnCapture(place, piece.getPosition(), (Pawn)piece) && isOccupied(place)) 
//				this.updateScore();
		
			if(Piece.isEnPossant(this, place) && place.getX() == lastPieceEnd.getX() && 
				lastPieceEnd.getY() == piece.getPosition().getY()) {	
					 this.updateScore();
					 this.setSquare(null, lastPieceEnd);
				}
		 	}
		if(isOccupied(place) /*&& !piece.getClass().getName().equals("chess.Pawn")*/){	
			 this.updateScore(); 
		}	
		
			 String sideStale = "";
			 if(moveCount>0) { System.out.println(lastPieceMoved.side); sideStale = lastPieceMoved.side;}
		   	 this.setSquare(null, piece.getPosition());
			 this.setSquare(piece, place);
			 this.endTurn(piece, place);

			 if(inCheck==false && moveCount > 3) {
				 if(this.isStalemate(true, sideStale)) {
					 System.out.println("Stalemate!");
					 System.exit(0);
				 }
			 }
 
	}
	public void castle(King king, Rook rook, boolean kingSide){
		int b = -2;
		if(!kingSide)b +=5; 
		this.setSquare(this.getSquare(rook.getPosition()), new Vector(rook.getPosition().getX()+b, rook.getPosition().getY()));
		this.setSquare(null, rook.getPosition());
		rook.getPosition().setX(rook.getPosition().getX() + b);	
		
	}
	public boolean isStalemate(boolean halt, String sideToMove) {
		
		for(int a=0; a<8; a++) {
			for(int b=0; b<8; b++) {
				if(this.isOccupied(a,b)) {
				if(this.getSquare(a,b).side == sideToMove) {
					Vector pos = new Vector(a,b);
					ArrayList<Vector>moveList = this.peripheralMoves(pos, this.getSquare(a,b).getClass().getName(), this.getSquare(a,b).sideOfBoard);
					printList(moveList); System.out.println(a + "," + b);
					for(int c=0; c<moveList.size(); c++){ 
						if(this.isOccupied(a, b) && this.getSquare(a,b).side == sideToMove) {
							if(this.validateMove(this.getSquare(a,b), moveList.get(c), false)) return false;}
					}
				}}
			}
		}
		return true;
	}
	public boolean isCheckmate(boolean checkmate, String sideInCheck) {
		boolean isCheckmate = true;
		
		
		return isCheckmate;
	}
	
	public ArrayList<Vector> peripheralMoves(Vector pos, String type, String sideOfBoard){
		ArrayList<Vector> moves = new ArrayList<Vector>();
		int x = pos.getX(), y = pos.getY();
		switch(type) {
		case("chess.King"):{
			for(int i=0; i<4; i++){
				if(pos.getDiag().get(i).getX() > 7 || pos.getDiag().get(i).getX() < 0 ||
				   pos.getDiag().get(i).getY() > 7 || pos.getDiag().get(i).getY() < 0) continue;
				moves.add(pos.getDiag().get(i));
			}
			for(int i=0; i<4; i++){
				if(pos.getHorzVert().get(i).getX() > 7 || pos.getHorzVert().get(i).getX() < 0 ||
				   pos.getHorzVert().get(i).getY() > 7 || pos.getHorzVert().get(i).getY() < 0) continue;
				moves.add(pos.getHorzVert().get(i));
			}	
			break;
		}
		case("chess.Rook"):{
			for(int i=0; i<4; i++){
				if(pos.getHorzVert().get(i).getX() > 7 || pos.getHorzVert().get(i).getX() < 0 ||
				   pos.getHorzVert().get(i).getY() > 7 || pos.getHorzVert().get(i).getY() < 0) continue;
				moves.add(pos.getHorzVert().get(i));
			}
			break;
		}
		case("chess.Bishop"):{
			for(int i=0; i<4; i++){
				if(pos.getDiag().get(i).getX() > 7 || pos.getDiag().get(i).getX() < 0 ||
				   pos.getDiag().get(i).getY() > 7 || pos.getDiag().get(i).getY() < 0) continue;
				moves.add(pos.getDiag().get(i));
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
			for(int i=0; i<4; i++){
				if(pos.getDiag().get(i).getX() > 7 || pos.getDiag().get(i).getX() < 0 ||
				   pos.getDiag().get(i).getY() > 7 || pos.getDiag().get(i).getY() < 0) continue;
				moves.add(pos.getDiag().get(i));
			}
			for(int i=0; i<4; i++){
				if(pos.getHorzVert().get(i).getX() > 7 || pos.getHorzVert().get(i).getX() < 0 ||
				   pos.getHorzVert().get(i).getY() > 7 || pos.getHorzVert().get(i).getY() < 0) continue;
				moves.add(pos.getHorzVert().get(i));
			}
			break;
		}
		case("chess.Pawn"):{
			if(sideOfBoard == "Bottom") {
			moves.add(new Vector(pos.getX()-1, pos.getY()+1));
			moves.add(new Vector(pos.getX(), pos.getY()+1));
			moves.add(new Vector(pos.getX()+1, pos.getY()+1));}
			else {
			moves.add(new Vector(pos.getX()-1, pos.getY()-1));
			moves.add(new Vector(pos.getX(), pos.getY()-1));
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

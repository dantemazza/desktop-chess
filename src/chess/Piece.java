package chess;


public abstract class Piece implements Cloneable {
	public String name;
	public boolean hasMoved;
	public String side, sideOfBoard;
	public int score;

	private Vector Position;
	Piece(int x, int y, String name, String side, String sideOfBoard){
		Position = new Vector(x, y);
		this.name = name;
		this.side = side;
		this.hasMoved = false;
		this.sideOfBoard = sideOfBoard;
	}
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	abstract void move(Vector place, Board b);
	
	public Vector getPosition() {
		return this.Position;
	}
	
	public void setPosition(Vector newPosition) {
		this.Position = newPosition;
	}
	
	public static boolean isNotSame(Vector a, Vector b) {
		return a != b;
	}
	
	public static boolean isDiagonal(Vector a, Vector b){
		return Math.abs(a.getX() - b.getX()) == Math.abs(a.getY() - b.getY()) && isNotSame(a,b);
	}
	
	public static boolean isHorzVert(Vector a, Vector b) {
		return a.getX() == b.getX() || a.getY() == b.getY() && isNotSame(a,b);
	}
	public static boolean isL(Vector a, Vector b){
		return (Math.abs(a.getX()-b.getX()) == 2 && Math.abs(a.getY()-b.getY()) == 1) ||
			   (Math.abs(a.getX()-b.getX()) == 1 && Math.abs(a.getY()-b.getY()) == 2);
	}
	public static boolean isOneAway(Vector a, Vector b) {
		return (Math.abs(a.getX()-b.getX()) == 0 && Math.abs(a.getY()-b.getY()) == 1) ||
				(Math.abs(a.getX()-b.getX()) == 1 && Math.abs(a.getY()-b.getY()) == 0) ||
				(Math.abs(a.getX()-b.getX()) == 1 && Math.abs(a.getY()-b.getY()) == 1);
				
	}
	public static boolean isPawnCapture(Vector a, Vector b, Pawn p){

		return ((Math.abs(a.getX()-b.getX())) == 1) &&
				((p.sideOfBoard == "Top" && a.getY()-b.getY() == 1) || 
				 (p.sideOfBoard == "Bottom" && a.getY()-b.getY() == -1));
	}
	public static boolean areHorzAdj(Piece p1, Piece p2) {
		if(p1.Position.getX() - p2.Position.getX() == 1 && p1.Position.getY() == p2.Position.getY())
			return true;
		return false;
	}
	
	
	public static boolean isEnPassant(Board b, Vector place) {

		if(b.getMoveCount() == 0) return false;
		if(!b.lastPieceMoved.getClass().getName().equals("chess.Pawn") ||  b.lastPieceEnd.getX() != b.lastPieceStart.getX() ||
			Math.abs(b.lastPieceEnd.getY()-b.lastPieceStart.getY()) != 2 || place.getX() != b.lastPieceEnd.getX()) 
			return false;
		return true;
	}
	
	public static boolean isValidPawnMove(Vector a, Vector b, Pawn p) {
		
		boolean side2 = ((p.sideOfBoard == "Top" && a.getY()-b.getY() == 2) || 
				(p.sideOfBoard == "Bottom" && a.getY()-b.getY() == -2)) && !p.hasMoved;
		boolean side1 =  (p.sideOfBoard == "Top" && a.getY()-b.getY() == 1) || 
				(p.sideOfBoard == "Bottom" && a.getY()-b.getY() == -1);
		return (side2 || side1) && (a.getX() == b.getX());
			
	}
	public static boolean isClearHorzVertPath(Vector a, Vector b, Board c) {
		
		if(a.getX()==b.getX()) {
			if(a.getY()>b.getY()) {
			for(int i=b.getY()+1; i<a.getY(); i++){
				if(c.isOccupied(a.getX(), i)) return false;
			}
				
			} else {
				for(int i=b.getY()-1; i>a.getY(); i--) { 
					if(c.isOccupied(a.getX(), i)) return false;
				}
			}
		} else {
			if(a.getX()>b.getX()) {
			for(int i=b.getX()+1; i<a.getX(); i++){
				if(c.isOccupied(i,a.getY())) return false;
			}
				
			} else {
				for(int i=b.getX()-1; i>a.getX(); i--) {
					if(c.isOccupied(i,a.getY())) return false;
				}
			}
			
		}
			return true;
	}
		
	public static boolean isClearDiagPath(Vector a, Vector b, Board c) {
		int j = 0;
			if(a.getX() < b.getX() && a.getY() > b.getY()) {
				j = b.getX() - 1;
			for(int i=b.getY()+1; i<a.getY(); i++){
				
				if(c.isOccupied(new Vector(j,i))) return false;
				j--;
				}}
			if(a.getX() > b.getX() && a.getY() > b.getY()) {
				j = b.getX() + 1;
			for(int i=b.getY()+1; i<a.getY(); i++){
				
				if(c.isOccupied(new Vector(j,i))) return false;
				j++;
				}}
			if(a.getX() < b.getX() && a.getY() < b.getY()) {
				j = b.getX() - 1;
			for(int i=b.getY()-1; i>a.getY(); i--){
				
				if(c.isOccupied(new Vector(j,i))) return false;
				j--;
				}}
			if(a.getX() > b.getX() && a.getY() < b.getY()) {
				j = b.getX() + 1;
			for(int i=b.getY()-1; i>a.getY(); i--){
			
				if(c.isOccupied(new Vector(j,i))) return false;
				j++;
				}}
			
			return true;
		}
		
	
	}

	class Queen extends Piece{
		Queen(int x, int y, String name, String side, String sideOfBoard) {
			super(x, y, name, side, sideOfBoard);
			this.score = 9;
			
		}

		@Override
		void move(Vector place, Board b) {
			if (isHorzVert(this.getPosition(), place) && isClearHorzVertPath(this.getPosition(), place, b)
				|| isDiagonal(this.getPosition(), place) && isClearDiagPath(place, this.getPosition(), b)){
				b.move(this, place);
			}else b.wasMoveInvalid = true;
		}}
	class Rook extends Piece{
		Rook(int x, int y, String name, String side, String sideOfBoard) {
			super(x, y, name, side, sideOfBoard);
			this.score = 5;
		}

		@Override
		void move(Vector place, Board b) {
			if((isHorzVert(this.getPosition(), place) && isClearHorzVertPath(this.getPosition(), place, b))) {		
				b.move(this, place);
			}else b.wasMoveInvalid = true;
		}}
	class Bishop extends Piece{
		Bishop(int x, int y, String name, String side, String sideOfBoard) {
			super(x, y, name, side, sideOfBoard);
			this.score = 3;
		}

		@Override
		void move(Vector place, Board b) {
			if(isDiagonal(this.getPosition(), place) && isClearDiagPath(place, this.getPosition(), b)) {
				b.move(this, place);
			}else b.wasMoveInvalid = true;
			
		}}
	class King extends Piece{
		King(int x, int y, String name, String side, String sideOfBoard) {
			super(x, y, name, side, sideOfBoard);
		}

		@Override
		void move(Vector place, Board b) {
			
			if(isOneAway(this.getPosition(), place) || (Math.abs(this.getPosition().getX() - place.getX()) == 2) &&
					this.hasMoved ==false) {
				b.move(this, place);
				
			}else b.wasMoveInvalid = true;
			
		}}
	class Knight extends Piece{
		Knight(int x, int y, String name, String side, String sideOfBoard) {
			super(x, y, name, side, sideOfBoard);
			this.score = 3;
		}

		@Override
		void move(Vector place, Board b) {
			if(isL(this.getPosition(), place)) { 
			b.move(this, place);
			}else b.wasMoveInvalid = true;
		}} 
	class Pawn extends Piece{

		Pawn(int x, int y, String name, String side, String sideOfBoard) {
			super(x, y, name, side, sideOfBoard);
			this.score = 1;
		}

		@Override
		void move(Vector place, Board b) {
			if(isValidPawnMove(place, this.getPosition(), this) ||
					isPawnCapture(place, this.getPosition(), this)) {
			b.move(this, place);	
			
			}else b.wasMoveInvalid = true;
		}}



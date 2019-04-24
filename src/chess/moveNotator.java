package chess;

public class moveNotator {
	boolean specFile = false, specRank = false, specBoth = false;
	String moveNotation = "";

	moveNotator(Piece piece, boolean wasCapture, boolean wasCheck, boolean wasMate, boolean blackTop, Board board, Vector where, boolean wasQC, boolean wasKC){
		if(wasKC){
			moveNotation = "O-O";
			return;
		}
		if(wasQC) {
			moveNotation = "O-O-O";
			return;
		}
		if(!(piece instanceof Pawn) && !(piece instanceof Knight)) moveNotation += piece.getClass().getName().charAt(6);
		else if(piece instanceof Pawn){
			moveNotation += this.getFile(piece.getPosition().getX(), blackTop);
		}else {
			moveNotation += "N";
		}
		
		spec:{
			if(piece instanceof Queen || piece instanceof Rook) this.specHorzVert(where, piece, board);
			if(piece instanceof Knight) this.specKnightFileRank(where, (Knight)piece, board);
			if(specBoth) {
				moveNotation += this.getFile(piece.getPosition().getX(), blackTop);
				moveNotation += this.getRank(piece.getPosition().getY(), blackTop);
				break spec;
			}
			if(specFile) moveNotation += this.getFile(piece.getPosition().getX(), blackTop);
			if(specRank) moveNotation += this.getRank(piece.getPosition().getY(), blackTop);
		}
		
		
		if(wasCapture) { 
			moveNotation += "x";
			if(piece instanceof Pawn) moveNotation += this.getFile(where.getX(), blackTop);
		}
		if(!(piece instanceof Pawn)) moveNotation += this.getFile(where.getX(), blackTop);
		moveNotation += this.getRank(where.getY(), blackTop);
		
		if(wasMate){ 
			moveNotation += "#"; 
			return; 
			}
		if(wasCheck) moveNotation += "+";
		
		if(moveNotation.length() == 2 && moveNotation.charAt(0) == 'f' && board.whoseTurn == 1) moveNotation += "\t";
		
	}
	
	public String getMoveNotation() {
		return moveNotation;
	}
	
	
	public void specHorzVert(Vector pos, Piece piece, Board board) {
		
		int x = pos.getX(), y = pos.getY();
		if(pos.getX()<7) {
		for(int a = pos.getX()+1; a<=7; a++){
			if(board.isOccupied(a,y)) {
				if(!board.getSquare(a,y).side.equals(piece.side) || board.getSquare(a,y).getClass() != piece.getClass() || piece.getPosition().equals(a, y)) break;
				if(!board.validateMove(board.getSquare(a,y), pos, false)) specFile = true;
			}
		}}
		if(pos.getX()>0) {
		for(int a = pos.getX()-1; a>=0; a--){ 
			if(board.isOccupied(a,y)) {
				if(!board.getSquare(a,y).side.equals(piece.side) || board.getSquare(a,y).getClass() != piece.getClass() || piece.getPosition().equals(a, y)) break;
				if(!board.validateMove(board.getSquare(a,y), pos, false)) specFile = true;
			}
		}}
		
		if(pos.getY()<7) {
		for(int a = pos.getY()+1; a<=7; a++){
			if(board.isOccupied(x,a)) {
				if(!board.getSquare(x,a).side.equals(piece.side) || board.getSquare(x,a).getClass() != piece.getClass() || piece.getPosition().equals(x, a)) break;
				if(!board.validateMove(board.getSquare(x,a), pos, false)) {
					if(x != piece.getPosition().getX()) specFile = true;
					else specRank = true;
				}
			}
		}}
		if(pos.getY()>0) {
		for(int a = pos.getY()-1; a>=0; a--){
			if(board.isOccupied(x,a)) {
				if(!board.getSquare(x,a).side.equals(piece.side) || board.getSquare(x,a).getClass() != piece.getClass() || piece.getPosition().equals(x, a)) break;
				if(!board.validateMove(board.getSquare(x,a), pos, false)) {
					if(x != piece.getPosition().getX()) specFile = true;
					else specRank = true;
				}		
			}
		}}	
		specBoth = specRank && specFile;		
	}
	public void specKnightFileRank(Vector pos, Knight knight, Board board) {
		int x = pos.getX(), y = pos.getY();
		for(int i=0; i<8; i++) {
			if(x+Board.xC[i] > 7 || y+Board.yC[i] > 7 || x+Board.xC[i] < 0 || y+Board.yC[i] < 0) continue;
			if(board.isOccupied(x+Board.xC[i],y+Board.yC[i])) {
				if(!board.getSquare(x+Board.xC[i],y+Board.yC[i]).side.equals(knight.side) || board.getSquare(x+Board.xC[i],y+Board.yC[i]).getClass() != knight.getClass()
				|| knight.getPosition().equals(x+Board.xC[i],y+Board.yC[i])) continue;
				if(!board.validateMove(board.getSquare(x+Board.xC[i],y+Board.yC[i]), pos, false)) {
					if(knight.getPosition().getX() != x+Board.xC[i]) specFile = true;	
					else specRank = true;
				}
			}
		}		
	}
	
	public char getFile(int x, boolean isBT) {
		int b=0;
		if(isBT) {
			for(char a = 'a'; a<='h'; a++) {
				if(b == x) return a;
				b++;
			}}else{ b = 7;
			for(char a = 'a'; a<='h'; a++) {
				if(b == x)  return a;
				b--;
			}	
		} return 'z';
	}
	public String getRank(int y, boolean isBT) {
		return isBT ? Integer.toString(8-y) : Integer.toString(y+1);
	}
	
}

package chess;

public class Game {
	public int turnCount;
	public static void main(String args[]) {
	 final String white = "white";
	 final String black = "black";
	 
		Board b = new Board(black, white);
		b.print();
		System.out.println("MoveCount: " + b.getMoveCount() + " check: " + b.inCheck + " score: " + (b.getScore()));
		b.getSquare(1,0).move(new Vector(2,2), b);
		b.print();
		System.out.println("MoveCount: " + b.getMoveCount() + " check: " + b.inCheck + " score: " + (b.getScore()));
		b.getSquare(1,7).move(new Vector(2,5), b);
		b.print();
		System.out.println("MoveCount: " + b.getMoveCount() + " check: " + b.inCheck + " score: " + (b.getScore()));
		b.getSquare(2,2).move(new Vector(1,4), b);
		b.print();
		System.out.println("MoveCount: " + b.getMoveCount() + " check: " + b.inCheck + " score: " + (b.getScore()));
		b.getSquare(2,5).move(new Vector(1,3), b);
		System.out.println("MoveCount: " + b.getMoveCount() + " check: " + b.inCheck + " score: " + (b.getScore()));
		b.getSquare(1,4).move(new Vector(3,5), b);
		System.out.println("MoveCount: " + b.getMoveCount() + " check: " + b.inCheck + " score: " + (b.getScore()));
		b.getSquare(1,3).move(new Vector(2,2), b);
		System.out.println("MoveCount: " + b.getMoveCount() + " check: " + b.inCheck + " score: " + (b.getScore()));
		
		
		
//		Board b = new Board();
//		b.getSquare(new Vector(3,7)).move(new Vector(5,5), b);
//		 System.out.println("MoveCount: " + b.getMoveCount() + " check: " + b.inCheck);
//		b.getSquare(new Vector(3,3)).move(new Vector(2,4), b);
		b.print();
		
	}
	
	
	
	
	
}

package chess;

import java.util.ArrayList;

public class Vector {
    private int x, y;
    
    Vector(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    Vector(){}

    public void setVector(Vector vector) {
    	this.setX(vector.getX());
    	this.setY(vector.getY());
    }
    public ArrayList<Vector> getDiag(){
		ArrayList<Vector> moves = new ArrayList<Vector>();
		moves.add(new Vector(this.getX()+1, this.getY()-1));
		moves.add(new Vector(this.getX()+1, this.getY()+1));
		moves.add(new Vector(this.getX()-1, this.getY()-1));
		moves.add(new Vector(this.getX()-1, this.getY()+1));

		return moves;
    }
    public ArrayList<Vector> getHorzVert(){
 		ArrayList<Vector> moves = new ArrayList<Vector>();
 		moves.add(new Vector(this.getX(), this.getY()-1));
 		moves.add(new Vector(this.getX(), this.getY()+1));
 		moves.add(new Vector(this.getX()-1, this.getY()));
 		moves.add(new Vector(this.getX()-1, this.getY()));

 		return moves;
     } 
    
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
}

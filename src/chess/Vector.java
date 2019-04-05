package chess;

import java.util.ArrayList;

public class Vector {
    private int x, y;
    
    Vector(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    Vector(Vector vector){
    	this.setX(vector.getX());
    	this.setY(vector.getY());
    }
    Vector(){}
    
    public boolean equals(Vector vector) {
    	return this.getX() == vector.getX() && this.getY() == vector.getY();
    }
    public void setVector(Vector vector) {
    	this.setX(vector.getX());
    	this.setY(vector.getY());
    }
    
    public void setVector(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
    public ArrayList<Vector> getDiag(){
		ArrayList<Vector> moves = new ArrayList<Vector>();
		Vector v;
		v = new Vector(this.getX()+1, this.getY()-1);
		if(v.checkInBounds())
		moves.add(v);
		v = new Vector(this.getX()+1, this.getY()+1);
		if(v.checkInBounds())
		moves.add(v);
		v = new Vector(this.getX()-1, this.getY()-1);
		if(v.checkInBounds())
		moves.add(v);
		v = new Vector(this.getX()-1, this.getY()+1);
		if(v.checkInBounds())
		moves.add(v);
		return moves;
    }
    public boolean checkInBounds() {
    	return !(this.getX() > 7 || this.getX() < 0 ||
		   this.getY() > 7 || this.getY() < 0);  
    }    
    
    public boolean equals(int x, int y){
    	return this.x == x && this.y == y;
    }
    
    public ArrayList<Vector> getHorzVert(){
 		ArrayList<Vector> moves = new ArrayList<Vector>();
		Vector v;
		v = new Vector(this.getX(), this.getY()-1);
		if(v.checkInBounds())
		moves.add(v);
		v = new Vector(this.getX(), this.getY()+1);
		if(v.checkInBounds())
		moves.add(v);
		v = new Vector(this.getX()-1, this.getY());
		if(v.checkInBounds())
		moves.add(v);
		v = new Vector(this.getX()+1, this.getY());
		if(v.checkInBounds())
		moves.add(v);	

 		return moves;
     } 
    public void print() {
    	System.out.println(this.getX() + "," + this.getY());
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

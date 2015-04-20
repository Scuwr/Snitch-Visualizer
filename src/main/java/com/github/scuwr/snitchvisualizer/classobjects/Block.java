package com.github.scuwr.snitchvisualizer.classobjects;

public class Block {
	public int x;
	public int y;
	public int z;
	public Type b;
	
	public Block(int x, int y, int z, Type b){
		this.x = x;
		this.y = y;
		this.z = z;
		this.b = b;
	}
	
	public enum Type{
		USED(1),
		REMOVED(2),
		PLACED(3);
	
		private final int type;
	    
	    private Type(int type){
	        this.type = type;
	    }
	}
}

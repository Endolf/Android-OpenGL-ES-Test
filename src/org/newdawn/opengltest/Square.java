package org.newdawn.opengltest;

public class Square extends Mesh {
	public Square() {
		super(new float[] {1f,1f, 0.0f,
				-1f, 1f, 0.0f,
				-1f, -1f, 0.0f,
				1f,-1f,0f}, 
			new float[] {1f,0f,0f,1}, 
			new short[] {0,1,2,2,3,0});
	}
}
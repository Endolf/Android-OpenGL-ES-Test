package org.newdawn.opengltest;

public class Cube extends Mesh {
	public Cube() {
		super(new float[] {1f,1f, 1f,
				-1f, 1f, 1f,
				-1f, -1f, 1f,
				1f,-1f,1f,
				1f,1f, -1f,
				-1f, 1f, -1f,
				-1f, -1f, -1f,
				1f,-1f,-1f}, 
			new float[] {1f,0f,0f,1}, 
			new short[] {0,1,3,2,6,1,5,0,4,3,7,6,4,5},
			false
		);
	}
}

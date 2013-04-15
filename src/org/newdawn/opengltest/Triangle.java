package org.newdawn.opengltest;

public class Triangle extends Mesh {
	public Triangle() {
		super(new float[] {0.0f,  0.622008459f, 0.0f,-0.5f,
				-0.311004243f, 0.0f,
				0.5f, -0.311004243f, 0.0f}, 
			new float[] {0.639f,0.769f,0.224f,1}, 
			new short[] {0,1,2});
	}
}

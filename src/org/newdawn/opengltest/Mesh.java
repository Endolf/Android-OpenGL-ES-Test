package org.newdawn.opengltest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;


public class Mesh {
	public static final int DIMENSIONS = 3;
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_SHORT = 2;
	private static final int VERTEX_STRIDE = DIMENSIONS * 4;
	
	private final String vertexShaderCode =
	    "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        "  gl_Position = uMVPMatrix * vPosition;" +        
        "}";

    private final String fragmentShaderCode =
        "uniform vec4 vColor;" +
        "void main() {" +
        "  gl_FragColor = vColor;" +
        "}";
		
	private FloatBuffer vertexBuffer;
	private ShortBuffer vertexOrderBuffer;
	private int numVertecies;
	private int shaderProgram;
	private float[] worldPosition = {0f,0f,0f};
	private float[] worldRotation = {0f,0f,1f,0f};
	
	private float[] colour;
	private boolean isTwoSided;

	public Mesh(float[] vertecies, float[] colour, short[] vertexOrder, boolean isTwoSided) {
		numVertecies = vertexOrder.length;
		
		this.isTwoSided = isTwoSided;
		this.colour = colour;
		
		vertexBuffer = ByteBuffer.allocateDirect(BYTES_PER_FLOAT * vertecies.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexOrderBuffer = ByteBuffer.allocateDirect(BYTES_PER_SHORT * vertexOrder.length).order(ByteOrder.nativeOrder()).asShortBuffer();		
		
		vertexBuffer.put(vertecies).position(0);
		vertexOrderBuffer.put(vertexOrder).position(0);

		int vertexShader = Mesh.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = Mesh.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		shaderProgram = GLES20.glCreateProgram();
	    GLES20.glAttachShader(shaderProgram, vertexShader);
	    GLES20.glAttachShader(shaderProgram, fragmentShader);
	    GLES20.glLinkProgram(shaderProgram);
	}

	public void setPosition(float[] newPosition) {
		for(int i=0;i<3;i++) {
			worldPosition[i] = newPosition[i];
		}
	}
	
	public void getPosition(float[] retVal) {
		for(int i=0;i<3;i++) {
			retVal[i] = worldPosition[i];
		}
	}
	
	public void setRotation(float[] newRotation) {
		for(int i=0;i<4;i++) {
			worldRotation[i] = newRotation[i];
		}
	}
	
	public void getRotation(float[] retVal) {
		for(int i=0;i<4;i++) {
			retVal[i] = worldRotation[i];
		}
	}
		
	public void draw(float[] mVMatrix, float[] mProjMatrix) {
		if(isTwoSided) {
	        GLES20.glDisable(GLES20.GL_CULL_FACE);
		} else {
	        GLES20.glEnable(GLES20.GL_CULL_FACE);
		}
		
        GLES20.glUseProgram(shaderProgram);

        int mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, DIMENSIONS,
                                     GLES20.GL_FLOAT, false,
                                     VERTEX_STRIDE, vertexBuffer);

        int mColorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");

        GLES20.glUniform4fv(mColorHandle, 1, colour, 0);
        
        float[] worldPositionMatrix = new float[16];
		Matrix.setIdentityM(worldPositionMatrix , 0);
		Matrix.translateM(worldPositionMatrix, 0, worldPosition[0], worldPosition[1], worldPosition[2]);
		Matrix.rotateM(worldPositionMatrix, 0, worldRotation[3], worldRotation[0], worldRotation[1], worldRotation[2]);
		
		float[] mvpMatrix = new float[16];
		Matrix.multiplyMM(mvpMatrix, 0, mVMatrix, 0, worldPositionMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, mProjMatrix, 0, mvpMatrix, 0);

        int mVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, numVertecies,
                GLES20.GL_UNSIGNED_SHORT, vertexOrderBuffer);
        
        GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
	
	public static int loadShader(int type, String shaderCode){

	    int shader = GLES20.glCreateShader(type);

	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
}

package org.newdawn.opengltest;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class MyGL20Renderer implements GLSurfaceView.Renderer {
	private List<Mesh> meshes = new ArrayList<Mesh>();

	private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
	
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0f, 0f, 0f, 1.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        
        meshes.add(new Square());
        meshes.add(new Triangle());
    }

	@Override
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        for(Mesh mesh: meshes) {
        	mesh.draw(mMVPMatrix);
        }
    }

	@Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        
        float verticalClip = 1;
        float horizontalClip = 1;
        if(height>width) {
        	verticalClip = (float) height / width;
        } else {
            horizontalClip = (float) width / height;
        }
        

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -horizontalClip, horizontalClip, -verticalClip, verticalClip, 3, 7);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

    }

	public static int loadShader(int type, String shaderCode){

	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
}

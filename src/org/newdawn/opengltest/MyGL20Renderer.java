package org.newdawn.opengltest;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

public class MyGL20Renderer implements GLSurfaceView.Renderer {
	private static final int FRONT_CLIP_DISTANCE = 1;

	private List<Mesh> meshes = new ArrayList<Mesh>();

    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private float cameraXRotation = -45;
    private float cameraYRotation = 45;
    private float cameraDistance = 7.1f;
    private Triangle triangle;
    private Cube cube;
	
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LESS);
        
        triangle = new Triangle();
        cube = new Cube();
        
		meshes.add(triangle);
		meshes.add(cube);
    }

	@Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        long time = SystemClock.uptimeMillis() % 2000;
        float deltaTime = time/2000f;
        float offset = (float) Math.sin(deltaTime * 2 * Math.PI);
        
        cube.setPosition(new float[] {offset * 3f,0,-3});
        cube.setRotation(new float[] {1,0,0,deltaTime * 360});

        for(Mesh mesh: meshes) {
        	mesh.draw(mVMatrix, mProjMatrix);
        }
    }

	public void rotateCamera(float xAngle, float yAngle) {
		Matrix.setIdentityM(mVMatrix, 0);
		Matrix.translateM(mVMatrix, 0, 0, 0, -cameraDistance);
		
		cameraYRotation = cameraYRotation + yAngle;
		if(cameraYRotation>180) cameraYRotation = cameraYRotation - 180;
		if(cameraYRotation < -180) cameraYRotation = cameraYRotation + 180;
		
		cameraXRotation = cameraXRotation + xAngle;
		if(cameraXRotation > 90) cameraXRotation = 90;
		if(cameraXRotation < -90) cameraXRotation = -90;
		
		Matrix.rotateM(mVMatrix, 0, -cameraXRotation, 1, 0, 0);
		Matrix.rotateM(mVMatrix, 0, -cameraYRotation, 0, 1, 0);
	}
	
	@Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width / height;
		float frustumH = (float) (Math.tan(60 / 360.0f * Math.PI) * FRONT_CLIP_DISTANCE);
		float frustumW = frustumH * ratio;

		Matrix.frustumM(mProjMatrix, 0, -frustumW, frustumW, -frustumH,	frustumH, FRONT_CLIP_DISTANCE, 3000);
		
		rotateCamera(0, 0);
    }

	public static int loadShader(int type, String shaderCode){

	    int shader = GLES20.glCreateShader(type);

	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
}

package org.newdawn.opengltest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {

	private MyGL20Renderer renderer;
	
	public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        
        // Set the Renderer for drawing on the GLSurfaceView
        renderer = new MyGL20Renderer();
		setRenderer(renderer);
        
        // Render the view only when there is a change in the drawing data
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        setKeepScreenOn(true);
	}

    @Override
    public boolean onTouchEvent(MotionEvent e) {
    	switch (e.getAction()) {
        	case MotionEvent.ACTION_MOVE:
        		if(e.getHistorySize()>0) {
	        		float deltaX = e.getX() - e.getHistoricalX(e.getHistorySize()-1);
	        		float deltaY = e.getY() - e.getHistoricalY(e.getHistorySize()-1);
	        		
	        		renderer.rotateCamera(deltaY / 2, deltaX / 2);
        		}
    	}
        	
        return true;
    }
}

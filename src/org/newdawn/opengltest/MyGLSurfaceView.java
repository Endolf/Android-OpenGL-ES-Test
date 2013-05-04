package org.newdawn.opengltest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {

	private MyGL20Renderer renderer;
	
	public MyGLSurfaceView(Context context){
        super(context);

        setEGLContextClientVersion(2);
        
        renderer = new MyGL20Renderer();
		setRenderer(renderer);
        
        setKeepScreenOn(true);
	}

    @Override
    public boolean onTouchEvent(MotionEvent e) {
    	switch (e.getAction()) {
        	case MotionEvent.ACTION_MOVE:
        		if(e.getHistorySize()>0) {
	        		float deltaX = e.getX() - e.getHistoricalX(e.getHistorySize()-1);
	        		float deltaY = e.getY() - e.getHistoricalY(e.getHistorySize()-1);
	        		
	        		renderer.rotateCamera(-deltaY, -deltaX);
        		}
    	}
        	
        return true;
    }
}

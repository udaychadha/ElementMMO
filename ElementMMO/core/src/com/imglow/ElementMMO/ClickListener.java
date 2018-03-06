package com.imglow.ElementMMO;

import com.badlogic.gdx.math.Vector3;

public interface ClickListener {
	public abstract void onClick(Vector3 clickPos);
	public abstract boolean isInside(Vector3 clickPos);
	
	public int getDepth();
}

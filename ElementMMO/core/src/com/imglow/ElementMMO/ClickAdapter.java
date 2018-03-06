package com.imglow.ElementMMO;

import com.badlogic.gdx.math.Vector3;

public abstract class ClickAdapter implements ClickListener{
	public float x, y, width, height;

	@Override
	public abstract void onClick(Vector3 clickPos);

	@Override
	public boolean isInside(Vector3 clickPos) {
		if(clickPos.y < y + height && clickPos.y > y && clickPos.x > x && clickPos.x < x + width)
		{
			//System.out.println("IS isInside");
			return true;
		}
		return false;
	}

	@Override
	public int getDepth() {
		return 0;
	}
}

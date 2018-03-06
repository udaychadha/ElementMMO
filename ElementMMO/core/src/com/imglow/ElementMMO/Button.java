package com.imglow.ElementMMO;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Button implements ClickListener {
	public TextureRegion spr;
	float x, y, width, height;
	private OnClickListener l;
	
	public Button(TextureRegion spr, float x, float y, float width, float height, OnClickListener l)
	{
		this.l = l;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.spr = spr;
		ClickController.getInstance().addHandler(this);
	}

	public void draw(SpriteBatch sb)
	{
		sb.draw(spr, x, y, width, height);
	}
	
	@Override
	public boolean isInside(Vector3 clickPos) {
		//System.out.println("In isInside");
		if(clickPos.y < y + height && clickPos.y > y && clickPos.x > x && clickPos.x < x + width)
		{
			//System.out.println("IS isInside");
			return true;
		}
		return false;
	}
	
	public void dispose()
	{
		ClickController.getInstance().remaddHandler(this);
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public void onClick(Vector3 clickPos) {
		l.onClick(this, clickPos);
	}
}

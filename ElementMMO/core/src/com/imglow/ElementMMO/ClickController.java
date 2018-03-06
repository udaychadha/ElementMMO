package com.imglow.ElementMMO;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class ClickController {
	
	private static ClickController instance;
	ArrayList<ClickListener> items;
	ArrayList<PriorityClick> prorityItems; //Items that can overide all other clicks
	MainClient parent;
	
	private ClickController(){
		
	}
	
	public void initalize(MainClient parent)
	{
		items = new ArrayList<ClickListener>();
		prorityItems = new ArrayList<PriorityClick>();
		this.parent = parent;
	}
	
	void addHandler(ClickListener a)
	{
		
		if(a instanceof PriorityClick)
		{
			prorityItems.add((PriorityClick) a);
			return;
		}
		
		int i = 0;
		
		while(i < items.size() - 2 && items.get(i + 1).getDepth() < a.getDepth()) //liner insert
			i++;
		
		items.add(i, a);
	}
	
	void remaddHandler(ClickListener a)
	{
		if(a != null)
			items.remove(a);
	}
	
	void remaddAll()
	{
		items.clear();
	}
	
	boolean clickEvent(Vector3 clickPos)
	{
		System.out.println("click, size: " + items.size());
		

		for(int i = 0; i < prorityItems.size(); i++)
		{
			if(prorityItems.get(i).getPriority())
			{
				if(prorityItems.get(i).isInside(clickPos))
				{
					prorityItems.get(i).onClick(clickPos);
				}
				
				return true;
			}
		}
		
		for(int i = 0; i < items.size(); i++)
		{
			if(items.get(i).isInside(clickPos))
			{
				items.get(i).onClick(clickPos);
				
//				if(parent.soundOn)
//					parent.Sound1.play();
				
				return true;
			}
		}
		return false;
	}
	
	public static ClickController getInstance() {
		if(instance == null) {
			instance = new ClickController();
		}
		return instance;
	}
}

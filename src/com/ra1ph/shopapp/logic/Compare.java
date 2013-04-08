package com.ra1ph.shopapp.logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Compare{
	public ArrayList<Item> items_compare;
	private static Compare instance;
	
public Compare(){
	items_compare = new ArrayList<Item>();
}

public boolean search(int id){
	int i=0;
	for(i=0;i<items_compare.size();i++) if(items_compare.get(i).id==id) return true;
	return false;
}

public void removeItem(int id){
	int i=0;
	for(i=0;i<items_compare.size();i++) if(items_compare.get(i).id==id) items_compare.remove(i);
}

public static Compare getInstance() {
    if (instance == null) {
        instance = new Compare();
    }
    return instance;
}
}
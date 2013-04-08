package com.ra1ph.shopapp.logic;

import java.util.ArrayList;

import com.ra1ph.shopapp.update.ApiMultiRequest.OnMultiResultListener;

public class ApiMultiStruct {
	public ArrayList<String> request = new ArrayList<String>();
	public int type;
	public OnMultiResultListener listener;
}
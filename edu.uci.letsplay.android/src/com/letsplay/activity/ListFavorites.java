package com.letsplay.activity;

import java.util.ArrayList;
import java.util.List;

import com.letsplay.adapter.SeparatedListAdapter;
import com.letsplay.adapter.UsersAdapter;
import com.letsplay.comm.AccessServletException;
import com.letsplay.datamodel.UserEntry;


import android.content.res.Resources.NotFoundException;
import android.util.Log;

public class ListFavorites extends ListUsers{

	@Override
	protected List<UserEntry> getListOfUsers() throws NotFoundException,
			AccessServletException {
		List<UserEntry> allUsers = super.getListOfUsers();
		List<UserEntry> list = new ArrayList<UserEntry>(); 
		for(UserEntry entry : allUsers){
			if (entry.isFavorite()){
				list.add(entry);
			}
		}
		return list;
	}

	protected void populate(List<UserEntry> list){
		adapter = new SeparatedListAdapter(this);
		adapter.addSection("Nearby users", new UsersAdapter(this,list,true));	
		setListAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshList();
	}
}

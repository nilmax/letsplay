package com.letsplay.comm;

import java.util.HashMap;
import java.util.List;

import com.letsplay.datamodel.SportEntry;


public interface MessageUpdateListener {
	public void update(List<HashMap<String,SportEntry>> messages);
}

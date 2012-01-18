package com.webservice.task.progressmonitor;
/**
 * UIHookAdapter contains some shared code between all the UIHook adapters. A progress monitor
 * may have multiple clients use it, whereas an AbstractClient will only have 1 monitor. So it's
 * not safe to keep references to the clients here...
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Oct 8, 2007, 3:33:00 PM
 */
public class UIHookAdapter {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
protected UIHookIF _uiHook;

public UIHookIF getUIHook() {
  return _uiHook;
}

}//end class UIHookAdapter

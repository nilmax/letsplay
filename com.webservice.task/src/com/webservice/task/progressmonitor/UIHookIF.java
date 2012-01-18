package com.webservice.task.progressmonitor;

import java.io.*;

/**
 * UIHookIF is an interface that allows the apps to interact with a UI component
 * that's displaying status information. Two kinds of interaction are possible:
 * <ol>
 * <li>reporting status information to the UI - read status
 * <li>reporting status information to the UI - write status
 * <li>user cancelling the operation, from the UI - cancel IO operation
 * </ol>
 * <p/>
 * <b>EDT and Threading concerns</b>
 * <p/>
 * Make sure that the Swing implementation of this UIHookIF dispatches each of it's methods
 * on the EDT! Otherwise, the main UI thread will be affected by UI updates.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Sep 30, 2007, 2:30:22 PM
 */
public interface UIHookIF {

/**
 * When the status is reported (SEND or RECIEVE), this message is passed along with those
 * method calls to the UI itself.
 *
 * @param msg this message is passed to the UI along with status updates on SEND/RECIEVE
 *            this may be null.
 */
public void setProgressMessage(String msg);

/**
 * This method simply returns the message set by {@link #setProgressMessage(String)}.
 * When status updates are sent to the UI, it can choose to use this message to update
 * the UI with.
 *
 * @return may be null
 */
public String getProgressMessage();

/**
 * The SEND status lets the UI know how much of the "send to service" operation has been
 * completed. This represents the POST request being sent to an HTTP servlet for example.
 *
 * @param progress current number of bytes read
 * @param total    bytes to be read, can be 0
 */
public void updateSendStatus(int progress, int total);

/**
 * The RECIEVE status lets the UI know how much of the "recieve from service" operation has
 * been completed. This represents the POST response being recieved by the client from an
 * HTTP servlet for example.
 *
 * @param progress current number of bytes read
 * @param total    bytes to be read, can be 0
 */
public void updateRecieveStatus(int progress, int total);

/**
 * allows the UI to cancel IO operations. once called this bit is set, and all subsequent IO ops will be cancelled.
 * to undo this call {@link #resetCancelFlag}.
 */
public void cancel();

/** if the user has called {@link #cancel()} then this returns true */
public boolean isCancelled();

/** if {@link #cancel} has been called, then this method must be called to allow future IO ops to proceed. */
public void resetCancelFlag();

/** this is called when the underlying IO stream is closed, a signal that the IO operation has ended */
public void close();

/**
 * this is called when {@link #cancel} is called, or the underlying SwingWorker is cancelled. it results in the
 * underlying IO operation to be interrupted, and an {@link InterruptedIOException} to be thrown
 */
public void interrupedIO();


}//end interface UIHookIF

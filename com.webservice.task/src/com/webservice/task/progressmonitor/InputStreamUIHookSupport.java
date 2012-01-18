package com.webservice.task.progressmonitor;

import org.apache.commons.httpclient.*;

import java.io.*;

/**
 * InputStreamUIHookSupport is used to notify a UIHookIF that IO operations are being performed
 * and to report the status of the IO operation. Contrary to what you would think, this class
 * reports the WRITE status (not READ status). This class is used by HttpMBClient to send
 * the POST request. The POST method uses chunking, and the bytes are read, then sent to the
 * service... therefore, the reads tell us the "SEND status", not the "RECIEVE status".
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Sep 30, 2007, 3:41:37 PM
 */
public class InputStreamUIHookSupport extends FilterInputStream {

public enum Type {
  SendStatus,
  RecvStatus
}

private UIHookIF _uiHook;
private int _nread = 0;
private int _size = 0;
private Type _type;

/**
 * Constructs an object to monitor the progress of an input stream.
 *
 * @param in The input stream to be monitored.
 */
public InputStreamUIHookSupport(Type type,
                                UIHookIF uiHook,
                                InputStream in)
{
  super(in);

  // set the type
  _type = type;

  try {
    _size = in.available();
  }
  catch (IOException ioe) {
    _size = 0;
  }

  // save the ref to the uihook
  _uiHook = uiHook;
}

public InputStreamUIHookSupport(Type type,
                                UIHookIF uiHook,
                                HttpMethodBase method) throws IOException
{
  super(method.getResponseBodyAsStream());

  // set the type
  _type = type;

  _size = (int) method.getResponseContentLength();
  if (_size < 0) _size = 0;

  // save the ref to the uihook
  _uiHook = uiHook;
}


/**
 * Get the ProgressMonitor object being used by this stream. Normally
 * this isn't needed unless you want to do something like change the
 * descriptive text partway through reading the file.
 *
 * @return the ProgressMonitor object used by this object
 */
public UIHookIF getUIHook() {
  return _uiHook;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// internal helper methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private void _setProgress(int bytesProcessed) {
  if (_uiHook != null) {
    switch (_type) {
      case RecvStatus:
        _uiHook.updateRecieveStatus(bytesProcessed, _size);
        break;
      case SendStatus:
        _uiHook.updateSendStatus(bytesProcessed, _size);
        break;
    }
  }
}

private boolean _isCanceled() {
  if (_uiHook != null) {
    return _uiHook.isCancelled();
  }
  else {
    return false;
  }
}

private void _close() {
  if (_uiHook != null) _uiHook.close();
}

private void _interruptedIO() {
  if (_uiHook != null) _uiHook.interrupedIO();
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// FilteredInputStream override...
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX


/**
 * Overrides <code>FilterInputStream.read</code>
 * to update the progress monitor after the read.
 */
@Override public int read() throws IOException {
  int c = in.read();
  if (c >= 0) _setProgress(++_nread);
  _checkCancelled();
  return c;
}


/**
 * Overrides <code>FilterInputStream.read</code>
 * to update the progress monitor after the read.
 */
public int read(byte b[]) throws IOException {
  int nr = in.read(b);
  if (nr > 0) _setProgress(_nread += nr);
  _checkCancelled();
  return nr;
}


/**
 * Overrides <code>FilterInputStream.read</code>
 * to update the progress monitor after the read.
 */
public int read(byte b[],
                int off,
                int len) throws IOException
{
  int nr = in.read(b, off, len);
  if (nr > 0) _setProgress(_nread += nr);
  _checkCancelled();
  return nr;
}

private void _checkCancelled() throws IOException {
  if (_isCanceled()) {
    InterruptedIOException exc =
        new InterruptedIOException("IO operation cancelled");
    exc.bytesTransferred = _nread;
    //System.out.println("throwing io exception");
    _interruptedIO();
    throw exc;
  }
}

/**
 * Overrides <code>FilterInputStream.skip</code>
 * to update the progress monitor after the skip.
 */
public long skip(long n) throws IOException {
  long nr = in.skip(n);
  if (nr > 0) _setProgress(_nread += nr);
  return nr;
}


/**
 * Overrides <code>FilterInputStream.closeInUI</code>
 * to closeInUI the progress monitor as well as the stream.
 */
public void close() throws IOException {
  in.close();
  _close();
}


/**
 * Overrides <code>FilterInputStream.reset</code>
 * to reset the progress monitor as well as the stream.
 */
public synchronized void reset() throws IOException {
  in.reset();
  _nread = _size - in.available();
  _setProgress(_nread);
}

}//end class InputStreamUIHookSupport

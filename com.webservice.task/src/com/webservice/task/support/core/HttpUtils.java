package com.webservice.task.support.core;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.lang.*;

import com.webservice.task.progressmonitor.*;

import java.io.*;

/**
 * HttpUtils is a utility class that allows quick & easy access to monitored
 * HTTP GET and POST operations. Saves you the trouble of closing Methods and
 * hooking up {@link InputStreamUIHookSupport} objects, etc.
 * 
 * @author Nazmul Idris
 * @version 1.0
 * @since Mar 27, 2008, 9:31:29 PM
 */
public class HttpUtils {

	/**
	 * can monitor an HTTP GET or POST response. make sure to release the
	 * connection once the POST/GET method is complete, using
	 * {@link HttpMethodBase#releaseConnection()}
	 */
	public static ByteBuffer getMonitoredResponse(UIHookAdapter hook,
			HttpMethodBase method) throws IOException, IllegalArgumentException {
		Validate.notNull(method, "method can not be null");

		try {
			InputStreamUIHookSupport is = new InputStreamUIHookSupport(
					InputStreamUIHookSupport.Type.RecvStatus,
					hook == null ? null : hook.getUIHook(), method);

			return new ByteBuffer(is);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * can monitor HTTP POST request (sending data to the HTTP server. make sure
	 * to release the connection once the POST/GET method is complete, using
	 * {@link HttpMethodBase#releaseConnection()}
	 */
	public static PostMethod sendMonitoredPOSTRequest(String uri,
			UIHookAdapter hook, ByteBuffer data, String mimeType)
			throws IOException, IllegalArgumentException {
		Validate.notEmpty(uri, "uri can not be empty or null");
		Validate.notNull(data, "data can not be null");

		String type = CoreUtils.isNullOrEmpty(mimeType) ? "application/octet-stream"
				: mimeType;

		RequestEntity input = new InputStreamRequestEntity(
				new InputStreamUIHookSupport(
						InputStreamUIHookSupport.Type.SendStatus,
						hook == null ? null : hook.getUIHook(), data
								.getInputStream()), type);

		// send the input parameter
		PostMethod post = new PostMethod(uri);
		post.setRequestEntity(input);
		post.setContentChunked(true);
		new HttpClient().executeMethod(post);

		return post;
	}

}// end class HttpUtils

package com.sebster.poker.webservices;

import org.jabsorb.ExceptionTransformer;
import org.jabsorb.JSONRPCResult;
import org.json.JSONException;
import org.json.JSONObject;

public class NoStackTraceErrorTransformer implements ExceptionTransformer {

	private static final long serialVersionUID = 1L;

	@Override
	public Object transform(final Throwable throwable) {
		final JSONObject result = new JSONObject();
		try {
			result.put("code", JSONRPCResult.CODE_REMOTE_EXCEPTION);
			result.put("msg", throwable.getMessage());
			result.put("name", throwable.getClass().getName());
			return result;
		} catch (final JSONException e) {
			// Cannot happen.
			return null;
		}
	}


}

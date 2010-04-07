package com.sebster.poker.webservices;

import org.jabsorb.JSONSerializer;
import org.jabsorb.serializer.MarshallException;
import org.jabsorb.serializer.ObjectMatch;
import org.jabsorb.serializer.Serializer;
import org.jabsorb.serializer.SerializerState;
import org.jabsorb.serializer.UnmarshallException;
import org.json.JSONArray;
import org.json.JSONException;

import com.sebster.poker.odds.Odds;

@SuppressWarnings("unchecked")
public class OddsSerializer implements Serializer {

	private static final long serialVersionUID = 1L;

	@Override
	public void setOwner(final JSONSerializer owner) {
		// Do nothing.
	}

	@Override
	public boolean canSerialize(final Class clazz, final Class jsonClazz) {
		return Odds.class.isAssignableFrom(clazz) && (jsonClazz == null || String.class.equals(jsonClazz));
	}

	@Override
	public Class[] getJSONClasses() {
		return new Class[] { JSONArray.class };
	}

	@Override
	public Class[] getSerializableClasses() {
		return new Class[0];
	}

	@Override
	public ObjectMatch tryUnmarshall(final SerializerState state, final Class clazz, final Object json) throws UnmarshallException {
		return ObjectMatch.NULL;
	}

	@Override
	public Object unmarshall(final SerializerState state, final Class clazz, final Object json) throws UnmarshallException {
		throw new UnmarshallException("unmarshall not supported");
	}

	@Override
	public Object marshall(final SerializerState state, final Object parent, final Object object) throws MarshallException {
		final Odds odds = (Odds) object;
		if (object == null) {
			return null;
		}
		final Integer[] splits = new Integer[odds.getMaxN() + 1];
		for (int i = 0; i <= odds.getMaxN(); i++) {
			splits[i] = odds.getNWaySplits(i);
		}
		try {
			return new JSONArray(splits);
		} catch (final JSONException e) {
			// Cannot happen.
			throw new RuntimeException(e.getMessage());
		}
	}

}

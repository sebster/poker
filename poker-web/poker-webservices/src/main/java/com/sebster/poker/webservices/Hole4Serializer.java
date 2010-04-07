package com.sebster.poker.webservices;

import org.jabsorb.JSONSerializer;
import org.jabsorb.serializer.MarshallException;
import org.jabsorb.serializer.ObjectMatch;
import org.jabsorb.serializer.Serializer;
import org.jabsorb.serializer.SerializerState;
import org.jabsorb.serializer.UnmarshallException;

import com.sebster.poker.Hole4;

@SuppressWarnings("unchecked")
public class Hole4Serializer implements Serializer {

	private static final long serialVersionUID = 1L;

	@Override
	public void setOwner(final JSONSerializer owner) {
		// Do nothing.
	}

	@Override
	public boolean canSerialize(final Class clazz, final Class jsonClazz) {
		return Hole4.class.equals(clazz) && (jsonClazz == null || String.class.equals(jsonClazz));
	}

	@Override
	public Class[] getJSONClasses() {
		return new Class[] { String.class };
	}

	@Override
	public Class[] getSerializableClasses() {
		return new Class[] { Hole4.class };
	}

	@Override
	public ObjectMatch tryUnmarshall(final SerializerState state, final Class clazz, final Object json) throws UnmarshallException {
		return ObjectMatch.NULL;
	}

	@Override
	public Object unmarshall(final SerializerState state, final Class clazz, final Object json) throws UnmarshallException {
		return json != null ? Hole4.fromString((String) json) : null;
	}

	@Override
	public Object marshall(final SerializerState state, final Object parent, final Object object) throws MarshallException {
		return object != null ? object.toString() : null;
	}

}

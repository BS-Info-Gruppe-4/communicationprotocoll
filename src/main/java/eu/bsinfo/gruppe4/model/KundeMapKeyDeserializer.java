package eu.bsinfo.gruppe4.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;


public class KundeMapKeyDeserializer extends KeyDeserializer{
	
	@Override
	public Kunde deserializeKey(String key, DeserializationContext ctxt) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(key, Kunde.class);
	}

}
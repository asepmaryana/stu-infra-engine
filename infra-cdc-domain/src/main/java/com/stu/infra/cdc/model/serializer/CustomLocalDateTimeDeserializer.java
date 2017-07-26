package com.stu.infra.cdc.model.serializer;

import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
	
	private static final DateTimeFormatter formatter = 
	        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		
		return LocalDateTime.parse(parser.getText(), formatter);
	}

}

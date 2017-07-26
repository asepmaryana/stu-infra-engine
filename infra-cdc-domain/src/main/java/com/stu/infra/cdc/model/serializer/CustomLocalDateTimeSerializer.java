package com.stu.infra.cdc.model.serializer;

import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
	
	private static final DateTimeFormatter formatter = 
	        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void serialize(LocalDateTime dateTime, JsonGenerator gen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		gen.writeString(formatter.print(dateTime));
	}

}

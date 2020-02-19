package br.com.damoreira.commons.axon.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import org.axonframework.serialization.Converter;
import org.axonframework.serialization.SerializedObject;
import org.axonframework.serialization.SerializedType;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.xml.CompactDriver;
import org.axonframework.serialization.xml.XStreamSerializer;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class MultipleDeserializerAndXStreamSerializer implements Serializer {

    private XStreamSerializer xStreamSerializer;

    private JacksonSerializer jacksonSerializer;

    public MultipleDeserializerAndXStreamSerializer() {
        XStream xStream = new XStream(new CompactDriver());
        xStream.ignoreUnknownElements();

        xStreamSerializer = XStreamSerializer.builder()
            .xStream(xStream)
            .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);

        jacksonSerializer = JacksonSerializer.builder()
            .objectMapper(mapper)
            .build();
    }

    @Override
    public <T> SerializedObject<T> serialize(Object object, Class<T> expectedRepresentation) {
        return xStreamSerializer.serialize(object, expectedRepresentation);
    }

    @Override
    public <T> boolean canSerializeTo(Class<T> expectedRepresentation) {
        return xStreamSerializer.canSerializeTo(expectedRepresentation);
    }

    @Override
    public <S, T> T deserialize(SerializedObject<S> serializedObject) {
        try {
            return xStreamSerializer.deserialize(serializedObject);
        } catch (Throwable e) {
            return jacksonSerializer.deserialize(serializedObject);
        }
    }

    @Override
    public Class classForType(SerializedType type) {
        return xStreamSerializer.classForType(type);
    }

    @Override
    public SerializedType typeForClass(Class type) {
        return xStreamSerializer.typeForClass(type);
    }

    @Override
    public Converter getConverter() {
        return xStreamSerializer.getConverter();
    }

}
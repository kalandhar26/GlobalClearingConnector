package com.hdfc.imps.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@Component
public class JAXBUtil {
    public static String marshal(MapWrapper obj) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(MapWrapper.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, new StreamResult(writer));
        return writer.toString();
    }
}

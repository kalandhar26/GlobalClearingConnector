package com.hdfc.imps.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.hdfc.imps.constants.BasicConstants.CLASSPATH;

@Service
@Slf4j
public class ProducerService {
    private final ResourceLoader resourceLoader;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerService(ResourceLoader resourceLoader, KafkaTemplate<String, String> kafkaTemplate) {
        this.resourceLoader = resourceLoader;
        this.kafkaTemplate = kafkaTemplate;
    }

    public String readXmlFileData(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource(CLASSPATH + fileName);
        /*
        Path path = Paths.get(resource.getURI());
        return Files.readString(path);
        */

        try(InputStream inputStream = resource.getInputStream()){
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

    public Map<String, Object> parseXmlToMap(String xmlContent) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xmlContent, Map.class);
    }

    public Map<String, Object> processXmlFile(String fileName, String topic) {
        try {
            String xmlContent = readXmlFileData(fileName);
            Map<String, Object> xmlmap = parseXmlToMap(xmlContent);
            log.info("xml data sent : {}", xmlmap);
            kafkaTemplate.send(topic, xmlContent);
            return xmlmap;
        } catch (IOException e) {
            log.error("Error processing XML file", e);
            throw new RuntimeException(e);
        }
    }
}

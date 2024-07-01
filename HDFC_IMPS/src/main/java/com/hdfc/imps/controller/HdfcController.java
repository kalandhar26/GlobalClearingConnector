package com.hdfc.imps.controller;

import com.hdfc.imps.service.ProducerService;
import com.hdfc.imps.util.JAXBUtil;
import com.hdfc.imps.util.MapWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1")
public class HdfcController {
    private final ProducerService producerService;

    public HdfcController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping(value = "/sendXml", produces = "application/xml")
    public ResponseEntity<String> sendXmlData(@RequestParam String fileName, @RequestParam String topic) {
        Map<String, Object> xmlData = producerService.processXmlFile(fileName, topic);

        try {
            MapWrapper mapWrapper = new MapWrapper(xmlData);
            String result = JAXBUtil.marshal(mapWrapper);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.hdfc.imps.util;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Map;

@XmlRootElement(name = "Message")
public class MapWrapper {
    @XmlElement(name = "originBank")
    private String originBank;

    @XmlElement(name = "originCountry")
    private String originCountry;

    @XmlElement(name = "clearing")
    private String clearing;

    @XmlElement(name = "destinationBank")
    private String destinationBank;

    @XmlElement(name = "destinationCountry")
    private String destinationCountry;

    @XmlElement(name = "MessageId")
    private String messageId;

    @XmlElement(name = "TransactionId")
    private String transactionId;

    @XmlElement(name = "FirmrootId")
    private String firmrootId;

    @XmlElement(name = "TransactionType")
    private String transactionType;

    public MapWrapper() {
    }

    public MapWrapper(Map<String, Object> map) {
        this.originBank = (String) map.get("originBank");
        this.originCountry = (String) map.get("originCountry");
        this.clearing = (String) map.get("clearing");
        this.destinationBank = (String) map.get("destinationBank");
        this.destinationCountry = (String) map.get("destinationCountry");
        this.messageId = (String) map.get("MessageId");
        this.transactionId = (String) map.get("TransactionId");
        this.firmrootId = (String) map.get("FirmrootId");
        this.transactionType = (String) map.get("TransactionType");
    }
}


package com.sbi.neft.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OutboundPayload {
    @Id
    private String outboundPayloadID;
    @ManyToOne
    @JoinColumn(name = "connectivityProfileID")
    private ConnectivityProfile connectivityProfile;
    @Lob
    @Column(name = "payloadRawData", columnDefinition = "LONGBLOB")
    private byte[] payloadRawData;
}

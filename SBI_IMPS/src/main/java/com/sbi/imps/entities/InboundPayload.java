package com.sbi.imps.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "InboundPayload")
@Data
public class InboundPayload {
    @Id
    private String inboundPayloadID;
    @ManyToOne
    @JoinColumn(name = "connectivityProfileID")
    private ConnectivityProfile connectivityProfile;
    private String workflowID;
    @Lob
    @Column(name = "payloadRawData", columnDefinition = "LONGBLOB")
    private byte[] payloadRawData;
}

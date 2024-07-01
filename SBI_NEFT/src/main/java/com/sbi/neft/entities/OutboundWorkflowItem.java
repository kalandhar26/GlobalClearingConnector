package com.sbi.neft.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OutboundWorkflowItem")
@Data
public class OutboundWorkflowItem {

    @Id
    private String outboundWorkflowID;

    private String workflowID;
    private String targetReference;
    private String messageID;
    private String transactionID;
    private String status;

    @ManyToOne
    @JoinColumn(name = "outboundPayloadID")
    private OutboundPayload outboundPayload;
    @ManyToOne
    @JoinColumn(name = "messageProfileID")
    private MessageProfile messageProfile;
}

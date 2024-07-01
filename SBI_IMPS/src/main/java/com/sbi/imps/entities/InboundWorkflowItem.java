package com.sbi.imps.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "InboundWorkflowItem")
@Data
public class InboundWorkflowItem {
    @Id
    private String uniqueID;
    private String workflowID;
    private String messageID;
    private String sourceReference;
    private String status;
}

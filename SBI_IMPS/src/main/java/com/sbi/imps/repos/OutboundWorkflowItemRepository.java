package com.sbi.imps.repos;

import com.sbi.imps.entities.OutboundWorkflowItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundWorkflowItemRepository extends JpaRepository<OutboundWorkflowItem, String> {
}


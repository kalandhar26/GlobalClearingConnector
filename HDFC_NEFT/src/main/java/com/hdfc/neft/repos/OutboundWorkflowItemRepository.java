package com.hdfc.neft.repos;

import com.hdfc.imps.entities.OutboundWorkflowItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundWorkflowItemRepository extends JpaRepository<OutboundWorkflowItem, String> {
}


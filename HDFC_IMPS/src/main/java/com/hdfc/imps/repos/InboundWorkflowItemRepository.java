package com.hdfc.imps.repos;

import com.hdfc.imps.entities.InboundWorkflowItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundWorkflowItemRepository extends JpaRepository<InboundWorkflowItem, String> {
}


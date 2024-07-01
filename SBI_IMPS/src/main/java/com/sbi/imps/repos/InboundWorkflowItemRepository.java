package com.sbi.imps.repos;

import com.sbi.imps.entities.InboundWorkflowItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundWorkflowItemRepository extends JpaRepository<InboundWorkflowItem, String> {
}


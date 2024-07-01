package com.sbi.imps.repos;

import com.sbi.imps.entities.OutboundPayload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundPayloadRepository extends JpaRepository<OutboundPayload, String> {
}

package com.hdfc.imps.repos;

import com.hdfc.imps.entities.OutboundPayload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundPayloadRepository extends JpaRepository<OutboundPayload, String> {
}

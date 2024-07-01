package com.hdfc.neft.repos;

import com.hdfc.imps.entities.OutboundPayload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundPayloadRepository extends JpaRepository<OutboundPayload, String> {
}

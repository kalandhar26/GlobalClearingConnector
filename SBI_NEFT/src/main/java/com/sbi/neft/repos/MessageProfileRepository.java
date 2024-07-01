package com.sbi.neft.repos;

import com.hdfc.imps.entities.MessageProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageProfileRepository extends JpaRepository<MessageProfile, String> {
}

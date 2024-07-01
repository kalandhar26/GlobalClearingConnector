package com.sbi.neft.repos;

import com.hdfc.imps.entities.ConnectivityProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectivityProfileRepository extends JpaRepository<ConnectivityProfile,String> {
}

package com.sbi.imps.repos;

import com.sbi.imps.entities.ConnectivityProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectivityProfileRepository extends JpaRepository<ConnectivityProfile,String> {
}

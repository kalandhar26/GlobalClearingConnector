package com.sbi.imps.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MessageProfile")
@Data
public class MessageProfile {
    @Id
    private String profileID;

    private String type;
    private String parent;
    private String description;
    @ManyToOne
    @JoinColumn(name = "connectivityProfileID")
    private ConnectivityProfile connectivityProfile;
}

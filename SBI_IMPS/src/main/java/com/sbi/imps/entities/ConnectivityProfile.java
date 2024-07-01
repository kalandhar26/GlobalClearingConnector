package com.sbi.imps.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="ConnectivityProfile")
@Data
public class ConnectivityProfile {

    @Id
    private String profileID;
    private String type;
    private String description;

    @OneToMany(mappedBy = "connectivityProfile", cascade = CascadeType.ALL)
    private List<MessageProfile> messageProfiles;

    @OneToMany(mappedBy = "connectivityProfile", cascade = CascadeType.ALL)
    private List<InboundPayload> inboundPayloads;

    @OneToMany(mappedBy = "connectivityProfile", cascade = CascadeType.ALL)
    private List<OutboundPayload> outboundPayloads;
}

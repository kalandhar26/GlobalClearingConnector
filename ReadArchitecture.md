# GC2 Payment Processing System Architecture

The Global Clearing Connector (GC2) is a microservice designed to facilitate payment processing between HDFC Bank (
Plutus) and various clearing bodies. This document details the architecture, data flow, and design of the GC2 system.

# RealTime Payment Clearing Services

- *Domestic and Cross Border Payments*

# Key Components

# GC2 Microservice:

Central processing unit receiving and sending data to other services.

# Oracle Database:

Stores inbound and outbound data in two primary tables: InboundPayload and WorkFlowItem.

# HDFC Bank (Plutus):

Sends transaction data to GC2 in JSON format.

# Clearing Bodies:

External entities like RBI that clear and settle transactions.

# Data Tables

# Inbound Tables

# InboundPayload Table

InboundPayloadId: Primary Key
WorkflowID: Foreign Key (references WorkFlowItem Table)
Payload: Raw data stored in binary format

# InboundWorkFlowItem Table

WorkflowID: Primary Key
Status: Tracks the processing status (e.g., Created, Accepted)

# Outbound Tables

# OutboundWorkFlowItem Table

OutBoundWorkflowID: Primary Key
WorkflowID: Foreign Key (references WorkFlowItem Table)
OutboundPayloadID: Foreign Key (references OutboundPayload Table)
Status: Tracks the processing status (e.g., Created, Sent)

# OutboundPayload Table

OutboundPayloadId: Primary Key
Payload: Transformed data stored in binary format

# Data Flow and Process Steps

# Inbound Processing (Receive Part)

1. Data Reception:
   GC2 receives JSON formatted data (Payload) from HDFC Bank (Plutus).
   Insert this data into the InboundPayload Table with initial status "Received".

2. Parsing and Validation:
   Parse the data from the InboundPayload Table.
   Validate the parsed data for correctness.
   Insert a new entry in the WorkFlowItem Table with status "Created".
   Update the WorkflowID in the InboundPayload Table with the generated WorkflowID.
3. Status Update:
   If validation is successful, update the status in the WorkFlowItem Table to "Accepted".
   If validation fails, send a Negative Acknowledgment (NACK) to the sender.

4. Trigger Transformation:
   An asynchronous call is triggered to start a new transaction for data transformation upon successfull validation.

# Outbound Processing (Send Part)

1. Initiate Transformation:
   Insert a new entry in the OutboundWorkFlowItem Table with status "Created" and link it to the WorkflowID from the
   WorkFlowItem Table.

2. Data Transformation:
   Transform the data to the required format using a tool like C24.
   Create a new entry in the OutboundPayload Table with the transformed data (Payload).

3. Update Outbound Status:
   Update the OutboundPayloadID in the OutboundWorkFlowItem Table and change the status to "Sent".

4. Sending Data:
   Send the transformed data to the appropriate clearing body.
   Await acknowledgment from the clearing body.

# Summary of Table Relationships

InboundPayload Table links to the InboundWorkFlowItem Table via WorkflowID.
OutboundWorkFlowItem Table links to:
InboundWorkFlowItem Table via WorkflowID.
OutboundPayload Table via OutboundPayloadID.

# Data Flow Diagram

# Inbound Flow:

HDFC Bank (Plutus) → GC2 → InboundPayload Table → Parse & Validate → WorkFlowItem Table (status: Created) → Status
Update (Accepted) → Asynchronous Call for Transformation.

# Outbound Flow:

WorkFlowItem Table (status: Accepted) → OutboundWorkFlowItem Table (status: Created) → Data Transformation (C24 Tool) →
OutboundPayload Table → Update OutboundWorkFlowItem Table (status: Sent) → Send Data to Clearing Body.

#Conclusion
The GC2 architecture ensures reliable and efficient payment processing by handling both inbound and outbound data flows,
maintaining data integrity, and providing resiliency through asynchronous processing and multiple communication methods.
This detailed design allows for scalability and adaptability to various clearing bodies and communication protocols.
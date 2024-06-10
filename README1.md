# End-to-End Payment Processing for a Single Channel (HDFC Channel)

This document outlines the step-by-step approach and design for processing payments through a single channel (HDFC
Channel) using the Global Clearing Connector (GC2). It details how the system handles different modes of communication,
ensures resiliency, and processes both outward and inward flows of transactions.

# Key Components

# GC2 (Global Clearing Connector):

Acts as an intermediary between banks and clearing bodies.

# HDFC Bank:

The initiating bank for transactions.

# Plutus:

Internal system handling transaction processing for HDFC Bank.

# Clearing Bodies:

Entities like RBI, which clear and settle transactions between banks.

# Communication Methods:

MQ (Message Queue), JMS (Java Messaging Service), HTTP/REST, TCP, Kafka.

# Transaction Flow

# Outward Flow (Sending Payments)

1. Initiation:
   HDFC Bank sends transaction data to Plutus.
   Plutus processes the transaction and sends data to GC2.
2. GC2 to Clearing Body:
   GC2 decides the communication method based on the clearing body's requirements (e.g., MQ).
   GC2 sends the data to the clearing body (e.g., RBI).
3. Clearing Body Processing:
   The clearing body processes the transaction and sends an acknowledgment back to GC2.
4. Acknowledgment Handling:
   GC2 receives the acknowledgment from the clearing body.
   GC2 converts the acknowledgment data into a format understood by Plutus.
   Plutus receives the acknowledgment and settles or rejects the transaction accordingly.

# Inward Flow (Receiving Payments)

1. Initiation:
   An external bank (e.g., SBI) sends transaction data to their clearing body (e.g., RBI).
   The clearing body processes the transaction and sends data to GC2.
2. GC2 to Plutus:
   GC2 converts the received data into the format required by Plutus.
   Plutus processes the transaction and updates HDFC Bank's records.
3. Acknowledgment Handling:
   Plutus sends an acknowledgment back to GC2.
   GC2 converts this acknowledgment into the format required by the clearing body.
   The clearing body receives the acknowledgment from GC2 and completes the transaction cycle.

# Handling Failures and Resiliency

Redundant Connections:
For each communication channel, there will be multiple connections (e.g., MQ1 and MQ2) to ensure resiliency.
If one connection fails, the other takes over.

Batch Processing:
Transactions are processed in batches to ensure efficiency and manageability.

Example Scenarios

# Outward Flow: IRCT (Real-Time Payment)

1. Sending Payment (IRCT):
   HDFC Bank (Plutus) sends ₹10,000 to SBI Bank through GC2.
   GC2 sends the data to RBI using MQ.
   RBI processes the transaction and sends an acknowledgment back to GC2.
   GC2 converts the acknowledgment to Plutus format and sends it to Plutus.
   Plutus settles the transaction in HDFC Bank's system.

2. Return Payment (IRCT Return):
   HDFC Bank sends payment to SBI, but the SBI account is closed.
   SBI sends the money back to RBI.
   RBI sends the acknowledgment and the money back to GC2.
   GC2 converts the data to Plutus format and sends it to Plutus.
   Plutus processes the return and updates HDFC Bank's records.

# Inward Flow: ICDT (Non-Real-Time Payment)

1. Receiving Payment (ICDT):
   SBI sends payment to HDFC through RBI.
   RBI sends the data to GC2.
   GC2 converts the data and sends it to Plutus.
   Plutus processes the transaction and credits the amount to HDFC Bank's account.

2. Return Payment (ICDT Return):
   HDFC Bank receives a payment, but the account details are incorrect.
   HDFC (Plutus) sends the return data to GC2.
   GC2 converts and sends the data to RBI.
   RBI processes the return and sends acknowledgment back to GC2.
   GC2 sends the acknowledgment to Plutus, which updates HDFC Bank's records.

# Conclusion

The GC2 system ensures seamless, resilient, and efficient payment processing across various communication methods and
clearing bodies. By understanding and implementing these steps, HDFC Bank can effectively handle both outward and inward
payment flows, ensuring reliable transaction processing and acknowledgment handling.






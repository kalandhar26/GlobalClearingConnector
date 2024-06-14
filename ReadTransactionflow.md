# Communication Flow Overview

## Connection Profiles

| Profile ID             | Type   | Description                    |
|------------------------|--------|--------------------------------|
| IND-FA-IRCT-C001       | Kafka  | From Plutus to GC2             |
| IND-FA-IRCT-C002       | MQ     | From GC2 to Clearing           |
| IND-FA-RRCT-C001       | MQ     | From Clearing to GC2           |
| IND-FA-RRCT-C002       | Kafka  | From GC2 to Plutus             |

## Tables

### ConnectionProfile Table

- **Fields**: Profile ID, Type, Description
- **Example Entries**:
    - `Profile ID`: IND-FA-IRCT-C001
    - `Type`: Kafka
    - `Description`: From Plutus to GC2

### MessageProfile Table

- **Fields**: MessageProfile ID, ConnectionProfile ID, Parent MessageProfile ID (if any)
- **Example Entries**:
    - `MessageProfile ID`: M1
    - `ConnectionProfile ID`: IND-FA-IRCT-C001
    - `Parent MessageProfile ID`: None

### InboundPayload Table

- **Fields**: InboundPayload ID, RawData, ConnectionProfile ID, WorkflowID
- **Example Entries**:
    - `InboundPayload ID`: 1
    - `RawData`: JSON/XML Data
    - `ConnectionProfile ID`: IND-FA-IRCT-C001
    - `WorkflowID`: W1

### InboundWorkFlowItem Table

- **Fields**: InboundWorkFlowItem ID, Status, UniqueFirmRootID, WorkflowID
- **Example Entries**:
    - `InboundWorkFlowItem ID`: 1
    - `Status`: Created/Accepted
    - `UniqueFirmRootID`: F1112345
    - `WorkflowID`: W1

### OutboundWorkFlowItem Table

- **Fields**: OutboundWorkFlowItem ID, Status, UniqueMessageID, TransactionID, WorkflowID
- **Example Entries**:
    - `OutboundWorkFlowItem ID`: 1
    - `Status`: Sent/Failed
    - `UniqueMessageID`: MESSID123
    - `TransactionID`: TRAN123
    - `WorkflowID`: W2

### OutboundPayload Table

- **Fields**: OutboundPayload ID, RawData, ConnectionProfile ID, WorkflowID
- **Example Entries**:
    - `OutboundPayload ID`: 1
    - `RawData`: XML Data
    - `ConnectionProfile ID`: IND-FA-IRCT-C002
    - `WorkflowID`: W2

### WorkFlowError Table

- **Fields**: ErrorID, WorkflowID, ErrorMessage, Timestamp
- **Example Entries**:
    - `ErrorID`: 1
    - `WorkflowID`: W2
    - `ErrorMessage`: Connection Timeout
    - `Timestamp`: 2024-06-10 10:00:00

## Transaction Identification and Status Check

### Identifying and Tracing IRCT Transaction

1. **Identify the ConnectionProfile**:
    - Example: Fetch `ConnectionProfile - IND-FA-IRCT-C001` for Kafka.

2. **Check InboundPayload Table**:
    - Retrieve entry for `IND-FA-IRCT-C001`.
    - Ensure raw data and connection information are correct.
    - Fields: `InboundPayloadID, RawData, ConnectionProfileID, WorkflowID`

3. **Check InboundWorkFlowItem Table**:
    - Retrieve status using the `UniqueFirmRootID`.
    - Example: Check status for entry with `FirmRoot ID = F1112345`.
    - Fields: `InboundWorkFlowItemID, Status, UniqueFirmRootID, WorkflowID`

4. **Identify Outbound ConnectionProfile**:
    - Example: Fetch `ConnectionProfile - IND-FA-IRCT-C002` for MQ.

5. **Check OutboundWorkFlowItem Table**:
    - Use `WorkflowID` from `InboundWorkFlowItem Table` to track the status of outbound processing.
    - Fields: `OutboundWorkFlowItemID, Status, UniqueMessageID, TransactionID, WorkflowID`

6. **Check OutboundPayload Table**:
    - Retrieve outbound payload status using the `OutboundPayloadID`.
    - Fields: `OutboundPayloadID, RawData, ConnectionProfileID, WorkflowID`

7. **Handle Errors**:
    - Log any errors in the `WorkFlowError Table`.
    - Fields: `ErrorID, WorkflowID, ErrorMessage, Timestamp`

## Validating Data Sent from GC2 to Clearing

1. **Check OutboundWorkFlowItem Table**:
    - Verify if the `OutboundWorkflowID` is generated with the `WorkflowID`.
    - Fields: `OutboundWorkFlowItemID, Status, UniqueMessageID, TransactionID, WorkflowID`

2. **Validate Data Sent to Clearing**:
    - Confirm that the status is marked as "Sent".
    - Retrieve the `OutboundPayloadID`.
    - Fields: `OutboundWorkFlowItemID, Status, WorkflowID`

3. **Check OutboundPayload Table**:
    - Use the `OutboundPayloadID` to get the raw data.
    - Fields: `OutboundPayloadID, RawData, ConnectionProfileID, WorkflowID`

## Common Logic for Communication Handling

1. **Loading Configuration**:
    - Load configurations from the properties file to determine the mode of communication.

2. **Establishing Connection**:
    - Use a generic method to establish a connection based on the mode (e.g., Kafka, MQ, TCP, REST).

3. **Processing Messages**:
    - Retrieve the appropriate `ConnectionProfile` and `MessageProfile`.
    - Handle inbound and outbound payloads and workflows based on the profile references.
    - Log errors in the `WorkFlowError Table` when necessary.

4. **Status Updates and Transformations**:
    - Once the status changes from "Created" to "Accepted" in the `InboundWorkFlowItem Table`, update the `InboundPayload Table` with the `WorkflowID`.
    - Transform data using the C24 tool library.

## Example JSON to XML Transformation

### JSON Input

```json
{
    "FirmrootId": "F1112345",
    "Amount": 100000,
    "Message_ID": "MESSID123",
    "Transaction_ID": "TRAN123"
}

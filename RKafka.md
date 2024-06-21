# Kafka Introduction

Kafka, at its core, is a high-throughput, low-latency platform designed for handling real-time data feeds.

## Key Concepts of Kafka

### Topics

- Stores streams of records in categories.
- Each record consists of a key, a value, and a timestamp.

### Producers

- Client applications that publish or write events to Kafka topics.

### Consumers

- Client applications that subscribe to (read and process) events from Kafka topics.

### Brokers

- Kafka runs as a cluster of one or more servers, each called a broker.
- Responsible for facilitating reads and writes, maintaining topic partitions, and replicating data.

### Partitions

- Topics are divided into several partitions.
- Allows data to be distributed and consumed in parallel across the Kafka cluster.

### Consumer Groups

- Consumers label themselves with a consumer group name.
- Each record published to a topic is delivered to one consumer instance within each subscribing consumer group.

### Offsets

- Each record within a partition has an offset, a unique identifier.
- Consumers keep track of offsets to know which records have been consumed.

```
Steps
Producer sends a message to a topic (one at a time).
A topic has zero or more producers.
A topic is replicated over one or more partitions.
A topic has zero or more consumers.
A consumer subscribes to one or more topics.
A consumer is a member of one consumer group.
A partition has one consumer (per group).
A consumer pulls messages from zero or more partitions (per topic).
A cluster has one or more brokers.
A broker is part of one cluster.
A replica is on one broker.
A partition has one leader.
A partition has zero or more followers.
```

### Offsets Management:

- Consumers commit offsets to Kafka to mark records as read.
- Offset management can be automatic or manual.

### Data Durability:

- Data in Kafka is written to disk and can be replicated across multiple brokers.
- Configurable retention policies for topics (e.g., retain data for a certain period).

### High Availability:
- Kafka's architecture ensures high availability and fault tolerance.
- Leader-follower model for partitions ensures data redundancy.

### Scalability:

- Kafka can handle hundreds of gigabytes of reads and writes per second from thousands of clients.
- Partitions allow horizontal scaling of topics across multiple brokers.

## Common Use Cases

### Messaging:

- Acts as a publish-subscribe messaging system.

### Website Activity Tracking:

- Collects and processes user activity data from websites and applications.

### Metrics and Logging:

- Aggregates logs and metrics for monitoring and analysis.

### Stream Processing:

- Processes continuous streams of data in real-time.

### Event Sourcing:

- Stores changes as a sequence of events, enabling reconstructing system states.

### Conclusion

Kafka's unique architecture, which includes topics, partitions, and consumer groups,  
allows it to handle massive dataflows efficiently. This makes Kafka an ideal choice  
for real-time data pipelines and streaming applications, enabling organizations to react to data in real-time.

- **Producer:** Sends messages to topics.  
- **Topic:** Has multiple producers and consumers; replicated over partitions.  
- **Partition:** The unit of parallelism in Kafka; each partition has one leader and multiple replicas.  
- **Consumer:** Subscribes to topics; part of consumer groups.  
- **Consumer Group:** Ensures each message is processed by only one consumer in the group.  
- **Broker:** Manages partitions, handles read and write requests.  
- **Cluster:** Consists of multiple brokers.  
- **Replica:** Copy of a partition for fault tolerance; includes leader and follower replicas.  
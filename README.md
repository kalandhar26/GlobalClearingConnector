# GC2: Global Clearing Connector

# Overview
This project involves developing a payment processing system called Global Clearing Connector (GC2) that interfaces with
clearing bodies across multiple countries. The goal is to facilitate the transfer of transaction data between banks and
national or international clearing bodies, ensuring that transactions are properly processed, validated, and formatted
according to the requirements of each country's clearing system.

# Payment Domain
A payment domain encompasses all activities involving the transfer of funds between parties. In our digital world,
transactions occur through various modes such as IMPS, NEFT, UPI, and card payments.

# What is a Transaction?
A transaction is the process where one party sends money to another party. It can occur within the same bank or between
different banks. For example:

Scenario 1: Ramesh (SBI) sends ₹10,000 to Suresh (HDFC). SBI confirms the transfer, and HDFC confirms the receipt.
Scenario 2: Ramesh (SBI) sends ₹5,000 to Rupesh (SBI). SBI confirms the transfer within the same bank.

# Role of RBI
The Reserve Bank of India (RBI) acts as the guarantor and clearing body for transactions across multiple bank accounts
in India. RBI ensures that transactions are properly cleared and settled between banks.

# Project Scope
GC2 aims to connect to clearing bodies in various countries, not directly to individual banks. This involves sending
transaction details (e.g., where the money should be deposited, bank name, account details) to these clearing bodies,
which have their own formats and standards.

# Example Process

1.Initiating a Payment:

Ramesh transfers an amount from SBI to Suresh in HDFC.
A channel must facilitate HDFC receiving the amount.

2. Validation by Receiving Bank (HDFC):
   Eligibility of the sender.
   Availability of funds.
   Domestic or international transaction checks.
   Compliance with sanctions, taxes, and additional charges.

3. Transaction Processing:

HDFC validates the transaction and confirms whether it has debited or credited the amount.
GC2 asks HDFC to confirm the completion of the process.
HDFC provides transaction data in a specified format (e.g., JSON).

4. Data Conversion and Transmission:

GC2 converts the transaction data into the required format for the clearing body (e.g., RBI).
GC2 sends the formatted data to the clearing body.
GC2 also converts and sends data from the clearing body back to the bank as needed.

# Technical Stack
Java
Spring Boot
Oracle Database
Kafka
Cucumber for Test Cases
Spring Event Handling
High Asynchronous Handling (JMS)
Jenkins (Jules)
Kubernetes for Deployment
Channel-Specific Development
For each payment channel (e.g., NEFT, IMPS in India), new code and entries will be created. Existing code will be
leveraged and tested for compatibility with the requirements of each country's clearing system. Any gaps identified will
be addressed.

## Clearing Bodies and Channels by Country

# China:

CNAPS (China National Advanced Payment System)
CIPS (Cross-Border Interbank Payment System)
Internet Banking Payment System (IBPS)

# Malaysia:

RENTAS (Real-time Electronic Transfer of Funds and Securities System)
FPX (Financial Process Exchange)
IBG (Interbank GIRO)

# Australia:

RITS (Reserve Bank Information and Transfer System)
BECS (Bulk Electronic Clearing System)
NPP (New Payments Platform)

# Governing Bodies
India: Reserve Bank of India (RBI)
China: People's Bank of China (PBOC)
Malaysia: Bank Negara Malaysia (BNM)
Australia: Reserve Bank of Australia (RBA)

# Summary
GC2 is designed to facilitate seamless international and domestic transactions by interfacing with various clearing
bodies, converting transaction data into the required formats, and ensuring compliance with all regulatory standards.
This project will involve a robust technical stack and a focus on channel-specific development to cater to the diverse
requirements of different countries' payment systems.

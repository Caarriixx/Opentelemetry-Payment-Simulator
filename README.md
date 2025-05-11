Asynchronous Payment Simulation

This project is an Asynchronous Payment Simulator designed to handle large-scale payment processing efficiently. The architecture leverages modern web technologies for real-time communication and distributed messaging, ensuring high scalability and resilience. The system is built with:

Angular for the frontend interface, allowing users to interact with the payment simulator.
Spring Boot for backend services, responsible for managing the payment logic and handling asynchronous processes.
WebSockets for real-time communication between the frontend and backend.
RabbitMQ for asynchronous messaging, ensuring payment operations are processed efficiently in the background.
MySQL for relational data storage, maintaining payment transactions and user data.
Elasticsearch for fast search and data indexing.

Technologies:
Frontend: Angular
Backend: Spring Boot (Java)
Messaging Queue: RabbitMQ
Database: MySQL
Search Engine: Elasticsearch
Real-Time Communication: WebSockets

Angular Frontend:
The user interacts with the system through the Angular interface. Payments and statuses are displayed in real time.
Angular communicates with Spring Boot through WebSockets, allowing instant updates on payment statuses.

Spring Boot Backend:
The backend service manages the business logic, handling payment requests from the frontend via WebSockets.
It communicates with RabbitMQ to asynchronously process payments.
It stores and retrieves transactional data from MySQL, and indexes/searches through Elasticsearch for fast data retrieval.

RabbitMQ:
RabbitMQ handles the queuing of payment operations, ensuring they are processed asynchronously without blocking the system.
This allows the system to handle multiple payment requests simultaneously, improving throughput.

MySQL & Elasticsearch:
MySQL is used for persisting payment transactions, ensuring data integrity and consistency.
Elasticsearch is employed to index payments and provide efficient search capabilities across large datasets.

Features:
Asynchronous Payment Processing: Payments are processed in the background, ensuring the system remains responsive during high-volume transactions.
Real-Time Updates: WebSockets provide immediate feedback to the user on the status of their payments.
Scalable Architecture: With RabbitMQ handling asynchronous messaging, the system can easily scale to manage large numbers of concurrent payment requests.
Search and Indexing: Elasticsearch allows for fast retrieval of transaction data, improving the user experience when querying payment histories.

# Asynchronous Payment Simulator with OpenTelemetry Observability

This project is a robust, real-time **Asynchronous Payment Simulator** enhanced with full-stack **observability and tracing** through [OpenTelemetry](https://opentelemetry.io/). It simulates high-volume, asynchronous financial transactions with end-to-end monitoring, real-time updates, and complete traceability. Built as a distributed system using modern web technologies and observability tools.

## 🧱 Architecture Overview

The system is composed of multiple coordinated components:

- **Frontend:** Angular
- **Backend:** Spring Boot (two independent services)
  - `springboot1`: Manages user logic, WebSockets, and payment storage
  - `springboot2`: Acts as the Payment Authorizer (Centro Autorizador)
- **Asynchronous Messaging:** RabbitMQ
- **Database:** MySQL
- **Search & Logging:** Elasticsearch + Kibana
- **Observability:** OpenTelemetry Collector + Prometheus + Grafana + Elastic APM
- **Real-Time Communication:** WebSockets (STOMP over SockJS)

## 🚀 Key Functionalities

### ✅ Payment Flow

- Users authenticate via a real-time WebSocket login screen.
- From the dashboard, they can:
  - View balance and transaction history
  - Send payments to other users
  - Deposit funds
- Payments are sent from `springboot1` to `springboot2` (Centro Autorizador) via RabbitMQ.
- The Centro Autorizador validates and updates the state (`Accepted` / `Rejected`).
- The frontend is instantly notified via WebSockets.

### 📡 Observability & OpenTelemetry

The entire application is instrumented with OpenTelemetry, enabling full observability across services:

- **Traces:** Track each request across frontend, backend, message queues, and database
- **Metrics:** Monitor latency, request volume, RabbitMQ usage, DB performance
- **Logs:** Indexed in Elasticsearch for filtering and debugging
- **Dashboards:**
  - Grafana for metrics and alerts (e.g., payments/minute, latency, CPU usage)
  - Kibana for trace exploration and error analysis
  - Elastic APM for unified view of service performance

All tracing context is propagated across services using the W3C `traceparent` standard.

## 📦 Technologies Used

| Layer         | Technology                  |
|---------------|------------------------------|
| Frontend      | Angular                     |
| Backend       | Spring Boot (Java)          |
| Messaging     | RabbitMQ                    |
| Database      | MySQL                       |
| Observability | OpenTelemetry, Prometheus   |
| Visualization | Grafana, Kibana, Elastic APM|
| Search        | Elasticsearch               |
| Realtime      | WebSockets (STOMP)          |
| Deployment    | Docker & Docker Compose     |

## 📊 Observability Dashboards

Grafana dashboards include:

- 📈 Payment throughput
- 🚦 Request latency by endpoint
- 🐰 RabbitMQ queue depth and retry counts
- 💾 MySQL connection performance
- 💻 CPU and memory usage per container

Kibana provides:

- 🔍 Full trace navigation for each payment
- 🧠 Error logs by component
- 📅 Temporal analysis: busiest hours, failed operations, latency spikes

## 💡 Why OpenTelemetry?

- Vendor-neutral observability (you can switch from Elastic APM to Jaeger or Zipkin easily)
- Unifies tracing, metrics, and logs under a single standard
- Enables root-cause analysis and proactive performance tuning

## 🧪 Load Testing & Traffic Simulation

A custom Postman collection is provided to simulate real traffic:

- 100 valid/invalid logins
- 100+ payments (accepted/rejected)
- Simulated failures, retries, delays

This traffic is visualized in real time in Grafana/Kibana and used for stress testing the observability stack.

## 🛡️ Security & Best Practices

- Passwords encrypted with BCrypt
- WebSocket authentication scoped per user
- Observability data excludes sensitive information (PII masking)
- Secure configuration for dashboards (Grafana/Kibana)

---

## 📝 License

MIT © 2025 Adrián Carral Martínez


Features:
Asynchronous Payment Processing: Payments are processed in the background, ensuring the system remains responsive during high-volume transactions.
Real-Time Updates: WebSockets provide immediate feedback to the user on the status of their payments.
Scalable Architecture: With RabbitMQ handling asynchronous messaging, the system can easily scale to manage large numbers of concurrent payment requests.
Search and Indexing: Elasticsearch allows for fast retrieval of transaction data, improving the user experience when querying payment histories.

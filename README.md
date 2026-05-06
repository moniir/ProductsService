# Products Microservice

A Spring Boot microservice that produces product events to Apache Kafka. This service exposes REST endpoints to create products and publishes them asynchronously to Kafka topics.

## Prerequisites

- Java 21
- Apache Kafka cluster running on:
  - `localhost:9092`
  - `localhost:9094`
  - `localhost:9096`
- Maven 3.6+

## Technology Stack

- **Spring Boot**: 4.0.6
- **Spring Kafka**: 4.0.5
- **Java**: 21
- **Jackson**: For JSON serialization

## Configuration

The application runs on port `53060` by default.

### Kafka Producer Configuration

The producer is configured with the following settings in `application.properties`:

- **Serialization**: JacksonJsonSerializer for JSON message serialization
- **Acknowledgment**: `acks=all` for maximum durability
- **Idempotence**: Enabled to prevent duplicate messages
- **Retries**: 10 retries with delivery timeout of 120 seconds
- **In-flight requests**: Limited to 5 per connection for idempotent delivery

## Building the Project

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/ProductsMicroservice-0.0.1-SNAPSHOT.jar
```

## API Endpoints

Base URL: `http://localhost:53060/products`

### Create Product (Async)

Creates a product and sends it to Kafka asynchronously without waiting for broker acknowledgment.

**Endpoint**: `POST /products/create-product-async`

**Request Body**:
```json
{
  "title": "Product Name",
  "price": 99.99,
  "quantity": 10
}
```

**Response**: `201 CREATED` with product ID

### Create Product (Sync)

Creates a product and sends it to Kafka synchronously, waiting for broker acknowledgment.

**Endpoint**: `POST /products/create-product-sync`

**Request Body**:
```json
{
  "title": "Product Name",
  "price": 99.99,
  "quantity": 10
}
```

**Response**: `201 CREATED` with product ID

## Example Usage

```bash
curl -X POST http://localhost:53060/products/create-product-async \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Laptop",
    "price": 1299.99,
    "quantity": 5
  }'
```

## Error Handling

The service returns error responses in the following format:

```json
{
  "timestamp": "2026-05-06T10:30:00.000+00:00",
  "message": "Error description",
  "path": "/products"
}
```

## Kafka Producer Features

- **Idempotent Producer**: Prevents duplicate messages even on retries
- **High Durability**: `acks=all` ensures all replicas acknowledge the message
- **Automatic Retries**: Up to 10 retries with configurable timeout
- **JSON Serialization**: Automatic conversion of Java objects to JSON using Jackson

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/mhb/ProductsMicroservice/
│   │       ├── ProductsMicroserviceApplication.java
│   │       ├── KafkaConfig.java
│   │       ├── controller/
│   │       │   └── ProductController.java
│   │       ├── model/
│   │       │   ├── CreateProductRestModel.java
│   │       │   └── ErrorMessage.java
│   │       └── services/
│   │           ├── ProductService.java
│   │           └── ProductServiceImpl.java
│   └── resources/
│       └── application.properties
```

## License

This is a sample project for demonstration purposes.

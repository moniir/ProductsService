package com.example.mhb.ProductsMicroservice.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.example.mhb.ProductsMicroservice.model.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {
	
	KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
	private final Logger LOGGER  = LoggerFactory.getLogger(this.getClass());
	
	public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public String createProductAsync(CreateProductRestModel productRestModel) throws Exception {
		String productId = UUID.randomUUID().toString();
		// TODO: Persist Product Details into database table before publishing an Event
		ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, productRestModel.getTitle(),
				productRestModel.getPrice(), productRestModel.getQuantity());
		LOGGER.info("Before publishing a ProductCreatedEvent");
		CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
				kafkaTemplate.send("product-created-event-topic", productId, productCreatedEvent);
		future.whenComplete((result, exception) -> {
			if (exception != null) {
				LOGGER.error("******Failed to send messages*******" + exception.getMessage());
			} else {
				LOGGER.info("******Message send successfully*******" + result.getRecordMetadata());
			}
		});
		LOGGER.info("******Returning product Id*******");
		return productId;
	}
	@Override
	public String createProductSync(CreateProductRestModel productRestModel) throws Exception {
		String productId = UUID.randomUUID().toString();
		// TODO: Persist Product Details into database table before publishing an Event
		ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, productRestModel.getTitle(),
				productRestModel.getPrice(), productRestModel.getQuantity());
		LOGGER.info("Before publishing a ProductCreatedEvent");
		SendResult<String, ProductCreatedEvent> result =
				kafkaTemplate.send("product-created-event-topic", productId, productCreatedEvent).get();
		LOGGER.info("Partition: "+result.getRecordMetadata().partition());
		LOGGER.info("Topic: "+result.getRecordMetadata().topic());
		LOGGER.info("Offset: "+result.getRecordMetadata().offset());
		LOGGER.info("******Returning product Id*******");
		return productId;
	}
}

package com.example.mhb.ProductsMicroservice.services;


import com.example.mhb.ProductsMicroservice.model.CreateProductRestModel;

public interface ProductService {
	
	String createProductAsync(CreateProductRestModel productRestModel) throws Exception ;
	String createProductSync(CreateProductRestModel productRestModel) throws Exception ;

}

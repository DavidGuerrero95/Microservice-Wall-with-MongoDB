package com.appcity.app.muro.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.appcity.app.muro.models.Muro;

public interface MuroRepository extends MongoRepository<Muro, String>{

	@RestResource(path = "buscar")
	public Muro findByCodigoMuro(@Param("name") Integer nombre);
	
}

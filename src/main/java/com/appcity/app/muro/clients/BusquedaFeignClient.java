package com.appcity.app.muro.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "app-busqueda")
public interface BusquedaFeignClient {

	@PutMapping("/busqueda/editarMuro")
	public void editarMuro(@RequestParam Integer nombre);

	@PutMapping("/busqueda/eliminarMuro")
	public void eliminarMuro(@RequestParam List<Integer> listaMuro);

}

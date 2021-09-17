package com.appcity.app.muro.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "app-parametrizacion")
public interface ParametrosFeignClient {

	@GetMapping("/parametros/get/labelMuros")
	public List<Integer> getLabelMuros();

	@PutMapping("/parametros/editar/labelMurosManejo")
	public Boolean editarLabelMurosManejo();

	@PutMapping("/parametros/editar/labelMuroManejoDel/{number}")
	public void editarLabelMuroManejoDelete(@PathVariable Integer number);
}

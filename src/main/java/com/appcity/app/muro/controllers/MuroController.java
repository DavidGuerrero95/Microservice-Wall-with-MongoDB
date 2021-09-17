package com.appcity.app.muro.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.appcity.app.muro.clients.BusquedaFeignClient;
import com.appcity.app.muro.clients.ParametrosFeignClient;
import com.appcity.app.muro.models.Muro;
import com.appcity.app.muro.models.Proyectos;
import com.appcity.app.muro.repository.MuroRepository;
import com.appcity.app.muro.services.IMuroMath;

@RestController
public class MuroController {

	@Autowired
	MuroRepository murosRepository;

	@Autowired
	ParametrosFeignClient parametros;

	@Autowired
	IMuroMath iMuroMath;
	
	@Autowired
	BusquedaFeignClient busqueda;

	@GetMapping("/muros/listar")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Muro> getMuros() {
		return murosRepository.findAll();
	}

	@GetMapping("/muros/buscar/{codigo}")
	@ResponseStatus(code = HttpStatus.FOUND)
	public Muro getMuroCodigo(@PathVariable Integer codigo) {
		return murosRepository.findByCodigoMuro(codigo);
	}

	@PostMapping("/muros/crear")
	@ResponseStatus(code = HttpStatus.CREATED)
	public String crearMuros(@RequestBody Muro muro) {
		if(muro.getNombre()!=null && muro.getLocalizacion() != null) {
			List<Double> latLon = new ArrayList<Double>();
			BigDecimal bdlat = new BigDecimal(muro.getLocalizacion().get(0)).setScale(5, RoundingMode.HALF_UP);
			BigDecimal bdlon = new BigDecimal(muro.getLocalizacion().get(1)).setScale(5, RoundingMode.HALF_UP);
			latLon.add(bdlat.doubleValue());
			latLon.add(bdlon.doubleValue());
			muro.setLocalizacion(latLon);
			try {
				List<Integer> listaLabelMuro = parametros.getLabelMuros();
				muro.setCodigoMuro(listaLabelMuro.get(listaLabelMuro.size() - 1));
			} catch (Exception e) {
				List<Muro> listaLabel2 = murosRepository.findAll();
				muro.setCodigoMuro(listaLabel2.size() + 1);
			}
			murosRepository.save(muro);
			
			try {
				busqueda.editarMuro(muro.getCodigoMuro());
			} catch (Exception e) {
				System.out.println("Mensaje de eror: " + e.getMessage());
			}
			
			return "Muro: "+muro.getNombre()+" Creado";
		}
		else {
			return "Ingrese todos los datos del muro";
		}
	}
	
	@PostMapping("/muros/crearProyectos")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Integer crearMurosProyectos(@RequestBody Proyectos proyectos) {
		Muro muro = new Muro();
		//muro.setNombre("");
		if (murosRepository.findAll().isEmpty()) {
			try {
				List<Integer> listaLabelMuro = parametros.getLabelMuros();
				muro.setCodigoMuro(listaLabelMuro.get(listaLabelMuro.size() - 1));
			} catch (Exception e) {
				List<Muro> listaLabel2 = murosRepository.findAll();
				muro.setCodigoMuro(listaLabel2.size() + 1);
			}
			try {
				parametros.editarLabelMurosManejo();
			} catch (Exception e) {
				System.out.println("Mensaje de eror: " + e.getMessage());
			}
			muro.setLocalizacion(proyectos.getLocalizacion());
			List<String> listaPrimera = new ArrayList<String>();
			listaPrimera.add(proyectos.getNombre());
			muro.setNombreProyectos(listaPrimera);
			murosRepository.save(muro);
			return muro.getCodigoMuro();
		} else {
			Boolean bandera1 = false;
			for (int i=0; i < murosRepository.findAll().size(); i++) {
				Double distancia = iMuroMath.distanciaCoord(murosRepository.findAll().get(i).getLocalizacion(),
						proyectos.getLocalizacion());
				if (distancia <= 1 && !bandera1) {
					muro = murosRepository.findByCodigoMuro(i + 1);
					List<String> listaProyectos = muro.getNombreProyectos();
					listaProyectos.add(proyectos.getNombre());
					muro.setNombreProyectos(listaProyectos);
					if (listaProyectos.size() <= 4) {
						List<Double> listaNuevaLocalizacion = iMuroMath.distanciaMedia(muro.getLocalizacion(),
								proyectos.getLocalizacion());
						muro.setLocalizacion(listaNuevaLocalizacion);
					}
					murosRepository.save(muro);
					bandera1 = true;
				}
			}if(!bandera1) {
					List<String> lista = new ArrayList<String>();
					try {
						List<Integer> listaLabelMuro = parametros.getLabelMuros();
						muro.setCodigoMuro(listaLabelMuro.get(listaLabelMuro.size() - 1));
					} catch (Exception e) {
						List<Muro> listaLabel2 = murosRepository.findAll();
						muro.setCodigoMuro(listaLabel2.size() + 1);
					}
					try {
						parametros.editarLabelMurosManejo();
					} catch (Exception e) {
						System.out.println("Mensaje de eror: " + e.getMessage());
					}
					muro.setLocalizacion(proyectos.getLocalizacion());
					lista.add(proyectos.getNombre());
					muro.setNombreProyectos(lista);
					murosRepository.save(muro);
				}
			return muro.getCodigoMuro();
		}
	}
	
	@PutMapping("/muros/eliminarProyecto/{codigo}")
	@ResponseStatus(code = HttpStatus.OK)
	public void eliminarProyecto(@PathVariable Integer codigo, @RequestParam String nombre) {
		Muro muro = murosRepository.findByCodigoMuro(codigo);
		List<String> listaProyectos = muro.getNombreProyectos();
		listaProyectos.remove(nombre);
		if(listaProyectos.isEmpty()) {
			eliminarMuro(codigo);
		} else {
			muro.setNombreProyectos(listaProyectos);
			murosRepository.save(muro);
		}
	}
	
	@DeleteMapping("/muros/eliminarMuro/{codigo}")
	@ResponseStatus(code = HttpStatus.OK)
	public void eliminarMuro(@PathVariable Integer codigo) {
		Muro muro = murosRepository.findByCodigoMuro(codigo);
		String id = muro.getId();
		murosRepository.deleteById(id);
		List<Muro> murosList = murosRepository.findAll();
		List<Integer> busquedaLista = new ArrayList<Integer>();
		try {
			parametros.editarLabelMuroManejoDelete(murosList.size());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		List<Integer> listaLabel = parametros.getLabelMuros();
		for(int i=0; i<murosList.size(); i++) {
			murosList.get(i).setCodigoMuro(listaLabel.get(i));
			murosRepository.save(murosList.get(i));
			busquedaLista.add(listaLabel.get(i));
		}
		try {
			busqueda.eliminarMuro(busquedaLista);
		} catch (Exception e) {
			System.out.println("Error: busqueda" + e.getMessage());
		}
	}
	
}

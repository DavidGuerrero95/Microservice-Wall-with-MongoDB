package com.appcity.app.muro.models;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "muro")
public class Muro {

	@Id
	private String id;

	@Indexed(unique = true)
	private Integer codigoMuro;
	
	private String nombre;

	@NotBlank(message = "ubicacion cannot be null")
	private List<Double> localizacion;

	private List<String> nombreProyectos;
	
	public Muro() {
	}

	public Muro(Integer codigoMuro, String nombre, List<Double> localizacion, List<String> nombreProyectos) {
		super();
		this.codigoMuro = codigoMuro;
		this.nombre = nombre;
		this.localizacion = localizacion;
		this.nombreProyectos = nombreProyectos;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCodigoMuro() {
		return codigoMuro;
	}

	public void setCodigoMuro(Integer codigoMuro) {
		this.codigoMuro = codigoMuro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Double> getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(List<Double> localizacion) {
		this.localizacion = localizacion;
	}

	public List<String> getNombreProyectos() {
		return nombreProyectos;
	}

	public void setNombreProyectos(List<String> nombreProyectos) {
		this.nombreProyectos = nombreProyectos;
	}

	
	
}

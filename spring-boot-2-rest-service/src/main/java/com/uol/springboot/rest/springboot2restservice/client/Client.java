package com.uol.springboot.rest.springboot2restservice.client;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.uol.springboot.rest.springboot2restservice.pojo.Weather;

@Entity
public class Client {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	private Integer idade;
	
	@Transient
	private Weather temperatura;

	public Client() {
		super();
	}

	public Client(Long id, String nome, Integer idade) {
		super();
		this.id = id;
		this.nome = nome;
		this.idade = idade;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Integer getIdade() {
		return idade;
	}
	
	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Weather getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Weather temperatura) {
		this.temperatura = temperatura;
	}

}

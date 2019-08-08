package com.uol.springboot.rest.springboot2restservice.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.uol.springboot.rest.springboot2restservice.pojo.ClimateLocation;
import com.uol.springboot.rest.springboot2restservice.pojo.Distances;
import com.uol.springboot.rest.springboot2restservice.pojo.GeoLocation;

@Service
public class ClientService {
	
	private static final String geoLocationURI = "https://ipvigilante.com/";
	private static final String climateURI = "https://www.metaweather.com/api/location/search/?lattlong=";
	private static final String woeidClimateURI = "https://www.metaweather.com/api/location/";
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ClientRepository clientRepository;
	
	public List<Client> retrieveAllClients() {
		return clientRepository.findAll();
	}
	
	public Client retrieveClient(@PathVariable long id) {
		Optional<Client> client = clientRepository.findById(id);
		
		if (!client.isPresent()) throw new IllegalArgumentException("id - " + id);
		
		return client.get();
	}
	
	public void deleteClient(@PathVariable long id) {
		if (clientRepository.findById(id).isPresent()) clientRepository.deleteById(id);		
	}
	
	public ResponseEntity<Object> createClient(@RequestBody Client client, HttpServletRequest request) throws URISyntaxException {
		// chamada API de geolocalização por IP utilizando o IP do solicitante
		URI geoLocationUri = new URI(geoLocationURI + request.getRemoteAddr());
		GeoLocation geoLocation = restTemplate.getForObject(geoLocationUri, GeoLocation.class);

		// primeira chamada da API de clima por geolocalização: interessa receber a lista com as distâncias
		URI climateLocationURI = new URI(climateURI + geoLocation.getData().getLatitude() + "," + geoLocation.getData().getLongitude());
		ResponseEntity<List<Distances>> response = restTemplate.exchange(climateLocationURI, HttpMethod.GET, null, new ParameterizedTypeReference<List<Distances>>(){});
		
		// observei que a API traz sempre a distância mais curta primeiro, não havendo necessidade de procurá-la na lista.
		List<Distances> distancesList = response.getBody();
		
		// segunda chamada da API de clima por geolocalização: pelo woeid, salvamos uma lista com as temperaturas
		URI woeidClimateLocationUri = new URI(woeidClimateURI + distancesList.get(0).getWoeid());
		ClimateLocation climateLocation = restTemplate.getForObject(woeidClimateLocationUri, ClimateLocation.class);
		
		// aqui peguei a primeira previsão de temperatura da lista e setei no cliente para fins estatísticos como o desafio propõe
		client.setTemperatura(climateLocation.getConsolidated_weather().get(0));
		
		Client savedClient = clientRepository.save(client);
		System.out.println("PUT do cliente: " + client.getNome() + " realizado com sucesso.");
		System.out.println("Informações salvas:");
		System.out.println("Temperatura min: " + client.getTemperatura().getMinTemp());
		System.out.println("Temperatura max: " + client.getTemperatura().getMaxTemp());
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedClient.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	public ResponseEntity<Object> updateClient(@RequestBody Client client, @PathVariable long id) {
		Optional<Client> clientOptional = clientRepository.findById(id);
		
		if (!clientOptional.isPresent()) return ResponseEntity.notFound().build();
		
		client.setId(id);
		
		clientRepository.save(client);
		
		return ResponseEntity.noContent().build();
	}
	
}

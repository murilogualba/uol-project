package com.uol.springboot.rest.springboot2restservice.client;

import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientResource {
	
	@Autowired
	private ClientService clientService;
	
	@GetMapping("/clients")
	public List<Client> retrieveAllClients() {
		return clientService.retrieveAllClients();
	}
	
	@GetMapping("/clients/{id}")
	public Client retrieveClient(@PathVariable long id) {
		return clientService.retrieveClient(id);
	}
	
	@DeleteMapping("/clients/{id}")
	public void deleteClient(@PathVariable long id) {
		clientService.deleteClient(id);		
	}
	
	@PostMapping("/clients")
	public ResponseEntity<Object> createClient(@RequestBody Client client, HttpServletRequest request) throws URISyntaxException {
		return clientService.createClient(client, request);
	}
	
	@PutMapping("/clients/{id}")
	public ResponseEntity<Object> updateClient(@RequestBody Client client, @PathVariable long id) {
		return clientService.updateClient(client, id);
	}

}

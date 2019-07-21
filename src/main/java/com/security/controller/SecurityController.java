package com.security.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@GetMapping("/clientes")
	public String findAll() {
		return "Listando clientes...";
	}
	
	@DeleteMapping("/clientes/{id}")
	public String delete(@PathVariable String id) {
		return "Cliente com id" + id +  "deletado..";
	}
	
}

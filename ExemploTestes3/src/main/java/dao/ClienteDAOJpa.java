package dao;

import domain.ClienteJPA;
import generics.jpa.GenericDAOJpa;

public class ClienteDAOJpa extends GenericDAOJpa<ClienteJPA, Long> implements IClienteDAOJpa<ClienteJPA> {
	
	public ClienteDAOJpa() {
		super(ClienteJPA.class, "Postgre1");
	}
}
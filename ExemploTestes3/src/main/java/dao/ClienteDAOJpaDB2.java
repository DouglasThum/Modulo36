package dao;

import domain.ClienteJPA;
import generics.jpa.GenericDAOJpa;

public class ClienteDAOJpaDB2 extends GenericDAOJpa<ClienteJPA, Long> implements IClienteDAOJpa<ClienteJPA> {

	public ClienteDAOJpaDB2() {
		super(ClienteJPA.class, "Postgre2");
	}
}
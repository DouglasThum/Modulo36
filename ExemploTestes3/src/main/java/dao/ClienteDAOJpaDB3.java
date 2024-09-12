package dao;

import generics.jpa.GenericDAOJpa;
import domain.ClienteJPA2;

public class ClienteDAOJpaDB3 extends GenericDAOJpa<ClienteJPA2, Long> implements IClienteDAOJpa<ClienteJPA2> {

	public ClienteDAOJpaDB3() {
		super(ClienteJPA2.class, "Mysql1");
	}
}
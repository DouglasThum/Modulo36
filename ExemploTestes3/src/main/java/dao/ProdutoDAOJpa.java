package dao;

import domain.ProdutoJPA;
import generics.jpa.GenericDAOJpa;

public class ProdutoDAOJpa extends GenericDAOJpa<ProdutoJPA, Long> implements IProdutoDAOJpa{
	
	public ProdutoDAOJpa() {
		super(ProdutoJPA.class, "Postgre1");
	}
}

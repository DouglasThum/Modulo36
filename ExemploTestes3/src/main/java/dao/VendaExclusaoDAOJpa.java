package dao;

import domain.VendaJPA;
import exception.DAOException;
import exception.TipoChaveNaoEncontradoException;
import generics.jpa.GenericDAOJpa;

public class VendaExclusaoDAOJpa extends GenericDAOJpa<VendaJPA, Long> implements IVendaDAOJpa {
	
	public VendaExclusaoDAOJpa() {
		super(VendaJPA.class, "Postgre1");
	}

	@Override
	public void finalizarVenda(VendaJPA venda) throws TipoChaveNaoEncontradoException, DAOException {
		throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");	
	}

	@Override
	public void cancelarVenda(VendaJPA venda) throws TipoChaveNaoEncontradoException, DAOException {
		throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");		
	}

	@Override
	public VendaJPA consultarComCollection(Long id) {
		throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
	}

}

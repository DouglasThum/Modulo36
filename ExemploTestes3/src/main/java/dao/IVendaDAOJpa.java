package dao;

import domain.VendaJPA;
import exception.DAOException;
import exception.TipoChaveNaoEncontradoException;
import generics.jpa.IGenericDAOJpa;

public interface IVendaDAOJpa extends IGenericDAOJpa<VendaJPA, Long	> {
	
	public void finalizarVenda(VendaJPA venda) throws TipoChaveNaoEncontradoException, DAOException;
	
	public void cancelarVenda(VendaJPA venda) throws TipoChaveNaoEncontradoException, DAOException;
	
	public VendaJPA consultarComCollection(Long id);

}

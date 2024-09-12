package dao;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import domain.ClienteJPA;
import domain.ProdutoJPA;
import domain.VendaJPA;
import exception.DAOException;
import exception.TipoChaveNaoEncontradoException;
import generics.jpa.GenericDAOJpa;

public class VendaDAOJpa extends GenericDAOJpa<VendaJPA, Long> implements IVendaDAOJpa {

	public VendaDAOJpa() {
		super(VendaJPA.class, "Postgre1");
	}
	
	@Override
	public void finalizarVenda(VendaJPA venda) throws TipoChaveNaoEncontradoException, DAOException {
		super.alterar(venda);
	}
	
	@Override
	public void cancelarVenda(VendaJPA venda) throws TipoChaveNaoEncontradoException, DAOException {
		super.alterar(venda);
	}
	
	@Override
	public void excluir(VendaJPA entity) throws DAOException {
		throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA!");
	}
	
	@Override
	public VendaJPA cadastrar(VendaJPA entity) throws TipoChaveNaoEncontradoException, DAOException {
		try {
			openConnection();
			entity.getProdutos().forEach(prod -> {
			ProdutoJPA prodJpa = entityManager.merge(prod.getProduto());
			prod.setProduto(prodJpa);
			});
			ClienteJPA cliente = entityManager.merge(entity.getCliente());
			entity.setCliente(cliente);
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
			closeConnection();
			return entity;
		} catch (Exception e) {
			throw new DAOException("ERRO SALVANDO VENDA ", e);
		}
	}
	
	@Override
	public VendaJPA consultarComCollection(Long id) {
		openConnection();
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<VendaJPA> query = builder.createQuery(VendaJPA.class);
		Root<VendaJPA> root = query.from(VendaJPA.class);
		root.fetch("cliente");
		root.fetch("produtos");
		query.select(root).where(builder.equal(root.get("id"), id));
		TypedQuery<VendaJPA> tpQuery = entityManager.createQuery(query);
		VendaJPA venda = tpQuery.getSingleResult();
		closeConnection();
		return venda;
	}
	
}

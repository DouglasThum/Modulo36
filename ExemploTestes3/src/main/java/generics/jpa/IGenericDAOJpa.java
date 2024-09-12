package generics.jpa;

import java.io.Serializable;
import java.util.Collection;

import dao.Persistente;
import exception.DAOException;
import exception.MaisDeUmRegistroException;
import exception.TableException;
import exception.TipoChaveNaoEncontradoException;

public interface IGenericDAOJpa <T extends Persistente, E extends Serializable> {

	public T cadastrar(T entity) throws TipoChaveNaoEncontradoException, DAOException;
	
	public void excluir(T entity) throws DAOException;
	
	public T alterar(T entity) throws TipoChaveNaoEncontradoException, DAOException;
	
	public T consultar(E id) throws MaisDeUmRegistroException, TableException, DAOException;
	
	public Collection<T> buscarTodos() throws DAOException;
	
}

package JPA;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Random;

import org.junit.After;
import org.junit.Test;

import dao.ClienteDAOJpa;
import dao.IClienteDAOJpa;
import domain.ClienteJPA;
import exception.DAOException;
import exception.MaisDeUmRegistroException;
import exception.TableException;
import exception.TipoChaveNaoEncontradoException;

public class ClienteDAOJpaTest {
	
	private IClienteDAOJpa<ClienteJPA> clienteDao;
	
	private Random rd;
	
	public ClienteDAOJpaTest() {
		this.clienteDao = new ClienteDAOJpa();
		this.rd = new Random();
	}
	
	@After
	public void end() throws DAOException {
		Collection<ClienteJPA> list = clienteDao.buscarTodos();
		list.forEach(cliente -> {
			try {
				clienteDao.excluir(cliente);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
	}
	
	@Test
	public void pesquisarCliente() throws TipoChaveNaoEncontradoException, DAOException, MaisDeUmRegistroException, TableException {
		ClienteJPA cliente = instanciarCliente();
		clienteDao.cadastrar(cliente);
		
		ClienteJPA clienteConsultado = clienteDao.consultar(cliente.getId());
		assertNotNull(clienteConsultado);		
	}
	
	@Test
	public void salvarCliente() throws TipoChaveNaoEncontradoException, DAOException, MaisDeUmRegistroException, TableException {
		ClienteJPA cliente = instanciarCliente();
		ClienteJPA retorno = clienteDao.cadastrar(cliente);
		assertNotNull(retorno);
		
		ClienteJPA clienteConsultado = clienteDao.consultar(retorno.getId());
		assertEquals(clienteConsultado.getCpf(), retorno.getCpf());
		assertNotNull(clienteConsultado);
		
		clienteDao.excluir(cliente);
		ClienteJPA clienteConsultado2 = clienteDao.consultar(retorno.getId());
		assertNull(clienteConsultado2);
	}
	
	@Test
	public void excluirCliente() throws TipoChaveNaoEncontradoException, DAOException, MaisDeUmRegistroException, TableException {
		ClienteJPA cliente = instanciarCliente();
		ClienteJPA retorno = clienteDao.cadastrar(cliente);
		assertNotNull(retorno);
		
		ClienteJPA clienteConsultado = clienteDao.consultar(retorno.getId());
		assertEquals(clienteConsultado.getCpf(), retorno.getCpf());
		assertNotNull(clienteConsultado);
		
		clienteDao.excluir(cliente);
		ClienteJPA clienteConsultado2 = clienteDao.consultar(retorno.getId());
		assertNull(clienteConsultado2);
	}
	
	@Test
	public void alterarCliente() throws TipoChaveNaoEncontradoException, DAOException, MaisDeUmRegistroException, TableException {
		ClienteJPA cliente = instanciarCliente();
		ClienteJPA retorno = clienteDao.cadastrar(cliente);
		assertNotNull(retorno);
		
		ClienteJPA clienteConsultado = clienteDao.consultar(retorno.getId());
		assertEquals(clienteConsultado.getCpf(), retorno.getCpf());
		assertNotNull(clienteConsultado);
		
		clienteConsultado.setNome("Douglas Oliveira");
		clienteDao.alterar(clienteConsultado);
		
		ClienteJPA clienteAlterado = clienteDao.consultar(clienteConsultado.getId());
		assertNotNull(clienteAlterado);
		assertEquals(clienteAlterado.getNome(), "Douglas Oliveira");
		assertNotEquals(cliente.getNome(), clienteAlterado.getNome());
		
		clienteDao.excluir(cliente);
		ClienteJPA clienteConsultado2 = clienteDao.consultar(retorno.getId());
		assertNull(clienteConsultado2);
	}
	
	@Test
	public void buscarTodos() throws TipoChaveNaoEncontradoException, DAOException {
		ClienteJPA cliente1 = instanciarCliente();
		ClienteJPA cliente2 = instanciarCliente();
		
		ClienteJPA retorno1 = clienteDao.cadastrar(cliente1);
		ClienteJPA retorno2 = clienteDao.cadastrar(cliente2);

		assertNotNull(retorno1);
		assertNotNull(retorno2);
		
		Collection<ClienteJPA> list = clienteDao.buscarTodos();
		assertTrue(list != null);
		assertTrue(list.size() == 2);
		
		list.forEach(cliente -> {
			try {
				clienteDao.excluir(cliente);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
		
		Collection<ClienteJPA> list2 = clienteDao.buscarTodos();
		assertTrue(list2 != null);
		assertTrue(list2.size() == 0);
	}

	private ClienteJPA instanciarCliente() {
		ClienteJPA cliente = new ClienteJPA();
		
		cliente.setNome("Douglas");
		cliente.setCpf(rd.nextLong());
		cliente.setTel(51987654321L);
		cliente.setEnd("Ipiranga");
		cliente.setNum(1000L);
		cliente.setCidade("Porto Alegre");
		cliente.setEstado("Rio Grande do Sul");
		return cliente;
	}
}

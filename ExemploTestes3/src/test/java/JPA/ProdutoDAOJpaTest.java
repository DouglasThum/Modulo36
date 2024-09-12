package JPA;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;

import dao.IProdutoDAOJpa;
import dao.ProdutoDAOJpa;
import domain.ProdutoJPA;
import exception.DAOException;
import exception.MaisDeUmRegistroException;
import exception.TableException;
import exception.TipoChaveNaoEncontradoException;

public class ProdutoDAOJpaTest {
	
	private IProdutoDAOJpa produtoDao;

	public ProdutoDAOJpaTest() {
		this.produtoDao = new ProdutoDAOJpa();
	}
	
	@After
	public void end() throws DAOException {
		Collection<ProdutoJPA> list = produtoDao.buscarTodos();
		list.forEach(produto -> {
			try {
				produtoDao.excluir(produto);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
	}
	
	@Test
	public void pesquisarProduto() throws TipoChaveNaoEncontradoException, DAOException, MaisDeUmRegistroException, TableException {
		ProdutoJPA produto = instanciarProduto("2807");
		produtoDao.cadastrar(produto);
		
		ProdutoJPA produtoConsultado = produtoDao.consultar(produto.getId());
		assertNotNull(produtoConsultado);		
	}
	
	@Test
	public void salvarProduto() throws TipoChaveNaoEncontradoException, DAOException, MaisDeUmRegistroException, TableException {
		ProdutoJPA produto = instanciarProduto("2807");
		ProdutoJPA retorno = produtoDao.cadastrar(produto);
		assertNotNull(retorno);
		
		ProdutoJPA produtoConsultado = produtoDao.consultar(retorno.getId());
		assertEquals(produtoConsultado.getCodigo(), retorno.getCodigo());
		assertNotNull(produtoConsultado);
		
		produtoDao.excluir(produto);
		ProdutoJPA produtoConsultado2 = produtoDao.consultar(retorno.getId());
		assertNull(produtoConsultado2);
	}
	
	@Test
	public void excluirProduto() throws TipoChaveNaoEncontradoException, DAOException, MaisDeUmRegistroException, TableException {
		ProdutoJPA produto = instanciarProduto("2807");
		ProdutoJPA retorno = produtoDao.cadastrar(produto);
		assertNotNull(retorno);
		
		ProdutoJPA produtoConsultado = produtoDao.consultar(retorno.getId());
		assertEquals(produtoConsultado.getCodigo(), retorno.getCodigo());
		assertNotNull(produtoConsultado);
		
		produtoDao.excluir(produto);
		ProdutoJPA produtoConsultado2 = produtoDao.consultar(retorno.getId());
		assertNull(produtoConsultado2);
	}
	
	@Test
	public void alterarProduto() throws TipoChaveNaoEncontradoException, DAOException, MaisDeUmRegistroException, TableException {
		ProdutoJPA produto = instanciarProduto("2807");
		ProdutoJPA retorno = produtoDao.cadastrar(produto);
		assertNotNull(retorno);
		
		ProdutoJPA produtoConsultado = produtoDao.consultar(retorno.getId());
		assertEquals(produtoConsultado.getCodigo(), retorno.getCodigo());
		assertNotNull(produtoConsultado);
		
		produtoConsultado.setNome("PC Gamer");
		produtoDao.alterar(produtoConsultado);
		
		ProdutoJPA produtoAlterado = produtoDao.consultar(produtoConsultado.getId());
		assertNotNull(produtoAlterado);
		assertEquals(produtoAlterado.getNome(), "PC Gamer");
		assertNotEquals(produto.getNome(), produtoAlterado.getNome());
		
		produtoDao.excluir(produto);
		ProdutoJPA produtoConsultado2 = produtoDao.consultar(retorno.getId());
		assertNull(produtoConsultado2);
	}
	
	@Test
	public void buscarTodos() throws TipoChaveNaoEncontradoException, DAOException {
		ProdutoJPA produto1 = instanciarProduto("2807");
		ProdutoJPA produto2 = instanciarProduto("2800");
		
		ProdutoJPA retorno1 = produtoDao.cadastrar(produto1);
		ProdutoJPA retorno2 = produtoDao.cadastrar(produto2);

		assertNotNull(retorno1);
		assertNotNull(retorno2);
		
		Collection<ProdutoJPA> list = produtoDao.buscarTodos();
		assertTrue(list != null);
		assertTrue(list.size() == 2);
		
		list.forEach(produto -> {
			try {
				produtoDao.excluir(produto);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
		
		Collection<ProdutoJPA> list2 = produtoDao.buscarTodos();
		assertTrue(list2 != null);
		assertTrue(list2.size() == 0);
	}

	private ProdutoJPA instanciarProduto(String codigo) {
		ProdutoJPA produto = new ProdutoJPA();
		
		produto.setNome("Computador");
		produto.setCodigo(codigo);
		produto.setDescricao("Computador gamer");
		produto.setCategoria("Informática");
		produto.setValor(BigDecimal.valueOf(4500));
		return produto;
	}
}

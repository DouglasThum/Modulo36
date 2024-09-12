package JPA;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.ClienteDAOJpa;
import dao.IClienteDAOJpa;
import dao.IProdutoDAOJpa;
import dao.IVendaDAOJpa;
import dao.ProdutoDAOJpa;
import dao.VendaDAOJpa;
import dao.VendaExclusaoDAOJpa;
import domain.ClienteJPA;
import domain.ProdutoJPA;
import domain.VendaJPA;
import domain.VendaJPA.Status;
import exception.DAOException;
import exception.MaisDeUmRegistroException;
import exception.TableException;
import exception.TipoChaveNaoEncontradoException;

public class VendaDAOJpaTest {

	private IVendaDAOJpa vendaDao;
	private IVendaDAOJpa vendaExclusão;
	private IClienteDAOJpa<ClienteJPA> clienteDao;
	private IProdutoDAOJpa produtoDao;
	
	private Random rd;
	private ClienteJPA cliente;
	private ProdutoJPA produto;
	
	public VendaDAOJpaTest() {
		this.vendaDao = new VendaDAOJpa();
		this.vendaExclusão = new VendaExclusaoDAOJpa();
		this.clienteDao = new ClienteDAOJpa();
		this.produtoDao = new ProdutoDAOJpa();
		this.rd = new Random();
	}
	
	@Before
	public void init() throws TipoChaveNaoEncontradoException, DAOException {
		this.cliente = cadastrarCliente();
		this.produto = cadastrarProduto("A1", BigDecimal.valueOf(10.0));
	}
	
	@After
	public void end() throws DAOException {
		excluirVendas();
		excluirProdutos();
		clienteDao.excluir(this.cliente);
	}
	
	@Test
	public void pesquisar() throws MaisDeUmRegistroException, TableException, DAOException, TipoChaveNaoEncontradoException {
		VendaJPA venda = criarVenda("A1");
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		
		VendaJPA vendaConsultada = vendaDao.consultar(venda.getId());
		assertNotNull(vendaConsultada);
		assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());	
	}
	
	@Test
	public void salvar() throws TipoChaveNaoEncontradoException, DAOException, MaisDeUmRegistroException, TableException {
		VendaJPA venda = criarVenda("A2");
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		
		BigDecimal valorTotal = BigDecimal.valueOf(20).setScale(1, RoundingMode.HALF_DOWN);
		assertTrue(venda.getValorTotal().equals(valorTotal));
		assertTrue(venda.getStatus().equals(Status.INICIADA));
		
		VendaJPA vendaConsultada = vendaDao.consultar(venda.getId());
		assertNotNull(vendaConsultada.getId() != null);
		assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
	}
	
	@Test
	public void cancelarVenda() throws MaisDeUmRegistroException, TableException, DAOException, TipoChaveNaoEncontradoException {
		String codigoVenda = "A3";
		VendaJPA venda = criarVenda(codigoVenda);
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		assertNotNull(venda);
		assertEquals(codigoVenda, retorno.getCodigo());
		
		retorno.setStatus(Status.CANCELADA);
		vendaDao.cancelarVenda(venda);
		
		VendaJPA vendaConsultada = vendaDao.consultar(retorno.getId());
		assertEquals(codigoVenda, vendaConsultada.getCodigo());
		assertEquals(vendaConsultada.getStatus(), Status.CANCELADA);
	}
	
	@Test
	public void adicionarMaisProdutosDoMesmo() throws TipoChaveNaoEncontradoException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A4";
		VendaJPA venda = criarVenda(codigoVenda);
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		assertNotNull(venda);
		assertEquals(codigoVenda, venda.getCodigo());
		
		VendaJPA vendaConsultada = vendaDao.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(produto, 1);
		
		assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
		assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	} 
	
	@Test
	public void adicionarMaisProdutosDiferentes() throws TipoChaveNaoEncontradoException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A5";
		VendaJPA venda = criarVenda(codigoVenda);
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		assertNotNull(venda);
		assertEquals(codigoVenda, venda.getCodigo());
		
		ProdutoJPA produto = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
		assertNotNull(produto);
		assertEquals(codigoVenda, produto.getCodigo());

		VendaJPA vendaConsultada = vendaDao.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(produto, 1);
		
		assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
		assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	}
	
	@Test(expected = DAOException.class)
	public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontradoException, DAOException {
		VendaJPA venda1 = criarVenda("A6");
		VendaJPA retorno1 = vendaDao.cadastrar(venda1);
		assertNotNull(retorno1);
	
		VendaJPA venda2 = criarVenda("A6");
		VendaJPA retorno2 = vendaDao.cadastrar(venda2);
		assertNull(retorno2);
		assertTrue(venda1.getStatus().equals(Status.INICIADA));
	}
	
	@Test
	public void removerProduto() throws TipoChaveNaoEncontradoException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A7";
		VendaJPA venda = criarVenda(codigoVenda);
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		assertNotNull(venda);
		assertEquals(codigoVenda, venda.getCodigo());
		
		ProdutoJPA prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
		assertNotNull(prod);
		assertEquals(codigoVenda, prod.getCodigo());
		
		VendaJPA vendaConsultada = vendaDao.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(prod, 1);
		assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
		assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		
		
		vendaConsultada.removerProduto(prod, 1);
		assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
		valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
		assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	}
	
	@Test
	public void removerApenasUmProduto() throws TipoChaveNaoEncontradoException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A8";
		VendaJPA venda = criarVenda(codigoVenda);
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		assertNotNull(venda);
		assertEquals(codigoVenda, venda.getCodigo());
		
		ProdutoJPA prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
		assertNotNull(prod);
		assertEquals(codigoVenda, prod.getCodigo());
		
		VendaJPA vendaConsultada = vendaDao.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(prod, 1);
		assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
		assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		
		
		vendaConsultada.removerProduto(prod, 1);
		assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
		valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
		assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	} 
	
	@Test
	public void removerTodosProdutos() throws TipoChaveNaoEncontradoException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A9";
		VendaJPA venda = criarVenda(codigoVenda);
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		assertNotNull(venda);
		assertEquals(codigoVenda, venda.getCodigo());
		
		ProdutoJPA prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
		assertNotNull(prod);
		assertEquals(codigoVenda, prod.getCodigo());
		
		VendaJPA vendaConsultada = vendaDao.consultarComCollection(venda.getId());
		vendaConsultada.adicionarProduto(prod, 1);
		assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
		BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
		assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
		
		
		vendaConsultada.removerTodosProdutos();
		assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 0);
		assertTrue(vendaConsultada.getValorTotal().equals(BigDecimal.valueOf(0)));
		assertTrue(vendaConsultada.getStatus().equals(Status.INICIADA));
	} 
	
	@Test
	public void finalizarVenda() throws TipoChaveNaoEncontradoException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A10";
		VendaJPA venda = criarVenda(codigoVenda);
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		assertNotNull(venda);
		assertEquals(codigoVenda, venda.getCodigo());
		
		venda.setStatus(Status.CONCLUIDA);
		vendaDao.finalizarVenda(venda);
		
		VendaJPA vendaConsultada = vendaDao.consultarComCollection(venda.getId());
		assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
		assertEquals(Status.CONCLUIDA, vendaConsultada.getStatus());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontradoException, MaisDeUmRegistroException, TableException, DAOException {
		String codigoVenda = "A11";
		VendaJPA venda = criarVenda(codigoVenda);
		VendaJPA retorno = vendaDao.cadastrar(venda);
		assertNotNull(retorno);
		assertNotNull(venda);
		assertEquals(codigoVenda, venda.getCodigo());
		
		venda.setStatus(Status.CONCLUIDA);
		vendaDao.finalizarVenda(venda);
		
		VendaJPA vendaConsultada = vendaDao.consultarComCollection(venda.getId());
		assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
		assertEquals(Status.CONCLUIDA, vendaConsultada.getStatus());
		
		vendaConsultada.adicionarProduto(this.produto, 1);
		
	}
	
	private ClienteJPA cadastrarCliente() throws TipoChaveNaoEncontradoException, DAOException {
		ClienteJPA cliente = new ClienteJPA();
		
		cliente.setNome("Douglas");
		cliente.setCpf(rd.nextLong());
		cliente.setTel(51987654321L);
		cliente.setEnd("Ipiranga");
		cliente.setNum(1000L);
		cliente.setCidade("Porto Alegre");
		cliente.setEstado("Rio Grande do Sul");
		return clienteDao.cadastrar(cliente);
	}
	
	private ProdutoJPA cadastrarProduto(String codigo, BigDecimal valor) throws TipoChaveNaoEncontradoException, DAOException {
		ProdutoJPA produto = new ProdutoJPA();
		
		produto.setNome("Computador");
		produto.setCodigo(codigo);
		produto.setDescricao("Computador gamer");
		produto.setCategoria("Informática");
		produto.setValor(valor);
		return produtoDao.cadastrar(produto);
	}
	
	private VendaJPA criarVenda(String codigo) {
		VendaJPA venda = new VendaJPA();
		venda.setCodigo(codigo);
		venda.setDataVenda(Instant.now());
		venda.setCliente(this.cliente);
		venda.setStatus(Status.INICIADA);
		venda.adicionarProduto(this.produto, 2);
		return venda;
	}
	
	private void excluirVendas() throws DAOException {
		Collection<VendaJPA> list = vendaDao.buscarTodos();
		
		list.forEach(venda -> {
			try {
				vendaExclusão.excluir(venda);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private void excluirProdutos() throws DAOException {
		Collection<ProdutoJPA> list = produtoDao.buscarTodos();
		
		list.forEach(produto -> {
			try {
				produtoDao.excluir(produto);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
	}
}

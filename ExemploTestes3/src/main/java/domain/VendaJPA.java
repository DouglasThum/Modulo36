package domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import dao.Persistente;

@Entity
@Table(name="TB_VENDA")
public class VendaJPA implements Persistente {
	
	public enum Status {
		INICIADA, CONCLUIDA, CANCELADA;
		
		public static Status getByName(String value) {
			for (Status status : Status.values()) {
	            if (status.name().equals(value)) {
	                return status;
	            }
	        }
			return null;
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venda_seq")
	@SequenceGenerator(name = "venda_seq", sequenceName = "sq_venda", initialValue = 1, allocationSize = 1)
	private Long id;
	
	@Column(name="CODIGO", nullable = false, length = 10, unique = true)
	private String codigo;
	
	@ManyToOne
	@JoinColumn(name="id_cliente_fk",
			foreignKey = @ForeignKey(name="fk_venda_cliente"),
			referencedColumnName = "id", nullable = false)
	private ClienteJPA cliente;
	
	@OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
	private Set<ProdutoQuantidadeJPA> produtos;
	
	@Column(name="VALOR_TOTAL", nullable = false)
	private BigDecimal valorTotal;
	
	@Column(name="DATA_VENDA")
	private Instant dataVenda;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_VENDA", nullable = false)
	private Status status;
	
	public VendaJPA() {
		produtos = new HashSet<>();
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public ClienteJPA getCliente() {
		return cliente;
	}

	public void setCliente(ClienteJPA cliente) {
		this.cliente = cliente;
	}

	public Set<ProdutoQuantidadeJPA> getProdutos() {
		return produtos;
	}

	public void setProdutos(Set<ProdutoQuantidadeJPA> produtos) {
		this.produtos = produtos;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Instant getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Instant dataVenda) {
		this.dataVenda = dataVenda;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void adicionarProduto(ProdutoJPA produto, Integer quantidade) {
		validarStatus();
		Optional<ProdutoQuantidadeJPA> optional = produtos.stream()
				.filter(p -> p.getProduto().getCodigo().equals(produto.getCodigo()))
				.findAny();
		if (optional.isPresent()) {
			ProdutoQuantidadeJPA prodQuant = optional.get();
			prodQuant.adicionar(quantidade);
		} else {
			ProdutoQuantidadeJPA prod = new ProdutoQuantidadeJPA();
			prod.setVenda(this);
			prod.setProduto(produto);
			prod.adicionar(quantidade);
			produtos.add(prod);
		}
		recalcularValorTotalVenda();
	}
	
	public void validarStatus() {
		if(this.status == Status.CONCLUIDA) {
			throw new UnsupportedOperationException("IMPOSSÍVEL ALTERAR VENDA FINALIZADA");
		}
	}
	
	public void removerProduto(ProdutoJPA produto, Integer quantidade) {
		validarStatus();
		Optional<ProdutoQuantidadeJPA> optional = produtos.stream()
				.filter(p -> p.getProduto().getCodigo().equals(produto.getCodigo()))
				.findAny();
		if (optional.isPresent()) {
			ProdutoQuantidadeJPA prodQuant = optional.get();
			prodQuant.remover(quantidade);
		} else {
			produtos.remove(optional.get());
		}
		recalcularValorTotalVenda();
	}
	
	public void removerTodosProdutos() {
		validarStatus();
		produtos.clear();
		valorTotal = BigDecimal.ZERO;
	}
	
	public Integer getQuantidadeTotalProdutos() {
		Integer result = produtos.stream()
				.reduce(0, (parcialResult, prod) -> parcialResult + prod.getQuantidade(), Integer::sum);
		return result;		
	}
	
	public void recalcularValorTotalVenda() {
		validarStatus();
		BigDecimal valorTotal = BigDecimal.ZERO;
		for (ProdutoQuantidadeJPA prod : this.produtos) {
			valorTotal = valorTotal.add(prod.getValorTotal());
		}
		this.valorTotal = valorTotal;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}


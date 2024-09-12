package domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import dao.Persistente;

@Entity
@Table(name="TB_PRODUTO")
public class ProdutoJPA implements Persistente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prod_seq")
	@SequenceGenerator(name="prod_seq", sequenceName = "sq_prod", initialValue = 1, allocationSize = 1)
	private Long id;
	
	@Column(name="CODIGO", nullable = false, length = 10, unique = true)
	private String codigo;
	
	@Column(name="NOME", nullable = false, length = 50)
	private String nome;
	
	@Column(name="DESCRICAO", nullable = false, length = 50)
	private String descricao;
	
	@Column(name="VALOR", nullable = false)
	private BigDecimal valor;
	
	@Column(name="CATEGORIA", nullable = false, length = 50)
	private String categoria;
	
	public ProdutoJPA(Long id, String codigo, String nome, String descricao, BigDecimal valor, String categoria) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nome = nome;
		this.descricao = descricao;
		this.valor = valor;
		this.categoria = categoria;
	}
	
	public ProdutoJPA() {
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;		
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
}

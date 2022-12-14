package com.rc.curso_alura.modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Livro {
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String titulo;
	private String isbn;
	private String genero;
	private Double preco;
	
	@Temporal(TemporalType.TIMESTAMP)   //     (TemporalType.DATE): diz pro JPA gravar somente a data com horário zerado
	private Calendar dataLancamento = Calendar.getInstance();

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Autor> autores = new ArrayList<Autor>();
	
	public Livro() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Calendar getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Calendar dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public List<Autor> getAutores() {
		return autores;
	}

	public void adicionaAutor(Autor autorED) {
		this.autores.add(autorED);
	}

	public void removeAutor(Autor autor) {
		// TODO Auto-generated method stub
		this.autores.remove(autor);
	}
}
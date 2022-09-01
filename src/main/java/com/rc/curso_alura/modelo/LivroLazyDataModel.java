package com.rc.curso_alura.modelo;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.rc.curso_alura.dao.DAO;

public class LivroLazyDataModel extends LazyDataModel<Livro> {
	private static final long serialVersionUID = 1L;

	private DAO<Livro> dao;

	public LivroLazyDataModel() {
		dao = new DAO<Livro>(Livro.class);
		super.setRowCount(dao.quantidadeDeElementos());
	}

	 @Override
	 public String getRowKey(Livro livro) {
		 return String.valueOf(livro.getId());
	 }
	 
	@Override
	public List<Livro> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		
		String filterField = null;
		String filterValue = null;
		// -----
		filterValue = (String) filters.get("titulo");
		if (filterValue != null) {
			filterField = "titulo";
		}else {
			filterValue = (String) filters.get("genero");
			if (filterValue != null) {
				filterField = "genero";
			}else {
				filterValue = (String) filters.get("genero");
				if (filterValue != null) {
					filterField = "genero";
				}
			}
		}		
		// -----

		String sortOrderValue = null;
		if (sortOrder.name().equalsIgnoreCase("ASCENDING")) {
			sortOrderValue = "ASC";
		} else if (sortOrder.name().equalsIgnoreCase("DESCENDING")) {
			sortOrderValue = "DESC";
		} else {
			sortOrderValue = "ASC";
		}
		
		List<Livro> livros = dao.listaTodosPaginada(first, pageSize, filterField, filterValue, sortField,
				sortOrderValue);
		
		// Abaixo: para controle da paginação (no componente p:dataTable) quando o conjunto de registro está filtrado ou não (total).
		if (filterValue != null) {
			super.setRowCount(dao.quantidadeDeElementosFiltrados(filterField, filterValue));		
		}else super.setRowCount(dao.quantidadeDeElementos());
		
		return livros;

		// ==========
		/*
		 * String sortOrderValue = null; 
		 * if (campoOrdenacao == null) { 
		 *     campoOrdenacao =  "titulo"; 
		 * } 
		 * 
		 * if (sentidoOrdenacao.ASCENDING.equals("A")) { 
		 *  	sortOrderValue = * "ASC"; 
		 *  } else if (sentidoOrdenacao.DESCENDING.equals("D")) { 
		 *  	sortOrderValue = "DSC"; 
		 *  } else { 
		 *  	sortOrderValue = "ASC"; 
		 *  } //myPosts = getAllMyPosts();
		 *  
		 * //productsInfo = dao.getAllProducts(first, pageSize, sortField, sortOrderValue, filters); // rowCount int dataSize = super.getRowCount(); //
		 * myPosts.size(); 
		 * //this.setRowCount(dataSize); // paginate 
		 * if (dataSize > quantidade) { 
		 * 		try { 
		 * 			return livros.subList(inicio,inicio + quantidade); 
		 * 		} catch (IndexOutOfBoundsException e) { return livros.subList(inicio, inicio + (dataSize % quantidade)); 
		 * 		} 
		 * } else { 
		 * 		return livros; 
		 * }
		 */

		// ==========
	}

}

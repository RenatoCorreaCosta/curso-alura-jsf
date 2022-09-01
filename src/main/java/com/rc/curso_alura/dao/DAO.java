package com.rc.curso_alura.dao;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.rc.curso_alura.modelo.Autor;

public class DAO<T> {

	private final Class<T> classe;

	public DAO(Class<T> classe) {
		this.classe = classe;
	}

	public void adiciona(T t) {

		// consegue a entity manager
		EntityManager em = new JPAUtil().getEntityManager();

		// abre transacao
		em.getTransaction().begin();

		// persiste o objeto
		em.persist(t);

		// commita a transacao
		em.getTransaction().commit();

		// fecha a entity manager
		em.close();
	}

	public void remove(T t) throws Exception{
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();

		em.remove(em.merge(t));

		em.getTransaction().commit();
		em.close();
	}

	public void atualiza(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();

		em.merge(t);

		em.getTransaction().commit();
		em.close();
	}

	public List<T> listaTodos() {
		EntityManager em = new JPAUtil().getEntityManager();
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));

		List<T> lista = em.createQuery(query).getResultList();

		em.close();
		return lista;
	}

	public T buscaPorId(Integer id) {
		EntityManager em = new JPAUtil().getEntityManager();
		T instancia = em.find(classe, id);
		em.close();
		return instancia;
	}

	public int contaTodos() {
		EntityManager em = new JPAUtil().getEntityManager();
		long result = (Long) em.createQuery("select count(n) from livro n")
				.getSingleResult();
		em.close();

		return (int) result;
	}
	
	// Abaixo, para solução para paginação de dados por demanda - DataLazyModal -> PrimeFaces
	
	public int quantidadeDeElementos() {
        EntityManager em = new JPAUtil().getEntityManager();
        long result = (Long) em.createQuery("select count(n) from " + this.classe.getSimpleName() + " n")
                .getSingleResult();
        em.close();

        return (int)result;
    }

	public int quantidadeDeElementosFiltrados(String filterField, String filterValue) {
        EntityManager em = new JPAUtil().getEntityManager();
        
        CriteriaBuilder     cb = em.getCriteriaBuilder();		
		CriteriaQuery<T> query = cb.createQuery(classe);		
		Root<T>           root = query.from(classe);
		
		query = query.where(em.getCriteriaBuilder().like(root.get(filterField), "%" + filterValue + "%"));

		TypedQuery<T> typedQuery = em.createQuery(query);        
		List<T> lista = typedQuery.getResultList();
        		
        em.close();
        return (lista!=null?lista.size():0);
    }
	
	public List<T> listaTodosPaginada(int first, int pageSize, String filterField, String filterValue, String sortField, String sortOrderValue) {		
		EntityManager em = new JPAUtil().getEntityManager();		
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<T> query = cb.createQuery(classe);		
		Root<T> root = query.from(classe);
		
		if (sortField != null) { 
			if (sortOrderValue.equals("ASC")) {
				query.orderBy(cb.asc(root.get(sortField)));
			}else if (sortOrderValue.equals("DESC")) {
				query.orderBy(cb.desc(root.get(sortField)));
			}
		}
				
		if (filterValue != null) {
			query = query.where(em.getCriteriaBuilder().like(root.get(filterField), "%" + filterValue + "%"));
		}		
		
		List<T> lista; // = typedQuery.getResultList();
		TypedQuery<T> typedQuery = em.createQuery(query);
		typedQuery.setFirstResult(first);
		typedQuery.setMaxResults(pageSize);

		lista = typedQuery.getResultList();

		em.close();		
		return lista;

        // ** Abaixo, sem filtro **
		//query.select(query.from(classe));
		/*List<T> lista = em.createQuery(query).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
		em.close();		
		return lista;
		*/
	}
	
	// **************************************************************
	// **************************************************************
	public static void main(String[] args) {
		DAO<Autor> dao = new DAO<Autor>(Autor.class);
				
		ArrayList<Autor> lista = (ArrayList<Autor>) dao.listaTodos();
		Autor autorED;
		System.out.println("* Lista");
		for (int i = 0; i < lista.size(); i++) {
			autorED = lista.get(i);

			System.out.println("Id Autor: " + autorED.getId() + " - " + "Nome: " + autorED.getNome());
		}
		
	}
}

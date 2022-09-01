package com.rc.curso_alura.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import com.rc.curso_alura.modelo.Autor;

public class temp_AutorBD_Nao_Usado {
	private static EntityManagerFactory ENTITY_MANAGER_FACTORY_bdAlura = Persistence.createEntityManagerFactory("bdAlura");

	public DetachedCriteria montaCriterios(Autor contaEDPesquisada) {
		DetachedCriteria dc = DetachedCriteria.forClass(Autor.class);
		dc.add(Example.create(contaEDPesquisada).enableLike(MatchMode.ANYWHERE).ignoreCase());
		return dc;
	}

	// método lista oficial Hibernate - arqjava Procergs
	public List<Autor> lista(Autor autorED) {
		EntityManager em = ENTITY_MANAGER_FACTORY_bdAlura.createEntityManager();

		DetachedCriteria dc = montaCriterios(autorED == null ? new Autor() : autorED);
		// adicionaOrdem(ped, dc);
		// return findByCriteria(dc, ped.getPropLista().getInicio(),
		// ped.getPropLista().getTamanho());

		Criteria criteria = dc.getExecutableCriteria(em.unwrap(Session.class));
		// criteria.setFirstResult(contaED.ge);
		// criteria.setMaxResults(maxResult);

		return criteria.list();
	}

	// **************************************************************
	// **************************************************************
	public static void main(String[] args) {
				
		temp_AutorBD_Nao_Usado autorBD = new temp_AutorBD_Nao_Usado();
		Autor autorED = null;
		try {
			// ----- Lista -------
			System.out.println(" ** Lista Especial, pesquisada...: ***");
			autorED = new Autor();
			//autorED.setNome("RE");

			ArrayList<Autor> lista = (ArrayList<Autor>) autorBD.lista(autorED);
			System.out.println("* Lista");
			for (int i = 0; i < lista.size(); i++) {
				autorED = lista.get(i);

				System.out.println("Id Autor: " + autorED.getId() + " - " + "Nome: " + autorED.getNome());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

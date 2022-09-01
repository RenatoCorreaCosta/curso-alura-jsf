package com.rc.curso_alura.util;

import javax.persistence.EntityManager;

import com.rc.curso_alura.dao.JPAUtil;

public class AtualizaBancoDadosDDL {
	public static void main(String[] args) {		
		EntityManager em = new JPAUtil().getEntityManager();
		em.close();
	}
}

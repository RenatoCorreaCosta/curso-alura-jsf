package com.rc.curso_alura.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.rc.curso_alura.modelo.Usuario;

public class UsuarioDAO {

	public boolean loginAutenticado(Usuario usuario) {  // no curso o nome do método é: public boolean existe (Usuario usuario){  ..... }

		EntityManager em = new JPAUtil().getEntityManager();
		
		TypedQuery<Usuario> query = em.createQuery("select u from Usuario u where u.email = :pEmail and u.senha = :pSenha", Usuario.class);
		query.setParameter("pEmail", usuario.getEmail());
		query.setParameter("pSenha", usuario.getSenha());
		
		Usuario resultado = null;
		try {
			resultado = query.getSingleResult();
			
		}catch(Exception e){
			return false;
		}finally {
			em.close();
		}

		return resultado != null; //true
	}

}

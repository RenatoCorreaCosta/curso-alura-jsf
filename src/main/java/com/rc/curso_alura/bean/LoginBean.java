package com.rc.curso_alura.bean;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import com.rc.curso_alura.dao.UsuarioDAO;
import com.rc.curso_alura.modelo.Usuario;

@Named
@ViewScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	@PostConstruct
	public void init() {
		System.out.println("Instanciou o bean UsuarioBean");
		usuario = new Usuario();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public String efetuaLogin() {
		System.out.println("Efetuando o login do usuário: " + this.usuario.getEmail());

		FacesContext context = FacesContext.getCurrentInstance(); // recupera a requisição corrente. // acesso a objetos do nivel do servet (controller)
		context.getExternalContext().getFlash().setKeepMessages(true); // mantém as mensagens por duas requisições. Com o redirect=true (abaixo) presisamos setar este recurso, pois o redirect=true fazer mais uma requisição ao servidor.
		
		boolean autenticado = new UsuarioDAO().loginAutenticado(this.usuario);		
		if (autenticado) {
			//---- Guarda usuário autenticado na sessão (http) ----			
			Map<String,Object> map = context.getExternalContext().getSessionMap(); // mapa onde se guarda dados da sessão. 
			map.put("usuarioLogado", this.usuario);
			map.put("indoSurf", "Loner Surfer");  // -> tirar no futuro
			//------------------------------------------------------
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Login","Usuário autenticado (validado)"));
			
			return "livro?faces-redirect=true";
		}
		
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Login","usuario naooooo autenticado (validado)"));
		
		return "login?faces-redirect=true"; // faces-redirect=true -> diz pro navegador enviar outra requisição ao servidor.
	}
	
	public String deslogar() {
		FacesContext context   = FacesContext.getCurrentInstance(); // recupera a requisição corrente. // acesso a objetos do nivel do servet (controller)
		Map<String,Object> map = context.getExternalContext().getSessionMap(); // mapa onde se guarda dados da sessão. 
		map.remove("usuarioLogado"); // ou   map.put("usuarioLogado", null);
		map.remove("indoSurf"); // ou map.put("indoSurf",null) // -> tirar no futuro
		
		return "login?faces-redirect=true";
	}
}

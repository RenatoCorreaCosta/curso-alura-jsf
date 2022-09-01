package com.rc.curso_alura.util;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.rc.curso_alura.modelo.Usuario;

public class AutorizadorPhaseListener implements PhaseListener{
	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent event) {		
		/* Quando já está recuperado a árvore de componentes
		 * assim podemos descobrir qual página que o usuário está acessando.
		 */
		
		FacesContext context = event.getFacesContext(); // recupera a requisição corrente. Nivel das servlets - Controlador.
		
		UIViewRoot arvore    = context.getViewRoot();   // pega a árvore de componentes (nodo raiz da arvore de componentes da página acessada).		
		String nomePagina    = arvore.getViewId();
		if (nomePagina==null) return;
		
		System.out.println("nome página: " + nomePagina);
		if (nomePagina.equals("/login.xhtml")){
			return;
		}
		
		Map<String,Object> map = context.getExternalContext().getSessionMap(); // mapa onde se guarda dados da sessão. 
		Usuario usuariLogado = (Usuario)map.get("usuarioLogado"); // objeto injetado no mapa da sessão em: loginBean -> método: efetuaLogin
		if (usuariLogado != null) {
			return; // usuário já logado.
		}
		
		// abaixo: redirecionamento para login.xhtml *  nota: na api do omnifaces (import org.omnifaces.util.Faces) já possui objeto Faces que tem um método de redirecionamento.		
		Application       application   = context.getApplication();
		NavigationHandler handler       = application.getNavigationHandler();
		handler.handleNavigation(context, null, "/login?faces-redirect=true");
		
		context.renderResponse(); // pula as fases de 1 à 5, indo para a ultima (6) para renderizar a resposta.
	}

	
	@Override
	public void beforePhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW; // fase onde é recuperado a árvore de componentes
	}

}

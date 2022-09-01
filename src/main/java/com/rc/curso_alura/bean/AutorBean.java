package com.rc.curso_alura.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import com.rc.curso_alura.dao.DAO;
import com.rc.curso_alura.modelo.Autor;
import com.rc.curso_alura.modelo.Livro;

@Named
@ViewScoped
public class AutorBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Autor autor = new Autor();

	private Integer autorId;

	@PostConstruct
	public void init() {
		System.out.println("Instanciou o bean AutorBean");
	}

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Autor getAutor() {
		return autor;
	}

	// método abaixo: para conseguir setar a propriedade this.autor via JSF
	// (<f:setPropertyActionListener target="#{autorBean.autor}" value="#{autor}"
	// />)
	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public String gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());

		if (this.autor.getId() == null) {
			new DAO<Autor>(Autor.class).adiciona(this.autor);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro incluído com sucesso!", "registro.inlcui-sucesso"));
			return "livro?faces-redirect=true";
		} else {
			new DAO<Autor>(Autor.class).atualiza(this.autor);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro alterado com sucesso!", "registro.altera-sucesso"));
		}
		this.autor = new Autor();
		return null;
	}

	public void validaContatoAutor(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		if (value.toString().length() < 4)
			throw new ValidatorException(new FacesMessage("** Mínimo 4 caracteres: " + value.toString()));
	}

	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}

	public void carregar(Autor autor) {
		System.out.println("carregando formulario para alteração");
		this.autor = autor;
	}

	public void remover(Autor autor) {
		System.out.println("Removendo Autor");
		try {
			new DAO<Autor>(Autor.class).remove(autor);
			this.autor = new Autor();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro removido com sucesso!", "registro.remove-sucesso"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					e.getMessage() + " - " + e.getCause(), "registro.remove-insucesso"));
		}
	}

	public void carregarAutorPelaId() {
		this.autor = new DAO<Autor>(Autor.class).buscaPorId(this.autorId);
	}
}

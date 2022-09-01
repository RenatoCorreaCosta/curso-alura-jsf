package com.rc.curso_alura.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.data.FilterEvent;

import com.rc.curso_alura.dao.DAO;
import com.rc.curso_alura.modelo.Autor;
import com.rc.curso_alura.modelo.Livro;
import com.rc.curso_alura.modelo.LivroLazyDataModel;
import com.rc.curso_alura.modelo.Usuario;

@ViewScoped
@Named
public class LivroBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Livro livro = new Livro();

	private Integer livroId; // utilizado para receber o id do livro via url
	private Integer autorId; // utilizado para montar um link de alteração de Autor, chamando a página
								// autor.xhtml passando parametro.

	private List<Livro> livros;
	private List<Livro> livrosFiltrados;
	
	private List<String> generos = Arrays.asList("Policial","Romance","Comédia","Drama","Suspense","Aventura");
	
	private LivroLazyDataModel livroLazyDataModel = new LivroLazyDataModel();

	@PostConstruct
	public void init() {
		System.out.println("Instanciou o bean livroMB");
		
		//livrosFiltrados = new ArrayList<Livro>();
	}

	public Livro getLivro() {
		return livro;
	}

	// método abaixo: para conseguir setar a propriedade this.livro via JSF
	// (<f:setPropertyActionListener target="#{livroBean.livro}" value="#{livro}"
	// />)
	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Integer getLivroId() {
		return livroId;
	}

	public void setLivroId(Integer livroId) {
		this.livroId = livroId;
	}

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}

	public List<Autor> getAutoresDoLivro() {
		return this.livro.getAutores();
	}

	public List<Livro> getLivros() {
		if (this.livros == null) {
			DAO<Livro> dao = new DAO<Livro>(Livro.class);
			this.livros = dao.listaTodos();
		}
		return this.livros;
	}
		
	public List<String> getGeneros() {
		return generos;
	}

	public void setGeneros(List<String> generos) {
		this.generos = generos;
	}

	public List<Livro> getLivrosFiltrados() {
		return livrosFiltrados;
	}

	public void setLivrosFiltrados(List<Livro> livrosFiltrados) {
		this.livrosFiltrados = livrosFiltrados;
	} 
	
	public LivroLazyDataModel getLivroLazyDataModel() {
		return livroLazyDataModel;
	}

	public void setLivroLazyDataModel(LivroLazyDataModel livroLazyDataModel) {
		this.livroLazyDataModel = livroLazyDataModel;
	}

	public void gravarAutor() {
		// atualiza dados do autor
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(this.autorId);

		List<Autor> listaAutoresLivro = livro.getAutores();
		for (Autor aut : listaAutoresLivro) {
			if (this.autorId.intValue() == aut.getId().intValue()) {
				// System.out.println("Autor já CADASTRADO: " + autor.getNome());
				FacesContext.getCurrentInstance().addMessage("autor",
						new FacesMessage("Autor já CADASTRADO: " + autor.getNome()));
				return;
			}
		}
		System.out.println("Vai gravar Autor: " + autor.getNome());
		this.livro.adicionaAutor(autor);
	}

	public void gravar() {
		System.out.println("Vai gravar Livro com ID: " + this.livro.getId() + " - Titulo: " + this.livro.getTitulo()
				+ "  - Valor: " + this.livro.getPreco());

		if (livro.getAutores().isEmpty()) {
			// throw new RuntimeException("Livro deve ter pelo menos um Autor.");
			FacesContext.getCurrentInstance().addMessage("autor",
					new FacesMessage("Livro deve ter pelo menos um Autor"));
			return;
		}

		DAO<Livro> dao = new DAO<Livro>(Livro.class);
		if (this.livro.getId() == null) {
			dao.adiciona(this.livro);
			this.livros = dao.listaTodos(); // Somente é remontado a lista de livros quando se tem um novo livro
											// incluido.

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro incluido com sucesso!", "registro.inclui-sucesso"));
		} else {
			dao.atualiza(this.livro);
			/*
			 * Nota: o código "this.livros = dao.listaTodos();" --> não é necessário
			 * remontar a lista de livros na atualização do livro, pois o objeto (livro) que
			 * foi alterado, está contido na lista de livros. Ele foi obtido a partir da
			 * seleção do objeto livro dentro da lista de livros, logo se alterar este
			 * objeto, a lista de livros (que já está em memória) o terá atualizado, por
			 * isto que nao precisa ir novamente no banco de dados pedir a lista atualizada.
			 */

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro alterado com sucesso!", "registro.altera-sucesso"));
		}
		this.livro = new Livro();
	}

	public void remover(Livro livro) {
		System.out.println("Removendo livro");
		DAO<Livro> dao = new DAO<Livro>(Livro.class);
		try {
			dao.remove(livro);
			this.livros = dao.listaTodos();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro removido com sucesso!", "registro.remove-sucesso"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "registro.remove-insucesso"));
		}

	}

	public void removeAutorDoLivro(Autor autor) {
		System.out.println("Removendo autor do livro");
		this.livro.removeAutor(autor);
		// getAutores().remove(autor);
	}

	public void carregar(Livro livro) {
		System.out.println("carregando formulario para alteração");
		this.livro = livro;
	}

	public void carregarLivroPeloId() {
		this.livro = new DAO<Livro>(Livro.class).buscaPorId(this.livroId);
	}

	public String formAutor() {
		System.out.println("Chamando o formulário do Autor");
		return "autor?faces-redirect=true";
	}

	public String formAutorAlterar() {
		System.out.println("Chamando o formulário do Autor - alterar");
		return "autor?faces-redirect=true&autorId=" + this.autorId;
	}

	private void atualizaListaLivros() {
		// in the future... centralizar aqui a atualização dos livros e chamar este
		// método quando necessário.
		/*
		 * DAO<Livro> dao = new DAO<Livro>(Livro.class); this.livros = dao.listaTodos();
		 */
	}

	/*
	 * FacesContext -> um contexto que permite obter informações da view processada
	 * no momento, UIComponent: -> o componente da view que está sendo validado e
	 * Object: -> um objeto que representa o valor digitado pelo usuário.
	 * 
	 * ValidatorException -> exception que sinalizará para o JSF que algo saiu
	 * errado
	 */
	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		if (!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("ISBN - deve começar com 1: "));
		}
	}

	public boolean precoAteh(Object valorColuna, Object filtroDigitado, Locale locale) { //java.util.Locale		
		String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();
		
		System.out.println("Filtrando pelo " + textoDigitado + ", Valor do elemento: " + valorColuna);
		
		// o filtro é nulo ou vazio?
        if (textoDigitado == null || textoDigitado.equals("")) {
            return true;
        }

        // elemento da tabela é nulo?
        if (valorColuna == null) {
            return false;
        }

        try {
            // fazendo o parsing do filtro para converter para Double
            Double precoDigitado = Double.valueOf(textoDigitado);
            Double precoColuna = (Double) valorColuna;

            // comparando os valores, compareTo devolve um valor negativo se o value é menor do que o filtro e zero se é igual
            return precoColuna.compareTo(precoDigitado) <= 0;

        } catch (NumberFormatException e) {

            // usuario nao digitou um numero
            return false;
        }		
	}
	
	public void filterListener(FilterEvent filterEvent) {
		System.out.println(" ** filterListener ** ");
		//System.out.println(" ** Tamanho lista filtrada:  " + livrosFiltrados.size());
	}
	
	// abaixo, deprecated -> apenas para testes.
	// Tirar no futuro quando houver um session bean para tal. Senao em todos os
	// beans tem que fazer algo parecido com isto.
	public String usuarioLogado() {
		FacesContext context = FacesContext.getCurrentInstance();
		return ((Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado")).getEmail();
	}

	public void testeAzaxEventBlur() {
		System.out.println(" ** ajax: blur ** ");
	}
}

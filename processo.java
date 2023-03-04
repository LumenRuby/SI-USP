package escalonador;

enum Estado {
	EXECUTANDO,
	PRONTO,
	BLOQUEADO
}

public class Processo {
	String nome;
	final String [] instrucao;
	private Estado estado;

	public Processo(String nome, String[] instrucao, Estado estado) {
		this.nome = nome;
		this.setEstado(estado);
		this.instrucao = instrucao;
	}

	public Estado getEstado() {
		return estado;
	}

	public setEstado(Estado estado) {
		this.estado = estado;
	}
}

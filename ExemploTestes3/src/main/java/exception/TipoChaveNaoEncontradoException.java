package exception;

public class TipoChaveNaoEncontradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public TipoChaveNaoEncontradoException(String msg) {
		this(msg, null);
	}
	
	public TipoChaveNaoEncontradoException(String msg, Throwable e) {
		super(msg, e);
	}
}

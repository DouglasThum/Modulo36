package exception;

public class TipoElementoNaoEncontradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public TipoElementoNaoEncontradoException (String msg) {
		super(msg);
	}
	
	public TipoElementoNaoEncontradoException (String msg, Exception e) {
		super(msg, e);
	}
}

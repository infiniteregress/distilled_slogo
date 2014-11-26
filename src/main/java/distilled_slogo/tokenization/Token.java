package distilled_slogo.tokenization;

/**
 * An implementation of a token used for tokenization
 *
 */
public class Token implements IToken{

	private final String text;
	private final String label;

	/**
	 * Create a new token
	 * 
	 * @param text The raw text of the token
	 * @param label The abstract identifier for this token
	 */
	public Token(String text, String label){
		this.text = text;
		this.label = label;
	}
	@Override
	public String text() {
		return text;
	}

	@Override
	public String label() {
		return label;
	}
	@Override
	public String toString(){
		return label + ": " + text;
	}
}

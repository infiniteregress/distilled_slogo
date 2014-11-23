package distilled_slogo.tokenization;


public class Token implements IToken{

	private String text;
	private String label;

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

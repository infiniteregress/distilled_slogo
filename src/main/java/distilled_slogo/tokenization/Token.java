package distilled_slogo.tokenization;


public class Token implements IToken{

	private String text;
	private String type;

	public Token(String text, String type){
		this.text = text;
		this.type = type;
	}
	@Override
	public String text() {
		return text;
	}

	@Override
	public String type() {
		return type;
	}
	@Override
	public String toString(){
		return type + ": " + text;
	}
}

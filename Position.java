
public class Position {
	private String word;
	private Coordinate from;
	private Coordinate to;
	
	public Position(String word, Coordinate from) {
		this.word = word;
		this.from = from;
	}
	
	public Position(String word, Coordinate from, Coordinate to) {
		this.word = word;
		this.from = from;
		this.to = to;
	}
	
	@Override
	public String toString() {
		return "\n" + word + ": (" + from.getRow() + "," + from.getCol() + ") to (" + to.getRow() + "," + to.getCol() + ")";
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Coordinate getFrom() {
		return from;
	}

	public void setFrom(Coordinate from) {
		this.from = from;
	}

	public Coordinate getTo() {
		return to;
	}

	public void setTo(Coordinate to) {
		this.to = to;
	}
	
}

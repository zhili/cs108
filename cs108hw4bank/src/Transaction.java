
public class Transaction {
	final int fromId;
	final int toId;
	final int amount;
	
	public Transaction(int fromId, int toId, int amount) {
		this.fromId = fromId;
		this.toId = toId;
		this.amount = amount;
	}
	
	public Transaction(int[] trans) {
		this(trans[0], trans[1], trans[2]);
	}
	
	public int getSrcId() {
		return fromId;
	}
	
	public int getDesId() {
		return toId;
	}
	
	public int getAmount() {
		return amount;
	}
}

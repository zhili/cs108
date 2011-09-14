
public class Account {
	private int id;
	private int balance;
	private int numTransactions;
	
	public Account(int id, int balance, int numTransactions) {
		super();
		this.id = id;
		this.balance = balance;
		this.numTransactions = numTransactions;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", balance=" + balance
				+ ", numTransactions=" + numTransactions + "]";
	}
	
	public synchronized void inc(int money) {
		balance += money;
		numTransactions++;
	}
	
	public synchronized void dec(int money) {
		balance -= money;
		numTransactions++;
	}
}

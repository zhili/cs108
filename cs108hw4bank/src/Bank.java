import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;


public class Bank {
	private ArrayList<Account> accounts;
	private ArrayBlockingQueue<Transaction> queue;
	
	static CountDownLatch latch;
	private final int QUEUESIZE = 30;
	private final Transaction nullTrans;
	private final int numThreads;
	private final int numAccounts = 20;
	
	private class Worker extends Thread {
		@Override
		public void run() {
			boolean done = false;
			while (!done) {
				try {
					Transaction cur = queue.take();
					if (cur == nullTrans) {
						done = true;
					} else {
						accounts.get(cur.getSrcId()).dec(cur.getAmount());
						accounts.get(cur.getDesId()).inc(cur.getAmount());
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
			}
			System.out.println(Thread.currentThread().getName() + " is done.");
			latch.countDown();
		}
	}
	
	public Bank(int numThreads) {
		this.queue = new ArrayBlockingQueue<Transaction>(QUEUESIZE);
		for (int i = 0; i < numThreads; i++) {
			 new Worker().start();
		}
		this.nullTrans = new Transaction(-1,0,0);
		this.numThreads = numThreads;
		accounts = new ArrayList<Account>();
		for (int i = 0; i < numAccounts; i++) {
			accounts.add(new Account(i, 1000, 0));
		}
	}
	
	private static int[] StringToInts(String line) {
		int[] result = new int[3];
		int s = 0, e = 0, found = 0;
		
		for (; e < line.length(); e++) {
			if (Character.isSpaceChar(line.charAt(e)) && found < 3) {
				result[found] = Integer.parseInt(line.substring(s, e));
				found++;
				s = e+1;
			}
		}
		result[found++] = Integer.parseInt(line.substring(s, e));
		return result;
	}
	
	public void kickOff(File f) {
		try {
			String line; 
			BufferedReader in = new BufferedReader(new FileReader(f));

			while ((line = in.readLine()) != null) {
				int[] trans = StringToInts(line);
				queue.put(new Transaction(trans));
			}
			in.close();
			// file reading ends, now send end signals to workers.
			// one per thread.
			for (int i = 0; i < numThreads; i++)
				queue.put(nullTrans);

		} catch (IOException e) {
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Bank input_file numThreads");
			System.exit(-1);
		}
		int numThreads = 0;
		try {
			numThreads = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
		}
		latch = new CountDownLatch(numThreads);

		Bank icbc = new Bank(numThreads);
		 
		icbc.kickOff(new File(args[0]));

		try {
			latch.await();
		} catch (InterruptedException ignored) {}
		
		System.out.println("All done");	
		for (Account a : icbc.accounts) {
			System.out.println(a);
		}
	}
}

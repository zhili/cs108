import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;


public class Cracker {


	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	static CountDownLatch latch;
	private int maxPassLen;
	private byte[] hashedArray;

	public Cracker(String hashedString, int maxPassLen) {
		this.hashedArray = hexToArray(hashedString);
		this.maxPassLen = maxPassLen;
	}
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	
	public static String genenrateSHA(String pass) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(pass.getBytes());
			return hexToString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public class RunnableCrack implements Runnable {

		public RunnableCrack(int begin, int len) {
			this.begin = begin;
			this.len = len;
//			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			System.out.println("begin: "+ begin+" len: "+len);
			for (int i = begin; i < begin+len; i++) {
				// i: start char index
				// j: password length
				for (int j = 1; j <= maxPassLen; j++) {
					
					byte[] buff = new byte[j];
					buff[0] = (byte) CHARS[i]; // set first character.
					perm(1, buff, j);
				}
			}
			latch.countDown();
		}
		
		private void perm(int pos, byte[] buff, int tLen) {
			if (pos == tLen) {
				//System.out.println(Arrays.toString(buff));

				md.update(buff);
				//md.digest();
				if(MessageDigest.isEqual(md.digest(), hashedArray)) {
					System.out.println(new String(buff));
				}
				return;
			}
			for (int c = 0; c < CHARS.length; c++) {
				buff[pos] =  (byte) CHARS[c];
				perm(pos+1, buff, tLen);
			}
		}
		private int begin;
		private int len;
		private MessageDigest md;
	}
	
	public void kickOff(int numThreads) {

		int avgLen = CHARS.length / numThreads;
		int remain = CHARS.length % numThreads;
		int begin = 0, len;
		for (int i = 0; i < numThreads; i++) {
			if (remain > 0) {
				len = avgLen + 1;
			} else {
				len = avgLen;
			}
			Thread a = new Thread(new RunnableCrack(begin, len));
			a.start();
			remain--;
			begin += len;
		}
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	
	public static void main(String args[]) {

		if (args.length == 1) {
			// generate mode
			System.out.println(genenrateSHA(args[0]));
		} else if (args.length == 3) {
			// crack mode
			String hasedString = args[0];
			int maxPassLen = Integer.parseInt(args[1]);
			int numThreads = Integer.parseInt(args[2]);
			latch = new CountDownLatch(numThreads);
			Cracker myCracker = new Cracker(hasedString, maxPassLen);
			long startTime = System.currentTimeMillis();
			myCracker.kickOff(numThreads);
			try {
				latch.await();
			} catch (InterruptedException e) {	
			}
			long endTime = System.currentTimeMillis()-startTime;
			System.out.println("elapsed:" + endTime);
			System.out.println("all done!");
		} else {
			// argument error
			System.err.println("check your argument");
			System.exit(-1);
		}
	}
}

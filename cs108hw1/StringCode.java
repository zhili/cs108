import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int maxRun = 0;
		int at = 0;
		for (;at < str.length(); at++) {
			char ch = str.charAt(at);
			int cur = at + 1;
			while (cur < str.length() && ch == str.charAt(cur)) {
				cur++;
			}
			if (maxRun < (cur - at))
				maxRun = cur - at;
		}
		return maxRun; // YOUR CODE HERE
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {

		 int at = 0;
		 StringBuilder result = new StringBuilder();
		 for (;at < str.length(); at++) {
			 char ch = str.charAt(at);

			 if (Character.isDigit(ch)) {
				 int num = ch - 48;
				 while((at+1) != str.length() && num > 0) {
					 result.append(str.charAt(at+1));
					 num--;
				 }
			 } else {
				 result.append(ch);
			 }

		 }
		 return result.toString();
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		
		if (len <= 0)
			return false;
		if (a.length() < len || b.length() < len)
			return false;
		
		Set<String> subSetA = new HashSet<String>();
		int posA = 0;
		int posB = 0;
		for (; posA + len < a.length(); posA += 1) {
			subSetA.add(a.substring(posA, posA+len));
		}
		
		for (; posB + len < b.length(); posB += 1)
			if (subSetA.contains(b.substring(posB, posB+len)))
				return true;		
		return false; // YOUR CODE HERE
	}
}

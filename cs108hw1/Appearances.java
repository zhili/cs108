import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		Map<T, Integer> freqA = new HashMap<T, Integer>();
		Map<T, Integer> freqB = new HashMap<T, Integer>();
		for (T item : a) {
			int count = freqA.containsKey(item) ? freqA.get(item) : 0;
			freqA.put(item, count + 1);
		}
		
		for (T item : b) {
			int count = freqB.containsKey(item) ? freqB.get(item) : 0;
			freqB.put(item, count + 1);
		}
		int result = 0;
		for(T key : freqA.keySet())
			if (freqA.get(key) == freqB.get(key))
				result += 1;
		
		return result; 
	}
	
}

// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {
	
	public void testNoFollowBasic() {
		Set<Integer> orig = new HashSet<Integer>();
		orig.add(2);
		List<Integer> a = Arrays.asList(1, 2, 3, 4);
		Taboo<Integer> tbo = new Taboo<Integer>(a);
		assertEquals(orig, tbo.noFollow(1));
	}
	
	public void testNoFollowEmpty() {
		Set<Integer> orig = new HashSet<Integer>();
		//orig.add(2);
		List<Integer> a = Arrays.asList(1, 2, 3, 4);
		Taboo<Integer> tbo = new Taboo<Integer>(a);
		assertEquals(orig, tbo.noFollow(5));
	}
	
	public void testNoFollowNull() {
		Set<String> orig = new HashSet<String>();
		orig.add("b");
		List<String> a = Arrays.asList("a", "b", null, "c", "d");
		Taboo<String> tbo = new Taboo<String>(a);
		assertEquals(orig, tbo.noFollow("a"));
	}
	
	public void testNoFollowMore() {
		Set<String> orig = new HashSet<String>();
		orig.add("c");
		orig.add("b");
		List<String> a = Arrays.asList("a", "c", "a", "b");
		Taboo<String> tbo = new Taboo<String>(a);
		assertEquals(orig, tbo.noFollow("a"));
	}
	
	public void testReduceBasic() {

		String[] after = {"a", "x", "c"};
		List<String> rule = Arrays.asList("a", "c", "a", "b");
		List<String> before =  new ArrayList<String>(Arrays.asList("a", "c", "b", "x", "c", "a"));
		Taboo<String> tbo = new Taboo<String>(rule);
		tbo.reduce(before);
		assertTrue(Arrays.deepEquals(after, before.toArray()));
	}
	
	public void testReduceMore() {

		String[] after = {"a", "d", "b", "x", "c"};
		List<String> rule = Arrays.asList("a", "c", "a", "b");
		List<String> before =  new ArrayList<String>(Arrays.asList("a", "d", "b", "x", "c", "a"));
		Taboo<String> tbo = new Taboo<String>(rule);
		tbo.reduce(before);
		assertTrue(Arrays.deepEquals(after, before.toArray()));
	}
}

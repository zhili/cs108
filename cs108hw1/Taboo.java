
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	private List<T> rules;
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
	   this.rules = new ArrayList<T>(rules);
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
	   Set<T> noFollowSet = new HashSet<T>();
	   ListIterator<T> rulesIter = rules.listIterator();

	   while (rulesIter.hasNext()) {
		   T next = rulesIter.next();
		   if (next != null && next.equals(elem) && rulesIter.hasNext())
	    	  noFollowSet.add(rulesIter.next());
	   } 
	   
	   if (noFollowSet.isEmpty())
		   return Collections.emptySet();
	   return noFollowSet;
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		ListIterator<T> liter = list.listIterator();
		for (;liter.hasNext();) {
			T next = liter.next();
			Set<T> noFollowSet = noFollow(next);
			while (liter.hasNext()) {
				if (!noFollowSet.contains(liter.next())) {
					liter.previous();
					break;
				}
				else
					liter.remove();
			}
		}
	}
	
}

import junit.framework.TestCase;

import org.junit.Test;


public class CrackerTest extends TestCase {

	@Test
	public void testGenenrateSHA() {
		assertEquals(Cracker.genenrateSHA("a"), "86f7e437faa5a7fce15d1ddcb9eaeaea377667b8");
		assertEquals(Cracker.genenrateSHA("fm"), "adeb6f2a18fe33af368d91b09587b68e3abcb9a7");
		assertEquals(Cracker.genenrateSHA("a!"), "34800e15707fae815d7c90d49de44aca97e2d759");
		assertEquals(Cracker.genenrateSHA("xyz"), "66b27417d37e024c46526c2f6d358a754fc552f3");
	}

}

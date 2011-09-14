import java.sql.SQLException;
import junit.framework.TestCase;

public class MetropolisTableModelTest extends TestCase{

	MetropolisTableModel model;
	protected void setUp() throws Exception {
		model = new MetropolisTableModel();
	}
	
	public void testSearch() throws SQLException {

		model.search("", "", 20400000);
		assertEquals(model.getRowCount(), 1);
	}
	
	public void testSearchMore() throws SQLException {
		model.search("", "North America", -1);
		assertEquals(model.getRowCount(), 3);
	}
	
	public void testSearchLast() throws SQLException {
		model.search("Melbourne", "", -1);
		assertEquals(model.getRowCount(), 1);
	}
}

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.*;

public class MetropolisTableModel extends AbstractTableModel {

	private static String account = "cs108";
	private static String password = "123456";
	private static String server = "localhost";
	private static String database = "c_cs108"; 
	private Connection con;
	private Statement stmt;
	private PreparedStatement pstmt;
	
	private List<String> colNames;	// defines the number of cols
	private List<List> data;	// one List for each row
	
	public MetropolisTableModel() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://" + server, account ,password);
		stmt = con.createStatement();
		stmt.executeQuery("USE " + database);
		colNames = new ArrayList<String>();
		data = new ArrayList<List>();
	}
	
	@Override
	public String getColumnName(int col) {
		return colNames.get(col);
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return colNames.size();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		List rowList = data.get(row);
		Object result = null;
		if (col<rowList.size()) {
			result = rowList.get(col);
		}
		
		return result;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	// Adds the given row, returns the new row index
	public int addRow(List row) {
		data.add(row);
		fireTableStructureChanged();
		//fireTableRowsInserted(data.size()-1, data.size()-1);
		return(data.size() -1);
	}
	
	public int addRow(String mepolis, String continent, int popul) {
		if (mepolis.isEmpty()||continent.isEmpty()||popul<0) {
			return -1;
		}
		try {
			PreparedStatement statement = con.prepareStatement(
			    "insert into metropolises " + 
			    " (metropolis, continent, population)" + 
			    "values" + 
			    "  (?, ?, ?);");
			statement.setString(1, mepolis);
			statement.setString(2, continent);
			statement.setInt(3, popul);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List row = new ArrayList();
		row.add(mepolis);
		row.add(continent);
		row.add(new Integer(popul));
		return addRow(row);
	}
	/**
	 * do the search in database by criterion
	 * @param mepolis
	 * @param continent
	 * @param popul
	 * @throws SQLException
	 */
	public void search(String mepolis, String continent, int popul) throws SQLException {
		// clear the old data.
		data.clear();
		StringBuilder queryString = new StringBuilder();
		queryString.append("Select * from metropolises");
		if (!mepolis.isEmpty() || !continent.isEmpty() || popul > 0) {
			queryString.append(" where ");
		}
		
		if (!mepolis.isEmpty()) {
			queryString.append(" metropolis= '"+mepolis+"'");
		}
		
		if (!continent.isEmpty()) {
			if(!mepolis.isEmpty())
				queryString.append(" and continent= '"+continent+"'");
			else 
				queryString.append(" continent= '"+continent+"'");
		}
		
		if (popul >= 0) {
			if (!mepolis.isEmpty() || !continent.isEmpty()) {
				queryString.append(String.format(" and population=%d", popul));
			} else {
				queryString.append(String.format(" population=%d", popul));
			}
		}
		
		ResultSet rs = stmt.executeQuery(queryString.toString());
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int colCount = rsMetaData.getColumnCount();
		// clear column name.
		colNames.clear();
		for (int i = 1; i <= colCount; i++) {
			colNames.add(rsMetaData.getColumnName(i));
		}
		while (rs.next()) {
			List row = new ArrayList();
			for (int i = 1; i <= colCount; i++)
				row.add(rs.getObject(i));
			addRow(row);
		}

	}
}

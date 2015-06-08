package com.renhe.dbimport;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBMeta {
	
	public List<String> getTableNameByCon(Connection con) throws SQLException{
		List<String> result = new ArrayList<String>();
		DatabaseMetaData meta = con.getMetaData();  
		ResultSet rs = meta.getTables("m2f", null, "H/_%",new String[] { "TABLE" });  
		while (rs.next()) {  
			result.add(rs.getString(3));
			System.out.println("表名：" + rs.getString(3));  
			System.out.println("表所属用户名：" + rs.getString(2));
			System.out.println("------------------------------");  
		}
		return result;
	}  

	public List<Column> getColumnByTable(Connection con, String Table,String Owner) throws SQLException {
        List<Column> columns = new ArrayList<Column>();
    
        Statement stmt = con.createStatement();
        StringBuilder builder = new StringBuilder();
        builder.append("select");
        builder.append("        comments as \"Name\",");
        builder.append("         a.column_name as \"Code\",");
        builder.append("         a.DATA_TYPE as \"DataType\",");
        builder.append("         a.DATA_LENGTH as \"DataLength\",");
        builder.append("         b.comments as \"Comment\",");
        builder.append("         decode(c.column_name,null,'FALSE','TRUE') as \"Primary\",");
        builder.append("        decode(a.NULLABLE,'N','NO','Y','YES','') as \"Mandatory\",");
        builder.append("        ' ' as \"sequence\"");
        builder.append("   from ");
        builder.append("       all_tab_columns a, ");
        builder.append("       all_col_comments b,");
        builder.append("       (");
        builder.append("        select a.constraint_name, a.column_name");
        builder.append("          from user_cons_columns a, user_constraints b");
        builder.append("         where a.constraint_name = b.constraint_name");
        builder.append("               and b.constraint_type = 'P'");
        builder.append("               and a.table_name = '"+Table+"'");
        builder.append("       ) c");
        builder.append("   where ");
        builder.append("     a.Table_Name=b.table_Name ");
        builder.append("     and a.column_name=b.column_name");
        builder.append("     and a.Table_Name='"+Table+"'");
        builder.append("     and a.owner=b.owner ");
        builder.append("     and a.owner='"+Owner+"'");
        builder.append("     and a.COLUMN_NAME = c.column_name(+)");
        builder.append("  order by a.COLUMN_ID");
        
        System.out.println(builder.toString());
        ResultSet rs = stmt.executeQuery(builder.toString());
        while (rs.next()){
        	Column column = new Column();
        	column.setName(rs.getString("Name"));
        	column.setCode(rs.getString("Code"));
        	column.setDataType(rs.getString("DataType"));
        	column.setDataLength(rs.getInt("dataLength"));
        	column.setComment(rs.getString("Comment"));
        	column.setPrimary(rs.getString("Primary"));
        	column.setMandatory(rs.getString("Mandatory"));
            columns.add(column);
        }
        
        return columns;
    }
	
	public List<Table> getAllTableInfo() {
		List<Table> result = new ArrayList<Table>();
		
		Connection conn = JDBCUtil.openConnection();
		try {
			List<String> tableNames = getTableNameByCon(conn);
			for (String table : tableNames) {
				Table t = new Table();
				t.setName(table);
				List<Column> columns = getColumnByTable(conn,table, "M2F");
				t.setColumns(columns);
				
				result.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
		return result;
	}
}

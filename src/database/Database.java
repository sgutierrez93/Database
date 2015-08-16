package database;

import java.sql.Connection;
//import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Sergio Gutierrez
 */
public class Database{
    public Database(DBMS dbms, String database, String ip, String user, String password) throws SQLException{
        if(dbms == null)
            System.exit(1);
        
        Pattern pat = Pattern.compile("^(([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]).){3}([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
        
        if(ip == null || ip.length() == 0)
            ip = "127.0.0.1";
        else if(!pat.matcher(ip).matches())
            System.exit(1);
        
        String clase = new String();
        String dsn = new String();
        
        try{
            if(dbms == DBMS.MySQL){
                clase = "com.mysql.jdbc.Driver";
                dsn = "jdbc:mysql://" + ip + "/" + database;
            }else if(dbms == DBMS.Oracle){
                clase = "oracle.jdbc.driver.OracleDriver";
                dsn = "jdbc:oracle:thin:@" + ip + ":1521:" + database;
            }else if(dbms == DBMS.SqlServer){
                clase = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                dsn = "jdbc:sqlserver://" + ip + ":1433;databaseName=" + database;
            }
            
            Class.forName(clase);
            
            conexion = DriverManager.getConnection(dsn, user, password);
            sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //dbmd = conexion.getMetaData();
        }catch(ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "DB: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    public void closeDatabase(){
        try{
            if(sentencia != null)
                sentencia.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "DB: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        try{
            if(conexion != null)
                conexion.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "DB: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void closeQuery(){
        try{
            if(sql != null)
                sql.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "DB: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public int executeNonQuery(String query) throws SQLException{
        return sentencia.executeUpdate(query);
    }
    
    public ResultSet executeReader(String query) throws SQLException{
        return sql = sentencia.executeQuery(query);
    }
    
    public Connection getConexion(){
        return conexion;
    }
    
    /*public ResultSet getTables(String table) throws SQLException{
        return sql = dbmd.getTables(null, null, table, null);
    }*/
    
    //private DatabaseMetaData dbmd;
    private Connection conexion;
    private Statement sentencia;
    private ResultSet sql;
}
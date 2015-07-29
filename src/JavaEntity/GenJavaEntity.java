package JavaEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date; 




public class GenJavaEntity {

	 private String packageOutPath = "com.itaas.entity";// ָ��ʵ���������ڰ���·��  
	    private String authorName = "Auto generated,Not allowed modification";// ��������  
	    
	    private String tablename = "User";  
	  
	    private String[] colnames; // ��������  
	  
	    private String[] colTypes; // ������������  
	  
	    private int[] colSizes; // ������С����  
	  
	    private boolean f_util = false; // �Ƿ���Ҫ�����java.util.*  
	  
	    private boolean f_sql = false; // �Ƿ���Ҫ�����java.sql.*  
	  
	    public GenJavaEntity() throws Exception {  
	    	 //����SQLite��JDBC

	         Class.forName("org.sqlite.JDBC");
	         
	         //����һ�����ݿ���zieckey.db�����ӣ���������ھ��ڵ�ǰĿ¼�´���֮
	       //  String fileName = "C:/Users/huanglan.it/Documents/JNFTest";
	        // String fileName = "F:/EclipseWorkSpace/GenEntityTool/JNFTest.db";
	        // String fileName="F:/JNFTest11.db";
	        // Connection conn = DriverManager.getConnection("jdbc:sqlite:JNFTest.db");
	         Connection conn =null;
	         try {  
	        	 conn= DriverManager.getConnection("jdbc:sqlite:JNFTest.db");//DriverManager.getConnection("jdbc:sqlite:"+fileName);
	         
	         Statement stat = conn.createStatement();
	         String strsql = "select * from " + tablename; 
	       
	        	/* stat.executeUpdate( "create table tbl1(name varchar(20), salary int);" );//����һ��������

	             
	             stat.executeUpdate( "insert into tbl1 values('ZhangSan',8000);" ); //��������
	             
	             ResultSet rs = stat.executeQuery("select * from tbl1;"); //��ѯ���� 

	             while (rs.next()) { //����ѯ�������ݴ�ӡ����

	                 System.out.print("name = " + rs.getString("name") + " "); //������һ

	                 System.out.println("salary = " + rs.getString("salary")); //�����Զ�

	             }
	             rs.close();*/
	           PreparedStatement pstmt = conn.prepareStatement(strsql);  
	            ResultSetMetaData rsmd = pstmt.getMetaData();  
	            int size = rsmd.getColumnCount(); // ���ж�����  
	            colnames = new String[size];  
	            colTypes = new String[size];  
	            colSizes = new int[size];  
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {  
	                colnames[i] = rsmd.getColumnName(i + 1);  
	                colTypes[i] = rsmd.getColumnTypeName(i + 1);  
	                if (colTypes[i].equalsIgnoreCase("datetime")) {  
	                    f_util = true;  
	                }  
	                if (colTypes[i].equalsIgnoreCase("image")  
	                        || colTypes[i].equalsIgnoreCase("text")) {  
	                    f_sql = true;  
	                }  
	                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);  
	            }  
	            String content = parse(colnames, colTypes, colSizes);  
	            try {  
	                FileWriter fw = new FileWriter(initcap(tablename) + ".java");  
	                PrintWriter pw = new PrintWriter(fw);  
	                pw.println(content);  
	                pw.flush();  
	                pw.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	        } finally {  
	        	  conn.close();  
	        }  
	    }  
	  
	    /** 
	    * ��������(����ʵ�����������) 
	    */  
	    private String parse(String[] colNames, String[] colTypes, int[] colSizes) {  
	        StringBuffer sb = new StringBuffer();  
	        if (f_util) {  
	            sb.append("import java.util.Date;\r\n");  
	        }  
	        if (f_sql) {  
	            sb.append("import java.sql.*;\r\n\r\n\r\n");  
	        } 
	        sb.append("package " + this.packageOutPath + ";\r\n");    
            sb.append("\r\n");     
            //ע�Ͳ���    
            sb.append("/**\r\n");    
            sb.append(" * "+tablename+" ʵ����\r\n");             
            sb.append(" * "+new Date()+" "+this.authorName+"\r\n");    
            sb.append("*/ \r\n");   
	        sb.append("public class " + initcap(tablename) + " {\r\n");  
	        processAllAttrs(sb);  
	        processAllMethod(sb);  
	        sb.append("}\r\n");  
	        System.out.println(sb.toString());  
	        return sb.toString();  
	  
	    }  
	  
	    /** 
	    * �������еķ��� 
	    *  
	    * @param sb 
	    */  
	    private void processAllMethod(StringBuffer sb) {  
	        for (int i = 0; i < colnames.length; i++) {  
	            sb.append("\tpublic void set" + initcap(colnames[i]) + "("  
	                    + sqlType2JavaType(colTypes[i]) + " " + colnames[i]  
	                    + "){\r\n");  
	            sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\r\n");  
	            sb.append("\t}\r\n");  
	  
	            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get"  
	                    + initcap(colnames[i]) + "(){\r\n");  
	            sb.append("\t\treturn " + colnames[i] + ";\r\n");  
	            sb.append("\t}\r\n");  
	        }  
	    }  
	  
	    /** 
	    * ����������� 
	    *  
	    * @return 
	    */  
	    private void processAllAttrs(StringBuffer sb) {  
	        for (int i = 0; i < colnames.length; i++) {  
	            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " "  
	                    + colnames[i] + ";\r\n");  
	  
	        }  
	    }  
	  
	    /** 
	    * �������ַ���������ĸ�ĳɴ�д 
	    *  
	    * @param str 
	    * @return 
	    */  
	    private String initcap(String str) {  
	        char[] ch = str.toCharArray();  
	        if (ch[0] >= 'a' && ch[0] <= 'z') {  
	            ch[0] = (char)(ch[0]-32); 
	        }  
	        return new String(ch);  
	    }  
	  
	    private String sqlType2JavaType(String sqlType) {  
	        if (sqlType.equalsIgnoreCase("bit")) {  
	            return "bool";  
	        } else if (sqlType.equalsIgnoreCase("tinyint")) {  
	            return "byte";  
	        } else if (sqlType.equalsIgnoreCase("smallint")) {  
	            return "short";  
	        } else if (sqlType.equalsIgnoreCase("int")) {  
	            return "int";  
	        } else if (sqlType.equalsIgnoreCase("bigint")) {  
	            return "long";  
	        } else if (sqlType.equalsIgnoreCase("float")) {  
	            return "float";  
	        } else if (sqlType.equalsIgnoreCase("decimal")  
	                || sqlType.equalsIgnoreCase("numeric")  
	                || sqlType.equalsIgnoreCase("real")) {  
	            return "double";  
	        } else if (sqlType.equalsIgnoreCase("money")  
	                || sqlType.equalsIgnoreCase("smallmoney")) {  
	            return "double";  
	        } else if (sqlType.equalsIgnoreCase("varchar")  
	                || sqlType.equalsIgnoreCase("char")  
	                || sqlType.equalsIgnoreCase("nvarchar")  
	                || sqlType.equalsIgnoreCase("nchar")
	                ||sqlType.toLowerCase().indexOf("char")>-1) {  
	            return "String";  
	        } else if (sqlType.equalsIgnoreCase("datetime")) {  
	            return "Date";  
	        }  
	  
	        else if (sqlType.equalsIgnoreCase("image")) {  
	            return "Blob";  
	        } else if (sqlType.equalsIgnoreCase("text")) {  
	            return "Clob";  
	        }  
	        return null;  
	    }  
}
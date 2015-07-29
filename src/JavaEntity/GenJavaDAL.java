package JavaEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class GenJavaDAL {
	 private String packageOutPath = "com.itaas.dal";// ָ��ʵ���������ڰ���·��  
	    private String authorName = "Auto generated,Not allowed modification";// ��������  
	    
	    private String tablename = "User";  
	  
	    private String[] colnames; // ��������  
	  
	    private String[] colTypes; // ������������  
	  
	    private int[] colSizes; // ������С����  
	  
	    private boolean f_util = false; // �Ƿ���Ҫ�����java.util.*  
	  
	    private boolean f_sql = false; // �Ƿ���Ҫ�����java.sql.*  
	  
	    public GenJavaDAL() throws Exception {  
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
	        	 
	        	 /* stat.executeUpdate( "create table tbl1(name varchar(20), salary int);" );//����һ��������

	             
	             stat.executeUpdate( "insert into tbl1 values('ZhangSan',8000);" ); //��������
	             
	             ResultSet rs = stat.executeQuery("select * from tbl1;"); //��ѯ���� 

	             while (rs.next()) { //����ѯ�������ݴ�ӡ����

	                 System.out.print("name = " + rs.getString("name") + " "); //������һ

	                 System.out.println("salary = " + rs.getString("salary")); //�����Զ�

	             }
	             rs.close();*/
	         Statement stat = conn.createStatement();
	         String strsql = "select * from " + tablename; 
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
	                FileWriter fw = new FileWriter(initcap(tablename) + "DAL.java");  
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
	            sb.append(" * "+tablename+" ʵ�������\r\n");             
	            sb.append(" * "+new Date()+" "+this.authorName+"\r\n");    
	            sb.append("*/ \r\n");   
		        sb.append("public class " + initcap(tablename) + "DAL {\r\n");  
		        createInsertMethod(sb);  
		        createUpdateMethod(sb); 
		        createSelectMethod(sb);
		        sb.append("}\r\n");  
		        System.out.println(sb.toString());  
		        return sb.toString();  
		  
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
			    

	    private void createInsertMethod(StringBuffer sb)
	    {
	    	  //ע�Ͳ���    
            sb.append("\t/**\r\n");    
            sb.append("\t * "+tablename+" Insert ����\r\n");   
            sb.append("\t*/ \r\n");   
	    	String entityName=tablename.toLowerCase()+"Entity";
	    	sb.append("\tpublic void Insert"+tablename+"("+initcap(tablename)+" "+entityName+"){\r\n");
	    	sb.append("\t\tStringBuffer sqlSb=new StringBuffer();\r\n");
	    	sb.append("\t\tsqlSb.append(\""+"insert into "+tablename+" values(\")\r\n");
	    	for(int i=0;i<colnames.length;i++)
	    	{
	    		String value=entityName+".get"+colnames[i]+"()";
	    	//	String sqlType=colTypes[i];
	    		if(i==colnames.length-1)
	    		{
	    			sb.append("\t\tsqlSb.append(\"'\"+"+value+"+\"'\");\r\n");
	    		}
	    		else{
	    		//	if(sqlType.toLowerCase().indexOf("char")>-1)||sqlType.equalsIgnoreCase("datetime"))
	    			sb.append("\t\tsqlSb.append(\"'\"+"+value+"+\"',\");\r\n");
	    		}
	    	}
	    	sb.append("\t\tsqlSb.append(\"\\\";\");\r\n");
	    	sb.append("\t\tString sql=sqlSb.toString();\r\n");
	    	sb.append("\t}\r\n");
	    	
	    }
	    
	    private void createUpdateMethod(StringBuffer sb)
	    {
	    	  //ע�Ͳ���    
	    	sb.append("\r\n");
            sb.append("\t/**\r\n");    
            sb.append("\t * "+tablename+" Update ����\r\n");   
            sb.append("\t*/ \r\n");   
	    	String entityName=tablename.toLowerCase()+"Entity";
	    	sb.append("\tpublic void Update"+tablename+"("+initcap(tablename)+" "+entityName+"){\r\n");
	    	sb.append("\t\tStringBuffer sqlSb=new StringBuffer();\r\n");
	    	sb.append("\t\tsqlSb.append(\""+"update "+tablename+" set \")\r\n");
	    	String value;
	    	for(int i=0;i<colnames.length;i++)
	    	{
	    		value=entityName+".get"+colnames[i]+"()";
	    	//	String sqlType=colTypes[i];
	
	    		//	if(sqlType.toLowerCase().indexOf("char")>-1)||sqlType.equalsIgnoreCase("datetime"))
	    			sb.append("\t\tsqlSb.append(\""+colnames[i]+"='\"+"+value+"+\"',\");\r\n");
	    	}
	    	value=entityName+".get"+colnames[0]+"()";
	    	sb.append("\t\tsqlSb.append(\"where "+colnames[0]+"='\"+"+value+"+\"'\");\r\n");
	    	sb.append("\t\tString sql=sqlSb.toString();\r\n");
	    	sb.append("\t}\r\n");
	    	
	    }
	    
	    private void createSelectMethod(StringBuffer sb)
	    {
	    	  //ע�Ͳ���    
	    	sb.append("\r\n");
            sb.append("\t/**\r\n");    
            sb.append("\t * "+tablename+" Select ����\r\n");   
            sb.append("\t*/ \r\n");   
            String entityCls=initcap(tablename);
	    	String entityName=tablename.toLowerCase()+"Entity";
	    	sb.append("\tpublic "+entityCls+" Get"+tablename+"ByKeyId(String "+colnames[0]+"){\r\n");
	    	sb.append("\t\tString sql=\"select * from "+tablename+" where "+colnames[0]+"='\"+"+colnames[0]+"+\"'\";\r\n");
	    	sb.append("\t\tCursor c = null;//todo ��ѯ����\r\n");
	    	String entityObj=tablename+"Entity";
	    	sb.append("\t\t"+entityCls+" "+entityObj+"=null;\r\n");
	    	sb.append("\t\tif(c.moveToNext()){\r\n");
	    	sb.append("\t\t\t"+entityObj+"=new "+entityCls+"();\r\n");
	    	String value;
	    	for(int i=0;i<colnames.length;i++)
	    	{
	    		value=entityName+".get"+colnames[i]+"()";
	    		sb.append("\t\t\t"+entityObj+".set"+ initcap(colnames[i])+"(c."+sqlType2GetJavaType(colTypes[i])+"(c.getColumnIndex(\""+colnames[i]+"\")));\r\n");
	    		
	    	}
	    	sb.append("\t\t}\r\n");
	    	sb.append("\t\tc.close();\r\n");
	    	sb.append("\t\treturn "+entityObj+";\r\n");
	    	sb.append("\t}\r\n");
	    	
	    }
	    
	    private String sqlType2GetJavaType(String sqlType) {  
	        if (sqlType.equalsIgnoreCase("bit")) {  
	            return "getString";  
	        } else if (sqlType.equalsIgnoreCase("tinyint")) {  
	            return "getShort";  
	        } else if (sqlType.equalsIgnoreCase("smallint")) {  
	            return "getShort";  
	        } else if (sqlType.equalsIgnoreCase("int")) {  
	            return "getInt";  
	        } else if (sqlType.equalsIgnoreCase("bigint")) {  
	            return "getLong";  
	        } else if (sqlType.equalsIgnoreCase("float")) {  
	            return "getFloat";  
	        } else if (sqlType.equalsIgnoreCase("decimal")  
	                || sqlType.equalsIgnoreCase("numeric")  
	                || sqlType.equalsIgnoreCase("real")) {  
	            return "getDouble";  
	        } else if (sqlType.equalsIgnoreCase("money")  
	                || sqlType.equalsIgnoreCase("smallmoney")) {  
	            return "getDouble";  
	        } else if (sqlType.equalsIgnoreCase("varchar")  
	                || sqlType.equalsIgnoreCase("char")  
	                || sqlType.equalsIgnoreCase("nvarchar")  
	                || sqlType.equalsIgnoreCase("nchar")
	                ||sqlType.toLowerCase().indexOf("char")>-1) {  
	            return "getString";  
	        } else if (sqlType.equalsIgnoreCase("datetime")) {  
	            return "getString";  
	        }  
	  
	        else if (sqlType.equalsIgnoreCase("image")) {  
	            return "getBlob";  
	        } else if (sqlType.equalsIgnoreCase("text")) {  
	            return "getString";  
	        }  
	        return null;  
	    } 
	  
}

//import java.io.Writer;import java.io.OutputStream;import java.io.StringWriter;import java.io.File;import java.io.OutputStreamWriter;import java.io.PrintWriter;import java.lang.reflect.Method;import java.util.Collection;import java.util.Enumeration;
import java.io.IOException;
import java.lang.reflect.Field;

import java.net.URL;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

import java.util.HashMap;
import java.util.LinkedList;
import java.sql.Connection			;
import java.sql.Statement			;
import java.sql.PreparedStatement	;
import java.sql.ResultSet			;
import java.sql.ResultSetMetaData	;
import java.sql.SQLException		;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class DB {
enum context{
	pool("MysqlConnectionPoolDataSource")
	,reqCon("javax.sql.PooledConnection")
	,server("localhost")
	,dbName("props")
	,un("root")
,pw("root")
 ;String str,a[];context(String...p){str=p[0];a=p;}
}//context

	final static String Name="moh";
	final static boolean logOut=false;

	static void log(Object...p){}
	static void error(Throwable x,Object...p){}

	/**returns a jdbc pooled Connection.
		uses MysqlConnectionPoolDataSource with a database from the enum context.url.str,
		sets the pool as an application-scope attribute named context.pool.str
		when first time called, all next calls uses this context.pool.str*/
	public static synchronized Connection c()throws SQLException {
        Connection r=(Connection)Json.m(context.reqCon.str);
        if(r!=null)return r;
        MysqlConnectionPoolDataSource d=(MysqlConnectionPoolDataSource)Json.m(context.pool.str);
        if(d!=null){
            r=d.getPooledConnection().getConnection();
            Json.m(context.reqCon.str,r);
            return r;//if(r!=null)
        }
        else try
        {int x=0;//context.getContextIndex(t);
            if(x!=-1)
            {	r=c(x,x,x,x);//t.log("DB.c:1:c2:",p);
                return r;}
        }catch(Throwable e){error(e,"DB.MysqlConnectionPoolDataSource:throwable:");}//ClassNotFoundException
        
        if(r==null)try
        {r=java.sql.DriverManager.getConnection
            ("jdbc:mysql://"+context.server.str
                +"/"+context.dbName.str
                ,context.un.str,context.pw.str
            );Object[]b={r,null};
            Json.m(context.reqCon.str,b);
        }catch(Throwable e){error(e,"DB.DriverManager:");}
        return r;}
	
	public static synchronized Connection c(int idb,int iun,int ipw,int isr) throws SQLException{
		MysqlConnectionPoolDataSource d=new MysqlConnectionPoolDataSource();
		String ss=null,s=context.dbName.a[Math.min(context.dbName.a.length-1,idb)];
		//if(logOut)ss="\ndb:"+s;
		d.setDatabaseName(s);d.setPort(3306);
		s=context.server.a[Math.min(context.server.a.length-1,isr)];
		//if(logOut)ss+="\nsrvr:"+s;
		d.setServerName(s);
		s=context.un.a[Math.min(context.un.a.length-1,iun)];if(logOut)ss+="user:"+s;
		d.setUser(s);
		s=context.pw.a[Math.min(context.pw.a.length-1,ipw)];if(logOut)ss+="\npw:"+s;
		d.setPassword(s);
		Connection r=d.getPooledConnection().getConnection();
		Json.m(context.pool.str,d);//t.h.a(context.pool.str,d);
		Json.m(context.reqCon.str,r);//Object[]a={d,r,ss};//,b={r,null};t.s(context.reqCon.str,b);
		//stack(t,r);
		return r;}

	/**returns a jdbc-PreparedStatement, setting the variable-length-arguments parameters-p, calls dbP()*/
	public static PreparedStatement p( String sql, Object...p)throws SQLException{return P(sql,p);}

	/**returns a jdbc-PreparedStatement, setting the values array-parameters-p, calls TL.dbc() and log()*/
	public static PreparedStatement P(String sql,Object[]p)throws SQLException{return P(sql,p,true);}

	public static PreparedStatement P(String sql,Object[]p,boolean odd)throws SQLException {
		Connection c=c();//TL t=tl();
		PreparedStatement r=c.prepareStatement(sql);
		//if(logOut)	t.log(Name,"("+t+").DB.P(sql="+sql+",p="+p+",odd="+odd+")");
		if(odd){if(p.length==1)
			r.setObject(1,p[0]);else
			for(int i=1,n=p.length;p!=null&&i<n;i+=2)if((!(p[i] instanceof List)) ) // ||!(p[i-1] instanceof List)||((List)p[i-1]).size()!=2||((List)p[i-1]).get(1)!=Tbl.Co.in )
				r.setObject(i/2+1,p[i]);//if(t.logOut)TL.log("dbP:"+i+":"+p[i]);
		}else
			for(int i=0;p!=null&&i<p.length;i++)
			{r.setObject(i+1,p[i]);}//if(logOut)t.log("dbP:"+i+":"+p[i]);
		return r;}//if(logOut)t.log("dbP:sql="+sql+":n="+(p==null?-1:p.length)+":"+r);

	/**returns a jdbc-ResultSet, setting the variable-length-arguments parameters-p, calls dbP()*/
	public static ResultSet r( String sql, Object...p)throws SQLException{return R(sql,p);}//changed 2017.7.17

	/**returns a jdbc-ResultSet, setting the values array-parameters-p, calls dbP()*/
	public static ResultSet R(String sql,Object[]p)throws SQLException{
		PreparedStatement x=P(sql,p,true);
		ResultSet r=x.executeQuery();
		//push(r,tl());
		return r;}

	public static void close(ResultSet r){close(r,false);}

	public static void close(ResultSet r,boolean closeC){
		if(r!=null)try{
			Statement s=r.getStatement();
			Connection c=closeC?s.getConnection():null;
			r.close();s.close();
			if(closeC)c.close();
		}catch(Exception e){e.printStackTrace();}
	}

	/**returns a string or null, which is the result of executing sql,
	calls dpR() to set the variable-length-arguments parameters-p*/
	public static String q1str(String sql,Object...p)throws SQLException{return q1Str(sql,p);}

	public static String q1Str(String sql,Object[]p)throws SQLException
	{String r=null;ResultSet s=null;try{s=R(sql,p);r=s.next()?s.getString(1):null;}finally{close(s,true);}return r;}//CHANGED:2015.10.23.16.06:closeRS ; CHANGED:2011.01.24.04.07 ADDED close(s,dbc());

	public static String newUuid()throws SQLException{return q1str("select uuid();");}

	/**returns an java obj, which the result of executing sql,
	calls dpR() to set the variable-length-arguments parameters-p*/
	public static Object q1obj(String sql,Object...p)throws SQLException{return q1Obj(sql,p);}

	public static Object q1Obj(String sql,Object[]p)throws SQLException {
		ResultSet s=null;try{s=R(sql,p);return s.next()?s.getObject(1):null;}finally{close(s,true);}

	/*public static <T>T q1(String sql,Class<T>t,Object[]p)throws SQLException {
		ResultSet s=null;
		try{s=R(sql,p);
			T x=null;
			if(s.next())
				x=(T)s.getObject(1,t);
			return x;
		}finally{
			close(s,true);}
	}*/}

	/**returns an integer or df, which the result of executing sql,
	calls dpR() to set the variable-length-arguments parameters-p*/
	public static int q1int(String sql,int df,Object...p)throws SQLException{return q1Int(sql,df,p);}

	public static int q1Int(String sql,int df,Object[]p)throws SQLException
	{ResultSet s=null;try{s=R(sql,p);return s.next()?s.getInt(1):df;}finally{close(s,true);}}//CHANGED:2015.10.23.16.06:closeRS ;

	/**returns a double or df, which is the result of executing sql,
	calls dpR() to set the variable-length-arguments parameters-p*/
	public static double q1dbl(String sql,double df,Object...p)throws SQLException{
	 ResultSet s=null;try{s=R(sql,p);return s.next()?s.getDouble(1):df;}finally{close(s,true);}}//CHANGED:2015.10.23.16.06:closeRS ;

	/**returns as an array of rows of arrays of columns of values of the results of the sql
	, calls dbL() setting the variable-length-arguments values parameters-p*/
	public static Object[][]q(String sql,Object...p)throws SQLException{return Q(sql,p);}

	public static Object[][]Q(String sql,Object[]p)throws SQLException{
		List<Object[]>r=L(sql,p);Object b[][]=new Object[r.size()][];r.toArray(b);r.clear();return b;}

	/**return s.getMetaData().getColumnCount();*/
	public static int cc(ResultSet s)throws SQLException{return s.getMetaData().getColumnCount();}

	/**calls L()*/
	public static List<Object[]> l(String sql,Object...p)throws SQLException{return L(sql,p);}


	/**returns a new linkedList of the rows of the results of the sql
	,each row/element is an Object[] of the columns
	,calls dbR() and dbcc() and dbclose(ResultSet,TL.dbc())*/
	public static List<Object[]> L(String sql,Object[]p)throws SQLException {
		ResultSet s=null;List<Object[]> r=null;try{s=R(sql,p);Object[]a;r=new LinkedList<Object[]>();
			int cc=cc(s);while(s.next()){r.add(a=new Object[cc]);
				for(int i=0;i<cc;i++){a[i]=s.getObject(i+1);
				}}return r;}finally{
                    close(s,true);//CHANGED:2015.10.23.16.06:closeRS ;
			//if(logOut)try{t.log(t.jo().w(Name).w(".DB.L:sql=").o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){error(x,"DB.List:",sql);}
        }}

	public static List<Integer[]>qLInt(String sql,Object...p)throws SQLException{return qLInt(sql,p);}//2017.07.14

public static List<Integer[]>QLInt(String sql,Object[]p)throws SQLException{//2017.07.14
		ResultSet s=null;//TL tl=tl();
		List< Integer[]> r=null;
		try{s=R(sql,p);
			Integer[]a;
			r=new LinkedList<Integer[]>();
			int cc=cc(s);
			while(s.next()){
				r.add(a=new Integer[cc]);
				for(int i=0;i<cc;i++)
					a[i]=s.getInt(i+1);
			}return r;
		}finally
		{close(s,true);
			//if(tl.h.logOut)try{tl.log(tl.jo().w(Name).w(".DB.Lt:sql=").o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){tl.error(x,Name,".DB.Lt:",sql);}
		}
	}

	public static List<Object> q1colList(String sql,Object...p)throws SQLException{
	 ResultSet s=null;List<Object> r=null;try{s=R(sql,p);r=new LinkedList<Object>();
		while(s.next())r.add(s.getObject(1));return r;}
		finally{close(s,true);
			//TL t=tl();if(logOut)try{t.log(t.jo().w(Name).w(".DB.q1colList:sql=")//CHANGED:2015.10.23.16.06:closeRS ;
			//.o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){t.error(x,Name,".DB.q1colList:",sql);}
		}


/*	public static <T>List<T> q1colTList(String sql,Class<T>t,Object...p)throws SQLException
	{ResultSet s=null;List<T> r=null;try{s=R(sql,p);r=new LinkedList<T>();//Class<T>t=null;
		while(s.next())r.add(
			s.getObject(1,t)
			//s.getObject(1)
		);return r;}
        finally{close(s,true //tl
            );//TL tl=tl();if(tl.h.logOut)try{tl.log(tl.jo().w(Name).w(".DB.q1colList:sql=")//CHANGED:2015.10.23.16.06:closeRS ;
		//.o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){tl.error(x,Name,".DB.q1colList:",sql);}
	}}
    public static <T>T[] q1colT(String sql,Class<T>t,Object...p)throws SQLException
	{List<T> l=q1colTList(sql,t,p);T[]r=(T[]) java.lang.reflect.Array.newInstance(t,l.size());l.toArray(r);l.clear();return r;}
*/
}

	public static Object[] q1col(String sql,Object...p)throws SQLException{
		List<Object> l=q1colList(sql,p);Object r[]=new Object[l.size()];l.toArray(r);l.clear();return r;}


    /**returns a row of columns of the result of sql
	,calls dbR(),dbcc(),and dbclose(ResultSet,TL.dbc())*/
	public static Object[] q1row(String sql,Object...p)throws SQLException{return q1Row(sql,p);}

	public static Object[] q1Row(String sql,Object[]p)throws SQLException {
		ResultSet s = null;
		try {
			s = R( sql, p );
			Object[] a = null;
			int cc = cc( s );
			if ( s.next() ) {
				a = new Object[ cc ];
				for ( int i = 0; i < cc; i++ )
					try {
						a[ i ] = s.getObject( i + 1 );
					} catch ( Exception ex ) {
						error( ex, Name, ".DB.q1Row:", sql );
						a[ i ] = s.getString( i + 1 );
					}
			}
			return a;
		} finally {
			close( s, true );
		}//CHANGED:2015.10.23.16.06:closeRS ;
	}

    /**returns the result of (e.g. insert/update/delete) sql-statement
	,calls dbP() setting the variable-length-arguments values parameters-p
	,closes the preparedStatement*/
	public static int x(String sql,Object...p)throws SQLException{return X(sql,p);}

	public static int X(String sql,Object[]p)throws SQLException {
		int r=-1;Connection c=null;PreparedStatement s=null;try{
			s=P(sql,p,false);r=s.executeUpdate();c=s.getConnection();return r;}
		finally{s.close();c.close();
			//TL t=tl();if(logOut)try{t.log(t.jo().w(Name).w(".DB.x:sql=").o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){t.error(x,Name,".DB.X:",sql);}
        }}
        
	/**output to Json.jo() the Json.Output.oRS() of the query*/
	public static void q2json(String sql,Object...p)throws SQLException{
		ResultSet s=null;//TL tl=tl();
		try{
			s=R(sql,p);
			try{
				Json.jo().o(s);//tl.getOut() .o(s); // (new Json.Output()) // TODO:investigate where the Json.Output.w goes
			}catch (IOException e) {e.printStackTrace();}
		}
		finally
		{close(s,true);
			//if(tl.h.logOut)try{tl.log(tl.jo().w(Name).w(".DB.L:q2json=").o(sql).w(",prms=").o(p).toStrin_());}catch(IOException x){tl.error(x,Name,".DB.q1json:",sql);}
		}
    }

	/**return a list of maps , each map has as a key a string the name of the column, and value obj*/
	//static List<Map<String,Object>>json(String sql,Object...p) throws SQLException{return Lst(sql,p);}
	static List<Map<String,Object>>Lst(String sql,Object[ ]p) throws SQLException{
		List<Map<String,Object>>l=new LinkedList< Map < String ,Object>>();
		ItTbl i=new ItTbl(sql,p);
		List<String>cols=new LinkedList<String>();
		for(int j=1;j<=i.row.cc;j++)cols.add(i.row.m.getColumnLabel(j));
		for(ItTbl.ItRow w:i){Map<String,Object>m= new HashMap<String,Object>();l.add(m);
			for(Object o:w)m.put(cols.get(w.col-1),o);
        }return l;}

	public static class ItTbl implements Iterator<ItTbl.ItRow>,Iterable<ItTbl.ItRow>{
		public ItRow row=new ItRow();

		public ItRow getRow(){return row;}

		public static ItTbl it(String sql,Object...p){return new ItTbl(sql,p);}

		public ItTbl(String sql,Object[]p){
			try {init(DB.R(sql, p));}
			catch (Exception e) {error(e,".DB.ItTbl.<init>:Exception:sql=",sql,",p=",p);}}

		//public ItTbl(ResultSet o) throws SQLException{init(o);}

		public ItTbl init(ResultSet o) throws SQLException{
			row.rs=o;row.m=o.getMetaData();row.row=row.col=0;
			row.cc=row.m.getColumnCount();return this;}

		static final String ErrorsList="DB.ItTbl.errors";

		@Override public boolean hasNext(){
			boolean b=false;try {if(b=row!=null&&row.rs!=null&&row.rs.next())row.row++;
			else DB.close(row.rs,true);//CHANGED:2015.10.23.16.06:closeRS ; 2017.7.17
			}catch (SQLException e) {//TL t=TL.tl();//changed 2016.06.27 18:05final String str=Name+".DB.ItTbl.next";t.error(e,str);List l=(List)t.json.get(ErrorsList);if(l==null)t.json.put(ErrorsList,l=new LinkedList());l.add(Util.lst(str,row!=null?row.row:-1,e));
				error(e,this);
			}return b;}

		@Override public ItRow next() {if(row!=null)row.col=0;return row;}

		@Override public void remove(){throw new UnsupportedOperationException();}

		@Override public Iterator<ItRow>iterator(){return this;}

		public class ItRow implements Iterator<Object>,Iterable<Object>{
			ResultSet rs;int cc,col,row;ResultSetMetaData m;

			public int getCc(){return cc;}

			public int getCol(){return col;}

			public int getRow(){return row;}

			@Override public Iterator<Object>iterator(){return this;}

			@Override public boolean hasNext(){return col<cc;}

			@Override public Object next(){
				try {return rs==null?null:rs.getObject(++col);}
				catch (SQLException e) {//changed 2016.06.27 18:05
					error(e,"DB.ItTbl.ItRow.next");
					//TL t=TL.tl();final String str=Name+;List l=(List)t.json.get(ErrorsList);if(l==null)t.json.put(ErrorsList,l=new LinkedList());l.add(Util.lst(str,row,col,e));
				}
				return null;}

			@Override public void remove(){throw new UnsupportedOperationException();}

			public int nextInt(){
				try {return rs==null?-1:rs.getInt(++col);}
				catch (SQLException e) {e.printStackTrace();}
				return -1;}

			public String nextStr(){
				try {return rs==null?null:rs.getString(++col);}
				catch (SQLException e) {e.printStackTrace();}
				return null;}

		}//ItRow
	}//ItTbl

	/**represents one entity , one row from a table in a relational database*/
	public abstract static class Tbl {

		@Override public String toString(){return toJson(null);}

		public abstract String getName();
	
		public Json.Output jsonOutput(Json.Output o,String ind,String path)throws java.io.IOException{return jsonOutput( o,ind,path,true );}

		public Json.Output jsonOutput(Json.Output o,String ind,String path,boolean closeBrace)throws java.io.IOException{
			//if(o.comment)o.w("{//TL.Form:").w('\n').p(ind);else//.w(p.getClass().toString())
			o.w('{');
			Field[]a=fields();String i2=ind+'\n';
			o.w("\"class\":").oStr(getClass().getSimpleName(),ind);//w("\"name\":").oStr(p.getName(),ind);
			for(Field f:a)
			{	o.w(',').oStr(f.getName(),i2).w(':')
					.o(v(f),ind,o.comment?path+'.'+f.getName():path);
				if(o.comment)o.w("//").w(f.toString()).w("\n").p(i2);
			}
			if(closeBrace){
				if(o.comment)
					o.w("}//DB.Tbl&cachePath=\"").p(path).w("\"\n").p(ind);
				else o.w('}');}
			return o; }

		public String toJson(Json.Output o){if(o==null);try {jsonOutput(o, "", "");}catch (IOException ex) {}return o.toString();}


		public abstract CI[]columns();//public abstract FI[]flds();

		public Object[]vals(){
			Field[]a=fields();
			Object[]r=new Object[a.length];
			int i=-1;
			for(Field f:a){i++;
				r[i]=v(a[i]);
			}return r;}


		public Tbl vals (Object[]p){
			Field[]a=fields();int i=-1;
			for(Field f:a)
				v(f,p[++i]);
			return this;}

		public Map asMap(){
			return asMap(null);}

		public Map asMap(Map r){
			Field[]a=fields();
			if(r==null)r=new HashMap();
			int i=-1;
			for(Field f:a){i++;
				r.put(f.getName(),v(a[i]));
			}return r;}

		public Tbl fromMap (Map p){
			Field[]a=fields();
			for(Field f:a)
				v(f,p.get(f.getName()));
			return this;}

		public Field[]fields(){return fields(getClass());}

		public static Field[]fields(Class<?> c){
			List<Field>l=fields(c,null);
			int n=l==null?0:l.size();
			Field[]r=new Field[n];
			if(n>0)l.toArray(r);
			return r;}

		public static List<Field>fields(Class<?> c,List<Field>l){
			//this is beautiful(tear running down cheek)
			Class s=c==null?c:c.getSuperclass();
			if(s!=null&&Tbl.class .isAssignableFrom( s))
				l=fields( s,l );
			Field[]a=c.getDeclaredFields();
			if(l==null)l=new LinkedList<Field>();
			for(Field f:a){F i=f.getAnnotation(F.class);
				if(i!=null)l.add(f);}
			return l;}

		public Tbl v(CI p,Object v){return v(p.f(),v);}//this is beautiful(tear running down cheek)

		public Object v(CI p){return v(p.f());}//this is beautiful(tear running down cheek)

		public Tbl v(Field p,Object v){//this is beautiful(tear running down cheek)
			try{Class <?>t=p.getType();
				if(v!=null && !t.isAssignableFrom( v.getClass() ))//t.isEnum()||t.isAssignableFrom(URL.class))
					v=Util.parse(v instanceof String?(String)v:String.valueOf(v),t);
				p.set(this,v);
			}catch (Exception ex) {error(ex,Name,".DB.Tbl.v(",this,",",p,",",v,")");}
			return this;}

		public Object v(Field p){//this is beautiful(tear running down cheek)
			try{return p.get(this);}
			catch (Exception ex) {//IllegalArgumentException,IllegalAccessException
				error(ex,Name,".DB.Tbl.v(",this,",",p,")");return null;}}

		/**Field annotation to designate a java member for use in a Html-Form-field/parameter*/
		@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
		public @interface F{	boolean prmPw() default false;boolean group() default false;boolean max() default false;boolean json() default false; }

		/**Interface for enum-items from different forms and sql-tables ,
			* the enum items represent a reference Column Fields for identifing the column and selection.*/
		public interface CI{public Field f();}//interface I

	//}//public abstract static class Form
	
		/**Sql-Column Interface, for enum -items that represent columns in sql-tables
		* the purpose of creating this interface is to centerlize
		* the definition of the names of columns in java source code*/
		//public interface CI extends FI{}//interface CI//			public String prefix();public String suffix();

		public static CI[]cols(CI...p){return p;}

		public static Object[]where(Object...p){return p;}

		public abstract CI pkc();

		public abstract Object pkv();
		//public abstract CI[]columns();//@Override public CI[]flds(){return columns();}

		public String sql(CI[]cols,Object[]where){
			return sql(cols,where,null,null,getName());}

		public static String sql(CI[]cols,Object[]where,String name){
			return sql( cols, where,null,null,name);}//StringBuilder sql,

		public String sql(CI[]cols,Object[]where,CI[]groupBy){
			return sql(cols,where,groupBy,null,getName());}


		public String sql(String cols,Object[]where,CI[]groupBy,CI[]orderBy) {
			StringBuilder sql=new StringBuilder("select ");
			sql.append(cols);//Co.generate(sql,cols);
			sql.append(" from `").append(getName()).append("` ");
			if(where!=null&&where.length>0)
				DB.Tbl.Co.where(sql, where);
			if(groupBy!=null && groupBy.length>0){
				sql.append(" group by ");
				Co.generate(sql,groupBy);}
			if(orderBy!=null && orderBy.length>0){
				sql.append(" order by ");
				Co.generate(sql,orderBy);}
			return sql.toString();}

		public static String sql(CI[]cols,Object[]where,CI[]groupBy,CI[]orderBy,String dbtName){
			//if(cols==null)cols=columns();
			StringBuilder sql=new StringBuilder("select ");
			Co.generate( sql,cols );//sql.append(cols);
			sql.append(" from `").append(dbtName).append("` ");
			if(where!=null&&where.length>0)
				DB.Tbl.Co.where(sql, where);
			if(groupBy!=null && groupBy.length>0){
				sql.append(" group by ");
				Co.generate(sql,groupBy);}
			if(orderBy!=null && orderBy.length>0){
				sql.append(" order by ");
				Co.generate(sql,orderBy);}
			return sql.toString();}

		/** returns a list of 3 lists,
		* the 1st is a list for the db-table columns-CI
		* the 2nd is a list for the db-table-key-indices
		* the 3rd is a list for row insertion
		*
		* the 1st list, the definition of the column is a string
		* , e.i. varchar(255) not null
		* or e.i. int(18) primary key auto_increment not null
		* the 2nd list of the db-table key-indices(optional)
		* each dbt-key-index can be a CI or a list , if a list
		* each item has to be a List
		* ,can start with a prefix, e.i. unique , or key`ix1`
		* , the items of this list should be a CI
		* ,	or the item can be a list that has as the 1st item the CI
		* and the 2nd item the length of the index
		* the third list is optional, for each item in this list
		* is a list of values to be inserted into the created table
		*/
		public abstract List creationDBTIndices();//TL tl

		public void checkDBTCreation(){//TL tl
			String dtn=getName();Object o=Json.m(Name+":db:show tables");
			if(o==null)
				try {o=DB.q1colList("show tables");
					Json.m(Name+":db:show tables",o);
				} catch (SQLException ex) {
					error(ex, Name+".DB.Tbl.checkTableCreation:check-pt1:",dtn);}
			List l=(List)o;
			try{if(o==null||(!l.contains( dtn )&&!l.contains( dtn.toLowerCase()))){
				StringBuilder sql= new StringBuilder("CREATE TABLE `").append(dtn).append("` (\n");
				CI[]ci=columns();int an,x=0;
				List a=creationDBTIndices(),b=(List)a.get(0);
				for(CI i:ci){
					if(x>0 )
						sql.append("\n,");
					sql.append('`').append(i).append('`')
						.append(String.valueOf(b.get(x)) );
					x++;}
				an=a.size();b=an>1?(List)a.get(1):b;
				if(an>1)for(Object bo:b)
				{sql.append("\n,");x=0;
					if(bo instanceof CI)
						sql.append("KEY(`").append(bo).append("`)");
					else if(bo instanceof List)
					{	List bl=(List)bo;x=0;boolean keyHeadFromList=false;
						for(Object c:bl){
							boolean s=c instanceof String;
							if(x<1 && !s&& !keyHeadFromList)
								sql.append("KEY(");
							if(x>0)
								sql.append(',');//in the list
							if(s){sql.append((String)c);if(x==0){x--;keyHeadFromList=true;}}
							else {l=c instanceof List?(List)c:null;
								sql.append('`').append(
									l==null?String.valueOf(c)
										:String.valueOf(l.get(0))
								).append("`");
								if(l!=null&&l.size()>1)
									sql.append('(').append(l.get(1)).append(')');
							}x++;
						}sql.append(")");
					}else
						sql.append(bo);
				}
				sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 ;");
				log(Name,".DB.Tbl.checkTableCreation:before:sql=",sql);
				int r=DB.x(sql.toString());
				log(Name,".DB.Tbl.checkTableCreation:executedSql:",dtn,":returnValue=",r);
				b=an>2?(List)a.get(2):b;if(an>2)
					for(Object bo:b){
						List c=(List)bo;
						Object[]p=new Object[c.size()];
						c.toArray(p);
						vals(p);
						try {save();} catch (Exception ex) {
							error(ex, Name,".DB.Tbl.checkTableCreation:insertion",c);}
					}
			}
			} catch (SQLException ex) {
				error(ex, Name,".DB.Tbl.checkTableCreation:errMain:",dtn);}
		}//checkTableCreation

		/**where[]={col-name , param}*/
		public int count(Object[]where) throws Exception{return count(where,null,getName());}

		public static int count(Object[]where,CI[]groupBy,String name) throws Exception{
				String sql=sql(cols(Co.count),where,groupBy,null,name);//new StringBuilder("select count(*) from `").append(getName()).append("` where `").append(where[0]).append("`=").append(Co.m(where[0]).txt);//where[0]instanceof CI?m((CI)where[0]):'?');
				return DB.q1int(sql,-1,where[0],where[1]);}

		public int maxPlus1(CI col) throws Exception{
			String sql=sql("max(`"+col+"`)+1",null,null,null);
			return DB.q1int(sql,1);}

		public static int maxPlus1(CI col,String dbtn) throws Exception{
			String sql="SELECT max(`"+col+"`)+1 from `"+dbtn+"`";
			return DB.q1int(sql,1);}

		// /**returns one object from the db-query*/ /**where[]={col-name , param}*/public Object obj(CI col,Object[]where) throws Exception{return DB.q1Obj(sql(cols(col),where),where);}
		/**returns one string*/
		public String select(CI col,Object[]where) throws Exception{
			String sql=sql(cols(col),where);
			return DB.q1Str(sql,where);}

		// /**returns one column, where:array of two elements:1st is column param, 2nd value of param*/Object[]column(CI col,Object...where) throws Exception{ return DB.q1col(sql(cols(col),where),where[0],where[1]);}//at
		/**returns a table*/
		public Object[][]select(CI[]col,Object[]where)throws Exception{
			return DB.Q(sql(col,where), where);}

		/**loads one row from the table*/
		Tbl load(ResultSet rs)throws Exception{return load(rs,fields());}

		/**loads one row from the table*/
		Tbl load(ResultSet rs,Field[]a)throws Exception{
			int c=0;for(Field f:a)v(f,rs.getObject(++c));
			return this;}

		/**loads one row from the table*/
		public Tbl load(Object pk){
			ResultSet r=null;//TL t=tl();
			try{r=DB.r(sql(cols(Co.all), where(pkc()))
				,pk);
				if(r.next())load(r);
				else{error(null,Name,".DB.Tbl(",this,").load(pk=",pk,"):resultset.next=false");nullify();}}
			catch(Exception x){error(x,Name,".DB.Tbl(",this,"):",pk);}
			finally{DB.close(r,true);}
			return this;}

		public Tbl nullify(){return nullify(fields());}
		public Tbl nullify(Field[]a){for(Field f:a)v(f,null);return this;}
		// /**loads one row from the table*/ Tbl load(){return load(pkv());}

		/**loads one row using column CI c */
		Tbl loadBy(CI c,Object v){
			try{Object[]a=DB.q1row(sql(cols(Co.all),where(c)),v);
				vals(a);}
			catch(Exception x){error(x,Name,".DB.Tbl(",this,").loadBy(",c,",",v,")");}
			return this;}//loadBy

		Tbl save(CI c){
			CI pkc=pkc();
			Object cv=v(c),pkv=pkv();TL t=TL.tl();
			if(cv instanceof Map)try
			{String j=t.jo().clrSW().o(cv).toString();cv=j;}
			catch (IOException e) {error(e,Name,".DB.Tbl.save(CI:",c,"):");}
			try{DB.x("replace into `"+getName()+"` (`"+pkc+
						"`,`"+c+"`) values(?"//+Co.m(pkc).txt
						+",?"//+Co.m(c).txt
						+")",pkv,cv);
				Integer k=(Integer)pkv;
				//DB.Tbl.Log.log( DB.Tbl.Log.Entity.valueOf(getName()), k, DB.Tbl.Log.Act.Update, TL.Util.mapCreate(c,v(c)) );
			}catch(Exception x){error(x
				,Name,".DB.Tbl(",this,").save(",c,"):pkv=",pkv);}
			return this;
		}//save

		/**store this entity in the dbt , if pkv is null , this method uses the max+1 */
		public Tbl save() throws Exception{
			Object pkv=pkv();CI pkc=pkc();boolean nw=pkv==null;//Map old=asMap();
			if(nw){
				int x=DB.q1int(//"select max(`" +pkc+"`)+1 from `"+getName()+"`"
					sql("max(`"+pkc+"`)+1",null,null,null)
					,1);
				v(pkc,pkv=x);
				//log(Name,".DB.Tbl(",toJson(),").save-new:max(",pkc,") + 1:",x);
			}CI[]cols=columns();
			StringBuilder sql=new StringBuilder("replace into`").append(getName()).append("`( ");
			Co.generate(sql, cols);//.toString();
			sql.append(")values(").append(Co.prm.txt);//Co.m(cols[0]).txt
			for(int i=1;i<cols.length;i++)
				sql.append(",").append(Co.prm.txt);//Co.m(cols[i]).txt
			sql.append(")");//int x=
			DB.X( sql.toString(), vals() ); //TODO: investigate vals() for json columns
			log("save");//log(nw?DB.Tbl.Log.Act.New:DB.Tbl.Log.Act.Update);
			return this;}//save


		//void log(DB.Tbl.Log.Act act){	Map val=asMap();Integer k=(Integer)pkv();DB.Tbl.Log.log( DB.Tbl.Log.Entity.valueOf(getName()), k, act, val);}

		public int delete() throws SQLException{
			Object pkv=pkv();
			int x=DB.x("delete from `"+getName()+"` where `"+pkc()+"`=?", pkv);
			//log(DB.Tbl.Log.Act.Delete);
			return x;}

		/**retrieve from the db table all the rows that match
		* the conditions in < where > , create an iterator
		* , e.g.<code>for(Tbl row:query(
		* 		Tbl.where( CI , < val > ) ))</code>*/
		public Itrtr query(Object[]where){
			Itrtr r=new Itrtr(where);
			return r;}

		public Itrtr query(Object[]where,boolean makeClones){return query(columns(),where,null,makeClones);}

		public Itrtr query(CI[]cols,Object[]where,CI[]groupBy,boolean makeClones){//return query(sql(cols,where,groupBy),where,makeClones);}//public Itrtr query(String sql,Object[]where,boolean makeClones){
				Itrtr r=new Itrtr(sql(cols,where,groupBy),where,makeClones);
				return r;}

		public class Itrtr implements Iterator<Tbl>,Iterable<Tbl>{
			public ResultSet rs=null;public int i=0;Field[]a;boolean makeClones=false;

			public Itrtr(String sql,Object[]where,boolean makeClones){
				this.makeClones=makeClones;a=fields();
				try{rs=DB.R(sql, where);}
				catch(Exception x){
					error(x,Name,".DB.Tbl(",this,").Itrtr.<init>:where=",where);}
			}

			public Itrtr(Object[]where){a=fields();
				try{rs=DB.R(sql(cols(Co.all),where), where);}
				catch(Exception x){error(x,Name,".DB.Tbl(",this,").Itrtr.<init>:where=",where);}}

			@Override public Iterator<Tbl>iterator(){return this;}

			@Override public boolean hasNext(){boolean b=false;
				try {b = rs!=null&&rs.next();} catch (SQLException x)
				{error(x,Name,".DB.Tbl(",this,").Itrtr.hasNext:i=",i,",rs=",rs);}
				if(!b&&rs!=null){DB.close(rs);rs=null;}
				return b;}

			@Override public Tbl next(){i++;Tbl t=Tbl.this;TL tl=TL.tl();
				if(makeClones)try{
					t=t.getClass().newInstance();}catch(Exception ex){
					error(ex,Name,".DB.Tbl(",this,").Itrtr.next:i=",i,":",rs,":makeClones");
				}
				try{t.load(rs,a);}catch(Exception x){
					error(x,Name,".DB.Tbl(",this,").Itrtr.next:i=",i,":",rs);
					close(rs,true);rs=null;
				}
				return t;}

			@Override public void remove(){throw new UnsupportedOperationException();}

		}//Itrtr

			/**Class for Utility methods on set-of-columns, opposed to operations on a single column*/
		public enum Co implements CI {//Marker ,sql-preparedStatement-parameter
			uuid("uuid()")
			,now("now()")
			,count("count(*)")
			,all("*")
			,prm("?")
			,password("password(?)")
			,Null("null")
			,lt("<"),le("<="),ne("<>"),gt(">"),ge(">=")
			,or("or"),like("like"),in("in"),maxLogTime("max(`logTime`)")//,and("and"),prnthss("("),max("max(?)")
			;String txt;

			Co(String p){txt=p;}

			public Field f(){return null;}

			public static Field f(String name,Class<? extends Tbl>c){
				//for(Field f:fields(c))if(name.equals(f.getName()))return f;return null;
				Field r=null;try{r=c.getField(name);}catch(Exception x)
				{error(x,Name,".DB.Tbl.f(",name,c,"):");}
				return r;}

			/**generate Sql into the StringBuilder*/
			public static StringBuilder generate(StringBuilder b,CI[]col){
				return generate(b,col,",");}

			static StringBuilder generate(StringBuilder b,CI[]col,String separator){
				if(separator==null)separator=",";
				for(int n=col.length,i=0;i<n;i++){
					if(i>0)b.append(separator);
					if(col[i] instanceof Co)
						b.append(((Co)col[i]).txt);
					else
						b.append("`").append(col[i]).append("`");}
				return b;}

			static StringBuilder where(StringBuilder b,Object[]where){
				if(where==null || where.length<1)return b;
				b.append(" where ");
				for(int n=where.length,i=0;i<n;i++){Object o=where[i];
					if(i>0)b.append(" and ");
					if(o instanceof Co)b.append(o);else
					if(o instanceof CI)
						b.append('`').append(o).append("`=")
							.append('?');//Co.m(o).txt
					else if(o instanceof List){List l=(List)o;
						o=l.size()>1?l.get(1):null;
						if(o ==Co.in && i+1<n && where[i+1] instanceof List){
							b.append('`').append(l.get(0)).append("` ").append(o);
							l=(List)where[i+1];
							b.append(" (");boolean comma=false;
							for(Object z:l){
								if(comma)b.append( ',' );else comma=true;
								if(z instanceof Number)
									b.append( z );else
									b.append( '\'' )
										.append(
											(z instanceof String?(String)z:z.toString()
											).replaceAll( "'","''" )
										)
										.append( '\'' );
							}b.append(")");
						}else if(o instanceof Co)//o!=null)//if(ln==2 && )
						{	Co m=(Co)o;o=l.get(0);
							if(o instanceof CI || o instanceof Co)
								b.append('`').append(o).append('`');
							else
								log(Name,".DB.Tbl.Co.where:unknown where-clause item:o=",o);
							b.append(m.txt).append("?");
						}else
							log(Name,".DB.Tbl.Co.where:unknown where-clause item: o=",o);
					}
					else error(null,Name,".DB.Tbl.Col.where:for:",o);
					i++;
				}//for
				return b;}
		}//enum Co

		/**output to jspOut one row of json of this row*/
		public void outputJson(){try{Json.jo().o(this);}catch(IOException x){error(x,"moh.DB.Tbl.outputJson:IOEx:");}}

		/**output to jspOut rows of json that meet the 'where' conditions*/
		public void outputJson(Object...where){
		 try{
			Json.Output o=Json.jo();
			o.w('[');boolean comma=false;
			for(Tbl i:query(where)){
				if(comma)o.w(',');else comma=true;
				i.outputJson();}
			o.w(']');
		 } catch (IOException e){error(e,Name,".DB.Tbl.outputJson:");}
		}//outputJson(Object...where)

		public static List<Class<? extends Tbl>>registered=new LinkedList<Class<? extends Tbl>>();

		static void check(TL tl){
			for(Class<? extends Tbl>c:registered)try
			{String n=c.getName(),n2=".checkDBTCreation."+n;
				if( Json.m(n2)==null){
					Tbl t=c.newInstance();
					t.checkDBTCreation();
					Json.m(n2,System.currentTimeMillis() );//tl.now
				}}catch(Exception ex){error( ex,Name,".DB.Tbl.check" );}
		}
	}//class Tbl
}//class DB
	
class Util{
	public static <T>T parse(String s,T defval){
		if(s!=null)try{
			Class<T> ct=(Class<T>) defval.getClass();
			Class c=ct;
			boolean b=c==null?false:c.isEnum();
			if(!b){c=ct.getEnclosingClass();b=c==null?false:c.isEnum();}
			if(b){
				for(Object o:c.getEnumConstants())
					if(s.equalsIgnoreCase(o.toString()))
						return (T)o;
			}}catch(Exception x){//changed 2016.06.27 18:28
			DB.error(x, DB.Name,".Util.<T>T parse(String s,T defval):",s,defval);}
		return defval;}

	public static Object parse(String s,Class c){
		if(s!=null)try{if(String.class.equals(c))return s;
		else if(Number.class.isAssignableFrom(c)||c.isPrimitive()) {
			if (Integer.class.equals(c)|| "int"	.equals(c.getName())) return new Integer(s);
			else if (Double .class.equals(c)|| "double".equals(c.getName())) return new Double(s);
			else if (Float	.class.equals(c)|| "float" .equals(c.getName())) return new Float(s);
			else if (Short	.class.equals(c)|| "short" .equals(c.getName())) return new Short(s);
			else if (Long	.class.equals(c)|| "long"	.equals(c.getName())) return new Long(s);
			else if (Byte	.class.equals(c)|| "byte"	.equals(c.getName())) return new Byte(s);
		}///else return new Integer(s);}
		else if(Boolean.class.equals(c)||(c.isPrimitive()&&"boolean".equals(c.getName())))return new Boolean(s);
		else if(Date.class.equals(c))return parseDate(s);
		else if(Character.class.isAssignableFrom(c)||(c.isPrimitive()&&"char".equals(c.getName())))
			return s.length()<1?'\0':s.charAt(0);
		else if(URL.class.isAssignableFrom(c))try {
			return new URL("file:" //+TL.tl().h.getServletContext().getContextPath()
					               +'/'+s);}
		catch (Exception ex) {DB.error(ex,DB.Name,".Util.parse:URL:p=",s," ,c=",c);}
			boolean b=c==null?false:c.isEnum();
			if(!b){Class ct=c.getEnclosingClass();b=ct==null?false:ct.isEnum();if(b)c=ct;}
			if(b){
				for(Object o:c.getEnumConstants())
					if(s.equalsIgnoreCase(o.toString()))
						return o;
			}
			return Json.Prsr.parse(s);
		}catch(Exception x){//changed 2016.06.27 18:28
			DB.error(x, DB.Name,".Util.<T>T parse(String s,Class):",s,c);}
		return s;}

	static Date parseDate(String s){
		Integer[]a=parseInts(s);int n=a.length;
		if(n<2){long l=Long.parseLong(s);
			Date d=new Date(l);
			return d;}
		java.util.GregorianCalendar c=new java.util.GregorianCalendar();
		c.set(n>0?a[0]:0,n>1?a[1]-1:0,n>2?a[2]:0,n>3?a[3]:0,n>4?a[4]:0);
		return c.getTime();}

	public static Integer[]parseInts(String s){
		java.util.Scanner b=new java.util.Scanner(s),
			c=b.useDelimiter("[\\s\\.\\-/\\:A-Za-z,]+");
		List<Integer>l=new LinkedList<Integer>();
		while(c.hasNextInt()){
			//if(c.hasNextInt())else c.skip();
			l.add(c.nextInt());
		}c.close();b.close();
		Integer[]a=new Integer[l.size()];l.toArray(a);
		return a;}
}//class Util


	/**
	 *	db-tbl ORM wrapper
	 */
class JsonStorage extends DB.Tbl{
	public static final String dbtName="JsonStorage";

	@Override public String getName(){return dbtName;}

	@Override public CI pkc(int i){return C.id;}

	@Override public CI[]pkcols(){C[]a={C.id};return a;}

	@F public int id ;

	@F public Date lastModified ;

	@F public String usr,domain,mac,prop,val;

	@Override public int pkv(int i){return id;}

	@Override public int pkv(int v){return id=v;}


	public enum C implements CI{id,lastModified,usr,domain,mac,val;
		@Override public Field f(){return Co.f(name(), JsonStorage.class);}
		@Override public String getName(){return name();}
		@Override public Class getType(){return this==id?Integer.class:this==lastModified?Date.class:String.class;}
	}//C

	@Override public C[]columns(){return C.values();}

		@Override public List creationDBTIndices(TL tl){
			return Util.lst(Util.lst(
				"varchar(255) NOT NULL DEFAULT 'home' "//app \u1F3E0 ??
				,"varchar(255) NOT NULL DEFAULT 'home' "//key
				,"enum('txt','json','key','num','real','date','bytes','serverSideJs','javaObjectStream') NOT NULL DEFAULT 'txt' "//typ
				,"blob"),Util.lst("unique(`app`,`key`)")
			);//val
			/*
			CREATE TABLE `JsonStorage` (
			`app` varchar(255) NOT NULL DEFAULT '??',
			`key` varchar(255) NOT NULL DEFAULT '??',
			`typ` enum('txt','json','key','num','real','date','bytes','serverSideJs','javaObjectStream') NOT NULL DEFAULT 'txt',
			`val` blob ,
			unique(`app`,`key`)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;
??  lock with ink pen Unicode code point: U+1F50F
??  closed lock with key Unicode code point: U+1F510
??  key Unicode code point: U+1F511
??  lock Unicode code point: U+1F512
??  open lock Unicode code point: U+1F513
			*/
		}

		static{registered.add(JsonStorage.class);}
		public static JsonStorage sttc=new JsonStorage( );

		public static JsonStorage loadBy(String app,String key){
			JsonStorage j=new JsonStorage(),x=null;
			for(TL.DB.Tbl i:j.query(where( C.app,app,C.key,key )))
				x=j;
			return x;}

		public static Object prmInstance(TL tl,String prmName){
			Object o=tl.json.get( prmName )
				,app=tl.json.get("app")
				,key=tl.json.get("key");
			JsonStorage j=o instanceof JsonStorage
				?(JsonStorage)o
				:app instanceof String && key instanceof String
				?loadBy( (String)app,(String)key )
				:null;
			if(j!=null && o instanceof String)
				tl.json.put( prmName,j );
			return j!=null ?j:o;}//&& "key".equals( prmName )

		@Op public static List<String>
		listApps() throws SQLException {
			return DB.q1colTList(
				sql(cols( Co.distinct,C.app ),null,dbtName)
				,String.class );}

		@Op public static Map//List<String>
		listKeys(@Op(prmName="app")String appName)throws SQLException{
			JsonStorage j=new JsonStorage();j.app=appName;
			Map m=TL.Util.mapCreate();
			for(DB.Tbl t:j.query(where( C.app ,appName)))
			{String s=j.typ.toString();
				List n=(List)m.get(s);
				if(n==null)
					m.put(s,n=TL.Util.lst());
				n.add(j.key);}
			return m;
			//return DB.q1colTList(sql(cols(C.key ),where( C.app ,appName),dbtName)// Co.distinct,,String.class ,appName);
			}

		@Op public static JsonStorage get(@Op(prmName="app")String appName
			,@Op(prmName="key",prmInstance=true)JsonStorage j){ return j;}

		@Op public static List<JsonStorage>
		getKeys(@Op(prmName="app")String appName,@Op(prmName="keys")List<String>keys){
			List<JsonStorage>l=new LinkedList<>(  );
			JsonStorage j=new JsonStorage();
			for(DB.Tbl t:j.query(
				where(C.app,appName
					,TL.Util.lst( C.key,Co.in),keys )
				,true))
				l.add( (JsonStorage ) t );
			return l;}

		@Op public static JsonStorage
		set(@Op(prmName="app")String appName
			   ,@Op(prmName="key")String key
			   ,@Op(prmName="typ")ContentType typ
			   ,@Op(prmName="val")Object val,TL tl)throws Exception
		{	JsonStorage j=new JsonStorage();
			j.val =val;j.app=appName;j.key=key;j.typ=typ;
			return store(j,tl);}

		@Op public static JsonStorage
		store(@Op(prmName="store")JsonStorage j ,TL tl)throws Exception
		{if(j!=null){j.save();
			if(j.typ==ContentType.serverSideJs ){
				javax.script.ScriptEngine e =eng(j.app,false,tl);
				if(e!=null){
					Object o=e.eval( j.val.toString() );//engine.put( key ,o);
					Map jm=(Map)e.get( "JsonStorageApp");
					jm.put( j.key,o );//List jl=(List)e.get( "JsonStorageApp.JsonStorages");jl.add( j );
				} } }return j;}

		static javax.script.ScriptEngine eng(String appName,boolean createIfNotInit,TL tl){
			String en="ScriptEngine.JavaScript."+appName;
			javax.script.ScriptEngine e =(javax.script.ScriptEngine)tl.h.s( en );
			if(e==null&& createIfNotInit) {
				javax.script.ScriptEngineManager man=(javax.script.ScriptEngineManager)tl.h.a( "ScriptEngineManager" );
				if(man==null )
					tl.h.a( "ScriptEngineManager",man=new javax.script.ScriptEngineManager() );
				tl.h.s( en, e = man.getEngineByName( "JavaScript" ) );
				e.put( "tl",tl );
				JsonStorage j=new JsonStorage();
				Map jm=TL.Util.mapCreate(  );//List jl=TL.Util.lst(  );
				for(DB.Tbl t:j.query( where(C.app,appName,C.typ	,ContentType.serverSideJs.toString()) ))
					try{jm.put( j.key,e.eval( j.val.toString() ));}catch ( Exception ex ){
						jm.put( j.key,TL.Util.mapCreate("sourceCode", j.val,"eval_Exception",ex ) );}
				e.put( "JsonStorageApp",jm);//e.put( "JsonStorageApp.JsonStorages",jl);
			}else if(e!=null)
				e.put( "tl",tl);
			return e; }

		@Op public static Object member(@Op(prmName = "app")String appName
			,@Op(prmName = "member")String m,@Op(prmName = "args")List args,TL tl) throws javax.script.ScriptException
		{	javax.script.ScriptEngine e=eng(appName,true,tl);
			e.put( "member",m);
			if(args!=null)e.put(  "args",args);
			return e.eval( "JsonStorageApp[member]"+(args==null?"":"(args,tl.json,tl)") );
		}

		@Op public static Object eval(@Op(prmName = "app")String appName
			,@Op(prmName = "src")String src,TL tl) throws javax.script.ScriptException
		{	javax.script.ScriptEngine e=eng(appName,true,tl);
			e.put( "src",src);
			return e.eval( src );
		}

		/**loads one row from the table*/
		@Override DB.Tbl load(ResultSet rs,CI[]a)throws Exception{
			int c=0;for(CI f:a)if(f!=C.val )v(f,rs.getObject(++c));else
				switch ( typ ){
					case bytes:
						val =rs.getBytes( ++c );break;
					case date:
						val =rs.getDate( ++c );break;
					case num:
						val =rs.getLong( ++c );break;
					case real:
						val =rs.getDouble( ++c );break;
					case javaObjectStream:java.io.ObjectInputStream p=
						                      new ObjectInputStream( rs.getBinaryStream( ++c ) );
						val =p.readObject();break;
					case json: val =Json.Prsr.parseItem(rs
						                                    .getCharacterStream( ++c ) );break;
					default://case txt: case key:
						val =rs.getString( ++c );break;
				}
			return this;}

		public static ContentType assesTyp(Class p) {
			ContentType typ=ContentType.javaObjectStream,t;
			if(p==null || p .isAssignableFrom(  Boolean.class))typ=ContentType.json;
			else if(p .isAssignableFrom( String.class))typ=ContentType.txt;
			else if(p .isAssignableFrom(Date.class ))typ=ContentType.date;
			else if(p .isAssignableFrom( Double.class )
				        || p.isAssignableFrom( Float.class ))typ=ContentType.real;
			else if(p .isAssignableFrom(Number.class ))typ=ContentType.num;
			else if(p .isArray()){Class c=p.getComponentType();
				if(c.isAssignableFrom( Byte.class ))typ=ContentType.bytes;
				else {t=assesTyp( c );
					if(t==ContentType.bytes || t==ContentType.date
						   || t==typ)
						return typ;
					return ContentType.json;
				}
			}
			else if(p .isAssignableFrom(Map.class)
				        ||p .isAssignableFrom(Iterator.class)
				        ||p .isAssignableFrom(Enumeration.class)
				        ||p .isAssignableFrom(Collection.class))
			{java.lang.reflect.Type[]c=p.getGenericInterfaces();
				t=assesTyp( c[p .isAssignableFrom(Map.class)?1:0].getClass());
				if(t==ContentType.bytes || t==ContentType.date
					   || t==ContentType.javaObjectStream)
					return typ;
				return ContentType.json; }
			return typ;}

		public static ContentType assesTyp(Object p) {
			ContentType j=ContentType.json
				,t,b=ContentType.bytes
				,d=ContentType.date
				,typ=ContentType.javaObjectStream;
			Class c=p==null?null:p.getClass();
			if(p==null )return j;
			else if(p instanceof Map){
				Map m=(Map)p;
				for ( Object v:m.values() )
				{   t=assesTyp( v );
					if(t==b || t==d|| t==typ)
						return typ;}
				typ= j; }
			else if( c.isArray() ){
				Class u=c.getComponentType();
				t=assesTyp( u );
				if(t==typ){
					Object[]a=(Object[])p;
					for ( Object i:a ){
						t=assesTyp( i );
						if(t==b || t==d || t==typ)
							return typ; }
					typ=j; } }
			else if( p instanceof Collection ){
				Collection a=(Collection)p;
				for ( Object i:a ){
					t=assesTyp( i );
					if(t==b || t==d || t==typ)
						return typ; }
				typ=j; }
			//else if(p instanceof Iterator )recursively assesTyp of items of the array
			//else if(p instanceof Enumeration )
			else typ=assesTyp( c );
			return typ;}

		public ContentType assesTyp(){return typ=assesTyp( val );}

		/**store this entity in the dbt , if pkv is null , this method uses the max+1 of pk-col*/
		@Override public DB.Tbl save() throws Exception{
			CI[] cols = columns();
			StringBuilder sql = new StringBuilder( "replace into`" ).append( getName() ).append( "`( " );
			Co.generate( sql, cols );//.toString();
			sql.append( ")values(" ).append( Co.prm.txt );//Co.m(cols[0]).txt
			for ( int i = 1; i < cols.length; i++ )
				sql.append( "," ).append( Co.prm.txt );//Co.m(cols[i]).txt
			sql.append( ")" );//int x=
			Object[] vals = vals();
			if(typ==ContentType.javaObjectStream) {
				//PreparedStatement ps=DB.P( sql.toString(),vals() ); ps.setBinaryStream( C.val.ordinal()+1,stream );
				ByteArrayOutputStream b=new ByteArrayOutputStream(  );
				ObjectOutputStream o=new ObjectOutputStream( b );
				o.writeObject( val );o.flush();o.close();b.close();
				vals[C.val.ordinal()]=b.toByteArray(); }
			else if(typ==ContentType.json)
				vals[C.val.ordinal()]=Json.Output.out( val );
			vals[C.typ.ordinal()]=(typ==null?ContentType.txt:typ).toString();
			DB.X( sql.toString(), vals );
			tl().log( "save", this );//log(nw?TL.DB.Tbl.Log.Act.New:TL.DB.Tbl.Log.Act.Update);
			return this;}//save


		@Override public Json.Output jsonOutput(Json.Output o,String ind,String path)throws IOException{
			o.w("{\"app\":").oStr(app,ind)
				.w(",\"key\":").oStr(key,ind)
				.w(",\"typ\":");
			if(typ==null)o.w( "null" );
			else o.oStr(typ.toString(),ind);
			return o.w(",\"val\":").o( val,ind,path).w('}');}

	}//class JsonStorage

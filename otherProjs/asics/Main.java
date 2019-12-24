/*
multi-threaded agent

thread-class1: (single thread-class instance, Main-class) init-from config-file and update global-config
	, and periodically check config-file changes
	,and, check local-agents and threads working/existing/alive
thread-class2: (single thread-class instance) scan ips, and update global-config
thread-class3: (multiple thread-class instances) read assigned-ip asic
thread-class4: (multiple thread-class instances) check assigned house

to read ascis, and other agents from other houses



*/
//{
import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection          ;
import java.sql.Statement           ;
import java.sql.PreparedStatement   ;
import java.sql.ResultSet           ;
import java.sql.ResultSetMetaData   ;
import java.sql.SQLException        ;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Enumeration;
import java.util.Date;
import java.util.Base64;// Java 8
//}
class TL extends Thread{
	Map m;//Connection dbc; used mainly for DB , jo
	static TL tl(){Thread t=Thread.currentThread();return t instanceof TL?(TL)t:Main.global;}
	static Object m(Object k){TL tl=tl();Object o=tl.m==null?null:tl.m.get(k);return o;}
	static Object m(Object k,Object v){TL tl=tl();if(tl.m==null)tl.m=new HashMap();tl.m.put(k,v);return v;}
	static Main.Json.Output jo(){
		Object o=m("jo");
		Main.Json.Output j=o instanceof Main.Json.Output?
		(Main.Json.Output)o
		:null;if(j==null)
			m("jo",j=new Main.Json.Output());
		return j;}
}//class TL

public class Main extends TL{
ThreadClass2ScanIps scan;//Map config;
List<ThreadClass3Asic >asics;
List<ThreadClass4House>houses;
File baseDir;

static Main global;

public static void main(String[]args)//class ThreadClass1 extends Thread{}
{global=new Main();//baseDir=File.;
	//check db config
	//start scan
}//main

public static void checkConfig(){
	
	/*
	db-tbl:props.props:
		rowId	int primary key auto_increment
		user			varChar not null default ''
		domain			varChar	//usually the house-id
		mac				varChar not null default ''
		lastModified	timestamp not null default current_timestamp on_update current_timestamp
		propName		varChar not null default ''
		propVal			json not null default ''
		unique(user,mac,propName)
		index (user,mac,propName,propVal)
		index (user,domain,lastModified)

	db-tbl:props.log:
		rowId	int primary key auto_increment
		user			varChar not null default ''
		domain			varChar not null default ''
		mac				varChar not null default ''
		logged			timestamp not null default current_timestamp on_update current_timestamp
		propName		varChar not null default ''
		propVal			json not null default ''
		index(user,logged,propName,propVal)
		index(user,propName,propVal,logged)
		index(user,domain,logged)
		index(user,mac,logged)

	config(per house)
	propVal:json={
		houses:[
			{houseNo:<str>
				ip:<str>
			},,,
		]
		checkDB:{freq:<seconds:int>}
		scan:{ip:{prefix:<str>,start:<int>,end:<int>},freq:<int:seconds>}
		devices:[<mac:str>,,,]
		]
	}
	<mac:str:asic>:json={
		mac:<str>
		ip:<str>
		config:[{url:<str>,wallet:<str>,pw:<str>},,,]
		info:{,,,}
		status:{found:<num>,firmwareVersion:<str>,,,}
		network:{type:<str>,host:<str>,,,}
	}

	<mac:str:asic>:propName:
		asic.status.board.tempr		: {boardNo:<int>,val:<num>}
		asic.status.board.hashRate	: {boardNo:<int>,val:<num>}
		asic.status.board.<str>,,,	: {boardNo:<int>,val:<num>}
		asic.status.fan.<str>,,,	: {fanNo:<int>,val:<num>}
		asic.found:<int>
		
	*/
}//checkConfig

//public static void writeJson(String base,Date t,String x,Object json){}//writeJson
enum AsicState{init,detectAsic,invalidWallet,wallet,config,monitor}

static 

class Asic extends TL{
	URL url;HttpURLConnection conn;//String ip,host;int[]port;
	String[]wallet;
	double[]tempr;int blockFound;
	AsicState state=AsicState.init;

	public void startScan(){
		/*https://www.baeldung.com/java-http-url-connection
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		String auth = user + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
		String authHeaderValue = "Basic " + new String(encodedAuth);
		connection.setRequestProperty("Authorization", authHeaderValue);
		int responseCode = connection.getResponseCode();
		
		
		http://www.avajava.com/tutorials/lessons/how-do-i-connect-to-a-url-using-basic-authentication.html
		try {
			String webPage = "http://192.168.1.1";
			String name = "admin";
			String password = "admin";

			String authString = name + ":" + password;
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: " + authStringEnc);

			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String result = sb.toString();

			System.out.println("*** BEGIN ***");
			System.out.println(result);
			System.out.println("*** END ***");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}


	public void run(){startScan();}
	public Asic(String ip){url=new URL("http",ip,80,"");}
	public String toJson(){return JSON.Output.o();}

}//class asic

class ThreadClass2ScanIps extends TL{
	String prefix;int[]ports;long sleep;

	public void run(){startScan();}

	public void startScan(){
		for(int i=port[0]li<=port[1];i++){
			Asic
		}
	}

	public ThreadClass2ScanIps(
		String prefix,int startPort,int endPort,long sleep){
		this.prefix=prefix;this.ip=ip;this.sleep=sleep;}
}//

class ThreadClass3Asic extends TL{
	String ip;
	
}//ThreadClass3Asic

class ThreadClass4House extends TL{}

public static class Json{

public static Output sttc=null;

static Object[]a(Object...a){return a;}
static List l(Object...a){List l=new LinkedList(a);return l;}
static Map m(Object...a){return ma(new HashMap(),a);}
static Map m(Map m,Object...a){return ma(m,a);}
static Map ma(Map m,Object[]a){
	for(int i=0;i<a.length;i+=2)m.put(a[i],a[i+1]);
	return m;}

	public static class Output
	{ public Writer w;
		public boolean initCache=false,includeObj=false,comment=false;
		Map<Object, String> cache;
		
		public static void out(Object o,Writer w,boolean initCache,boolean includeObj)throws IOException
		{Json.Output t=new Json.Output(w,initCache,includeObj);
			t.o(o);
			if(t.cache!=null)
			{t.cache.clear();t.cache=null;}
		}

		public static String out(Object o,boolean initCache,boolean includeObj)
		{StringWriter w=new StringWriter();
			try{out(o,w,initCache,includeObj);}
			catch(Exception ex){sttc.log("Json.Output.out",ex);}
			return w.toString();
		}

		public static String out(Object o){StringWriter w=new StringWriter();try{out(o,w,
			false,false);}catch(Exception ex){sttc.log("Json.Output.out",ex);}return w.toString();}

		public Output(){w=new StringWriter();}

		public Output(Writer p){w=p;}

		public Output(Writer p,boolean initCache,boolean includeObj)
		{w=p;this.initCache=initCache;this.includeObj=includeObj;}

		public Output(boolean initCache,boolean includeObj){this(new StringWriter(),initCache,includeObj);}

		public Output(String p)throws IOException{w=new StringWriter();w(p);}

		public Output(OutputStream p)throws Exception{w=new OutputStreamWriter(p);}

	public void log(Object...s){logA(s);}

public void logA(Object[]s){try{
	for(Object t:s)
		w(String.valueOf(t));
	//String t=toStrin_();h.getServletContext().log(t);if(logOut)out.flush().w(h.comments[0]).w(t).w(h.comments[1]);
}catch(Exception ex){ex.printStackTrace();}}

 public void error(Throwable x,Object...p){try{
	String s=w("error:").o(p,x).toString();
	//h.getServletContext().log(s);
	//if(h.logOut)out.w(h.comments[0]	).w("error:").w(s.replaceAll("<", "&lt;")).w("\n---\n").o(x).w(h.comments[1] );
	if(x!=null)x.printStackTrace();}
 catch(Exception ex){ex.printStackTrace();}}


		public String toString(){return w==null?null:w.toString();}
		public String toStrin_(){String r=w==null?null:w.toString();clrSW();return r;}
		public Output w(char s)throws IOException{if(w!=null)w.write(s);return this;}
		public Output w(String s)throws IOException{if(w!=null)w.write(s);return this;}
		public Output p(String s)throws IOException{return w(s);}
		public Output p(char s)throws IOException{return w(s);}
		public Output p(long s)throws IOException{return w(String.valueOf(s));}
		public Output p(int s)throws IOException{return w(String.valueOf(s));}
		public Output p(boolean s)throws IOException{return w(String.valueOf(s));}
		public Output o(Object...a)throws IOException{return o("","",a);}
		public Output o(Object a,String indentation)throws IOException{return o(a,indentation,"");}
		public Output o(String ind,String path,Object[]a)throws IOException
		{for(Object i:a)o(i,ind,path);return this;}

		public Output o(Object a,String ind,String path)throws IOException
		{if(cache!=null&&a!=null&&((!includeObj&&path!=null&&path.length()<1)||cache.containsKey(a)))
			{Object p=cache.get(a);
				if(p!=null)
				{	o(p.toString());
					o("/*cacheReference*/");
					return this;}
			}
			final boolean c=comment;
			if(a==null)w("null"); //Object\n.p(ind)
			else if(a instanceof String)oStr(String.valueOf(a),ind);
			else if(a instanceof Boolean||a instanceof Number)w(a.toString());
			else if(a instanceof Map<?,?>)oMap((Map)a,ind,path);
			else if(a instanceof Collection<?>)oCollctn((Collection)a,ind,path);
			else if(a instanceof Object[])oArray((Object[])a,ind,path);
			else if(a.getClass().isArray())oarray(a,ind,path);
			else if(a instanceof java.util.Date)oDt((java.util.Date)a,ind);
			else if(a instanceof Iterator<?>)oItrtr((Iterator)a,ind,path);
			else if(a instanceof Enumeration<?>)oEnumrtn((Enumeration)a,ind,path);
			else if(a instanceof Throwable)oThrbl((Throwable)a,ind);
			else if(a instanceof java.util.UUID)w("\"").p(a.toString()).w(c?"\"/*uuid*/":"\"");
			else{w("{\"class\":").oStr(a.getClass().getName(),ind)
					.w(",\"str\":").oStr(String.valueOf(a),ind)
					.w(",\"hashCode\":").oStr(Long.toHexString(a.hashCode()),ind);
				if(c)w("}//Object&cachePath=\"").p(path).w("\"\n").p(ind);
				else w("}");}return this;}

		public Output oStr(String a,String indentation)throws IOException
		{final boolean m=comment;if(a==null)return w(m?" null //String\n"+indentation:"null");
			w('"');for(int n=a.length(),i=0;i<n;i++)
			{char c=a.charAt(i);if(c=='\\')w('\\').w('\\');
			else if(c=='"')w('\\').w('"');
			else if(c=='\n'){w('\\').w('n');if(m)w("\"\n").p(indentation).w("+\"");}
			else if(c=='\r')w('\\').w('r');
			else if(c=='\t')w('\\').w('t');
			else if(c=='\'')w('\\').w('\'');
			else p(c);}return w('"');}

		public Output oDt(java.util.Date a,String indentation)throws IOException
		{if(a==null)return w(comment?" null //Date\n":"null");
			w("{\"class\":\"Date\",\"time\":").p(a.getTime())
				.w(",\"str\":").oStr(a.toString(),indentation);
			if(comment)w("}//Date\n").p(indentation);else w("}");
			return this;}

		public Output oThrbl(Throwable x,String indentation)throws IOException
		{w("{\"message\":").oStr(x.getMessage(),indentation).w(",\"stackTrace\":");
			try{StringWriter sw=new StringWriter();
				x.printStackTrace(new PrintWriter(sw));
				oStr(sw.toString(),indentation);}catch(Exception ex)
			{log("Json.Output.x("+x+"):",ex);}return w("}");}

		public Output oEnumrtn(Enumeration a,String ind,String path)throws IOException
		{final boolean c=comment;
			if(a==null)return c?w(" null //Enumeration\n").p(ind):w("null");
			boolean comma=false;String i2=c?ind+"\t":ind;
			if(c)w("[//Enumeration\n").p(ind);else w("[");
			if(c&&path==null)path="";if(c&&path.length()>0)path+=".";int i=0;
			while(a.hasMoreElements()){if(comma)w(" , ");else comma=true;
				o(a.nextElement(),i2,c?path+(i++):path);}
			return c?w("]//Enumeration&cachePath=\"").p(path).w("\"\n").p(ind):w("]");}

		public Output oItrtr(Iterator a,String ind,String path)throws IOException
		{final boolean c=comment;if(a==null)return c?w(" null //Iterator\n").p(ind):w("null");
			boolean comma=false;String i2=c?ind+"\t":ind;
			if(c){w("[//").p(a.toString()).w(" : Itrtr\n").p(ind);
				if(path==null)path="";if(path.length()>0)path+=".";}
			else w("[");int i=0;
			while(a.hasNext()){if(comma)w(" , ");else comma=true;o(a.next(),i2,c?path+(i++):path);}
			return c?w("]//Iterator&cachePath=\"").p(path).w("\"\n").p(ind):w("]");}

		public Output oArray(Object[]a,String ind,String path)throws IOException
		{final boolean c=comment;
			if(a==null)return c?w(" null //array\n").p(ind):w("null");
			String i2=c?ind+"\t":ind;
			if(c){w("[//array.length=").p(a.length).w("\n").p(ind);
				if(path==null)path="";if(path.length()>0)path+=".";}else w("[");
			for(int i=0;i<a.length;i++){if(i>0)w(" , ");o(a[i],i2,c?path+i:path);}
			return c?w("]//cachePath=\"").p(path).w("\"\n").p(ind):w("]");}

		public Output oarray(Object a,String ind,String path)throws IOException
		{final boolean c=comment;
			if(a==null)return c?w(" null //array\n").p(ind):w("null");
			int n= java.lang.reflect.Array.getLength(a);String i2=c?ind+"\t":ind;
			if(c){w("[//array.length=").p(n).w("\n").p(ind);
				if(path==null)path="";if(path.length()>0)path+=".";}else w("[");
			for(int i=0;i<n;i++){if(i>0)w(" , ");o( java.lang.reflect.Array.get(a,i),i2,c?path+i:path);}
			return c?w("]//cachePath=\"").p(path).w("\"\n").p(ind):w("]");}

		public Output oCollctn(Collection o,String ind,String path)throws IOException
		{if(o==null)return w("null");final boolean c=comment;
			if(c){w("[//").p(o.getClass().getName()).w(":Collection:size=").p(o.size()).w("\n").p(ind);
				if(cache==null&&initCache)cache=new HashMap<Object, String>();
				if(cache!=null)cache.put(o,path);
				if(c&&path==null)path="";if(c&&path.length()>0)path+=".";
			}else w("[");
			Iterator e=o.iterator();int i=0;
			if(e.hasNext()){o(e.next(),ind,c?path+(i++):path);
				while(e.hasNext()){w(",");o(e.next(),ind,c?path+(i++):path);}}
			return c?w("]//").p(o.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind) :w("]");}

		public Output oMap(Map o,String ind,String path) throws IOException
		{if(o==null)return w("null");final boolean c=comment;
			if(c){w("{//").p(o.getClass().getName()).w(":Map\n").p(ind);
				if(cache==null&&initCache)cache=new HashMap<Object, String>();
				if(cache!=null)cache.put(o,path);}else w("{");
			Iterator e=o.keySet().iterator();Object k,v;
			if(e.hasNext()){k=e.next();v=o.get(k);
				o(k,ind,c?path+k:path);w(":");o(v,ind,c?path+k:path);}
			while(e.hasNext()){k=e.next();v=o.get(k);w(",");
				o(k,ind,c?path+k:path);w(":");o(v,ind,c?path+k:path);}
			if(c) w("}//")
				.p(o.getClass().getName())
				.w("&cachePath=\"")
				.p(path)
				.w("\"\n")
				.p(ind);else w("}");
			return this;}

		Output oBean(Object o,String ind,String path)
		{final boolean c=comment;try{String i2=c?ind+"\t":ind,i3=c?i2+"\t":ind;Class z=o.getClass();
			(c?w("{//").p(z.getName()).w(":Bean\n").p(ind):w("{"))
					.w("\"str\":").o(o.toString(),i2,c?path+".":path)
//		.w(",:").o(o.(),i2,c?path+".":path)
			;Method[]a=z.getMethods();//added 2015.11.21
			for(Method m:a){String n=m.getName();
				if(n.startsWith("get")&&m.getParameterTypes().length==0)//.getParameterCount()
					w("\n").w(i2).w(",").p(n).w(':').o(m.invoke(o), i3, path+'.'+n);}
			if(c)w("}//").p(o.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind);
			else w("}");}catch(Exception ex){error(ex,"Json.Output.Bean:");}return this;}


		Output clrSW(){if(w instanceof StringWriter){((StringWriter)w).getBuffer().setLength(0);}return this;}

		Output flush() throws IOException{w.flush();return this;}

	} //class Output

 
public class Prsr {

 public StringBuilder buff=new StringBuilder() ,lookahead=new StringBuilder();
 public Reader rdr;public File f;public long fz,lm;public Object o;

public String comments=null;
public char c;Map<String,Object>cache=null;

enum Literal{Undefined,Null};//,False,True

public static Object parse(String p)throws Exception{
	Prsr j=new Prsr();j.rdr=new java.io.StringReader(p);return j.parse();}

public static Object parseItem(Reader p)throws Exception{
	Prsr j=new Prsr();j.rdr=p;return j.parseItem();}

public Object load(File f){
	long l=(this.f=f).lastModified();
	if( lm>=l)return o;
	lm=l;fz=f.length();
	try{rdr= new FileReader(f);
		o=parse();
	}catch(Exception ex){}
	return o;}

 /**skip Redundent WhiteSpace*/void skipRWS(){
	boolean b=Character.isWhitespace(c);
	while(b && c!='\0'){
		char x=peek();
		if(b=Character.isWhitespace(x))
			nxt();
	}
 }

 void skipRWSx(char...p){
	skipRWS();
	char x=peek();int i=-1,n=p==null?0:p.length;boolean b=false;
	do{
		if((b=++i<n)&&p[i]==x){
			b=false;nxt();
		}
	}while(b);
 }// boolean chk(){boolean b=Character.isWhitespace(c)||c=='/';while(b && c!='\0'){//Character.isWhitespace(c)||)char x=peek();if(c=='/' &&(lookahead("//") || lookahead("/*"))){	skipWhiteSpace();b=Character.isWhitespace(c);}else if(x=='/' &&(lookahead(x+"//") || lookahead(x+"/*") )){}else{	if(b=Character.isWhitespace(x))nxt();}}return false;}

 public Object parse()throws Exception{
	Object r=c!='\0'?parseItem():null;
	skipWhiteSpace();if(c!='\0')
	{LinkedList l=new LinkedList();l.add(r);
	 while(c!='\0'){
		r=parseItem();
		l.add(r);
	}r=l;}
	return r;
 }

 public Object parseItem()throws Exception{
  Object r=null;int i;skipWhiteSpace();switch(c)
  { case '"':case '`':r=extractStringLiteral();break;
	case '0':case '1':case '2':case '3':case '4':
	case '5':case '6':case '7':case '8':case '9':
	case '-':case '+':case '.':r=extractDigits();break;
	case '[':r=extractArray();break;
	case '{':Map m=extractObject();
		r=m==null?null:m.get("class");
		if("date".equals(r)){r=m.get("time");
			r=new Date(((Number)r).longValue());}
		else r=m;break;
	case '|':/**raw text , using Designated-Boundary-Text (DBT), the Designated-Boundary-Text may have a length of zero or one or more
		//|<Designated-Boundary-Text ,ending with | bar><then raw-text, until the same pattern repeats which is |<Designated-Boundary-Text>|
		//e.g. ||text maybe containing newlines ,and/or backslashes ,and/or a single hash  ,and/or single-quotation ,and/or double-quotation, and/or back-tic||
		//but in case the text(before-encoding) has two-consecutive-bar then the DBT length has to be longer than zero 
		//e.g. |-| I'm raw text with two consecutive bars ||, the end|-|
		//or instead of a single dash, any specified text as Designated-Boundary-Text, then bar*/
		char h=c,z='\0';bNxt();
		while(c!=z && c!=h )
			bNxt();
		buff();
		String boundary=consume();
		boolean b=true;nxt();
		while(c!=z && b)
			if(c==h && lookahead(boundary))
				b=false;
			else bNxt();
		r=consume();lookahead.setLength(0);
		nxt();
		break;
	case '(':nxt();
		//if(lookahead("sql:"));else//e.g. (sql:q2d(<sql-str>):params:<json-array of params>)
		//if(lookahead("R["));else
		{
		skipRWS();//skipWhiteSpace();
		r=parseItem();
		skipWhiteSpace();
		if(c==')')
			nxt();
		else{LinkedList l=new LinkedList();
			l.add(r);
			while(c!=')' && c!='\0'){
				r=parseItem();
				l.add(r);
				skipWhiteSpace();
		}if(c==')')
			nxt();
		r=l;}}break;
	default:r=extractIdentifier();
	}skipRWS();//skipWhiteSpace();
	if(comments!=null&&((i=comments.indexOf("cachePath=\""))!=-1
		||(cache!=null&&comments.startsWith("cacheReference"))))
	{	if(i!=-1)
		{	if(cache==null)
				cache=new HashMap<String,Object>();
			int j=comments.indexOf("\"",i+=11);
			cache.put(comments.substring(i,j!=-1?j:comments.length()),r);
		}else 
			r=cache.get(r);
		comments=null;
	}
  return r;}

 public String extractStringLiteral()throws Exception{
	char first=c;nxt();boolean b=c!=first&&c!='\0';
	while(b)
	{if(c=='\\'){nxt();switch(c)
		{case 'n':buff('\n');break;case 't':buff('\t');break;
			case 'r':buff('\r');break;case '0':buff('\0');break;
			case 'x':case 'X':buff( (char)
				java.lang.Integer.parseInt(
					next(2)//p.substring(offset,offset+2)
					,16));nxt();//next();
			break;
			case 'u':
			case 'U':buff( (char)
			java.lang.Integer.parseInt(
				next(4)//p.substring(offset,offset+4)
				,16));//next();next();next();//next();
			break;default:if(c!='\0')buff(c);}}
		else buff(c);
		nxt();b=c!=first&&c!='\0';
	}if(c==first)nxt();return consume();}

 public Object extractIdentifier(){
	while(!Character.isUnicodeIdentifierStart(c))
	{System.err.println("unexpected:"+c+" at row,col="+rc());nxt();return Literal.Null;}
	bNxt();
	while(c!='\0'&&Character.isUnicodeIdentifierPart(c))bNxt();
	String r=consume();
	return "true".equals(r)?new Boolean(true)
		:"false".equals(r)?new Boolean(false)
		:"null".equals(r)?Literal.Null:"undefined".equals(r)?Literal.Undefined:r;}

 public Object extractDigits(){
	if(c=='0')//&&offset+1<len)
	{char c2=peek();if(c2=='x'||c2=='X')
	{nxt();nxt();
		while((c>='A'&&c<='F')
			||(c>='a'&&c<='f')
			||Character.isDigit(c))bNxt();
		String s=consume();
		try{return Long.parseLong(s,16);}
		catch(Exception ex){}return s;}
	}boolean dot=c=='.';
	bNxt();//if(c=='-'||c=='+'||dot)bNxt();else{c=p.charAt(i);}
	while(c!='\0'&&Character.isDigit(c))bNxt();
	if(!dot&&c=='.'){dot=true;bNxt();}
	if(dot){while(c!='\0'&&Character.isDigit(c))bNxt();}
	if(c=='e'||c=='E')
	{dot=false;bNxt();if(c=='-'||c=='+')bNxt();
		while(c!='\0'&&Character.isDigit(c))bNxt();
	}else if(c=='l'||c=='L'||c=='d'||c=='D'||c=='f'||c=='F')bNxt();
	String s=consume();//p.substring(i,offset);
	if(!dot)try{return Long.parseLong(s);}catch(Exception ex){}
	try{return Double.parseDouble(s);}catch(Exception ex){}return s;}

 public List<Object> extractArray()throws Exception{
	if(c!='[')return null;
	nxt();char x=0;
	LinkedList<Object> l=new LinkedList<Object>();
	Object r=null;
	skipWhiteSpace();
	if(c!='\0'&&c!=']')
	{	r=parseItem();
		l.add(r);
	}if(c!='\0'&&c!=']')
		skipRWSx(']',',');//skipRWS();x=peek();if(x==']'||x==',') nxt();//skipWhiteSpace();
	while(c!='\0'&&c!=']')
	{	if(c!=','&&!Character.isWhitespace(c))//throw new IllegalArgumentException
			System.out.println("Array:"+rc()+" expected ','");
		nxt();
		r=parseItem();
		l.add(r);
		skipRWSx(']',',');//skipRWS();x=peek();if(x==']'||x==',')nxt();//skipWhiteSpace();
	}if(c==']')
		nxt();
	 skipRWS();
	return l;}

 public Map<Object,Object> extractObject()throws Exception{
	final char bo='{',bc='}';
	if(c==bo)nxt();
	else return null;
	skipWhiteSpace();
	HashMap<Object,Object> r=new HashMap<Object,Object>();
	Object k,v;Boolean t=new Boolean(true);
	while(c!='\0'&&c!=bc)
	{v=t;
		k=parseItem();//if(c=='"'||c=='\''||c=='`')k=extractStringLiteral();else k=extractIdentifier();
		skipWhiteSpace();
		if(c==':'||c=='='){//||Character.isWhitespace(c)
			nxt();
			v=parseItem();
			skipWhiteSpace();
		}//else if(c==','){nxt();
		if(c!='\0'&&c!=bo){
			if(c!=',')
				System.out.print(//throw new IllegalArgumentException(
				"Object:"+rc()+" expected '"+bc+"' or ','");
			nxt();
			skipWhiteSpace();
		}
		r.put(k,v);
	}
	if(c==bc)
		nxt();
	skipRWS();
	return r;}

 public void skipWhiteSpace(){
	boolean b=false;do{
	while(b=Character.isWhitespace(c))nxt();
	b=b||(c=='/'&&skipComments());}while(b);}

 public boolean skipComments(){
  char c2=peek();if(c2=='/'||c2=='*'){nxt();nxt();
	StringBuilder b=new StringBuilder();if(c2=='/')
	{while(c!='\0'&&c!='\n'&&c!='\r')bNxt();
		if(c=='\n'||c=='\r'){nxt();if(c=='\n'||c=='\r')nxt();}
	}else
	{while(c!='\0'&&c2!='/'){bNxt();if(c=='*')c2=peek();}
		if(c=='*'&&c2=='/'){b.deleteCharAt(b.length()-1);nxt();nxt();}
	}comments=b.toString();return true;}return false;}

 /**read a char from the rdr*/
 char read(){
	int h=-1;try{h=rdr.read();}
	catch(Exception ex){error(ex, "json.prsr.read");}
	char c= h==-1?'\0':(char)h;
	return c;}

 public char peek(){
	char c='\0';
	int n=lookahead.length();
	if(n<1){
		c=read();
		lookahead.append(c);}
	else c=lookahead.charAt(0);
	return c;}

 public int _row,_col;String rc(){return "("+_row+','+_col+')';}
 public void nlRC(){_col=1;_row++;}public void incCol(){_col++;}
 //boolean eof,mode2=false;
 public char setEof(){return c='\0';}

 /**update the instance-vars (if needed):c,row,col,eof*/
 public char nxt(char h){
	if(h=='\0'||h==-1||c=='\0')return setEof();
	//if(c=='\0')return setEof();//c='\0';
	else c=h;
	if(c=='\n')
		nlRC();
	else incCol();
	return c;}

 /**put into the buffer the current c , and then call nxt()*/
 public char bNxt(){buff();return nxt();}
 
 /**read from the reader a char and store the read char into member-variable c, @returns member-variable c*/
 public char nxt(){
	char h='\0';
	if(c=='\0')return setEof();//=h;
	if(lookahead.length()>0){
		h=lookahead.charAt(0);
		lookahead.deleteCharAt(0);
	}else h=read();
	c=nxt(h);
	return c;}

 /**this method works differently than next(), in particular how char c is read and buffered*/
 public String next(int n)
 {String old=consume(),retVal=null;while(n-->0)buff(nxt());retVal=consume();buff.append(old);return retVal;}

 public char buff(){return buff(c);}
 char buff(char p){buff.append(p);return p;}
 
 /**empty the member-variable buff , @returns what was stored in buff*/
 public String consume(){String s=buff.toString();buff.replace(0, buff.length(), "");return s;}

 public boolean lookahead(String p,int offset){
	int i=0,pn=p.length()-offset,ln=lookahead.length();
	boolean b=false;char c=0,h=0;if(pn>0)
	do{h=p.charAt(i+offset);
		if(i<ln)
			c=lookahead.charAt(i);
		else{
			c=read();
			lookahead.append(c);
		}
	}while( (b=(c==h || 
		Character.toUpperCase(c)==
		Character.toUpperCase(h))
		)&& (++i)<pn );
	return b;}

 public boolean lookahead(String p){return lookahead(p,0);}

 }//Prsr
}//class Json


/**calls the servlet log method*/
public static void log(Object...s){logA(s);}
public static void logA(Object[]s){/*try{
	jo().clrSW();
	for(Object t:s)jo.w(String.valueOf(t));
	String t=jo.toStrin_();
	//h.getServletContext().log(t);
	if(h.logOut)out.flush().w(h.comments[0]).w(t).w(h.comments[1]);
}catch(Exception ex){ex.printStackTrace();}*/}

public void error(Throwable x,Object...p){/*try{
	String s=jo().clrSW().w("error:").o(p,x).toString();
	h.getServletContext().log(s);
	if(h.logOut)out.w(h.comments[0]//"\n/*
	).w("error:").w(s.replaceAll("<", "&lt;"))
		            .w("\n---\n").o(x).w(h.comments[1] );
	if(x!=null)x.printStackTrace();}
catch(Exception ex){ex.printStackTrace();}*/}

public static class DB {
 enum context{
	pool("MysqlConnectionPoolDataSource")
	,reqCon("javax.sql.PooledConnection")
	,server("localhost")
	,dbName("props")
	,un("root")
	,pw("root")
	;String str,a[];DB(String...p){str=p[0];a=p;}
 }//context

 /**returns a jdbc pooled Connection.
	 uses MysqlConnectionPoolDataSource with a database from the enum context.url.str,
	 sets the pool as an application-scope attribute named context.pool.str
	 when first time called, all next calls uses this context.pool.str*/
 public static synchronized Connection c()throws SQLException {
		Connection r=(Connection)m(context.reqCon.str);
		if(r!=null)return r;
		MysqlConnectionPoolDataSource d=(MysqlConnectionPoolDataSource)global.m(context.pool.str);
		if(d!=null){r=d.getPooledConnection().getConnection();
			return m(context.reqCon.str,r);//if(r!=null)
		}
		else try
		{int x=0;//context.getContextIndex(t);
			if(x!=-1)
			{	r=c(t,x,x,x,x);//t.log("DB.c:1:c2:",p);
				return r;}
		}catch(Throwable e){t.error(e,"DB.MysqlConnectionPoolDataSource:throwable:");}//ClassNotFoundException
		
		if(r==null)try
		{r=java.sql.DriverManager.getConnection
			("jdbc:mysql://"+context.server.str
			     +"/"+context.dbName.str
			    ,context.un.str,context.pw.str
			);Object[]b={r,null};
			m(context.reqCon.str,b);
		}catch(Throwable e){t.error(e,"DB.DriverManager:");}
		return r;}

	public static synchronized Connection c(int idb,int iun,int ipw,int isr) throws SQLException{
		MysqlConnectionPoolDataSource d=new MysqlConnectionPoolDataSource();
		String ss=null,s=context.dbName.a[Math.min(context.dbName.a.length-1,idb)];
		//if(t.h.logOut)ss="\ndb:"+s;
		d.setDatabaseName(s);d.setPort(3306);
		s=context.server.a[Math.min(context.server.a.length-1,isr)];
		//if(t.h.logOut)ss+="\nsrvr:"+s;
		d.setServerName(s);
		s=context.un.a[Math.min(context.un.a.length-1,iun)];if(t.h.logOut)ss+="user:"+s;
		d.setUser(s);
		s=context.pw.a[Math.min(context.pw.a.length-1,ipw)];if(t.h.logOut)ss+="\npw:"+s;
		d.setPassword(s);
		Connection r=d.getPooledConnection().getConnection();
		global.m(context.pool.str,d);//t.h.a(context.pool.str,d);
		m(context.reqCon.str,r);//Object[]a={d,r,ss};//,b={r,null};t.s(context.reqCon.str,b);
		//stack(t,r);
		return r;}

	/**returns a jdbc-PreparedStatement, setting the variable-length-arguments parameters-p, calls dbP()*/
	public static PreparedStatement p( String sql, Object...p)throws SQLException{return P(sql,p);}
	/**returns a jdbc-PreparedStatement, setting the values array-parameters-p, calls TL.dbc() and log()*/
	public static PreparedStatement P(String sql,Object[]p)throws SQLException{return P(sql,p,true);}
	public static PreparedStatement P(String sql,Object[]p,boolean odd)throws SQLException {
		Connection c=c();//TL t=tl();
		PreparedStatement r=c.prepareStatement(sql);
		//if(t.h.logOut)	t.log(Name,"("+t+").DB.P(sql="+sql+",p="+p+",odd="+odd+")");
		if(odd){if(p.length==1)
			r.setObject(1,p[0]);else
			for(int i=1,n=p.length;p!=null&&i<n;i+=2)if((!(p[i] instanceof List)) ) // ||!(p[i-1] instanceof List)||((List)p[i-1]).size()!=2||((List)p[i-1]).get(1)!=Tbl.Co.in )
				r.setObject(i/2+1,p[i]);//if(t.logOut)TL.log("dbP:"+i+":"+p[i]);
		}else
			for(int i=0;p!=null&&i<p.length;i++)
			{r.setObject(i+1,p[i]);}//if(t.h.logOut)t.log("dbP:"+i+":"+p[i]);
		return r;}//if(t.h.logOut)t.log("dbP:sql="+sql+":n="+(p==null?-1:p.length)+":"+r);

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
		ResultSet s=null;try{s=R(sql,p);return s.next()?s.getObject(1):null;}finally{close(s,true);}}
	public static <T>T q1(String sql,Class<T>t,Object[]p)throws SQLException {
		ResultSet s=null;try{s=R(sql,p);return s.next()?s.getObject(1,t):null;}finally{close(s,true);}}
	/**returns an integer or df, which the result of executing sql,
	 calls dpR() to set the variable-length-arguments parameters-p*/
	public static int q1int(String sql,int df,Object...p)throws SQLException{return q1Int(sql,df,p);}
	public static int q1Int(String sql,int df,Object[]p)throws SQLException
	{ResultSet s=null;try{s=R(sql,p);return s.next()?s.getInt(1):df;}finally{close(s,true);}}//CHANGED:2015.10.23.16.06:closeRS ;
	/**returns a double or df, which is the result of executing sql,
	 calls dpR() to set the variable-length-arguments parameters-p*/
	public static double q1dbl(String sql,double df,Object...p)throws SQLException
	{ResultSet s=null;try{s=R(sql,p);return s.next()?s.getDouble(1):df;}finally{close(s,true);}}//CHANGED:2015.10.23.16.06:closeRS ;
	/**returns as an array of rows of arrays of columns of values of the results of the sql
	 , calls dbL() setting the variable-length-arguments values parameters-p*/
	public static Object[][]q(String sql,Object...p)throws SQLException{return Q(sql,p);}
	public static Object[][]Q(String sql,Object[]p)throws SQLException
	{List<Object[]>r=L(sql,p);Object b[][]=new Object[r.size()][];r.toArray(b);r.clear();return b;}
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
				}}return r;}finally{close(s,true);//CHANGED:2015.10.23.16.06:closeRS ;
			//if(t.h.logOut)try{t.log(t.jo().w(Name).w(".DB.L:sql=").o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}
			catch(IOException x){error(x,"DB.List:",sql);}}}

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

	public static List<Object> q1colList(String sql,Object...p)throws SQLException
	{ResultSet s=null;List<Object> r=null;try{s=R(sql,p);r=new LinkedList<Object>();
		while(s.next())r.add(s.getObject(1));return r;}
		finally{close(s,true);
			//TL t=tl();if(t.h.logOut)try{t.log(t.jo().w(Name).w(".DB.q1colList:sql=")//CHANGED:2015.10.23.16.06:closeRS ;
			//.o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){t.error(x,Name,".DB.q1colList:",sql);}
		}
	}

	public static <T>List<T> q1colTList(String sql,Class<T>t,Object...p)throws SQLException
	{ResultSet s=null;List<T> r=null;try{s=R(sql,p);r=new LinkedList<T>();//Class<T>t=null;
		while(s.next())r.add(
			s.getObject(1,t)
			//s.getObject(1)
		);return r;}
		finally{close(s,tl);//TL tl=tl();if(tl.h.logOut)try{tl.log(tl.jo().w(Name).w(".DB.q1colList:sql=")//CHANGED:2015.10.23.16.06:closeRS ;
		//.o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){tl.error(x,Name,".DB.q1colList:",sql);}
	}}
	public static Object[] q1col(String sql,Object...p)throws SQLException
	{List<Object> l=q1colList(sql,p);Object r[]=new Object[l.size()];l.toArray(r);l.clear();return r;}
	public static <T>T[] q1colT(String sql,Class<T>t,Object...p)throws SQLException
	{List<T> l=q1colTList(sql,t,p);T[]r=(T[]) java.lang.reflect.Array.newInstance(t,l.size());l.toArray(r);l.clear();return r;}
	/**returns a row of columns of the result of sql
	 ,calls dbR(),dbcc(),and dbclose(ResultSet,TL.dbc())*/
	public static Object[] q1row(String sql,Object...p)throws SQLException{return q1Row(sql,p);}
	public static Object[] q1Row(String sql,Object[]p)throws SQLException
	{ResultSet s=null;try{s=R(sql,p);Object[]a=null;int cc=cc(s);if(s.next())
	{a=new Object[cc];for(int i=0;i<cc;i++)try{a[i]=s.getObject(i+1);}
	catch(Exception ex){error(ex,Name,".DB.q1Row:",sql);a[i]=s.getString(i+1);}}
		return a;}finally{close(s,true);}}//CHANGED:2015.10.23.16.06:closeRS ;
	/**returns the result of (e.g. insert/update/delete) sql-statement
	 ,calls dbP() setting the variable-length-arguments values parameters-p
	 ,closes the preparedStatement*/
	public static int x(String sql,Object...p)throws SQLException{return X(sql,p);}
	public static int X(String sql,Object[]p)throws SQLException {
		int r=-1;Connection c;PreparedStatement s;try{
			s=P(sql,p,false);r=s.executeUpdate();c=s.getConnection();return r;}
		finally{s.close();c.close();
			//TL t=tl();if(t.h.logOut)try{t.log(t.jo().w(Name).w(".DB.x:sql=").o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){t.error(x,Name,".DB.X:",sql);}
		}}
	/**output to tl.out the Json.Output.oRS() of the query*/
	public static void q2json(String sql,Object...p)throws SQLException{
		ResultSet s=null;//TL tl=tl();
		try{
			s=R(sql,p);
			try{
				//tl.getOut() .o(s); // (new Json.Output()) // TODO:investigate where the Json.Output.w goes
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
			try {init(TL.DB.R(sql, p));}
			catch (Exception e) {error(e,".DB.ItTbl.<init>:Exception:sql=",sql,",p=",p);}}
		public ItTbl(ResultSet o) throws SQLException{init(o);}
		public ItTbl init(ResultSet o) throws SQLException
		{row.rs=o;row.m=o.getMetaData();row.row=row.col=0;
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

		public Json.Output jsonOutput(TL.Json.Output o,String ind,String path)throws java.io.IOException{return jsonOutput( o,ind,path,true );}
		public Json.Output jsonOutput(TL.Json.Output o,String ind,String path,boolean closeBrace)throws java.io.IOException{
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
					o.w("}//TL.DB.Tbl&cachePath=\"").p(path).w("\"\n").p(ind);
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
			}catch (Exception ex) {tl().error(ex,Name,".DB.Tbl.v(",this,",",p,",",v,")");}
			return this;}

		public Object v(Field p){//this is beautiful(tear running down cheek)
			try{return p.get(this);}
			catch (Exception ex) {//IllegalArgumentException,IllegalAccessException
				tl().error(ex,Name,".DB.Tbl.v(",this,",",p,")");return null;}}

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

		//public static String sql(CI[]cols,Object[]where,CI[]groupBy,String name) { return sql(cols,where,groupBy,null,name);}
		//public String sql(CI[]cols,Object[]where,CI[]groupBy,CI[]orderBy){ if(cols==null)cols=columns();return sql(cols,where,groupBy,orderBy,getName());}

		public String sql(String cols,Object[]where,CI[]groupBy,CI[]orderBy) {
			StringBuilder sql=new StringBuilder("select ");
			sql.append(cols);//Co.generate(sql,cols);
			sql.append(" from `").append(getName()).append("` ");
			if(where!=null&&where.length>0)
				TL.DB.Tbl.Co.where(sql, where);
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
				TL.DB.Tbl.Co.where(sql, where);
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
		public abstract List creationDBTIndices(TL tl);
		public void checkDBTCreation(TL tl){
			String dtn=getName();Object o=tl.h.a(Name+":db:show tables");
			if(o==null)
				try {o=TL.DB.q1colList("show tables");
					tl.h.a(Name+":db:show tables",o);
				} catch (SQLException ex) {
					tl.error(ex, Name+".DB.Tbl.checkTableCreation:check-pt1:",dtn);}
			List l=(List)o;
			try{if(o==null||(!l.contains( dtn )&&!l.contains( dtn.toLowerCase()))){
				StringBuilder sql= new StringBuilder("CREATE TABLE `").append(dtn).append("` (\n");
				CI[]ci=columns();int an,x=0;
				List a=creationDBTIndices(tl),b=(List)a.get(0);
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
				tl.log(Name,".DB.Tbl.checkTableCreation:before:sql=",sql);
				int r=TL.DB.x(sql.toString());
				tl.log(Name,".DB.Tbl.checkTableCreation:executedSql:",dtn,":returnValue=",r);
				b=an>2?(List)a.get(2):b;if(an>2)
					for(Object bo:b){
						List c=(List)bo;
						Object[]p=new Object[c.size()];
						c.toArray(p);
						vals(p);
						try {save();} catch (Exception ex) {
							tl.error(ex, Name,".DB.Tbl.checkTableCreation:insertion",c);}
					}
			}
			} catch (SQLException ex) {
				tl.error(ex, Name,".DB.Tbl.checkTableCreation:errMain:",dtn);}
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
			ResultSet r=null;TL t=tl();
			try{r=DB.r(sql(cols(Co.all), where(pkc()))
				,pk);
				if(r.next())load(r);
				else{t.error(null,Name,".DB.Tbl(",this,").load(pk=",pk,"):resultset.next=false");nullify();}}
			catch(Exception x){t.error(x,Name,".DB.Tbl(",this,"):",pk);}
			finally{DB.close(r,t);}
			return this;}
		public Tbl nullify(){return nullify(fields());}
		public Tbl nullify(Field[]a){for(Field f:a)v(f,null);return this;}
		// /**loads one row from the table*/ Tbl load(){return load(pkv());}

		/**loads one row using column CI c */
		Tbl loadBy(CI c,Object v){
			try{Object[]a=DB.q1row(sql(cols(Co.all),where(c)),v);
				vals(a);}
			catch(Exception x){tl().error(x,Name,".DB.Tbl(",this,").loadBy(",c,",",v,")");}
			return this;}//loadBy

		Tbl save(CI c){
			CI pkc=pkc();
			Object cv=v(c),pkv=pkv();TL t=TL.tl();
			if(cv instanceof Map)try
			{String j=t.jo().clrSW().o(cv).toString();cv=j;}
			catch (IOException e) {t.error(e,Name,".DB.Tbl.save(CI:",c,"):");}
			try{DB.x("replace into `"+getName()+"` (`"+pkc+
				         "`,`"+c+"`) values(?"//+Co.m(pkc).txt
				         +",?"//+Co.m(c).txt
				         +")",pkv,cv);
				Integer k=(Integer)pkv;
				//TL.DB.Tbl.Log.log( TL.DB.Tbl.Log.Entity.valueOf(getName()), k, TL.DB.Tbl.Log.Act.Update, TL.Util.mapCreate(c,v(c)) );
			}catch(Exception x){tl().error(x
				,Name,".DB.Tbl(",this,").save(",c,"):pkv=",pkv);}
			return this;}//save

		/**store this entity in the dbt , if pkv is null , this method uses the max+1 */
		public Tbl save() throws Exception{
			Object pkv=pkv();CI pkc=pkc();boolean nw=pkv==null;//Map old=asMap();
			if(nw){
				int x=DB.q1int(//"select max(`" +pkc+"`)+1 from `"+getName()+"`"
					sql("max(`"+pkc+"`)+1",null,null,null)
					,1);
				v(pkc,pkv=x);
				tl().log(Name,".DB.Tbl(",toJson(),").save-new:max(",pkc,") + 1:",x);
			}CI[]cols=columns();
			StringBuilder sql=new StringBuilder("replace into`").append(getName()).append("`( ");
			Co.generate(sql, cols);//.toString();
			sql.append(")values(").append(Co.prm.txt);//Co.m(cols[0]).txt
			for(int i=1;i<cols.length;i++)
				sql.append(",").append(Co.prm.txt);//Co.m(cols[i]).txt
			sql.append(")");//int x=
			DB.X( sql.toString(), vals() ); //TODO: investigate vals() for json columns
			tl().log("save");//log(nw?TL.DB.Tbl.Log.Act.New:TL.DB.Tbl.Log.Act.Update);
			return this;}//save
		public Tbl readReq_save() throws Exception{
			Map old=asMap();
			readReq("");
			Map val=asMap();
			for(CI c:columns()){String n=c.f().getName();
				Object o=old.get(n),v=val.get(n);
				if(o==v ||(o!=null && o.equals(v)))
				{val.remove(n);old.remove(n);}
				else save(c);}
			//log(TL.DB.Tbl.Log.Act.Update,old);
			return this;
		}//readReq_save
		public Tbl readReq_saveNew() throws Exception{
			Object pkv=pkv();readReq("");if(pkv()==null&&pkv!=null)v(pkc(),pkv);
			return save();//log(TL.DB.Tbl.Log.Act.Update,old);
		}//readReq_save

		//void log(TL.DB.Tbl.Log.Act act){	Map val=asMap();Integer k=(Integer)pkv();TL.DB.Tbl.Log.log( TL.DB.Tbl.Log.Entity.valueOf(getName()), k, act, val);}
		public int delete() throws SQLException{
			Object pkv=pkv();
			int x=TL.DB.x("delete from `"+getName()+"` where `"+pkc()+"`=?", pkv);
			//log(TL.DB.Tbl.Log.Act.Delete);
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
					tl().error(x,Name,".DB.Tbl(",this,").Itrtr.<init>:where=",where);}
			}
			public Itrtr(Object[]where){a=fields();
				try{rs=DB.R(sql(cols(Co.all),where), where);}
				catch(Exception x){tl().error(x,Name,".DB.Tbl(",this,").Itrtr.<init>:where=",where);}}
			@Override public Iterator<Tbl>iterator(){return this;}
			@Override public boolean hasNext(){boolean b=false;
				try {b = rs!=null&&rs.next();} catch (SQLException x)
				{tl().error(x,Name,".DB.Tbl(",this,").Itrtr.hasNext:i=",i,",rs=",rs);}
				if(!b&&rs!=null){DB.close(rs);rs=null;}
				return b;}
			@Override public Tbl next(){i++;Tbl t=Tbl.this;TL tl=TL.tl();
				if(makeClones)try{
					t=t.getClass().newInstance();}catch(Exception ex){
					tl.error(ex,Name,".DB.Tbl(",this,").Itrtr.next:i=",i,":",rs,":makeClones");
				}
				try{t.load(rs,a);}catch(Exception x){
					tl.error(x,Name,".DB.Tbl(",this,").Itrtr.next:i=",i,":",rs);
					close(rs,tl);rs=null;
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
				{tl().error(x,Name,".DB.Tbl.f(",name,c,"):");}
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
								tl().log(Name,".DB.Tbl.Co.where:unknown where-clause item:o=",o);
							b.append(m.txt).append("?");
						}else
							tl().log(Name,".DB.Tbl.Co.where:unknown where-clause item: o=",o);
					}
					else tl().error(null,Name,".DB.Tbl.Col.where:for:",o);
					i++;
				}//for
				return b;}
		}//enum Co

		/**output to jspOut one row of json of this row*/
		public void outputJson(){try{TL.tl().getOut().o(this);}catch(IOException x){tl().error(x,"moh.TL.DB.Tbl.outputJson:IOEx:");}}
		/**output to jspOut rows of json that meet the 'where' conditions*/
		public void outputJson(Object...where){try{
			TL.Json.Output o=TL.tl().getOut();
			o.w('[');boolean comma=false;
			for(Tbl i:query(where)){
				if(comma)o.w(',');else comma=true;
				i.outputJson();}
			o.w(']');
		} catch (IOException e){tl().error(e,Name,".DB.Tbl.outputJson:");}
		}//outputJson(Object...where)
		public static List<Class<? extends Tbl>>registered=new LinkedList<Class<? extends Tbl>>();
		static void check(TL tl){
			for(Class<? extends Tbl>c:registered)try
			{String n=c.getName(),n2=".checkDBTCreation."+n;
				if( tl.h.a(n2)==null){
					Tbl t=c.newInstance();
					t.checkDBTCreation(tl);
					tl.h.a(n2,tl.now);
				}}catch(Exception ex){tl.error( ex,Name,".DB.Tbl.check" );}
		}
	}//class Tbl
}//class DB

static class Util{
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
			TL.tl().error(x, Name,".Util.<T>T parse(String s,T defval):",s,defval);}
		return defval;}

	public static Object parse(String s,Class c){
		if(s!=null)try{if(String.class.equals(c))return s;
		else if(Number.class.isAssignableFrom(c)||c.isPrimitive()) {
			if (Integer.class.equals(c)|| "int"   .equals(c.getName())) return new Integer(s);
			else if (Double .class.equals(c)|| "double".equals(c.getName())) return new Double(s);
			else if (Float  .class.equals(c)|| "float" .equals(c.getName())) return new Float(s);
			else if (Short  .class.equals(c)|| "short" .equals(c.getName())) return new Short(s);
			else if (Long   .class.equals(c)|| "long"  .equals(c.getName())) return new Long(s);
			else if (Byte   .class.equals(c)|| "byte"  .equals(c.getName())) return new Byte(s);
		}///else return new Integer(s);}
		else if(Boolean.class.equals(c)||(c.isPrimitive()&&"boolean".equals(c.getName())))return new Boolean(s);
		else if(Date.class.equals(c))return parseDate(s);
		else if(Character.class.isAssignableFrom(c)||(c.isPrimitive()&&"char".equals(c.getName())))
			return s.length()<1?'\0':s.charAt(0);
		else if(URL.class.isAssignableFrom(c))try {return new URL(
			                                                         "file:" +TL.tl().h.getServletContext().getContextPath()+'/'+s);}
		catch (Exception ex) {TL.tl().error(ex,Name,".Util.parse:URL:p=",s," ,c=",c);}
			boolean b=c==null?false:c.isEnum();
			if(!b){Class ct=c.getEnclosingClass();b=ct==null?false:ct.isEnum();if(b)c=ct;}
			if(b){
				for(Object o:c.getEnumConstants())
					if(s.equalsIgnoreCase(o.toString()))
						return o;
			}
			return Json.Prsr.parse(s);
		}catch(Exception x){//changed 2016.06.27 18:28
			TL.tl().error(x, Name,".Util.<T>T parse(String s,Class):",s,c);}
		return s;}
}//class Util

}//public class Main
//{
import java.io.Writer;
import java.io.IOException;
//import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.lang.reflect.Method;
//import java.net.URL;//import java.lang.reflect.Field;//import java.net.URLConnection;import java.net.HttpURLConnection;//import java.util.Base64;// Java 8
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Enumeration;
import java.util.Date;
//}

class Json extends Thread{
	Map<Object,Object> m;//Connection dbc; used mainly for DB , jo
	static Json sttc;
	static Json tl(){Thread t=Thread.currentThread();return t instanceof Json?(Json)t:sttc;}
	static Object m(Object k){Json tl=tl();Object o=tl.m==null?null:tl.m.get(k);return o;}
	static Object m(Object k,Object v){
		Json tl=tl();if(tl.m==null)tl.m=new HashMap<Object,Object>();tl.m.put(k,v);return v;}
	static Output jo(){
		Object o=m("jo");
		Output j=o instanceof Output?
		(Output)o
		:null;if(j==null)
			m("jo",j=new Output());
		return j;}

	public static void log(Object...s){logA(s);}
	public static void error(Throwable x,Object...p){errorA(x,p);}

	public static void logA(Object[]s){try{
		Output jo=jo();
		jo.clrSW();
		for(Object t:s)jo.w(String.valueOf(t));
		String t=jo.toStrin_();
		//h.getServletContext().log(t);
		System.out.println(t);//if(h.logOut)out.flush().w(h.comments[0]).w(t).w(h.comments[1]);
		}catch(Exception ex){ex.printStackTrace();}}

	public static void errorA(Throwable x,Object[]p){try{
		String s=jo().clrSW().w("error:").o(p,x).toString();
		//h.getServletContext().log(s);if(h.logOut)out.w(h.comments[0]//"\n/ *).w("error:").w(s.replaceAll("<", "&lt;")).w("\n---\n").o(x).w(h.comments[1] );
		System.err.println(s);
		if(x!=null)x.printStackTrace();
		}catch(Exception ex){ex.printStackTrace();}}

	static Object[]a(Object...a){return a;}
	static List<Object> l(Object...a){List<Object>l=new LinkedList<Object>();for(int i=0;i<a.length;i++)l.add(a[i]);return l;}//static List<T>l<T>(T...a){List<T>l=new LinkedList<T>();for(int i=0;i<a.length;i++)l.add(a[i]);return l;}
	static Map<Object,Object> map(Object...a){return ma(new HashMap<Object,Object>(),a);}
	static Map<Object,Object> m(Map<Object,Object> m,Object...a){return ma(m,a);}
	static Map<Object,Object> ma(Map<Object,Object> m,Object[]a){
	for(int i=0;i<a.length;i+=2)m.put(a[i],a[i+1]);
	return m;}

	public static class Output
	{ public Writer w;
		public boolean initCache=false,includeObj=false,comment=false;
		Map<Object, String> cache;
		
		public static void out(Object o,Writer w,boolean initCache,boolean includeObj)throws IOException
		{Output t=new Output(w,initCache,includeObj);
			t.o(o);
			if(t.cache!=null)
			{t.cache.clear();t.cache=null;}
		}

		public static String out(Object o,boolean initCache,boolean includeObj)
		{StringWriter w=new StringWriter();
			try{out(o,w,initCache,includeObj);}
			catch(Exception ex){log("Json.Output.out",ex);}
			return w.toString();
		}

		public static String out(Object o){StringWriter w=new StringWriter();try{out(o,w,
			false,false);}catch(Exception ex){log("Json.Output.out",ex);}return w.toString();}

		public Output(){w=new StringWriter();}

		public Output(Writer p){w=p;}

		public Output(Writer p,boolean initCache,boolean includeObj)
		{w=p;this.initCache=initCache;this.includeObj=includeObj;}

		public Output(boolean initCache,boolean includeObj){this(new StringWriter(),initCache,includeObj);}

		public Output(String p)throws IOException{w=new StringWriter();w(p);}

		public Output(OutputStream p)throws Exception{w=new OutputStreamWriter(p);}

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

		public Output oEnumrtn(Enumeration<Object> a,String ind,String path)throws IOException
		{final boolean c=comment;
			if(a==null)return c?w(" null //Enumeration\n").p(ind):w("null");
			boolean comma=false;String i2=c?ind+"\t":ind;
			if(c)w("[//Enumeration\n").p(ind);else w("[");
			if(c&&path==null)path="";if(c&&path.length()>0)path+=".";int i=0;
			while(a.hasMoreElements()){if(comma)w(" , ");else comma=true;
				o(a.nextElement(),i2,c?path+(i++):path);}
			return c?w("]//Enumeration&cachePath=\"").p(path).w("\"\n").p(ind):w("]");}

		public Output oItrtr(Iterator<Object> a,String ind,String path)throws IOException
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

		public Output oCollctn(Collection<Object> o,String ind,String path)throws IOException
		{if(o==null)return w("null");final boolean c=comment;
			if(c){w("[//").p(o.getClass().getName()).w(":Collection:size=").p(o.size()).w("\n").p(ind);
				if(cache==null&&initCache)cache=new HashMap<Object, String>();
				if(cache!=null)cache.put(o,path);
				if(c&&path==null)path="";if(c&&path.length()>0)path+=".";
			}else w("[");
			Iterator<Object> e=o.iterator();int i=0;
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


public static class Prsr {

	public StringBuilder buff=new StringBuilder() ,lookahead=new StringBuilder();
	public Reader rdr;public File f;public long fz,lm;public Object o;

	public String comments=null;
	public char c;Map<String,Object>cache=null;

	enum Literal{Undefined,Null};//,False,True

	public static Object parse(String p)throws Exception{
		Prsr j=new Prsr();j.rdr=new java.io.StringReader(p);
		j.c=j.read();//2020-02-01 1:01
		return j.parse();}

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
	{LinkedList<Object> l=new LinkedList<Object>();l.add(r);
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
	case '{':Map <Object,Object>m=extractObject();
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
		else{LinkedList<Object>l=new LinkedList<Object>();
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
	return "true".equals(r)?Boolean.TRUE
		:"false".equals(r)?Boolean.FALSE
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
	nxt();//char x=0;
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
	Object k,v;Boolean t=Boolean.TRUE;//new Boolean(true);
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

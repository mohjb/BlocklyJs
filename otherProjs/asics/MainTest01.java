//{
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.net.URLConnection;//import java.net.HttpURLConnection;
//}

public class MainTest01 extends Json{
static String baseDir;
static MainTest01 global;
AsicsScanner scan;
Date now;
List<Asic>asics=new LinkedList<Asic>();

static String dt2path(Date d){
	//DateTimeFormatter df=new DateTimeFormatter("yyyy/MM/dd/HH/mm/");//new SimpleDateFormat("yyyy/MM/dd/HH/mm/");
	String pattern = "yyyy/MM/dd/HH/mm/";//"yyyy-MM-dd";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	String date = simpleDateFormat.format(d);
	return date;}

static String dt2secs(Date d){
		String pattern = ".ss.SSS.";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(d);
		return date;}

static void w(String pf,Date now,String fn,String ext,String x){
	try {
		String p=(baseDir.endsWith("/")?baseDir:baseDir+"/")+(pf!=null?pf:"")+dt2path(now);
		File f=new File(p);
		f.mkdirs();
		Files.write(Paths.get(p,fn+dt2secs(now)+ext),x.getBytes());
	 } catch (Exception e) {
		error(e,"MainTest01.w",fn,x);}}

public static void main(String[]args){
 try{baseDir=new File("./output/").getCanonicalPath();

	new MainTest01();}catch(Exception e){
		Json.error(e,"main");}
}//main 

String fileString(String pth,String fn){
	String src="";try {
		byte[]ba=Files.readAllBytes(//readString(
			Paths.get(pth,fn));
		src=new String(ba);
	} catch (Exception e) {
		error(e, "fileString");
	}
		return src;
	}

MainTest01(){try{
	if(global==null)sttc=global=this;
	//String prefix="192.168.8.";int startPort= 141 ,endPort= 141 ,sleep=5000;
	String src=fileString(
		//"C:/Users/mohjb/Documents/GitHub/BlocklyJs/otherProjs/asics/output/output141/2020/01/10/15/04"
		"D:/apache-tomcat-8.0.15/webapps/ROOT/GitHub/BlocklyJs/otherProjs/asics/output/output141/2020/01/10/15/04"
	,"status.22.713..html");
	String result=	Asic.Path.status.parse.parse(src);//Asic.ParseStatus p=new Asic.ParseStatus(src);
	//Asic a=new Asic();//scan=new AsicsScanner(prefix, startPort, endPort, sleep);
	//scan.start();
	
	w("/"+258+"/",global.now=new Date(),"test-ParseStatus",".json",result);
} catch (Exception e) {
	error(e, "MainTest01.<init>");
}
}

static class Asic extends Json{
	final static String Authorization=
	"Digest username=\"root\", realm=\"antMiner Configuration\", nonce=\"b1b1652793d3109e9b29f0c3a111ffe5\", uri=\"/\", response=\"1fa4707b57aac3ad574bcd0f044f1cc8\", qop=auth, nc=00000001, cnonce=\"680d2ef66564dc19\"";
	URL base;
	int ip;	//String[]wallet;double[]tempr;int blocksFound;
	State state=State.init;
Map<String,Map<String,String>>vals;

	interface Parse{public String parse(String p); }

	static class Parse0 implements Parse{
	@Override public String parse(String p) {return p;}

}

	static class ParseConfig implements Parse{
	@Override public String parse(String p) {
		int i0=p.indexOf("{"),i1=p.indexOf("};");
		String r=p.substring(i0,i1+2);
		return r;}
	}

	static class ParseStatus implements Parse{
		@Override public String parse(String s) {
			String r="";try{
				ParseStatus p=new ParseStatus(s);
				r=Json.jo().clrSW().oMap(p.ids,"","").toStrin_();
			}catch ( IOException x ){
				error(x,"Asic.ParseStatus.parse");}
			return r;
		}//parse

		ParseStatus(String s){
			if(s==null)return;
			ids=new HashMap<String,Object>();
			doc=new Elem(s);
		}

		//static class P extends E{
			/**
			 * doc.txt is the source html-text
			 * doc.i is the current-index of parsing,previously (ix)
			 * doc.headEnd is indexOf "id=\"" ,previously (idix)
			 * doc.nextSibling was previously (parseStatus.c)
			 */
		Elem doc;Map<String,Object>ids;

		/**TextNode of a HTML-TreeNode*/
		class Nd{
			Nd nextSibling,previousSibling;
			Elem parentNode;
			/** is the source html-text */
			String txt;

			/**i:index-of start of textNode ; end:index-of end-of-textNode */
			int i,end;

			Nd(){doc.nextSibling=this;}
			Nd(Elem p){
				parentNode=p;
				Nd n=p.lastChild;
				i=doc.i;//<doc.end&&doc.x.charAt(doc.i)=='>'?doc.i+1:doc.i;//c.end==0?0:c.end+1;//p.headEnd==0?0:p.headEnd+1;;//c.end<doc.end&&doc.x.charAt(c.end)=='>'?c.end+1:c.end;//c.end==0?0:c.end+1;//p.headEnd==0?0:p.headEnd+1;
				if(n!=null)
					n.nextSibling=this;
				previousSibling=n;
				p.lastChild=this;
				if(p.firstChild==null)
					p.firstChild=this;
				doc.nextSibling=this;
			}
			Nd(Elem p,int separator){this(p,doc.i,separator);}
			Nd(Elem p,int start,int separator){
				this(p);i=start;end=separator;if(separator!=-1)
					txt=sub(chr(i)=='>'?i+1:i,doc.i=separator);
			}

			Nd parse(){return this;}//parse

			
			int fnd(String p,int from){
				int t=ixOf(p,from);
				if(t==-1)
					t=doc.end;
				return t;}

			int ixOf(char p,int from){
				int t=doc.txt.indexOf(p,from);
				return t;}

			int ixOf(String p,int from){
				int t=doc.txt.indexOf(p,from);
				return t;}

			String sub(int from,int to){
				String t=doc.txt.substring(from,to);
				return t;}

			char chr(int x){
				char t=doc.txt.charAt(x);
				return t;}

		}//class Nd

		/**Element or TagNode of a HTML-TreeNode, or comment, or frag, basically anything that starts with a &lt; */
		class Elem extends Nd{
			Nd firstChild,lastChild;String id;
			/**i=indexOf('<') headEnd=indexOf('>',i) close=indexOf('<',headEnd) end=indexOf('>',close)*/
			int headEnd,close;
			Elem(Elem p){super(p);}
			Elem(String s){super();
				nextSibling=doc=this;
				txt=s;
				doc.i=i=headEnd=0;
				end=close=s.length();

				int iz=s.indexOf('<');
				headEnd=s.indexOf("id=\"");

				firstChild=lastChild=
					iz==0?new Elem(doc):new Nd(doc,iz);
				if(iz==0)nextSibling.parse();
				while(nextSibling.end<end){//int j=c.end;
					iz=s.indexOf('<',i);
					boolean b=iz-i<2 &&iz>=0;
					nextSibling=b?new Elem(doc):new Nd(doc,i,iz);
					if(b)nextSibling.parse();
				}
			}

			Nd parse(){
				headEnd=close=end=ixOf('>',i);
				char ch=chr(i+1);
				if(ch=='?' || ch=='!'){
					if( ch=='!'
					 && chr(i+2)=='-'
					 && chr(i+3)=='-'
					){	headEnd=close=ixOf("-->",i+6);
						doc.i=(end=close+2)+1;
					}else doc.i=end;
					txt=sub(i,end);
					return this;
				}
				if(end==-1)
				{	doc.i=headEnd=close=end=doc.end;
					return this;
				}doc.i=++end;
				int e =fnd(" ",i);
				txt=sub(i+1,e>headEnd?(e=headEnd):e);
				int d=doc.headEnd<end?doc.headEnd:doc.end
					,q=d>end?d:ixOf("\"",d+4);
				if(d==doc.headEnd)
					doc.headEnd=fnd("id=\"",close);
				if(d!=-1&&q!=-1&&q>=d+4)//id=d==-1||q==-1||q<d+4?null:doc.x.substring(d+4,q);
				{	id=sub(d+4,q);
					Object o=ids.get(id);
					if(o==null){
						ids.put(id, this);
						//System.out.println("ParseStatus:html-parser@"+doc.i+" :id="+id);
					}
					else{
						boolean b=o instanceof List;
						List<Elem>l=b?(List<Elem>)o:null;
						if(b){
							l.add(this);
							//System.out.println("ParseStatus:html-parser@"+doc.i+" :id="+id+": addTo-list:ListLen="+l.size());
						}
						else{
							ids.put(id,l=new LinkedList<Elem>());
							l.add((Elem)o);
							l.add(this);
							//System.out.println("ParseStatus:html-parser@"+doc.i+" :id="+id+" : created-list------------------------");
						}
					}
				}e=headEnd;
				if(chr(headEnd-1)=='/')
					close=headEnd-1;           //leaf
				else do{
					e =ixOf('<',d=doc.i);//d=e
					if(e==-1){
						if(headEnd<(doc.i=end=doc.end))
							new Nd(this,d,doc.end);
					}else {
						if ( e > d )//headEnd + 1
							new Nd( this ,e);
						if ( chr( e + 1 ) == '/' ) {
							doc.i=end = close = e;
							c = this;
							e += txt.length()+2;
							if ( txt.equalsIgnoreCase( sub( end + 2, e ) ) ) {
								doc.i=end = ixOf( ">", e );
								if ( end == -1 )
									doc.i=end = doc.end;
								else
									doc.i++;
							}
							return this;
						}else{
							Elem x=new Elem(this);
							x.parse();
							e=doc.i;//x.end;
						}
					}
				}while(e!=-1 && e<doc.end);
				return this;
			}//parse

			String txt(){return txt(new StringBuffer()).toString();}
			StringBuffer txt(StringBuffer b){
				Nd n=firstChild;
				while(n!=null){
					if(n instanceof Elem)
						((Elem)n).txt(b);
					else
						b.append(n.txt.trim());
					n=n.nextSibling;
				}
				return b;
			}

			String val(){
				Nd n=firstChild;return n==lastChild&&!(n instanceof Elem)?n.txt:null;}

		}//class E

		//}//class P

	}//class ParseStatus

	static Parse parse0=new Parse0()
		,parseConfig=new ParseConfig()
		,parseStatus=new ParseStatus(null);

	enum State{init,detectAsic,invalidWallet,wallet,config,monitor};
	enum Path{info("/cgi-bin/get_system_info.cgi",parse0)
		,config("/cgi-bin/minerConfiguration.cgi",parseConfig)
		,net("/cgi-bin/get_network_info.cgi",parse0)//,sys("")
		,status("/cgi-bin/minerStatus.cgi",parseStatus);
		String s;Parse parse;
		Path(String p,Parse prse){
			s=p;parse=prse;}
	}

	public void f(Path p)throws Exception{
		URL url = new URL(base,p.s);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", Authorization);
		InputStream is = urlConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		int numCharsRead;
		char[] charArray = new char[16383];
		StringBuffer sb = new StringBuffer();
		while ((numCharsRead = isr.read(charArray)) > 0)
			sb.append(charArray, 0, numCharsRead);
		String result = sb.toString();
		//TODO: implement parsing Path.info result string
		w(ip+"/",global.now,p.toString(),".html",p.parse.parse(result));
	}

	public void startScan()throws Exception{
		f(Path.info);
		global.scan.asics.remove(this);
		global.asics.add(this);
	}

	public void startMonitor(){
		while(ip>=0)
		try{
			for(Path p:Path.values())
				f(p);
			Thread.sleep(global.scan.sleep);
		}catch(Exception ex){error(ex,"Asic.startMonitor",base);}//,ipPrefix,p
	}

	public void run(){try{
		startScan();
		startMonitor();
		}catch(Exception ex){
			error(ex,"Asic.run",base);}
	}

	public Asic(String ipPrefix,int ip){
		try{base=new URL("http",ipPrefix+(this.ip=ip),80,"");
	}catch(Exception ex){error(ex,"Asic.<init>",ipPrefix,ip);}}

	public String toJson(){String t="";try{t=jo()
		.clrSW()
		.o("{base:",base
			,",wallet:",wallet
			,",tempr:",tempr
			,",blocksFound:",blocksFound
			,",state:\"",state,"\"}")
		.toStrin_();}catch(Exception ex){
			error(ex,"Asic.toJson");}
		return t;}

	public String toString(){return toJson();}

	public Map<String,String>filter1(Map<String,Object>p){
		Map<String,String>m=new HashMap<String,String>();
		for(String key :p.keySet()){
			Object o=p.get(key);String s;
			if(o instanceof List){int i=0;
				List<ParseStatus.Elem>l=(List<ParseStatus.Elem>)o;
				for(ParseStatus.Elem e:l){
					s=e.val();i++;
					if(s!=null)
						s=s.trim();
					if(s!=null && s.length()>0)
						m.put(key+'.'+i, s);
				}
			}
			else if(o instanceof ParseStatus.Elem){
				ParseStatus.Elem e=(ParseStatus.Elem)o;
				s=e.val();
				if(s!=null)
					s=s.trim();
				if(s!=null && s.length()>0)
					m.put(key, s);
			}
		}
		return m;}

	public Map<String,String>filter2(Map<String,String>p,Map<String,String>v){
		Map<String,String>m=null;
		for(String key :v.keySet()){
			String z=p.get(key),o=v.get(key);
			if(z==null || !z.equals(o))
				{	if(m==null)
						m=new HashMap<String,String>();	
					m.put(key, o);
					p.put(key, o);
				}
		}
		return m;}

}//class Asic

class AsicsScanner  extends Json{
	String prefix;int[]ports;long sleep;
	List<Asic>asics=new LinkedList<Asic>();

	public void run(){startScan();}

	public void startScan(){
		for(int ip=ports[0];ip<=ports[1];ip++)try{
			Asic asic=new Asic(prefix,ip);
			asics.add(asic);
			asic.start();
			log("AsicsScanner .startScan:",asic);
		}catch(Exception x){
			error(x,"AsicsScanner.startScan:",ip);
			//asics.remove(o)
		}
	}

	public AsicsScanner (
		String prefix,int startPort,int endPort,long sleep){
		int[]a={startPort,endPort};ports=a;this.prefix=prefix;this.sleep=sleep;}
}// class AsicsScanner 

}//public class MainTest01

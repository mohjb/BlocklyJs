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
	String src=fileString(//"C:/Users/mohjb/Documents/GitHub/BlocklyJs/otherProjs/asics/output/output141/2020/01/10/15/04"
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
	URL base;//HttpURLConnection conn;//String ip,host;int[]port;
	String[]wallet;int ip;
	double[]tempr;int blocksFound;
	State state=State.init;

	interface Parse{public String parse(String p); }

	static class Parse0 implements Parse{
	@Override public String parse(String p) {return p;}}

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
				Map<String,Object> m=p.m(p.doc);
				r=Json.jo().clrSW().oMap(m,"","").toStrin_();
			}catch ( IOException x ){}
			return r;
		}//parse

		ParseStatus(String s){
			if(s==null)return;
			doc=new E(s);
			int i=s.indexOf('<');
			doc.firstChild=doc.lastChild=
				i==0?new E(doc):new N(doc,i);
			if(i==0)c.parse();	//if(i>0)new E(doc).parse();
			while(c.end<doc.end){
				int j=c.end;
				i=s.indexOf('<',j);
				boolean b=i-j<2 &&i>=0;
				c=b?new E(doc):new N(doc,j,i);
				if(b)c.parse();
			}
		}

		//static class P extends E{
		E doc;N c;int ix=0;

		class N{
			N parentNode,nextSibling,previousSibling;
			String x;int i,end;//i:index-of start of textNode ; end:index-of end-of-textNode

			N(){c=this;}
			N(E p){
				parentNode=p;
				N n=p.lastChild;
				i=ix;//<doc.end&&doc.x.charAt(ix)=='>'?ix+1:ix;//c.end==0?0:c.end+1;//p.headEnd==0?0:p.headEnd+1;;//c.end<doc.end&&doc.x.charAt(c.end)=='>'?c.end+1:c.end;//c.end==0?0:c.end+1;//p.headEnd==0?0:p.headEnd+1;
				if(n!=null)
					n.nextSibling=this;
				previousSibling=n;
				p.lastChild=this;
				if(p.firstChild==null)
					p.firstChild=this;
				c=this;
			}
			N(E p,int separator){
				this(p);if(separator!=-1)
					x=doc.x.substring(i,ix=separator);
			}
			N(E p,int start,int separator){
				this(p);i=start;end=separator;if(separator!=-1)
					x=doc.x.substring(i,ix=separator);
			}

			N parse(){return this;}//parse
		}//class N

		class E extends N{
			N firstChild,lastChild;String id;
			/**i=indexOf('<') headEnd=indexOf('>',i) close=indexOf('<',headEnd) end=indexOf('>',close)*/
			int headEnd,close;
			E(E p){super(p);}
			E(String s){super();
				c=doc=this;
				x=s;
				ix=i=headEnd=0;
				end=close=s.length();
			}

			N parse(){
				headEnd=close=end=doc.x.indexOf(">",i);
				char ch=doc.x.charAt(i+1);
				if(ch=='?' || ch=='!'){
					if(ch=='!' && //doc.x.substring(i+2, i+4).equals("--")
						doc.x.charAt(i+2)=='-'&&
						doc.x.charAt(i+3)=='-'
					){
						headEnd=close=doc.x.indexOf("-->",i+6);
						ix=(end=close+2)+1;
					}ix=end;
					x=doc.x.substring(i,end);
					return this;
				}
				if(end==-1)
				{	ix=headEnd=close=end=doc.end;
					return this;
				}ix=++end;
				int e =doc.x.indexOf(" ",i)
					,d=doc.x.indexOf("id=\"",e)
					,q=d>end||d==-1?d:doc.x.indexOf("\"",d+4);
				x=doc.x.substring(i+1,e);
				id=d==-1?null:doc.x.substring(d+4,q);
				e=headEnd;
				if(doc.x.charAt(headEnd-1)=='/')
					close=headEnd-1;           //leaf
				else do{
					e =doc.x.indexOf('<',d=e);
					if(e==-1){
						if(headEnd<(ix=end=doc.end))
							new N(this,d,doc.end);
					}else {
						if ( e > d+1 )//headEnd + 1
							new N( this ,e);
						if ( doc.x.charAt( e + 1 ) == '/' ) {
							ix=end = close = e;
							c = this;
							e += x.length();
							if ( x.equalsIgnoreCase( doc.x.substring( end + 2, e ) ) ) {
								ix=end = doc.x.indexOf( ">", e );
								if ( end == -1 )
									ix=end = doc.end;
								else
									ix++;
							}
							return this;
						}else{
							E x=new E(this);
							x.parse();
							e=x.end;
						}
					}
				}while(e!=-1 && e<doc.end);
				return this;
			}//parse

		}//class E

		//}//class P

		Map<String,Object> m(E e){Map<String,Object> m =new HashMap<String,Object>();
			N n=e.firstChild;
			int i=0;
			while(n!=null){
				if(n instanceof E){
					E x=(E)n;
					m.put( x.id==null?String.valueOf(i):x.id,m(x) );
				}
				else m.put(String.valueOf(i),n.x);
				n=n.nextSibling;
				i++;
			}
			return m;}
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

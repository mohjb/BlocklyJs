//{1503603665 yd81a3
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
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

public static void main(String[]args)
{try{baseDir=new File("./output/").getCanonicalPath();
	new MainTest01();}catch(Exception e){
		error(e,"main");}
}//main 

MainTest01(){
	if(global==null)sttc=global=this;
	String prefix="192.168.8.";
	int startPort= 141 ,endPort= 141 ,sleep=5000;
	w(141+"/",global.now=new Date(),"testW",".html","test");
	scan=new AsicsScanner(prefix, startPort, endPort, sleep);
	scan.start();
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
			P p=new p();p.s=s;
			p.len=s.length();
			p.f=p.c=s.charAt(0)=='<'?new E():new N();
			p.c.i=0;p.c.p();

			Map m=new HashMap();
			p.pTxt(m,0,null);
			String r=jo().oMap(m);
			return r;
		}
		static class P {
			String s;int len;
			N f,c;//first
			class N{
				N parentNode,nextSibling,previousSibling;
				String x;
				int i,j;//0:index-of start of textNode ; 1:index-of end-of-textNode 
				//in-case-of element: 0:index-of '<' ; 1:index-of '>' ; 2:index-of '</' ; 3:index-of '>' ; 
				void p(){
					j=s.indexOf("<",i);
					if(j==-1)j=len;else{
						
					}}
			}
			class E extends N{
				N firstChild,lastChild;
				String id;int ei,ej;
				void p(){}
			}
			void pTxt(Map<K,V> p,int prevSiblingsCount,String id){
				/*
					html parser, looking for textNode and sub-tags, and attrib-id in tags
				*/
				if(id==null)id=String.valueOf(prevSiblingsCount);
				prevSiblingsCount=0;
				int ix=p.indeixOf("<",i);
				String val=s.substring(i,ix==-1?len:ix);
				while(ix==-1){
					p.put(id, val);
					if(s.charAt(ix+1)=='/'){
						int j=s.indexOf('>',ix);
						i=(j==-1?ix:j)+1;
						return;
					}
					else{ i=ix;
						pTag( p,prevSiblingsCount++);
						ix=p.indeixOf("<",i);
						val+=s.substring(i,ix==-1?len:ix);
					}
				}p.put(id, val);
			}

			Map pTag(Map p,int prevSiblingsCount){
				int x=i,j=s.indexOf('>',i);
				i=(j==-1?len:j)+1;
				int idx=s.indexOf("id=", x),ide=s.indexOf('"', idx+4);
				String id=idx==-1
					?String.valueOf(prevSiblingsCount)
					:s.substring(idx+4,ide);
				if(idx!=-1){

				}
				
				if(j!=-1&&'/'==s.charAt(j-1)){
					return p;
				}
				return p;
			}
		}//class P

	}//class ParseStatus

	static Parse parse0=new Parse0()
		,parseConfig=new ParseConfig()
		,parseStatus=new ParseStatus();

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
		w(ip+"/",global.now,p.toString(),".html",p.parse(result));
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

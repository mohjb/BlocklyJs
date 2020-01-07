//{
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.LinkedList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.nio.file.Files;

//}

public class MainTest01 extends TL{
AsicsScanner scan;
static String baseDir;
static MainTest01 sttc;
Date now;
List<Asic>asics=new LinkedList<Asic>();

static void w(String fn,String x){
	try {Files.write(Paths.get(baseDir,fn),x.getBytes());
	 } catch (Exception e) {
		error(e,"MainTest01.w",fn,x);}}

public static void main(String[]args)//class ThreadClass1 extends Thread{}
{baseDir=sttc=new File("./output/").getCanonicalPath();
	new MainTest01();
	/*{
multi-threaded agent

thread-class1: (single thread-class instance, MainTest01-class) init-from config-file and update global-config
	, and periodically check config-file changes
	,and, check local-agents and threads working/existing/alive
thread-class2: (single thread-class instance) scan ips, and update global-config
thread-class3: (multiple thread-class instances) read assigned-ip asic
thread-class4: (multiple thread-class instances) check assigned house

to read ascis, and other agents from other houses

}*/
}//main 

MainTest01(){
	//check db config
	if(global==null)global=this;//start scan
	String prefix="192.168.1.";
	int startPort=100,endPort=104,sleep=1000;
	scan=new AsicsScanner(prefix, startPort, endPort, sleep);
	scan.start();
}

public static void log(Object...s){Json.logA(s);}
public static void error(Throwable x,Object...p){Json.errorA(x,p);}

static class Asic extends TL{
	final static String Authorization=
	"Digest username=\"root\", realm=\"antMiner Configuration\", nonce=\"b1b1652793d3109e9b29f0c3a111ffe5\", uri=\"/\", response=\"1fa4707b57aac3ad574bcd0f044f1cc8\", qop=auth, nc=00000001, cnonce=\"680d2ef66564dc19\"";
	URL base;//HttpURLConnection conn;//String ip,host;int[]port;
	String[]wallet;int ip;
	double[]tempr;int blocksFound;
	State state=State.init;

	enum State{init,detectAsic,invalidWallet,wallet,config,monitor};
	enum Path{info("/cgi-bin/get_system_info.cgi")
		,config("/cgi-bin/minerConfiguration.cgi")
		,net("/cgi-bin/get_network_info.cgi")
		,sys("")
		,status("/cgi-bin/minerStatus.cgi");
		String s;Path(String p){s=p;}}

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
		w(ip+'.'+p+'.'+(new Date().getTime())+".html",result);
	}

	public void startScan()throws Exception{f(Path.info);}

	public void startMonitor(){
		while(ip>=0)try{
			for(Path p:Path.values())f(p);
			Thread.sleep(sttc.scan.sleep);
		}catch(Exception ex){error(ex,"Asic.startMonitor",ipPrefix,ip,p);}
		//loop
			//check status // tempr blocksFound hashRate
			//check network
			//check miner-config // wallets
			//check info
			//check diff sys-log
			//check any-new user-commands
	}

	public void run(){try{
		startScan();
		startMonitor();
		}finally{
			
		}
	}

	public void checkInfo(){}
	public void checkConfig(){}
	public void checkNet(){}
	public void checkStatus(){}
	public void checkSysLog(){}

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

class AsicsScanner  extends TL{
	String prefix;int[]ports;long sleep;
	List<Asic>asics=new LinkedList<Asic>();

	public void run(){startScan();}

	public void startScan(){
		for(int i=ports[0];i<=ports[1];i++){
			Asic asic=new Asic(prefix,i);
			asics.add(asic);
			asic.start();
			log("AsicsScanner .startScan:",asic);
		}
	}

	public AsicsScanner (
		String prefix,int startPort,int endPort,long sleep){
		int[]a={startPort,endPort};ports=a;this.prefix=prefix;this.sleep=sleep;}
}// class AsicsScanner 

}//public class MainTest01

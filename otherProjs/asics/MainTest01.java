//{
import java.util.*;
import java.io.File;
//}

public class MainTest01 extends Json{
static String baseDir;//static MainTest01 global;
AsicsScanner scan;
Asic head;long cnfgCheckFrequency=1000*10;//Map<String, DB.Prop.SD > cnfg;Date cnfgLog;
public static void main(String[]args){
	try{baseDir=new File("./output/").getCanonicalPath();
		System.out.println("baseDir="+baseDir);
		new MainTest01();}catch(Exception e){
		error(e,"main");}
}//main

MainTest01(){
	try{if(global==null)
		global=this;
		head=new Asic(Asic.LoadedBy.byDbMac);
		head.vals=DB.Prop.tl().loadProps(head.vals,"","","",new Date(0));
		cnfgCheckFrequency=cnfg("configCheckFrequency",(int)cnfgCheckFrequency);
		String domain=cnfg("domain","258");
		List<Asic>l=DB.Prop.tl().loadAsicsProps( "",domain,true );//usr="" means current user , domain="" means current domain/house/ headOfCurrentUsr
		//Asic config=Asic.macs.get( "" );// mac=="" means local/thisHouse/headOfTheHouse
		//cnfg=config==null?null:config.vals.get( "" );// path=="" ::= configuration / HeadOfTheAsic
		if(l!=null)for(Asic asic:l)try{
			if(asic.base==null){
				Map<String, DB.Prop.SD> m=asic.vals.get("net");
				DB.Prop.SD d=m==null?null:m.get("ipaddress");
				String s=d==null?null:d.s;
				int i=s==null?-1:s.lastIndexOf('.')
				,ip=s==null?-1:Util.parseInt(s.substring(i+1),-1);
				s=s==null?null:s.substring(0,i+1);
				if(s!=null)asic.init(s,ip);
			}
			if(asic.ip>0){
				Asic.asics.put(asic.ip,asic);
				/*if(asic.mac==null){
					DB.Prop.SD d=asic.vals.get("net").get("macaddr");
					asic.mac=d.s;
					Asic.macs.put(asic.mac,asic);}*/
				asic.start();}
		}catch (Exception e) {
			error(e, "MainTest01.<init>:for-mac");}
		String prefix=cnfg("prefix","192.168.8.");//,house=cnfg("house","258");
		int startPort= cnfg("startPort",2)
			,endPort= cnfg("endPort",255)//,sleep=Util.mapInt( cnfg,"sleep",2000)
		;
		scan=new AsicsScanner(prefix, startPort, endPort);
		scan.start();
	} catch (Exception e) {
		error(e, "MainTest01.<init>");}
	checkConfig();
}

void checkConfig(){
	long last=0
		,now=System.currentTimeMillis()
		,trgt=now+cnfgCheckFrequency;
	while(true)try{
		now=System.currentTimeMillis();
		if(now>=trgt){
			head.vals=DB.Prop.tl(). loadProps( head.vals,"","","",new Date(last));//Map<String, DB.Prop.SD >v=
			last=now;trgt=now+cnfgCheckFrequency;
/*
	reload::
		domain
		prefix
		startPort
		endPort
		cnfgCheckFrequency
		asicSleep
			(net,status,config,info )
			(stop , <null>/inherit , <num:milli-seconds>)
		scanSleep
			(stop , <null>/inherit , <num:milli-seconds>)
		configurationSleep
			(stop , <null>/inherit , <num:milli-seconds>)
		domainSleep
			(stop , <null>/inherit , <num:milli-seconds>)

	mac-specific
	domain-specific


	 */		}
		Thread.sleep(500);
	}catch(Exception x){
		error(x,"MainTest01.checkConfig");
	}
}

String cnfg(String prop,String defVal){
	//int i=prop.indexOf( '.' );	String c=i==-1?prop:prop.substring( 0,i )		,p=i==-1?prop:prop.substring( i+1 );	Map<String,String>x=cnfg.get( c );
	Map<String, DB.Prop.SD > cnfg=head==null?null:head.vals.get("");
	DB.Prop.SD o=cnfg==null?null:cnfg.get( prop );//x!=null?x.get( p ):x;
	return o==null?defVal:o.s;}

int cnfg(String prop,int defVal){
	String s=null;s=cnfg(prop,s);
	return s==null?defVal:Util.parseInt( s,defVal );}

class AsicsScanner  extends Json{
	String prefix;int[]ports;

	public void run(){startScan();}

	public void startScan(){
		for(int ip=ports[0];ip<=ports[1];ip++)try{
			Asic asic=Asic.asics.get(ip);
			if(asic==null){
				Asic.asics.put(ip,asic =new Asic(prefix,ip));
				asic.start();//log("AsicsScanner .startScan:",asic);
			}
		}catch(Exception x){
			error(x,"AsicsScanner.startScan:",ip);
			//asics.remove(o)
		}
	}

	public AsicsScanner (String prefix,int startPort,int endPort){
		int[]a={startPort,endPort};ports=a;this.prefix=prefix;}
}// class AsicsScanner 

}//public class MainTest01

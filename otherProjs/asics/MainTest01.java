//{
import java.util.*;
import java.io.File;
//}

public class MainTest01 extends Json{
static String baseDir;//static MainTest01 global;
AsicsScanner scan; //Date now;
Map<Integer,Asic>asics=new HashMap<Integer,Asic>();
Map<String, DB.Prop.SD > cnfg;Date cnfgLog;
public static void main(String[]args){
	try{baseDir=new File("./output/").getCanonicalPath();
		System.out.println("baseDir="+baseDir);
		new MainTest01();}catch(Exception e){
		Json.error(e,"main");}
}//main

MainTest01(){
	try{if(global==null)
		global=this;
		DB.Prop.tl().loadAsicsProps( "","",true );//usr="" means current user , domain="" means current domain/house/ headOfCurrentUsr
		Asic config=Asic.macs.get( "" );// mac=="" means local/thisHouse/headOfTheHouse
		cnfg=config==null?null:config.vals.get( "" );// path=="" ::= configuration / HeadOfTheAsic

		String prefix=cnfg("prefix","192.168.8.");//,house=cnfg("house","258");
		int startPort= cnfg("startPort",2)
			,endPort= cnfg("endPort",255)//,sleep=Util.mapInt( cnfg,"sleep",2000)
		;
		scan=new AsicsScanner(prefix, startPort, endPort);
		scan.start();
	} catch (Exception e) {
		error(e, "MainTest01.<init>");}
}

boolean checkConfig(){
	DB.Prop.tl(). loadProps( null,"","","config",cnfgLog);//Map<String, DB.Prop.SD >v=
	/*
	reload::
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


	 */
	return false;
}

String cnfg(String prop,String defVal){
	//int i=prop.indexOf( '.' );	String c=i==-1?prop:prop.substring( 0,i )		,p=i==-1?prop:prop.substring( i+1 );	Map<String,String>x=cnfg.get( c );
	DB.Prop.SD o=cnfg.get( prop );//x!=null?x.get( p ):x;
	return o==null?defVal:o.s;}

int cnfg(String prop,int defVal){
	String s=null;s=cnfg(prop,s);
	return s==null?defVal:Util.parseInt( s,defVal );}

class AsicsScanner  extends Json{
	String prefix;int[]ports;

	public void run(){startScan();}

	public void startScan(){
		for(int ip=ports[0];ip<=ports[1];ip++)try{
			if(asics.get( ip )==null){
				Asic asic=new Asic(prefix,ip);
				asics.put(ip,asic);
				asic.start();
				log("AsicsScanner .startScan:",asic);}
		}catch(Exception x){
			error(x,"AsicsScanner.startScan:",ip);
			//asics.remove(o)
		}
	}

	public AsicsScanner (String prefix,int startPort,int endPort){
		int[]a={startPort,endPort};ports=a;this.prefix=prefix;}
}// class AsicsScanner 

}//public class MainTest01

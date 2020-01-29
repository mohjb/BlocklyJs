//{
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
//}

public class MainTest01 extends Json{
static String baseDir;//static MainTest01 global;
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
	 System.out.println("baseDir="+baseDir);
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
	if(Asic.global==null)sttc=Asic.global=this;
	/*String src=fileString(
		//"C:/Users/mohjb/Documents/GitHub/BlocklyJs/otherProjs/asics/output/output141/2020/01/10/15/04"
		"D:/apache-tomcat-8.0.15/webapps/ROOT/GitHub/BlocklyJs/otherProjs/asics/output/output141/2020/01/10/15/04"
	,"status.22.713..html");
	Map result=	Asic.Path.status.parse.parse(src);//Asic.ParseStatus p=new Asic.ParseStatus(src);
	
	w("/"+258+"/",Asic.global.now=new Date(),"test-ParseStatus",".json"
		,jo().clrSW().o(result).toStrin_());*/
	String prefix="192.168.8.";int startPort= 141 ,endPort= 141 ,sleep=5000;
	scan=new AsicsScanner(prefix, startPort, endPort, sleep);
	scan.start();
} catch (Exception e) {
	error(e, "MainTest01.<init>");}}


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

//{
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
//}
class Asic extends Json{

final static String Authorization=
	"Digest username=\"root\", realm=\"antMiner Configuration\", nonce=\"b1b1652793d3109e9b29f0c3a111ffe5\", uri=\"/\", response=\"1fa4707b57aac3ad574bcd0f044f1cc8\", qop=auth, nc=00000001, cnonce=\"680d2ef66564dc19\"";

	static Map<String,Asic>
		macs=new HashMap<String,Asic>();

	static Map<Integer,Asic>asics=new HashMap<Integer,Asic>();

	URL base;
	int ip;
	String mac;

	LoadedBy state;enum LoadedBy{byIpScan,byDbMac};
	char[] charArray = new char[16383];

	/// **lastModified in Prop*/Date cnfgLog;
	Map<String,Map<String,DB.Prop.SD>>vals=new HashMap<String, Map<String,DB.Prop.SD >>();

interface Parse{public Map<String,DB.Prop.SD> parse(String p); }

static class Parse0 implements Parse{
	@Override public Map<String,DB.Prop.SD> parse(String p) {return mss( p);}}

static class ParseConfig implements Parse{
	@Override public Map<String,DB.Prop.SD> parse(String p) {
		int i0=p.indexOf("{"),i1=p.indexOf("};");
		return mss(p.substring(i0,i1+1));}}

static Parse parse0=new Parse0()
	,parseConfig=new ParseConfig()
	,parseStatus=new ParseStatus(null);

enum Path{info("/cgi-bin/get_system_info.cgi",parse0)
	,config("/cgi-bin/minerConfiguration.cgi",parseConfig)
	,net("/cgi-bin/get_network_info.cgi",parse0)//,sys("")
	,status("/cgi-bin/minerStatus.cgi",parseStatus);
	String s;Parse parse;
	Path(String p,Parse prse){
		s=p;parse=prse;}
}


public Map<String,DB.Prop.SD> check(Path p)throws Exception{
	String asicStr=readAsic(p);//,ps=p.toString();//Map<String,DB.Prop.SD>o=vals.get(ps);//if(o==null)vals.put(ps,o=new HashMap<String, DB.Prop.SD >());
	Map<String,DB.Prop.SD>chngs=filterChngs( p,asicStr);
	if(chngs!=null){
		if(mac==null){
			mac=chngs.get("macaddr").s;//TODO: might be null, might change
			if(mac==null)
				mac=String.valueOf(System.currentTimeMillis());//:mac.replaceAll(":", "-");
			macs.put( mac,this );
		}
		if(ip==0 || asics.get( ip )==null){
			ip=Util.parseInt( chngs.get("ipaddress").s,ip);//TODO: might be null, might change
			asics.put( ip,this );
		}String house=global.cnfg("house","258") ;
		for(String key:chngs.keySet())
			DB.Prop.save( "",house,mac,p.toString(),key,chngs.get( key ) );//MainTest01.w(mac+'/'+p+'/',new Date(),key,"json",m.get(key));
	}
	return chngs;}

public String readAsic(Path p)throws Exception{
	URL url = new URL(base,p.s);
	URLConnection urlConnection = url.openConnection();
	urlConnection.setRequestProperty("Authorization", Authorization);
	InputStream is = urlConnection.getInputStream();
	InputStreamReader isr = new InputStreamReader(is);
	int numCharsRead;

	StringBuffer sb = new StringBuffer();
	while ((numCharsRead = isr.read(charArray)) > 0)
		sb.append(charArray, 0, numCharsRead);
	String result = sb.toString();return result;
	//DB.log w(ip+"/",global.now,p.toString(),".html",p.parse.parse(result));
}


public void startMonitor(){
	long time=System.currentTimeMillis()+1000*30;
	while(mac!=null && time>=System.currentTimeMillis())
		try{for(Path p:Path.values())
			check(p);
			DB.D.close();
			Thread.sleep(Util.mapInt( global.cnfg,"sleep",2000));
		}catch(Exception ex){
			if(macs.get( mac )==this)
				macs.remove( mac );
			if(asics.get( ip )==this)
				asics.remove( ip );
			mac=null;
			error(ex,"Asic.startMonitor",base);
		}//,ipPrefix,p
}

public void run(){
	try{
		check(Path.net);
		startMonitor();
	}catch(java.net.ConnectException ex){
		//error(ex,"Asic.run:java.net.ConnectException:",base);
		asics.remove(ip);
	}
	catch(Exception ex){
		error(ex,"Asic.run:Exception:",base);
		asics.remove(ip);
	}
}

public Asic(LoadedBy p){state=p;}
public Asic(String mac){this(LoadedBy.byDbMac);this.mac=mac;}
public Asic(String ipPrefix,int ip){this(LoadedBy.byIpScan);init( ipPrefix,ip );}
void init(String ipPrefix,int ip){
	try{base=new URL("http",ipPrefix+(this.ip=ip),80,"");
	}catch(Exception ex){error(ex,"Asic.<init>",ipPrefix,ip);}}

	public String toJson(){
		String t="";try{t=jo()
		.clrSW()
		.w("{base:").o(base)
		.w(",mac:").o(mac)
		.w(",ip:").o(ip)
		//.w(",cnfgLog:").o(cnfgLog)
		.w(",vals:").o(vals)
		.w(",m:").o(m)
		.w("}")////,state:\"",state,"\"
		.toStrin_();}catch(Exception ex){
			error(ex,"Asic.toJson");}
		return t;}

public String toString(){return toJson();}

public Map<String,DB.Prop.SD>filterChngs( Path p,String asicStr ){
	Map<String,DB.Prop.SD>
		asicVals=p.parse.parse(asicStr)
		,vpm=vals.get(p.toString())
		,chngs=null;
	for(String key :asicVals.keySet()){
		DB.Prop.SD z=vpm==null?null:vpm.get(key)
			,o=asicVals.get(key);
		if(z==null || !z.s.equals(o.s))
		{	if( chngs==null)
				chngs=new HashMap<String,DB.Prop.SD>();
			chngs.put(key, o);
			if(vpm== null)
				vals.put(p.toString(),vpm=new HashMap< String, DB.Prop.SD >(  ));
			vpm.put(key, o);
		}
	}
	return chngs;}

	void mergeProps(Map<String,Map<String, DB.Prop.SD>>p){
		for(String c:p.keySet()){
			Map<String, DB.Prop.SD>m=p.get(c)
				,vm=vals.get(c);
			if(vm==null)
				vals.put(c,vm=new HashMap<>());
			for(String k:m.keySet()){
				DB.Prop.SD d=m.get(k),vd=vm.get(k);
				if(vd==null)
					vm.put(k,d);
			}
		}
	}

	Date latest(){
		Date r=null;
		for(String c:vals.keySet()){
			Map<String, DB.Prop.SD>m=vals.get(c);
			for(String k:m.keySet()){
				DB.Prop.SD d=m.get(k);
				if(r==null || d.d.after(r))
					r=d.d;
			}
		}
		return r;
	}

	static public Map<String,DB.Prop.SD>mss(Map p){
		if(p==null ) // || p.size()==0
			return null;
		Map<String,DB.Prop.SD>m=new HashMap<String,DB.Prop.SD>();//p instanceof Map<String,String>?(Map<String,String>)p: null;
		Json.Output jo=null;Date now=new Date();
		//prefix=(prefix==null||prefix.trim().length()==0)?"":prefix.trim()+'.';
		for(Object k:p.keySet()){
			Object o=p.get(k);
			String key=k==null?"":k.toString()//prefix:prefix+k
			,s=o instanceof String?(String)o:null;
		if(s==null&&o!=null)try{
			if(jo==null)
				jo=jo().clrSW();
			s=jo.o(o,"",key).toStrin_();}catch(Exception x){
		}
		m.put(key, new DB.Prop.SD(s,now));
	}
	return m;}

	static public Map<String,DB.Prop.SD>mss(String p){
		Object o=null;try{o=Json.Prsr.parse(p);}catch(Exception x){}
		Map m=o==null?null:o instanceof Map?(Map)o:Json.Util.mapCreate("",o);
		return mss(m);}

static class ParseStatus implements Parse{
	@Override public Map<String,DB.Prop.SD>parse(String s) {
		Map<String,DB.Prop.SD>r=null;try{
			ParseStatus p=new ParseStatus(s);
			r=p.filterMss();
			p.doc.fina();p.doc=null;p.ids.clear();p.ids=null;
		}catch ( Exception x ){
			error(x,"Asic.ParseStatus.parse");}
		return r;
	}//parse

	ParseStatus(String s){
		if(s==null)return;
		ids=new HashMap<String,Object>();
		doc=new Elem(s);
	}

	public Map<String,DB.Prop.SD>filterMss(){
		//prefix=prefix==null||prefix.length()<1?"":prefix+'.';
		Map<String,DB.Prop.SD>m=new HashMap<String,DB.Prop.SD>();
		Date now=new Date();
		for(String key :ids.keySet()){
			Object o=ids.get(key);String s;
			if(o instanceof List){int i=0;
				List<Elem>l=(List<Elem>)o;
				for(Elem e:l){
					s=e.val();i++;
					if(s!=null){
						s=s.trim();
						if( s.length()>0)
							m.put(//prefix+
								key+'.'+i, new DB.Prop.SD(s,now));
					}}
			}
			else if(o instanceof Elem){
				Elem e=(Elem)o;
				s=e.val();
				if(s!=null){
					s=s.trim();
					if( s.length()>0)
						m.put(//prefix+
							key, new DB.Prop.SD(s,now));
				}}
		}
		return m;}

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

		Nd(){if(doc!=null)doc.nextSibling=this;}
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
			this(p);
			txt=sub(chr(i=start)=='>'?i+1:i,doc.i=end=separator==-1?doc.end:separator);
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
		Nd fina(){
			txt=null;
			Nd n=previousSibling;
			nextSibling=null;
			if(parentNode!=null){
				parentNode.lastChild=n;
				parentNode=null;
			}
			if(n!=null){
				n.nextSibling=previousSibling=null;
				n.fina();
			}
			return this;
		}
	}//class Nd

	/**Element or TagNode of a HTML-TreeNode, or comment, or frag, basically anything that starts with a &lt; */
	class Elem extends Nd{
		Nd firstChild,lastChild;String id;
		/**i=indexOf('<') headEnd=indexOf('>',i) close=indexOf('<',headEnd) end=indexOf('>',close)*/
		int headEnd,close;byte depth;
		Elem(Elem p){super(p);depth=(byte)(p.depth+1);}
		Elem(String s){super();
			nextSibling=doc=this;
			txt=s;
			i=headEnd=depth=0;//doc.i=
			end=close=s.length();

			int iz=s.indexOf('<');
			headEnd=s.indexOf("id=\"");

				firstChild=iz==0?
					new Elem(doc):
					new Nd(doc,iz);
				if(iz==0)nextSibling.parse();
				while(nextSibling.end!=-1&&i<end&&nextSibling.end<end){//int j=c.end;
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
			if(end==-1){
				doc.i=headEnd=close=end=doc.end;
				return this;
			}doc.i=++end;
			int e =fnd(" ",i);
			txt=sub(i+1,e>headEnd?(e=headEnd):e);
			int d=doc.headEnd<end?doc.headEnd:doc.end
				,q=d>end?d:ixOf("\"",d+4);
			if(d==doc.headEnd)
				doc.headEnd=fnd("id=\"",close);
			if(d!=-1&&q!=-1&&q>=d+4){//id=d==-1||q==-1||q<d+4?null:doc.x.substring(d+4,q);
				id=sub(d+4,q);
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
						doc.nextSibling = this;
						e +=2;// txt.length()+2;
						d=ixOf('>', e);
						doc.previousSibling=matchClose(e,d);
						if(doc.previousSibling!=null){
							if ( doc.previousSibling==this) {//txt.equalsIgnoreCase( sub( end + 2, e ) )
								doc.i=end = d;//ixOf( ">", e );
								if ( end == -1 )
									doc.i=end = doc.end;
								else
									doc.i++;
								doc.previousSibling=null;
								return this;
							}
							return doc.previousSibling;//this;//
						}
						else{//System.out.println("Asic.ParseStatus:Elem.parse:closing:no match @"+d);
							e=doc.i=1+(end=d);
						}
					}else{
						Elem x=new Elem(this);
						Nd n=x.parse();n=doc.previousSibling;
						if(n!=null){
							if(n==this){
								doc.previousSibling=n=null;
								doc.i=1+(end=doc.close);
								doc.close=doc.end;
							}
							return n!=null?n:this;
						}
						else
							e=doc.i;//x.end;
					}
				}
			}while(e!=-1 && e<doc.end);
			return this;
		}//parse

		Elem matchClose(int e,int c){
			String x=sub(e, c);
			Elem n=this;
			while(n!=null){
				if(x.equalsIgnoreCase(n.txt))
				{	doc.close=c;
					return n;}
				else
					n=n.parentNode;
			}
			return null;}

		String val(){
			Nd n=firstChild;return n!=null&&n==lastChild&&!(n instanceof Elem)?n.txt:null;}

		Nd fina(){
			while(lastChild!=null)
				lastChild.fina();
			id=null;
			firstChild=null;
			return super.fina();
		}

	}//class E

	//}//class P

}//class ParseStatus


}//class Asic

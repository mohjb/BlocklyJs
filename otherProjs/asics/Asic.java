//{
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
//}
class Asic extends Json{
	static MainTest01 global;
	final static String Authorization=
	"Digest username=\"root\", realm=\"antMiner Configuration\", nonce=\"b1b1652793d3109e9b29f0c3a111ffe5\", uri=\"/\", response=\"1fa4707b57aac3ad574bcd0f044f1cc8\", qop=auth, nc=00000001, cnonce=\"680d2ef66564dc19\"";
	URL base;int ip;State state=State.init;
	Map<String,Map<String,String>>vals;

	interface Parse{public Map<String,String> parse(String p); }

	static class Parse0 implements Parse{
	@Override public Map<String,String> parse(String p) {return mss(p);}}

	static class ParseConfig implements Parse{
	@Override public Map<String,String> parse(String p) {
		int i0=p.indexOf("{"),i1=p.indexOf("};");
		return mss(p.substring(i0,i1+1));}}

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
		//DB.log w(ip+"/",global.now,p.toString(),".html",p.parse.parse(result));
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
			,",vals:",vals
			,",state:\"",state,"\"}")
		.toStrin_();}catch(Exception ex){
			error(ex,"Asic.toJson");}
		return t;}

	public String toString(){return toJson();}

	static public Map<String,String>filterNewVals(Map<String,String>p,Map<String,String>v){
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

	static public Map<String,String>mss(Map p){
		if(p==null ) // || p.size()==0
			return null;
		Map<String,String>m=new HashMap<String,String>();//p instanceof Map<String,String>?(Map<String,String>)p: null;
		Json.Output jo=null;
		for(Object k:p.keySet()){
			Object o=p.get(k);
			String key=k==null?"":k.toString()
			,s=o instanceof String?(String)o:null;
			if(s==null&&o!=null)try{
				if(jo==null)
					jo=jo().clrSW();
				s=jo.o(o,"","").toStrin_();}catch(Exception x){
					}
			m.put(key, s);
		}
		return m;}
	
	static public Map<String,String>mss(String p){
		Object o=null;try{o=Json.Prsr.parse(p);}catch(Exception x){}
		Map m=o==null?null:o instanceof Map?(Map)o:Json.map("",o);
		return mss(m);}


	static class ParseStatus implements Parse{
		@Override public Map<String,String>parse(String s) {
			Map<String,String>r=null;try{
				ParseStatus p=new ParseStatus(s);
				r=filterMss();//r=Json.jo().clrSW().oMap(p.ids,"","").toStrin_();
			}catch ( Exception x ){
				error(x,"Asic.ParseStatus.parse");}
			return r;
		}//parse

		ParseStatus(String s){
			if(s==null)return;
			ids=new HashMap<String,Object>();
			doc=new Elem(s);
		}

		public Map<String,String>filterMss(){
			Map<String,String>m=new HashMap<String,String>();
			for(String key :ids.keySet()){
				Object o=ids.get(key);String s;
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
							if ( doc.previousSibling==this) {//txt.equalsIgnoreCase( sub( end + 2, e ) )
								doc.i=end = d;//ixOf( ">", e );
								if ( end == -1 )
									doc.i=end = doc.end;
								else
									doc.i++;
								doc.previousSibling=null;
								return this;
							}//else {if( txt.equalsIgnoreCase( "table" ) ){System.out.println("Asic.ParseStatus:Elem.parse:table-tag:closing:");}
							else if(doc.previousSibling!=null)
								return this;//doc.previousSibling
						}else{
							Elem x=new Elem(this);
							x.parse();
							if(doc.previousSibling!=null){
								if(doc.previousSibling!=this){
									doc.previousSibling=null;
									doc.i=1+(end=doc.close);
									doc.close=doc.end;
								}
								return this;
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


}//class Asic

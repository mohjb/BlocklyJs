srvr={
Authorization:
'Digest username="root", realm="antMiner Configuration", nonce="b1b1652793d3109e9b29f0c3a111ffe5", uri="/", response="1fa4707b57aac3ad574bcd0f044f1cc8", qop=auth, nc=00000001, cnonce="680d2ef66564dc19"'
,macs:{},ips:{}
,decycle:function(a){
	var cache=[],paths=[],cyclic={},cc=0,d //,serial=0
	function f(o,path){
		var t=typeof(o)
		if(o && t=='object'){
			var x=cache.indexOf(o)
			if(!path)path=[]
			if( x!=-1){
				cc++
				o=cyclic[x]
				if(!o)
				{o=cyclic[x]=[paths[x],path]
					console.log('srvr.decycle:leadEntry:ix=',x,',cyclic=',o);
				}
				else{ o.push(path)
					console.log('srvr.decycle:ix=',x,',cyclic:',o);
				}
				o={cyclic:true,ix:x}
			}
			else
			{	x=cache.push(o);
				paths[x]=path
				for(var k in o){
					x=o[k]
					t=typeof(x)
					o[k]=(x && t=='object')?
						f(x,path.concat(k))
					:x
				}
			}
		}
		return o;
	}//function f ,v4

	d=f(a)
	if(cc)
		d={cc:cc,data:d
			,cyclic:cyclic}
	else 
		d=a;
	return d;
} // decycle:function



,httpPost:function httpPost(options,onResp){
	const pds = options&&options.postData?querystring.stringify(options.postData):0
	const asic=options&&options.asic
		,msgs=asic&&asic.msgs || []
	
	if(!options)options = {};
	if(!options.hostname)options.hostname='192.168.1.122'
	if(!options.port)options.port=80
	if(!options.path)options.path='/'
	if(!options.method)options.method= 'GET'
	options.chunks=[]

	if(pds){
		options.method='POST';
		var h=options. headers
		if(!h)
			h=options. headers={}
		if(!h['Content-Type'])
			h['Content-Type']='application/x-www-form-urlencoded';
		if(!h['Content-Length'])
			h['Content-Length']=Buffer.byteLength(pds);
	}
	console.log('srvr.http:options=',options);
	const req = srvr.http.request(options, (res) => {
		  var msg=`STATUS: ${res.statusCode} 
			, HEADERS: ${JSON.stringify(res.headers)}`
		msgs.push(msg)
		console.log('srvr.http:clb-resp:options=',options,',msg=',msg,',resp=',res);
		res.setEncoding('utf8');
		res.on('data', (chunk) => {
			console.log('srvr.http:clb-resp:evt-data:options=',options,',resp=',res,',chunk=',chunk);//console.log(`BODY: ${chunk}`);
			options.chunks.push(chunk)
		  });
		  res.on('end', () => {
			msgs.push('http.request:completed-chunks')//console.log('No more data in response.');
			options.rBody=options.chunks.join('')
			console.log('srvr.http:clb-resp:evt-end:options=',options,',resp=',res,'respBody=',options.rBody);
			if(onResp)
				onResp(res,options)
		  });
		}//onResp
	);//http.request

	req.on('error', (e) => {
		const msg=`problem with request: ${e.message}`
		msgs.push(msg);
		console.log('srvr.http:clb-error:options=',options,',msg=',msg,',error=',e);
		asic.error=JSON.stringify(e)
		--srvr.scan.pending
		if(onResp)
			onResp(e,options)
		if(srvr.scan.pending<=0)
			srvr.scanCompleted()
	});

	if(pds)
		// Write data to request body
		req.write(pds);
	req.end();
}//function httpPost

 ,Asic:function Asic(houseNo,ip,rack,shelf,col,info,config,status){
	var t=this; // ,AsicProto:{}t.mac=mac;
	t.houseInfo={houseNo:houseNo,ip:ip,rack:rack,shelf:shelf,col:col}
	t.info=info||{}
	t.config=config||{}
	t.status=status||{};t.msgs=[];//t.ress=[]
	t.proc=t.doScan;//'scan'//states enums: 'scan' , 'ideal' , 'engaged' , 'notAsic' , 
	t.time={//timestamps Date.getTime()
		discovered:0// when first discovered, spans file sessions
		,loaded:0//the start of the current session, in terms of the level of the scope of OS-process
		,lastChecked:0//in the future store statistics:history-of-lastChecked ,and mean of frequency ,and history of period of each run
	}
 }
,initAsicProto:function(){
	srvr.Asic.prototype={
		run:0
		,doScan:function(){
			/*
			is.httpPost({asic:asic
				,headers:{Authorization: is.Authorization}
				,path:'/cgi-bin/get_system_info.cgi'
			 ,hostname:ssn.ip+ip
			//,postData:{'msg': 'Hello World!'}
			}//reqOptions
			, (res,options)=>{
				const root = HTMLParser.parse(options.rBody)//,x=options.asic
				asic.msgs.push('root of response=',h2j(root))//,http:{resp:res//,rBody:rBody,tree:root,req:options}};
				asic.res=res//resps.unshift(x);//console.log('doScan:post-resp:root=',x); // JSON.stringify(x)
				is.onScanned(ssn,asic.ip,root,options)
			}//resp
		)//httpPost
			*/
			return new Promise().then()
		}
		,getSystemInfo:function(){
		/*f_get_system_info
					f_get_system_info() {
	$.ajax({
		url: '/cgi-bin/get_system_info.cgi',
		type: 'GET',
		dataType: 'json',
		timeout: 30000,
		cache: false,
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////

		*/	
		}
		,getPools:function(){
			/*
		//getPools
			//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
			getPools
<request>
GET /cgi-bin/minerConfiguration.cgi HTTP/1.1
Host: 192.168.1.102
Connection: keep-alive
Authorization: Digest username="root", realm="antMiner Configuration", nonce="b1b1652793d3109e9b29f0c3a111ffe5", uri="/cgi-bin/minerConfiguration.cgi", response="3426383a4304caa7b3b68fc424050568", qop=auth, nc=0000000b, cnonce="296e6b36dadbfb71"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*\/*;q=0.8,application/signed-exchange;v=b3
Referer: http://192.168.1.102/
Accept-Encoding: gzip, deflate
Accept-Language: en-US,en;q=0.9,ar;q=0.8
</request>	
			*/
		}
		,getNetConfig:function(){
			/*
			
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
			getNetConfg
			$.ajax({
		url: '/cgi-bin/get_network_info.cgi',
		type: 'GET',
		dataType: 'json',
		timeout: 30000,
		cache: false,
		success: function(data) {
			.
			.
			.
<request>
GET /network.html HTTP/1.1
Host: 192.168.1.102
Connection: keep-alive
Authorization: Digest username="root", realm="antMiner Configuration", nonce="b1b1652793d3109e9b29f0c3a111ffe5", uri="/network.html", response="b3c4dc29536d5b31622c1c434135fb4b", qop=auth, nc=00000011, cnonce="634652857481756b"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*\/*;q=0.8,application/signed-exchange;v=b3
Referer: http://192.168.1.102/cgi-bin/minerConfiguration.cgi
Accept-Encoding: gzip, deflate
Accept-Language: en-US,en;q=0.9,ar;q=0.8
</request>
			*/
		}
		,getMinerStatus:function(){
			/*
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////

getMinerStatus

 // returns huge multiple html-tables

must get files:(check if there are xhr requests for dyna-data)
<script type="text/javascript" src="/js/xhr.js"></script>
<script type="text/javascript" src="/js/json2.min.js"></script>


<request>
GET /cgi-bin/minerStatus.cgi HTTP/1.1
Host: 192.168.1.102
Connection: keep-alive
Authorization: Digest username="root", realm="antMiner Configuration", nonce="b1b1652793d3109e9b29f0c3a111ffe5", uri="/cgi-bin/minerStatus.cgi", response="46748ffa121a0f1f89557d71a4f2fb49", qop=auth, nc=00000013, cnonce="2421ac2bde892dd7"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*\/*;q=0.8,application/signed-exchange;v=b3
Referer: http://192.168.1.102/network.html
Accept-Encoding: gzip, deflate
Accept-Language: en-US,en;q=0.9,ar;q=0.8
</request>

//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////

			*/
			
		}
		,doUpdate:function(){
			
		}
	}
}
/////////////////////////////////////////////////////
//server
/////////////////////////////////////////////////////

,http:require('http')
, express :require('express')
,init:function init(){var t=srvr
	,app=t. app=t. express()
	,express=t.express

	var router=t.router=express.Router();
	if(app)app.router=router;

	router.use(	'/', express.static('./'));

	router.post('/post/', //async
		(req,rsp,next)=>{
		try{
			let b=req.body
			rsp.json(srvr.decycle(srvr));
		}catch(ex){
			console.log(ex);
			rsp.sendStatus(500);}
	} );

	router.get('/scan/:ip/:url/:json', //async
		(req,rsp,next)=>{
		try{var p=req.params,ip=p.ip,url=p.url,jsonStr=p.json
			rsp.json(srvr.decycle(srvr.check(ip,url,jsonStr)))
		}catch(ex){
			console.log(ex);
			rsp.sendStatus(500);}
	} );
	router.get('/cachedIp/:ip', //async
		(req,rsp,next)=>{
		try{var p=req.params,ip=p.ip
			rsp.json(srvr.decycle(p.ips[ip]))
		}catch(ex){
			console.log(ex);
			rsp.sendStatus(500);}
	} );
	router.get('/cachedMac/:mac', //async
		(req,rsp,next)=>{
		try{var p=req.params,mac=p.mac
			rsp.json(srvr.decycle(p.macs[mac]))
		}catch(ex){
			console.log(ex);
			rsp.sendStatus(500);}
	} );

	router.get('/macs/', //async
		(req,rsp,next)=>{
		try{var p=req.params,a=Object.keys(srvr.macs)
			rsp.json(srvr.decycle(a))
		}catch(ex){
			console.log(ex);
			rsp.sendStatus(500);}
	} );

	router.get('/ips/', //async
		(req,rsp,next)=>{
		try{var p=req.params,a=Object.keys(srvr.ips)
			rsp.json(srvr.decycle(a))
		}catch(ex){
			console.log(ex);
			rsp.sendStatus(500);}
	} );
//future todo: mysql-history; cables; setInterval scan; house-scan;

	app.use(t.express.json());

	app.use('/', router)//t.initRouter(t.express,app));

	app.listen(process.env.PORT || '80' , ()=>{
		console.log('Server is running on port 80 /december-client.html (version 2019.12.26)');
	 })
}//init

}//srvr

srvr.init()


app={
ng:{scopes:[],models:[],templates:{} // add prop 'listeners' on each observable
	/*,directives:{
		model:function(){} // input , textarea , select , selected , checked , value
		,repeat:function(){}//, ng-options										//needsTmpltReplacement
		,'if':function(){} // ,style ,class ,switch+switchWhen+switchDefault	//needsTmpltReplacement
		,init:function(){}
		,moustache:function(){}// textNode, attrib-name, attrib-value, styleSheet 
		,include:function(){} // change, click, form 							//needsTmpltReplacement
		,template:function(){}//,												//needsTmpltReplacement
		}//directives*/
	,listener:function(scope,obj,props){}//register new listener
	,directive:function(name,callBack,template){}//create new directive
	,parseDom:function(n,parentScope){//n:: str or domNode ,str not implemented
		const prefix=['ng-','{{','}}']
		parseDom.newScope=function newScope(n,parentScope,atrb){//,directive
			if(n&&!n.ngTmpltScope){
			var p=n.ngTmpltScope={directives:[],parentScope:parentScope,tmplt:n};
			if(atrb)p.atrb=atrb // if(directive)p.directive=directive //if(! parentScope.scopeChildren)parentScope.scopeChildren=[];parentScope.scopeChildren.push(p)
			}return n&&n.ngTmpltScope;}
		function parseMoustache(txt,parentScope,j,n,atrb){
			var p=newScope(n,parentScope,atrb),i=0//'moustache'
			while(j!=-1){
				p.directives.push(x.substr(i,j))
				j=x.indexOf(prefix[2],i=j+2)
				p.directives.push({directive:'moustache',expr:x.substr(i,j)})
				j=x.indexOf(prefix[1],i=j+2)
			}p.directives.push(x.substr(i))
			return p;} // function parseMoustache
		function nod(n,parentScope){
			if(n.nodeType==n.TEXT_NODE){
				var x=n.data,j=x.indexOf(prefix[1])
				if(j!=-1)
					parseMoustache(x,parentScope,j,n)
			}else if(n.nodeName=='SCRIPT'&&n.attributes&& 
				n.attributes.type in ['ng-scope','ng-directive','ng-template']){
				//TODO: implement
			}else if(n.attributes)// check for directives in attribs
			for(var i=0,a=n.attributes,p=0,o;a && i< a.length;i++){
				var b=a[i],an=b.name,av=b.value,j
				if(an.startsWith(prefix[0])){
					if(!n.ngTmpltScope)
						p=newScope(n,parentScope)
					p.directives.push(o={directive:an.substr(prefix[0].length),attrib:b,name:an,val:av})
					//if(o.directive in ng.needsTmpltReplacement)o.needsTmpltReplacement=true
				}	//else if(an.indexOf(prefix[1])!=-1){}//TODO: implement
				else if((j=av.indexOf(prefix[1]))!=-1)
					p=parseMoustache(x,p||parentScope,j,n,b)
			}
			if(n.firstChild)
				nod(n.firstChild,n.ngTmpltScope||parentScope)
			if(n.nextSibling)
				nod(n.nextSibling,parentScope);
		} // function nod(n)
		nod(n,parentScope)
	}//parseDom
	,f:function(){
		//f.newScope=function newScope(){}
		f.nod=function fNod(n,p){//p::=workingScope
			if(n.ngTmpltScope){
				var ts=n.ngTmpltScope
				,pt=Object.assign({templateScope:ts},ts)
				pt.n=ts.tmplt.cloneNode('deepClone')
				pt.prototype=p
				p=pt
				pt=pt.n.parentNode;pt.replaceChild(p.n,ts.tmplt)
				if(ts.directive=='moustache'){pt=[ts.directives];
				}else
				for(var i =0,a=ts.directives;i<a.length;i++)
				//for(var i=0,a=n.attributes,p=0;a && i< a.length;i++)
				{	var b=a[i],e=b.expr,v
					try{v=eval(expr)}
					catch(x){console.error('app.ng.f.nod:',n,b,x);}
					switch(b.directive){
						case 'moustache':if(n.nodeType==Node){//b.tmplt
								
							}else{
								
							}break;
						case 'model'	:switch(n.nodeName){
							case 'textarea':
							case 'input':switch(n.type){
								case 'checkbox':break;
								case 'radio':break;
								default:
									
									break;
								}break;
							case 'select':break;
						};break;
						case 'repeat'	:break;
						case 'if'		:break;
						case 'init'		:break;
						case 'include'	:break;
						case 'template'	:break;
						case 'options'	:break;
						case 'change'	:break;
						case 'click'	:break;
					}
				}
			}//if(n.ngTmpltScope)
			if(n.firstChild)
				f.nod(n.firstChild,n.ngScope||p)
			if(n.nextSibling)
				f.nod(n.nextSibling,p);
		} // function fNod(n)
		var workScope={ng:ng,app:app};
		workScope.templateScope=ng.rootTmplt=workScope;
		f.nod(document.body,workScope)
	}//f

	,onload:function appNG_onload(){
		ng.rootTmplt=parseDom.newScope(document.body,{})
		ng.rootTmplt.parentScope=ng.rootTmplt;
		app.ng.parseDom(ng.rootTmplt.tmplt,ng.rootTmplt)
	}//function onload()
}//ng
,did:function app_did(id,n){if(!n)return document.getElementById(id);
	var r=n;n=n.firstChild;while(n)if(n.id==id)return n;else n=n.nextSibling;
	n=r.firstChild;while(n)if(r=did(id,n))return r;else n=n.nextSibling;}
,dce:function app_dce(n,p,t,i){var r=document.createElement(n);if(i)r.id=i;if(t)app.dct(t,r);if(p)p.appendChild(r);return r;}
,dcb:function app_dcb(p,t,i,c){var r=app.dce('button',p,t,i);r.onclick=c;return r;}
,dct:function app_dct(t,p){var r=document.createTextNode(t);if(p)p.appendChild(r);return r;}
,dci:function app_dci(p,val,id,c,typ){var r=document.createElement('input');r.type=typ||'text';
	if(id)r.id=id;if(val)r.value=val;if(p)p.appendChild(r);if(c)r.onchange=c;return r;}
,createCollapsable:function app_createCollapsable(title,prnt,id,nod){
	var t=app,fs=t.dce('fieldset',prnt),l=t.dce('legend',fs,title);
	if(!nod)nod=t.dce('span',fs,0,id);else fs.appendChild(nod);//t.dcb(l,'+',0,
	l.onclick=function(evt){try{var d=nod.style.display
			console.log('onclickCollapsable:d='+d+' ,nod='+nod+' ,'+nod.innerHTML);
			nod.style.display=(d=='none'?'block':'none');
		}catch(ex){console.error('onclickCollapsable:ex:',ex);}return nod;}}
,bld:function app_bld(params,parent)//BuildDomTree params::= id ,n:nodeName ,t:text ,clk:onclick-function ,chng:onchange-function ,a:jsobj-attributes ,c:jsarray-children-recursive-params ,s:jsobj-style ,clpsbl:string-title:collapsable; or params canbe string, or array:call bldTbl ; parent: domElement
{var t=app;try{if(typeof(params)=='string')return t.dct(params,parent);
 var p=params,n=p.n?document.createElement(p.n):p.t!=undefined?document.createTextNode(p.t):p;
	if(p.n&&(p.t!=undefined))n.appendChild(document.createTextNode(p.t));
	if(p.n&&p.n.toLowerCase()=='input')n.type=(p.a?p.a.type:0)||'text';
	if(p.id)n.id=p.id;
	if(p.name||(p.a&&p.a.name))n.name=p.name||p.a.name;
	if(p.clk)n.onclick=p.clk;
	if(p.chng)n.onchange=p.chng;
	if(p.s)for(var i in p.s)n.style[i]=p.s[i];
	if(p.a)for(var i in p.a)n[i]=p.a[i];
	if(p.c){if(p.n&&p.n.toLowerCase()=='select')t.bldSlct(p.c,n,p);
		else if(p.n&&p.n.toLowerCase()=='table')t.bldTbl (p,n);
		else for(var i=0;i<p.c.length;i++)
		 if(typeof(p.c[i])=='string')//t.dct(p.c[i],n);
			n.appendChild(document.createTextNode(p.c[i]));
		else 
			t.bld(p.c[i],n);
	}
	if(p.clpsbl)n=t.createCollapsable(p.clpsbl,parent,p.id,n);
	else if(parent)parent.appendChild(n);
	}catch(ex){console.error('app.bld:ex',ex);}return n;}//function bld
,/**
 * parameter p attributes
 *	data: body data of the xhr request sent tot the server ,default null
 *	headers: json-object of name/value , set as request headers, defaults to null
 *	asyncFunc: reference to a function that is called in asynchronous mode , when the server successfully responds the func is given as a param the xhr obj and second param p, defaults to null which is synchronise mode
 *	method: string , POST, GET, defaults to POST
 *	url: the url of xhr , defaults to empty string
 *	asJson:boolean, if true returns JSON.parse(xhr.responseText) else returns xhr.responseText
 * //as: string 'text' , 'json' , defaults to text //'html', 'xml' 
 * */
xhr:function App_xhr(p)//data,callBack,asText)
{if(!p)return p;
 var ct='Content-Type',cs='charset',x=App.isIE?new 
		ActiveXObject("microsoft.XMLHTTP")
		:new XMLHttpRequest();
	x.open(p.method||'POST',p.url||'', p.asyncFunc);
	for(var i in headers)
		x.setRequestHeader(i,headers[i]);//console.log('scriptedReq:header['+i+']:'+headers[i]);
	if(!p.headers || !p.headers[ct])x.setRequestHeader(ct, 'application/x-www-form-urlencoded');
	if(!p.headers || !p.headers[cs])x.setRequestHeader(cs, 'utf-8');
	if(p.asyncFunc)x.onreadystatechange=
		function App_xhrAsync()
		{if (x.readyState === 4 && x.code==200)
			p.asyncFunc(x,p);
		};
	if(p.headers)for(var i in p.headers)
		x.setRequestHeader( i , p.headers [ i ] ) ; //setRequestHeader( Name, Value )

	x.send((typeof p.data)=='string'?p.data:JSON.stringfy(p.data));//console.log('scriptedReq:response:'+ajax.responseText);
	//return asText?x.responseText:async?0:eval('('+x.responseText+')');
	return p.asJson?JSON.parse ( x . responseText ) : x . responseText ;
	}//function xhr

}//app

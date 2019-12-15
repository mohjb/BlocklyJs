ng={prefix:['ng-','{{','}}'],scopes:[],models:[],templates:{} // add prop 'listeners' on each observable
	/*,directives:{
		model:function(){} // input , textarea , select , selected , checked , value
		,repeat:function(){}//, ng-options										//needsTmpltReplacement
		,'if':function(){} // ,style ,class ,switch+switchWhen+switchDefault	//needsTmpltReplacement
		,init:function(){}
		,moustache:function(){}// textNode, attrib-name, attrib-value, styleSheet 
		,include:function(){} // change, click, form 							//needsTmpltReplacement
		,template:function(){}//,												//needsTmpltReplacement
		}//directives*/
	,listener:function listener(scope,obj,props){}//register new listener
	,parseDom:function parseDom(n,parentScope){//n:: str or domNode ,str not implemented
		var prefix=ng.prefix
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
	,bld:function bld(){
		//bld.newScope=function newScope(){}
		function procMoustache(a){
			var b=[],c,x;for(var i in a){
				c=a[i]
				if(typeof(c)=='string' && c.length)
					b.push(c)
				else if(c.expr)
					try{x=eval(c.expr);b.push(x)}catch(ex){}
			}
			x= b.join('')
			return x;
		}
		bld.nod=function bldNod(n,p,exclude){//p::=workingScope
			var ts=n.ngTmpltScope,a=ts&&ts.directives,active=1
			if(ts){
				if(n.nodeType==n.TEXT_NODE)
					n.data=procMoustache(a)
				else{
					var pt=Object.assign({templateScope:ts},ts)
					pt.n=ts.tmplt.cloneNode('deepClone')
					pt.prototype=p
					p=pt
					pt=pt.n.parentNode;pt.replaceChild(p.n,ts.tmplt)
					for(var i =0;i<a.length;i++)if(a[i]!=exclude)
					{	var b=a[i],e=b.expr,v=e
						if(e && !['repeat','options','moustache'].includes(b.directive))
							try{v=eval(e)}
							catch(x){console.error('ng.ng.bld.nod:',n,b,x);}
						switch(b.directive){
							case 'moustache':
									v=procMoustache(b.directives)
									n.attributes[b.atr.name].value=v
								break;
							case 'model'	:switch(n.nodeName){
								case 'TEXTAREA':n.value=v;break;
								case 'INPUT':switch(n.type){
									case 'CHECKBOX':n.checked=v;break;
									case 'RADIO':break;//TODO:investigate,implement
									default:
										n.value=v;
										break;
									}break;
								case 'SELECT':
									for(var o in n.options)
										o.selected=o==v||o.text==v||o.value==v
									o.selected=v;
								break;
							};break;
							case 'repeat'	:
								n.removeAttribute(ng.prefix[0]+'repeat');
								var s=e.split('in'),ix=p['$index']=0
								try{v=p['$repeatIterations']=eval(s[1])
									s=s[0].split('.');
									for(var ri in v)
									{	x=v[ri]
										try{ng.byPath(p,s.concat[x],1,1)
											pt.insertBefore(ts.tmplt,n)
											bld.nod(ts.tmplt,p)
										}catch(x){console.error('ng.ng.bld.nod:',n,b,x);}
										p['$index']=++ix
									}}
								catch(x){console.error('ng.ng.bld.nod:',n,b,x);}
								pt.removeChild(n)
								break;
							case 'if'		:n.style.display=(active=v)?'':'none';break;
							case 'init'		:break;
							case 'include'	:break;
							case 'template'	:break;
							case 'options'	:if(n.nodeName=='SELECT'){
									while(s.options.length)
										n.removeChild(n.options[0])
									for(var i in v){
										x=document.createElement('option')
										x.innerHTML=v[i]
										n.appendChild(x)
									}
								};break;
							case 'change'	:break;
							case 'click'	:break;
						}
					}
				}
			}//if(n.ngTmpltScope)
			if(active && n.firstChild)
				bld.nod(n.firstChild,n.ngScope||p)
			if(n.nextSibling)
				bld.nod(n.nextSibling,p);
		} // function bldNod(n)
		var workScope={ng:ng};
		workScope.templateScope=ng.rootTmplt=workScope;
		bld.nod(document.body,workScope)
	}//bld
	,onload:window.onload=function appNG_onload(){
		ng.rootTmplt=parseDom.newScope(document.body,{})
		ng.rootTmplt.parentScope=ng.rootTmplt;
		ng.parseDom(ng.rootTmplt.tmplt,ng.rootTmplt)
	}//function onload()
	,byPath:function byPath(o,p,isCreate,isSetLastValue){
		var i=0,x, n=p&&p.length
		while(o && i<n )
		{	x=o[p[i]]
			if(i<n && !x && isCreate)
				x=o[p[i]]={}
			if(isSetLastValue && i+2>=n && x)
			{x=o[p[i]]=p[i+1];i++;}
			o=x;i++
		}return x;}//function byPath
	,did:function app_did(id,n){if(!n)return document.getElementById(id);
		var r=n;n=n.firstChild;while(n)if(n.id==id)return n;else n=n.nextSibling;
		n=r.firstChild;while(n)if(r=did(id,n))return r;else n=n.nextSibling;}
	,dce:function app_dce(n,p,t,i){var r=document.createElement(n);if(i)r.id=i;if(t)ng.dct(t,r);if(p)p.appendChild(r);return r;}
	,dct:function app_dct(t,p){var r=document.createTextNode(t);if(p)p.appendChild(r);return r;}
}//ng

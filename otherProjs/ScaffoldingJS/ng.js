ng={scopes:[],models:[],templates:{} // add prop 'listeners' on each observable
	/*,directives:{
		model:function(){} // input , textarea , select , selected , checked , value
		,repeat:function(){}//, ng-options										//needsTmpltReplacement
		,'if':function(){} // ,style ,class ,switch+switchWhen+switchDefault	//needsTmpltReplacement
		,init:function(){}
		,moustache:function(){}// textNode, attrib-name, attrib-value, styleSheet 
		,include:function(){} // change, click, form 							//needsTmpltReplacement
		,template:function(){}//,												//needsTmpltReplacement
		}//directives*/
	,did:function app_did(id,n){if(!n)return document.getElementById(id);
		var r=n;n=n.firstChild;while(n)if(n.id==id)return n;else n=n.nextSibling;
		n=r.firstChild;while(n)if(r=did(id,n))return r;else n=n.nextSibling;}
	,listener:function(scope,obj,props){}//register new listener
	,compile:function(n,parentScope){//n:: domNode
		const prefix=['ng-','{{','}}']
		compile.newScope=function newScope(n,parentScope,directive){
			if(n&&!n.ngTmpltScope){
			var p=n.ngTmpltScope={directives:[],parentScope:parentScope,tmplt:n};
			if(directive)p.directive=directive
			//if(! parentScope.scopeChildren)parentScope.scopeChildren=[];parentScope.scopeChildren.push(p)
			}return n&&n.ngTmpltScope;}
		function parseMoustache(txt,parentScope,j,n){
			var p=newScope(n,parentScope,'moustache'),i=0
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
			for(var i=0,a=n.attributes,p=n.ngTmpltScope,o;a && i< a.length;i++){	
				var b=a[i],an=b.name,av=b.value,j
				if(an.startsWith(prefix[0])){
					if(!n.ngTmpltScope)
						p=newScope(n,parentScope)
					p.directives.push(o={directive:an.substr(3),attrib:b,name:an,val:av})
					//if(o.directive in ng.needsTmpltReplacement)o.needsTmpltReplacement=true
				}	//else if(an.indexOf(prefix[1])!=-1){}//TODO: implement
				else if((j=av.indexOf(prefix[1]))!=-1)
					p=parseMoustache(x,p||parentScope,j,[n,b])
			}
			// check child
			if(n.firstChild)
				nod(n.firstChild,n.ngTmpltScope||parentScope)
			//check next sibling
			if(n.nextSibling)
				nod(n.nextSibling,parentScope);
		} // function nod(n)
		nod(n,parentScope)
	}//compile
	,apply:function(){
		//apply.newScope=function newScope(){}
		function nod(n,p){//p::=workingScope
			if(n.ngTmpltScope){
				var ts=n.ngTmpltScope
				,pt=Object.assign({templateScope:ts},ts)
				pt.n=ts.tmplt.cloneNode('deepClone')
				pt.prototype=p
				p=pt
				pt=ts.tmplt.parentNode;pt.replaceChild(p.n,ts.tmplt)
				if(ts.directive=='moustache'){
					pt=[ts.directives]
				}else
				for(var i =0,a=ts.directives;i<a.length;i++)
				//for(var i=0,a=n.attributes,p=0;a && i< a.length;i++)
				{	var b=a[i],e=b.expr,v
					try{v=eval(expr)}
					catch(x){console.error('ng.apply.nod:',n,b,x);}
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
			}
			// check child
			if(n.firstChild)
				nod(n.firstChild,n.ngScope||p)
			//check next sibling
			if(n.nextSibling)
				nod(n.nextSibling,p);
		} // function nod(n)
		var workScope={ng:ng,templateScope:ng.rootTmplt};
		nod(document.body,workScope)
	}//apply

	,onload:function appNG_onload(){
		ng.rootTmplt=compile.newScope(document.body,{})
		ng.rootTmplt.parentScope=ng.rootTmplt;
		ng.compile(ng.rootTmplt.tmplt,ng.rootTmplt)
	}//function onload()
}//ng

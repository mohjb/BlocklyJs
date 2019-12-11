# BlocklyJs
<a href="https://developers.google.com/blockly/guides/create-custom-blocks/overview">custom-blocks</a> for Blockly that comprehensively cover all Javascript constructs , and blocks beyond the js-grammar

build custom-blocks that correspond to every construct and every syntax in javascript

some examples of the custom blocks: (almost 200 custom blocks for the level0 fundamental-language, the remaining levels at a later stage of the development of this project)
<pre>
Lang -LEVEL0 Core //classPackage
  Expr
	scope-property operators
		dotNotation		[count=1]
		assign , (pre-add , post-add , pre-subtract , post-subtract)(+= , -= , *=,/=,%=,<<=,>>=,>>>=,&=,^=,|=,Destructuring-assignment-arrayOrObj) [count=1, accum=2] enums=17
		delete	 [count=1, accum=3]
	instanceof/	[count=1, accum=4]
	Typeof,new/instanciate		[count=1, accum=5] enums=2
	Arithmetic (add, subtract, multiply, divide, intDiv , ** )	[count=1, accum=6] enums=6
	logical ( and , or , not-uniary)	[count=1, accum=7] enums=3
	comparison ( greaterThan , lessThan ,lessOrEqueal ,greaterOrEqual ,notEqual ,equal , eqStrict === , !== , in)	[count=1, accum=8] enums=7
	bitwise ( and, or, xor, not-uniary, rotateRight, rotateLeft, shiftLeft)	[count=1, accum=9] enums=7
	Conditional (ternary) operator	[count=1, accum=10]
	Comma operator	[count=1, accum=11]
	eval() uneval() decodeURI() decodeURIComponent() encodeURI() encodeURIComponent()	[count=1, accum=12] enums=6
  Scope
	(This,Super/prototype,arguments object)	[count=1, accum=13] enums=3
	Object <constructor> ( now on, everything has a nested block "prototype")	[count=1, accum=14]
	Function 	<constructor>	[count=1, accum=15]
	Number <constructor> ( EPSILON MAX_SAFE_INTEGER MAX_VALUE MIN_SAFE_INTEGER MIN_VALUE NaN NEGATIVE_INFINITY POSITIVE_INFINITY)	[count=2, accum=17] enums=8
		( isNaN isFinite isInteger isSafeInteger parseFloat parseInt)	[count=1, accum=18] enums=6
		( toExponential toFixed toLocaleString toPrecision )	[count=4, accum=22]
	Math (abs acos acosh asin asinh atan atanh atan2 cbrt ceil floor clz32 exp cos cosh expm1 fround hypot imul log log1p log10 log2 max min pow random round sign sin sinh sqrt tan tanh trunc )	[count=1, accum=23] enums=35
		constants (PI E LN2 LN10 LOG2E LOG10E SQRT1_2 SQRT2 )	[count=1, accum=24] enums=8
	Date <constructor> ( (value) (dateString) (year, monthIndex [, day [, hours [, minutes [, seconds [, milliseconds]]]]]) )	[count=1, accum=25] enums=3
		Date.now() Date.parse() Date.UTC()	[count=3, accum=28]
		( getDate getFullYear getHours getMilliseconds getMinutes getMonth getSeconds getTime getUTCDate getUTCFullYear getUTCHours getUTCMilliseconds getUTCMinutes getUTCMonth getUTCSeconds getYear getDay getTimezoneOffset getUTCDay )	[count=1, accum=29] enums=19
		( setDate setFullYear setHours setMilliseconds setMinutes setMonth setSeconds setTime setUTCDate setUTCFullYear setUTCHours setUTCMilliseconds setUTCMinutes setUTCMonth setUTCSeconds setYear )	[count=1, accum=30] enums=16
		( toDateString toISOString toJSON toGMTString toLocaleDateString toLocaleFormat toLocaleString toLocaleTimeString toSource toString toTimeString toUTCString valueOf )	[count=1, accum=31] enums=48
	String <constructor>	[count=1, accum=32]
		(fromCharCode fromCodePoint)	[count=3, accum=36]
		0arg (constructor length toLowerCase toSource toString toUpperCase trim trimStart trimLeft trimEnd trimRight valueOf )	[count=1, accum=37] enums=12
		1arg (charAt charCodeAt codePointAt match matchAll normalize repeat search split )	[count=1, accum=38] enums=9
		1argv(concat localeCompare includes indexOf endsWith lastIndexOf padEnd padStart replace slice startsWith substr substring )	[count=1, accum=39] enums=13
		0v(toLocaleLowerCase toLocaleUpperCase)	[count=1, accum=40] enums=2
	RegExp <constructor>	[count=1, accum=41]
		( length lastIndex constructor flags dotAll global ignoreCase multiline source sticky unicode )	[count=1, accum=42] enums=11
		( compile exec test toSource )	[count=1, accum=43] enums=4
	Array <constructor> (from isArray of )	[count=4, accum=47]
		0arg (length pop push reverse shift toSource toString )	[count=1, accum=48] enums=7
		0arg1(sort )	[count=1, accum=49]
		1arg (atIndex setLength join )	[count=1, accum=50] enums=3
		1arg2(includes indexOf lastIndexOf slice )	[count=1, accum=51] enums=4
		0argv(toLocaleString)	[count=1, accum=52]
		1argv(concat copyWithin fill splice unshift)	[count=1, accum=53] enums=5
		(entries every filter find findIndex forEach keys map reduce reduceRight some values)	[count=1, accum=54] enums=12
	JSON(parse,stringify)	[count=1, accum=55] enums=2 
	Promise(constructor,then)	[count=1, accum=56] enums=2 
  Literal
	String
	Number
	Boolean	[count=3, accum=59]
	undefined null globalThis	[count=2, accum=61]
	Array
		Item
	Object
		Property
			Id
			Expr
  Define
	Scope
	Field	[count=1, accum=62]
		Id
		type
		modifiers
			accessControl
			meta-rbac
		Sections
			Annotation
			Responsibilities
			View
			Triat
			Inheritance	[count=6, accum=68]
	Method
		arrowFunction	[count=1, accum=69]
		returnType
		throws
		params(default,rest,destruct)	[count=1, accum=70]
  ControlFlow
	If(elseif,else)	[count=1, accum=71]
	For	[count=1, accum=72]
	While	[count=1, accum=73]
	Do	[count=1, accum=74]
	Switch(case)	[count=1, accum=75]
	Try(catch,finally)	[count=1, accum=76]
	Return	[count=1, accum=77]
	Throw	[count=1, accum=78]
	Continue	[count=1, accum=79]
	Break	[count=1, accum=80]
	methodCall(setParam//arrowFunction)	[count=1, accum=81]

LANG-LEVEL2
	Reflect
		Class
		Field
		Method
		Package
		Type
		Section
		Annotation	[count=7, accum=207]
	GraphicReflection
		IDE
			Workspace
			Toolkit
				Sections
					Annotation
					Responsibilities
					View
					Triat
					Inheritance
			Template
		Defaults
			Workspace (and hierarchy and methods)
			Toolkit (and hierarchy and methods)
	BlocklyFactory
		blockFactory (and hierarchy and methods)
		?

LANG-LEVEL3
  io
	fs (and hierarchy and methods)
	db
		mysql (and hierarchy and methods)
  npmLib
	express (and hierarchy and methods)
	sequelize (and hierarchy and methods)
  threeD (and hierarchy and methods)
  canvas ( and methods)
  dom
	html
	document
	window
		location
	angularjs
	XMLHttpRequest	[count=1, accum=]
	Proxy

LANG-LEVEL4

  net
	rest (and hierarchy and methods)
	socket(and methods)

  screen
  system
  FSM
  Thread
	CallStack
	CurrentThread
	await
	async
	sync

  Scope
	BigInt
	Int8Array
	Uint8ClampedArray
	Int16Array
	Uint16Array
	Int32Array
	Uint32Array
	Float32Array
	Float64Array
	BigInt64Array
	BigUint64Array
	Map
	Set
	WeakMap
	WeakSet
	ArrayBuffer
	SharedArrayBuffer 
	Atomics 
	DataView
	Generator
	GeneratorFunction
	AsyncFunction 
	Reflect
	
	Intl
	Intl.Collator
	Intl.DateTimeFormat
	Intl.ListFormat
	Intl.NumberFormat
	Intl.PluralRules
	Intl.RelativeTimeFormat
	Intl.Locale
</pre>

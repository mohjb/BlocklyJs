# BlocklyJs
<a href="https://developers.google.com/blockly/guides/create-custom-blocks/overview">custom-blocks</a> for Blockly that comprehensively cover all Javascript constructs , and blocks beyond the js-grammar

build custom-blocks that correspond to every construct and every syntax in javascript

some examples of the custom blocks: (almost 200 custom blocks for the level0 fundamental-language, the remaining levels at a later stage of the development of this project)
<pre>
Lang -LEVEL0 Core //classPackage
  Expr
	scopes/properties
		dotNotation		[count=1]
		assign , (pre-add , post-add , pre-subtract , post-subtract, assign-add , assign-subtract , assign-mul, [count=1, accum=2] enums=8
		delete	 [count=1, accum=3]
	Typeof/instanceof	[count=2, accum=5]
	Arithmetic (add, subtract, multiply, divide, intDiv , ** ) 	[count=1, accum=6] enums=6
	logical ( and , or , not)	[count=1, accum=7] enums=3
	comparison ( greaterThan , lessThan ,lessOrEqueal ,greaterOrEqual ,notEqual ,equal , eqStrict ===)	[count=1, accum=8] enums=7
	bitwise ( and, or, xor, not, rotateRight, rotateLeft, shiftLeft)	[count=1, accum=9] enums=7
	eval() uneval() decodeURI() decodeURIComponent() encodeURI() encodeURIComponent()	[count=6, accum=15]
  Scope
	This	[count=1, accum=16]
	Super/prototype	[count=1, accum=17]
	Object <constructor> ( now on, everything has a nested block "prototype")	[count=1, accum=18]
	Function 	<constructor>	[count=1, accum=19]
	Number <constructor> ( EPSILON MAX_SAFE_INTEGER MAX_VALUE MIN_SAFE_INTEGER MIN_VALUE NaN NEGATIVE_INFINITY POSITIVE_INFINITY)	[count=2, accum=21] enums=8
		( isNaN isFinite isInteger isSafeInteger parseFloat parseInt)	[count=1, accum=22] enums=6
		( toExponential toFixed toLocaleString toPrecision )	[count=4, accum=26]
	Math (abs acos acosh asin asinh atan atanh atan2 cbrt ceil floor clz32 exp cos cosh expm1 fround hypot imul log log1p log10 log2 max min pow random round sign sin sinh sqrt tan tanh trunc )	[count=1, accum=27] enums=35
		constants (PI E LN2 LN10 LOG2E LOG10E SQRT1_2 SQRT2 )	[count=1, accum=28] enums=8
	Date <constructor> ( (value) (dateString) (year, monthIndex [, day [, hours [, minutes [, seconds [, milliseconds]]]]]) )	[count=1, accum=29] enums=3
		Date.now() Date.parse() Date.UTC()	[count=3, accum=32]
		( getDate getDay getFullYear getHours getMilliseconds getMinutes getMonth getSeconds getTime getTimezoneOffset getUTCDate getUTCDay getUTCFullYear getUTCHours getUTCMilliseconds getUTCMinutes getUTCMonth getUTCSeconds getYear  setDate setFullYear setHours setMilliseconds setMinutes setMonth setSeconds setTime setUTCDate setUTCFullYear setUTCHours setUTCMilliseconds setUTCMinutes setUTCMonth setUTCSeconds setYear  toDateString toISOString toJSON toGMTString toLocaleDateString toLocaleFormat toLocaleString toLocaleTimeString toSource toString toTimeString toUTCString valueOf )	[count=48, accum=80]
	String <constructor>	[count=1, accum=81]
		(fromCharCode fromCodePoint)	[count=3, accum=84]
		(constructor length)	[count=1, accum=85] enums=2
		(charAt charCodeAt codePointAt concat includes endsWith indexOf lastIndexOf localeCompare match matchAll normalize padEnd padStart  repeat replace search slice split startsWith substr substring toLocaleLowerCase toLocaleUpperCase toLowerCase toSource toString toUpperCase trim trimStart trimLeft trimEnd trimRight valueOf String.prototype[@@iterator] )	[count=36, accum=121]
	RegExp <constructor>	[count=1, accum=122]
		( length lastIndex constructor flags dotAll global ignoreCase multiline source sticky unicode )	[count=11, accum=133]
		( compile exec test toSource )	[count=4, accum=137]
	Array <constructor> (from isArray of )( length atIndex )	[count=6, accum=143]
		( copyWithin fill pop push reverse shift sort splice unshift)	[count=9, accum=152]
		(concat includes indexOf join lastIndexOf slice toSource toString toLocaleString)	[count=9, accum=161]
		(entries every filter find findIndex forEach keys map reduce reduceRight some values)	[count=12, accum=173]
	JSON(parse,stringify)	[count=1, accum=174] enums=2 
	Promise(constructor,then)	[count=1, accum=175] enums=2 
	
  Literal
	String
	Number
	Boolean	[count=3, accum=178]
	undefined null globalThis	[count=2, accum=180]
	Array
		Item
	Object
		Property
			Id
			Expr
  Define
	Scope
	Field	[count=1, accum=181]
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
			Inheritance	[count=6, accum=187]
	Method
		arrowFunction	[count=1, accum=188]
		returnType
		throws
		params	[count=1, accum=189]
  ControlFlow
	If(elseif,else)	[count=1, accum=190]
	For	[count=1, accum=191]
	While	[count=1, accum=192]
	Do	[count=1, accum=193]
	Switch(case)	[count=1, accum=194]
	Try(catch,finally)	[count=1, accum=195]
	Return	[count=1, accum=196]
	Throw	[count=1, accum=197]
	Continue	[count=1, accum=198]
	Break	[count=1, accum=199]
	methodCall(setParam//arrowFunction)	[count=1, accum=200]

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

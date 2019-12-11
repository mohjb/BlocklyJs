# BlocklyJs
<a href="https://developers.google.com/blockly/guides/create-custom-blocks/overview">custom-blocks</a> for Blockly that comprehensively cover all Javascript constructs , and blocks beyond the js-grammar

build custom-blocks that correspond to every construct and every syntax in javascript

some examples of the custom blocks: (almost 140 custom blocks)
<pre>
Core //classPackage
	Lang
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
			Number <constructor> ( EPSILON MAX_SAFE_INTEGER MAX_VALUE MIN_SAFE_INTEGER MIN_VALUE NaN NEGATIVE_INFINITY POSITIVE_INFINITY)	[count=1, accum=12] enums=8
				( isNaN isFinite isInteger isSafeInteger parseFloat parseInt)	[count=1, accum=13] enums=6
				( toExponential toFixed toLocaleString toPrecision )	[count=4, accum=17]
			Math (abs acos acosh asin asinh atan atanh atan2 cbrt ceil floor clz32 exp cos cosh expm1 fround hypot imul log log1p log10 log2 max min pow random round sign sin sinh sqrt tan tanh trunc )	[count=1, accum=18] count=35
				constants (PI E LN2 LN10 LOG2E LOG10E SQRT1_2 SQRT2 )	[count=1, accum=19] enums=8
			Date <constructor> ( (value) (dateString) (year, monthIndex [, day [, hours [, minutes [, seconds [, milliseconds]]]]]) )	[count=1, accum=20] enums=3
				Date.now() Date.parse() Date.UTC()	[count=3, accum=23]
				( getDate getDay getFullYear getHours getMilliseconds getMinutes getMonth getSeconds getTime getTimezoneOffset getUTCDate getUTCDay getUTCFullYear getUTCHours getUTCMilliseconds getUTCMinutes getUTCMonth getUTCSeconds getYear  setDate setFullYear setHours setMilliseconds setMinutes setMonth setSeconds setTime setUTCDate setUTCFullYear setUTCHours setUTCMilliseconds setUTCMinutes setUTCMonth setUTCSeconds setYear  toDateString toISOString toJSON toGMTString toLocaleDateString toLocaleFormat toLocaleString toLocaleTimeString toSource toString toTimeString toUTCString valueOf )	[count=48, accum=71]
			String <constructor>	[count=1, accum=72]
				(fromCharCode fromCodePoint)	[count=3, accum=75]
				(constructor length)	[count=1, accum=76] enums=1
				(charAt charCodeAt codePointAt concat includes endsWith indexOf lastIndexOf localeCompare match matchAll normalize padEnd padStart  repeat replace search slice split startsWith substr substring toLocaleLowerCase toLocaleUpperCase toLowerCase toSource toString toUpperCase trim trimStart trimLeft trimEnd trimRight valueOf String.prototype[@@iterator] )	[count=36, accum=112]
			RegExp <constructor>	[count=1, accum=113]
				( length lastIndex constructor flags dotAll global ignoreCase multiline source sticky unicode )	[count=11, accum=124]
				( compile exec test toSource )	[count=4, accum=128]
			Array (from isArray of )( length atIndex )	[count=5, accum=133]
				( copyWithin fill pop push reverse shift sort splice unshift)	[count=9, accum=142]
				(concat includes indexOf join lastIndexOf slice toSource toString toLocaleString)	[count=9, accum=151]
				(entries every filter find findIndex forEach keys map reduce reduceRight some values)	[count=12, accum=154]
			JSON	[count=2, accum=156]
			Promise	[count=2, accum=158]
			

		Reflect (LEVEL2)
			Class
			Field
			Method
			Package
			Type
			Section
			Annotation	[count=7, accum=165]
		Literal
			String
			Number
			Boolean	[count=3, accum=168]
			undefined null globalThis	[count=2, accum=170]
			Array
				Item
			Object
				Property
					Id
					Expr
		Define
			Scope
			Field	[count=1, accum=171]
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
					Inheritance	[count=6, accum=177]
			Method
				arrowFunction	[count=1, accum=178]
				returnType
				throws
				params	[count=1, accum=179]
		ControlFlow
			If(elseif,else)	[count=1, accum=180]
			For	[count=1, accum=181]
			While	[count=1, accum=182]
			Do	[count=1, accum=183]
			Switch(case)	[count=1, accum=184]
			Try(catch,finally)	[count=1, accum=184]
			Return	[count=1, accum=185]
			Throw	[count=1, accum=186]
			Continue	[count=1, accum=187]
			Break	[count=1, accum=188]
			methodCall(setParam//arrowFunction)	[count=1, accum=189]
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

LEVEL2-LANG

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

	Boolean
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
	XMLHttpRequest
	Reflect
	Proxy
	
	Intl
	Intl.Collator
	Intl.DateTimeFormat
	Intl.ListFormat
	Intl.NumberFormat
	Intl.PluralRules
	Intl.RelativeTimeFormat
	Intl.Locale
</pre>

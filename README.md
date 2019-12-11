# BlocklyJs
<a href="https://developers.google.com/blockly/guides/create-custom-blocks/overview">custom-blocks</a> for Blockly that comprehensively cover all Javascript constructs , and blocks beyond the js-grammar

build custom-blocks that correspond to every construct and every syntax in javascript

some examples of the custom blocks: (almost 140 custom blocks)
<pre>
Core //classPackage
	Lang
		Expr
			scopes/properties
				dotNotation
				assign , (pre-add , post-add , pre-subtract , post-subtract, assign-add , assign-subtract , assign-mul, 
				delete
				ArrayIndex
				ArrayLength
			Typeof/instanceof
			Arithmetic (add, subtract, multiply, divide, intDiv , ** )
			logical ( and , or , not)
			comparison ( greaterThan , lessThan ,lessOrEqueal ,greaterOrEqual ,notEqual ,equal , eqStrict ===)
			bitwise ( and, or, xor, not, rotateRight, rotateLeft, shiftLeft)
			eval() uneval() decodeURI() decodeURIComponent() encodeURI() encodeURIComponent()
		Scope
			This
			Super/prototype
			Object ( now on, everything has a nested block "prototype")
			Function (prototype)
			Number ( EPSILON MAX_SAFE_INTEGER MAX_VALUE MIN_SAFE_INTEGER MIN_VALUE NaN NEGATIVE_INFINITY POSITIVE_INFINITY)
				( isNaN isFinite isInteger isSafeInteger parseFloat parseInt) (prototype)
				( toExponential toFixed toLocaleString toPrecision )
			Math (abs, acos, acosh, asin, asinh, atan, atanh, atan2, cbrt, ceil, floor, clz32, exp, cos, cosh, expm1, fround, hypot, imul, log, log1p, log10, log2, max, min, pow, random, round, sign, sin, sinh, sqrt, tan, tanh, trunc )
				constants (PI , E , LN2 , LN10 , LOG2E , LOG10E , SQRT1_2 , SQRT2 )(prototype)
			Date
				new Date();new Date(value);new Date(dateString);new Date(year, monthIndex [, day [, hours [, minutes [, seconds [, milliseconds]]]]]); 
				Date.now() Date.parse() Date.UTC()
				( getDate getDay getFullYear getHours getMilliseconds getMinutes getMonth getSeconds getTime getTimezoneOffset getUTCDate getUTCDay getUTCFullYear getUTCHours getUTCMilliseconds getUTCMinutes getUTCMonth getUTCSeconds getYear  setDate setFullYear setHours setMilliseconds setMinutes setMonth setSeconds setTime setUTCDate setUTCFullYear setUTCHours setUTCMilliseconds setUTCMinutes setUTCMonth setUTCSeconds setYear  toDateString toISOString toJSON toGMTString toLocaleDateString toLocaleFormat toLocaleString toLocaleTimeString toSource toString toTimeString toUTCString valueOf )
			String
				(fromCharCode fromCodePoint)
				(constructor length)
				(charAt charCodeAt codePointAt concat includes endsWith indexOf lastIndexOf localeCompare match matchAll normalize padEnd padStart  repeat replace search slice split startsWith substr substring toLocaleLowerCase toLocaleUpperCase toLowerCase toSource toString toUpperCase trim trimStart trimLeft trimEnd trimRight valueOf String.prototype[@@iterator] )
			RegExp
				( length lastIndex constructor flags dotAll global ignoreCase multiline source sticky unicode )
				( compile exec test toSource )
			Array ( length ) ( copyWithin fill pop push reverse shift sort splice unshift)
				(concat includes indexOf join lastIndexOf slice toSource toString toLocaleString)
				(entries every filter find findIndex forEach keys map reduce reduceRight some values)
			JSON
			Promise
			

		Reflect
			Class
			Field
			Method
			Package
			Type
			Section
			Annotation
		Literal
			String
			Number
			Boolean
			undefined null globalThis
			Array
				Item
			Object
				Property
					Id
					Expr
		Define
			Scope
			Field
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
					Inheritance
			Method
				arrowFunction
				returnType
				throws
				params
		ControlFlow
			If
			For
			While
			Do
			Switch
			Try
			Return
			Throw
			Continue
			Break
			methodCall
				setParam
				arrowFunction
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

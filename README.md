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
				assign
				delete
				ArrayIndex
				ArrayLength
			Typeof/instanceof
			Math //Arithmetic
				arithmitic
					add
					sub
					mul
					div
					intDiv
					power
					log
					sin
					cos
				logical
					and
					or
					not
				comparison
					gt
					lt
					lte
					gte
					neq
					eq
				bitwise
					and
					or
					xor
					not
					rotateRight
					rotateLeft
					shiftLeft
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
			Null/undefined
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
		Scope
			This
			Super/prototype
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
net
	rest (and hierarchy and methods)
	socket(and methods)
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
screen
system
fsm
Thread
	CallStack
	CurrentThread
	await
	async
	sync
</pre>

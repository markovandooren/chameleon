The Chameleon framework is a framework for creating software languages.

Design priciples
================

* Modularity is extremely important.
  - Language semantics are modularized as much as possible.
  - Tools contain no language semantics, language models contain no tool related functionality. This makes it very easy to create language-independent tools.
  - By doing this, the framework can do a lot of work for you without imposing limitations. You can plug in your own semantics when required.
  - Another advantage is that you can easily reuse language constructs from other languages, as they generally don't depend on other details.
  - Many default implementations are provided to limit the amount of code you must write.
  - Examples
    - Every language automatically has support for namespaces and imports.
    - The dependency analysis works out of the box for any language.
    - The optional Eclipse IDE only needs a few lines of code to support a new language.

* Models are allowed to be incomplete. See the CrossReference interface for information on how this is achieved.
* No code generation, everything is programming in Java. Programming with generated code is too messy. You can use a parser generator for parsing files, but the framework does not require it.

Most important abstractions
===========================

Read the documentation of the following types.

- Element: every language construct is an element.
- Cross-Reference: everything that points to another element
- Declaration: something that can be referenced. Basically everything with a 'name'.
- Signature: a 'name' of a declaration.
- Namespace: the namespace of a declaration. Think of the package structure of Java, or the namespace structur of C#
- Document: containers of model descriptions. This is usually a file.
- InputSource: an object that loads a single document
- DocumentLoader: an object that create input sources from a resource (a .jar file, a directory, a database,...)


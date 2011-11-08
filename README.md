# Yolan - yocto language

Minimalistic language for easy mobile development, with on device support.

Homoiconic and with simple static type system designed to make it easy to develop on the phone itself

Easy compilable to java/javascript, for targeting phonegap(android, iphone, etc.) as well as j2me.

# Syntax

Mainly inspired by the c-language family, lisp and smalltalk.

# Literals

- functions: within curly braces `{` ... `}`
- strings: delimited by double-quotes `"([^"] | \.)*"`
- symbols: starts with a single quote `'[^ ]*`
- numbers: `[0-9]*(.[0-9]+)?`


# Semantics

Mainly inspired by JavaScript and Java, which also are the virtual machines it compiles to.


# Features

- java-like interfaces/classes
- true closures / first class functions
- javascript-like event based programming
- designed for autocompletion / coding on mobile device

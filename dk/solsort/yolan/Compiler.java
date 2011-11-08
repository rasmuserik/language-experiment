package dk.solsort.yolan;
import java.io.*;

class Token {
    public static final int EOF = 0;
    public static final int RCURLY = 1;
    public static final int LCURLY = 2;
    public static final int SYMBOL = 3;
    public static final int NUMBER = 4;
    public static final int STRING = 5;
    public static final int ID = 6;
    public static final int EOL = 7;
    public static final int LPAREN = 8;
    public static final int RPAREN = 9;
    public static final Token eof = new Token(EOF, "[EOF]");
    public static final Token rcurly = new Token(RCURLY, "}");
    public static final Token lcurly = new Token(LCURLY, "{");
    public static final Token eol = new Token(EOL, "[EOL]");
    public static final Token lparen = new Token(LPAREN, "(");
    public static final Token rparen = new Token(RPAREN, ")");

    public int type;
    public String val;
    public Token(int type) {
        this.type = type;
    }
    public Token(int type, String val) {
        this.type = type;
        this.val = val;
    }
    public String toString() {
        return "[" + type + ": " + val + "]";
    }
}

class Tokeniser {
    String source;
    int pos;
    int c;
    void nextc() {
        if(pos >= source.length()) {
            c = -1;
        } else {
            c = source.charAt(pos);
            ++pos;
        }
    }

    boolean isWs() {
        return c == ' ' || c == '\t' || c == '\r';
    }
    boolean isSpecial() {
        return isWs() || isEOF() || c == '(' || c == ')' || c == '{' || c == '}' || c == '\n';
    }

    boolean isEOF() {
        return c == -1;
    }

    public Tokeniser(String source) {
        this.source = source;
        this.pos = 0;
        nextc();
    }

    public char unescapeAndNext() {
        int result = c;
        if(c == '\\') {
            nextc();
            result = c;
        }
        nextc();
        return (char) result;
    }

    public Token next() {
        Token result;
        while(isWs()) {
            nextc();
        }
        switch(c) {
            case -1: return Token.eof;
            case '(': nextc(); return Token.lparen;
            case ')': nextc(); return Token.rparen;
            case '{': nextc(); return Token.lcurly;
            case '}': nextc(); return Token.rcurly;
            case '\n': nextc(); return Token.eol;
            case '\'': {
                StringBuffer sb = new StringBuffer();
                nextc();
                while(!isSpecial()) {
                    sb.append(unescapeAndNext());
                }
                return new Token(Token.SYMBOL, sb.toString());
            }
            case '"': {
                StringBuffer sb = new StringBuffer();
                nextc();
                while(c != '"' && !isEOF()) {
                    sb.append(unescapeAndNext());
                }
                return new Token(Token.STRING, sb.toString());
            }
            case '1': case '2': case '3': case '4': case '5':
            case '6': case '7': case '8': case '9': case '0': {
                StringBuffer sb = new StringBuffer();
                while(!isSpecial()) {
                    sb.append(unescapeAndNext());
                }
                return new Token(Token.NUMBER, sb.toString());
            }
            default: {
                StringBuffer sb = new StringBuffer();
                while(!isSpecial()) {
                    sb.append(unescapeAndNext());
                }
                return new Token(Token.ID, sb.toString());
            }
        }

    }
}


/*

class Parser {
    Ast obj;
    String id;
    Stack args;
    Parser() {
        obj = null;
        id = null;
        args = new Stack();
    }
    void next() {
        obj = new Ast(obj, id, args);
    }
    void stringLiteral(s) {
    }
}

id:
    next();
    id = id;

literal:
    args.push(literal)



next() {
    obj = Fn(obj, id, args);
}

class Parser {
    Tokeniser tokeniser;
    Token t;
    public Parser(Tokeniser tokeniser) {
        this.tokeniser = tokeniser;
        t = Tokeniser.next();
    }
    
    Ast next() {
        Stack stack;
        Ast prev = null;
        Stack args = null;
        Ast node;
        switch(t.type) {
            case Token.SYMBOL:
            case Token.NUMBER:
            case Token.STRING:
                if(args == null && prev == null) {
                    prev = t;
                } else if(args != null && prev != null) {
                    args.push(t);
                } else {
                    throw Error("Unexpected literal: " + t.toString());
                }
            break;
            case Token.LCURLY:
                stack = new Stack();
                t = Tokeniser.next();
                while(t.type != Token.RCURLY && t.type !== Token.EOF) {
                    node = next();
                    if(node != null) {
                        stack.push(node);
                    }
                }
                return 

                
        }
    }

    {
    Stack stack = new Stack;
    Stack codeblock = new Stack;
    boolean inFn = false;
    switch(t.type) {
        case Token.EOF:
        break;
        case Token.LCURLY:
            codeBlock = new Stack();
        break;
        case Token.RCURLY:
            restore state;
            args.push(new CodeBlock(code));
            
        case Token.SYMBOL:
        case Token.NUMBER:
        case Token.STRING:
            args.push(Token);
        break;
        case Token.ID:
            if(id != null) {
                prev = new MethodCall(prev, id, args);
            }
            id = Token;
            args = new Stack();
        break;
        case Token.EOL:
            if(id != null) {
                prev = new MethodCall(prev, id, args);
            }
            id = null;
        break;
        case Token.LPAREN:
        break;
        case Token.RPAREN:
        break;
    }
    }
}
*/

class Compiler {
    String destinationPackage;

    Compiler(String destinationPackage) {
        this.destinationPackage = destinationPackage;
    }


    Object[] parse(String s) {
        Tokeniser tokeniser = new Tokeniser(s);
        Token t;
        while((t = tokeniser.next()) != Token.eof) {
            System.out.println(t.toString());
        }
        return null;
    }

    void compileToJava(Object [] tree) {
    }

    public static final void main(String[] args) throws Exception {
        if(args.length < 1) {
            System.out.println("src1.yl src2.yl ... java.destination.package");
            return;
        }
        Compiler compiler = new Compiler(args[args.length - 1]);

        for(int i = 0; i < args.length-1; ++i) {
            StringBuffer source = new StringBuffer();
            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[i]), "UTF-8"));
            int c;
            while((c = reader.read()) != -1) {
                source.append((char) c);
            }
            compiler.compileToJava(compiler.parse(source.toString()));
        }
    }
}

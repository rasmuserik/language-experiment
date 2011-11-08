package dk.solsort.yolan;
import java.io.*;
import java.util.Stack;

class Ast {
    static Ast createCall(Ast obj, String id, Stack args) {
        if(id == null && obj == null && args.size() == 0) {
            return null;
        }
        if(id == null && obj == null && args.size() == 1) {
            return (Ast) args.get(0);
        }
        if(obj == null) {
            obj = new Special("Context");
        }
        return new Call(obj, id, args);
    }
};

class Special extends Ast {
    public String val;
    public Special(String s) {
        val = s;
    }
    public String toString() { return "[" + String.valueOf(val) + "]"; }
}

class Symb extends Ast {
    public String val;
    public Symb(String s) {
        val = s;
    }
    public String toString() { return "[Symb " + String.valueOf(val) + "]"; }
}

class Str extends Ast {
    public String val;
    public Str(String s) {
        val = s;
    }
    public String toString() { return "[Str " + String.valueOf(val) + "]"; }
}

class Num extends Ast {
    public String val;
    public Num(String s) {
        val = s;
    }
    public String toString() { return "[Num " + String.valueOf(val) + "]"; }
}

class Call extends Ast {
    public Ast obj;
    public String id;
    public Stack args;
    public Call(Ast obj, String id, Stack args) {
        this.obj = obj;
        this.id = id;
        this.args = args;
    }
    public String toString() { return "" + String.valueOf(obj) + "." + String.valueOf(id) + "(" + (args.size() >0 ?String.valueOf(args) : "") + ")"; }
}
class Block extends Ast {
    public Stack stmts;
    public Block(Stack stmts) {
        this.stmts = stmts;
    }

    public String toString() { return "{" + String.valueOf(stmts) + "}"; }
}

class Tokeniser {
    String source;
    int pos;
    int c;
    public boolean done() {
        return c == -1;
    }
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

    public Ast next() {
        Ast obj = null;
        String id = null;
        Stack args = new Stack();
        Stack block;
        for(;;) {
            while(isWs()) { nextc(); }
            switch(c) {
                case '\n': case ')': nextc(); 
                case -1: case '}': 
                    return Ast.createCall(obj, id, args);
                case '(': 
                    nextc(); 
                    args.push(next());
                    break;
                case '{': nextc(); 
                    block = new Stack();
                    while(isWs()) { nextc(); }
                    while(c != -1 && c != '}') {
                        Object o = next();
                        if(o != null) {
                            block.push(o);
                        }
                    }
                    nextc();
                    args.push(new Block(block));
                break;
                case '\'': {
                    StringBuffer sb = new StringBuffer();
                    nextc();
                    while(!isSpecial()) {
                        sb.append(unescapeAndNext());
                    }
                    args.push(new Symb(sb.toString()));
                }
                break;
                case '"': {
                    StringBuffer sb = new StringBuffer();
                    nextc();
                    while(c != '"' && !isEOF()) {
                        sb.append(unescapeAndNext());
                    }
                    args.push(new Str(sb.toString()));
                }
                break;
                case '1': case '2': case '3': case '4': case '5':
                case '6': case '7': case '8': case '9': case '0': {
                    StringBuffer sb = new StringBuffer();
                    do {
                        sb.append(unescapeAndNext());
                    } while(!isSpecial());
                    args.push(new Num(sb.toString()));
                }
                break;
                default: {
                    StringBuffer sb = new StringBuffer();
                    do {
                        sb.append(unescapeAndNext());
                    } while(!isSpecial());
                    obj = Ast.createCall(obj, id, args);
                    id = sb.toString();
                    args = new Stack();
                }
            }
        }
    }
}

class Compiler {
    String destinationPackage;

    Compiler(String destinationPackage) {
        this.destinationPackage = destinationPackage;
    }


    Object[] parse(String s) {
        Tokeniser tokeniser = new Tokeniser(s);
        Ast t;
        while(!tokeniser.done()) {
            t = tokeniser.next();
            System.out.println(String.valueOf(t));
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

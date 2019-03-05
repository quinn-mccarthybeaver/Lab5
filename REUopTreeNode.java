/*

Node type for the expressions using the unary operators: ?, +, *

Here is the grammar for the expression language with

Variables:  S, A, B, C

Terminals:  a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q,
            r, s, t, u, v, w, x, y, z, 0, .

            and operator characters

            +, ?, *, |

            and grouping characters

            (, )

The letters stand for themselves, the 0 stands for the empty set,
and . stands for any letter(for convenience).

The operators are, from high priority to low

+, ?, *   postfix unary operators for repetition, meaning respectively,
          repeat >= 1 time, repeat 0 times or once, repeat >= 0 times

(nothing) concatenation; symbolized by juxtaposition, as in xy is the
          concatenation of x with y

|         selection (or union of languages)

S is the start symbol

The rules are(the terminal | is quoted where it is part of the
language being defined; where it is not quoted it separates replacement
strings for the variable on the left of the ::= )

S ::=  S '|' A | A
A ::=  A B | B
B ::=  B ? | B + | B * | C
C ::= :  a | b | c | d | e | f | g | h | i | j | k | l | m | n | o |
         p | q   r | s | t | u | v | w | x | y | z | 0 | . | ( S )


White space in the input string will be ignored, so

a   ?   b c

parses the same way as

a?bc

 **********************************************************************************/
public class REUopTreeNode extends RETreeNode {

    private char operator;   // must be one of '?' or '+' or '*'


    private RETreeNode sub;   // the subtree


    public REUopTreeNode(char op, RETreeNode chld) throws Exception {

        if ((op != '?' && op != '+' && op != '*') || chld == null) {
            StringBuilder b = new StringBuilder();

            b.append("Invalid call to REUopTreeNode constructor.\n");

            if (op != '?' && op != '+' && op != '*')
                b.append("Operator \'" + op + "\' is not '?' or '+' or '*'.\n");

            if (chld == null)
                b.append("The subtree is null.\n");

            throw new Exception(b.toString());
        } else {
            operator = op;
            sub      = chld;
        }
    }

    public boolean isLeaf() {
        return false;
    }

    public char getOperator() {
        return operator;
    }

    public RETreeNode getSub() {
        return sub;
    }

    public String convertToJavaPattern() {

        StringBuilder bldr = new StringBuilder();

        bldr.append('(');
        bldr.append(sub.convertToJavaPattern());
        bldr.append(')');
        bldr.append(operator);
        return bldr.toString();
    }
    public String toString() {
        StringBuilder b = new StringBuilder();

        if (sub.isLeaf())
            b.append(sub.toString());
        else {
            b.append('(');
            b.append(sub.toString());
            b.append(')');
        }

        b.append(operator);
        return b.toString();
    }


    // Stub definitions
    // YOU MUST CODE THESE
    // See implementation notes for a discussion.
    // +, *, ?

    public boolean isEmpty() {
        if (this.operator == '+') {
            return this.sub.isEmpty();
        } else {   // works for ? and *
            return false;
        }
    }

    public RETreeNode reversedLanguage() {

        try {
            RETreeNode toReturn = new REUopTreeNode(this.operator, this.sub.reversedLanguage());
            return toReturn;
        } catch (Exception e) {
            return null;
        }
    }

    public RETreeNode prefixLanguage() {

        try {
            if (this.getOperator() == '+') {
                RETreeNode toReturn = new REUopTreeNode('+', this.sub.prefixLanguage());
                return toReturn;
            } else if (this.getOperator() == '?') {
                RETreeNode toReturn = new REUopTreeNode('?', this.sub.prefixLanguage());
                return toReturn;
            } else {   // this.getOperator() == '*'
                RETreeNode toReturn = new REUopTreeNode('*', this.sub.prefixLanguage());
                return toReturn;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public RETreeNode suffixLanguage() {

        try {
            if (this.getOperator() == '+') {
                RETreeNode toReturn = new REUopTreeNode('+', this.sub.suffixLanguage());
                return toReturn;
            } else if (this.getOperator() == '?') {
                RETreeNode toReturn = new REUopTreeNode('?', this.sub.suffixLanguage());
                return toReturn;
            } else {   // this.getOperator() == '*'
                RETreeNode toReturn = new REUopTreeNode('*', this.sub.suffixLanguage());
                return toReturn;
            }
        } catch (Exception e) {
            return null;
        }
    }



    public static void main(String[] aa) throws Exception {

        RELeafTreeNode x = new RELeafTreeNode('a'), y = new RELeafTreeNode('c'),
                       z = new RELeafTreeNode('0'), w = new RELeafTreeNode('.');

        REBopTreeNode u = new REBopTreeNode('|', x, y), v = new REBopTreeNode('X', z, w),
                      s = new REBopTreeNode('|', u, v), t = new REBopTreeNode('|', s, x),
                      r = new REBopTreeNode('X', z, t);

        REUopTreeNode a = new REUopTreeNode('?', t), b = new REUopTreeNode('+', s),
                      c = new REUopTreeNode('*', r);

        System.out.println("a is " + a + '\n' + "b is " + b + '\n' + "c is " + c + '\n');

        try {
            a = new REUopTreeNode('x', v);
            System.out.println(a);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        try {
            a = new REUopTreeNode('x', null);
            System.out.println(a);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        try {
            a = new REUopTreeNode('+', null);
            System.out.println(a);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

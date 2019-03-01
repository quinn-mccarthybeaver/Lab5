/*

Node type for the expressions using the binary operators, | and concatenation
(which we will encode as 'X' for "times").


Here is the grammar for the expression language with

Variables:  S, A, B, C

Terminals:  a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q,
            r, s, t, u, v, w, x, y, z, 0, .

            and operator characters

            +, ?, *, |

            and for grouping

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
 ***********************************************************************/
public class REBopTreeNode extends RETreeNode {

    private char operator;   // must be one of '|' or 'X'


    private RETreeNode left, right;   // the two subtrees


    public REBopTreeNode(char op, RETreeNode lft, RETreeNode rht) throws Exception {

        if ((op != 'X' && op != '|') || lft == null || rht == null) {
            StringBuilder b = new StringBuilder();

            b.append("Invalid call to REBopTreeNode constructor.\n");

            if (op != 'X' && op != '|')
                b.append("Operator \'" + op + "\' is not 'X' or '|'.\n");

            if (lft == null)
                if (rht == null)
                    b.append("Both left and right subtrees are null.\n");
                else
                    b.append("The left subtree is null.\n");
            else if (rht == null)
                b.append("The right subtree is null.\n");

            throw new Exception(b.toString());
        } else {
            operator = op;
            left     = lft;
            right    = rht;
        }
    }

    public boolean isLeaf() {
        return false;
    }

    public char getOperator() {
        return operator;
    }

    public RETreeNode getLeft() {
        return left;
    }

    public RETreeNode getRight() {
        return right;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();

        if (left.isLeaf())
            b.append(left.toString());
        else {
            b.append('(');
            b.append(left.toString());
            b.append(')');
        }

        b.append((operator == '|' ? " | " : ""));

        if (right.isLeaf())
            b.append(right.toString());
        else {
            b.append('(');
            b.append(right.toString());
            b.append(')');
        }

        return b.toString();
    }

    public String convertToJavaPattern() {

        StringBuilder bldr = new StringBuilder();

        bldr.append('(');
        bldr.append(left.convertToJavaPattern());
        bldr.append(')');
        if (operator == '|')
            bldr.append('|');
        bldr.append('(');
        bldr.append(right.convertToJavaPattern());
        bldr.append(')');
        return bldr.toString();
    }


    // Stub definitions
    // YOU MUST CODE THESE
    // See implementation notes for a discussion.

    public boolean isEmpty() {
        if (this.operator == '|') {
            return this.left.isEmpty() && this.right.isEmpty();
        } else {   // operator must be *
            return this.left.isEmpty() || this.right.isEmpty();
        }
    }


    public RETreeNode reversedLanguage() {

        try {
            if (this.operator == 'X') {
                RETreeNode temp = new REBopTreeNode('X', this.right.reversedLanguage(), this.left.reversedLanguage());
                return temp;
            } else { // | operator
                RETreeNode temp = new REBopTreeNode('|', this.left.reversedLanguage(), this.right.reversedLanguage());
                return temp;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public RETreeNode prefixLanguage() {

        try {
            return new RELeafTreeNode('0');
        } catch (Exception e) {
            return null;
        }
    }

    public RETreeNode suffixLanguage() {

        try {
            return new RELeafTreeNode('0');
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] a) throws Exception {

        RELeafTreeNode x = new RELeafTreeNode('a'), y = new RELeafTreeNode('c'),
                       z = new RELeafTreeNode('0'), w = new RELeafTreeNode('.');

        REBopTreeNode u = new REBopTreeNode('|', x, y), v = new REBopTreeNode('X', z, w),
                      s = new REBopTreeNode('|', u, v), t = new REBopTreeNode('|', s, x),
                      r = new REBopTreeNode('X', z, t);

        System.out.println("r is " + r + '\n' + "s is " + s + '\n' + "t is " + t + '\n' + "u is " +
                           u + '\n' + "v is " + v + '\n' + "w is " + w + '\n' + "x is " + x + '\n' +
                           "y is " + y + '\n' + "z is " + z + '\n');

        try {
            u = new REBopTreeNode('x', v, w);
            System.out.println(u);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        try {
            u = new REBopTreeNode('x', null, null);
            System.out.println(u);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        try {
            u = new REBopTreeNode('x', null, w);
            System.out.println(u);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        try {
            u = new REBopTreeNode('x', v, null);
            System.out.println(u);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        try {
            u = new REBopTreeNode('|', null, null);
            System.out.println(u);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        try {
            u = new REBopTreeNode('X', null, w);
            System.out.println(u);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            u = new REBopTreeNode('|', v, null);
            System.out.println(u);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

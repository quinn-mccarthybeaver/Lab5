/*

Leaf node type for the regular expression trees.


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

This class accommodates all the values of C except the last, unless the S
in the last ultimately derives a single base letter surrounded by parens.

 **********************************************************************************/
public class RELeafTreeNode extends RETreeNode {

    private char label;   // can be any lower case letter, 0, or .


    public RELeafTreeNode(char c) throws Exception {

        if (!RegularExpressionFactory.baseLetters.contains(c))
            throw new Exception("Invalid character \'" + c + "\' to RELeafTreeNode constructor.");
        else
            label = c;
    }

    public char getLabel() {
        return label;
    }

    public boolean isLeaf() {
        return true;
    }

    public String convertToJavaPattern() {

        switch (label) {

            case '\\':
            case '.':   // represents the symbols in base letters other than 0
                return RegularExpressionFactory.PATTERN_SET_FOR_BASE_LETTERS;

            case '[':
            case ']':
            case '{':
            case '}':
            case '(':
            case ')':
            case '<':
            case '>':
            case '*':
            case '+':
            case '-':
            case '=':
            case '?':
            case '^':
            case '$':
            case '|':
                return "\\" + label;
            case '0':   // represents the empty set for us;
                // [] does not see to work for the empty set
                return "[a-c&&[d-f]]";

            default:
                return "" + label;
        }
    }

    public String toString() {

        return "" + label;
    }


    // Stub definitions
    // YOU MUST CODE THESE
    // See implementation notes for a discussion.

    public boolean isEmpty() {
        return this.label == '0';
    }


    public RETreeNode reversedLanguage() {

        try {
            return new RELeafTreeNode(this.label);
        } catch (Exception e) {
            return null;
        }
    }

    public RETreeNode prefixLanguage() {

        try {
            RETreeNode toReturn = new REUopTreeNode('?', new RELeafTreeNode(this.label));
            return toReturn;
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



    public static void main(String a[]) {

        RELeafTreeNode x;

        for (int i = 32; i < 127; i++) {
            try {
                x = new RELeafTreeNode((char)i);
                System.out.println(
                    "Successfully constructed node with label \'" + x.getLabel() + '\'');
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

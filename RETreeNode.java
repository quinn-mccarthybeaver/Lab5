/*

Abstract super type for the 3 kinds of regular expression
tree nodes: leaf, binary operator, and unary operator.


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
public abstract class RETreeNode {

    // methods for any node

    // not strictly necessary since this purpose can be accomplished by instanceof
    public abstract boolean isLeaf();

    public abstract String convertToJavaPattern();

    /*

       constructs the regular expression that includes exactly the  strings whose
       reversals are in the language represented by this.

       Suppose for a string s, we use rev(s) to stand for the reversal of s, as in
       rev("cat") is "tac",  rev("") is "", etc.

       Suppose further that for RETreeNodes x we use lang(x) to stand for the language
       that x represents.

       Then for all strings s, we want

       s is in lang(this) iff   rev(s) is in lang(this.reversedLanguage())

       YOU WILL NEED TO CODE THIS RECURSIVELY IN ALL THE SUBCLASSES
    */
    public abstract RETreeNode reversedLanguage();


    /*
       constructs the regular expression that represents the set of strings that are
       prefixes of strings in the language represented by this.

       A string s is a prefix of a string t iff there is some string u such that

       t is su, or in Java,  t.equals(s + u).

       For example, "cat" is a prefix of "catsup".  Note, for any string s, "" is a prefix
       of s and s is a prefix of itself, because s.equals("" + s) and s.equals(s + "").

       If we use pref(s) to stand for the set of strings that are prefixes of the string s,
       then for all strings t, we want

       t is in lang(this.prefixLanguage()) iff there exists s in lang(this) with t in pref(s)

    */
    public abstract RETreeNode prefixLanguage();


    /*
       constructs the regular expression that represents the set of strings that are
       suffixes of strings in the language represented by this.

       A string s is a suffix of a string t iff there is some string u such that

       t is us, or in Java,  t.equals(u + s).

       For example, "sup" is a suffix of "catsup".  Note, for any string s, "" is a suffix
       of s and s is a suffix of itself, because s.equals("" + s) and s.equals(s + "").

       If we use suff(s) to stand for the set of strings that are suffixes of the string s,
       then for all strings t, we want

       t is in lang(this.suffixLanguage()) iff there exists s in lang(this) with t in suff(s)

    */
    public abstract RETreeNode suffixLanguage();


    /*

       returns true exactly when the language represented by this contains no strings.

    */
    public abstract boolean isEmpty();
}

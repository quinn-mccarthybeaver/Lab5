/*

   A type to hold the regular expression.

   We could have just used, RETreeNode, I suppose, but we wanted to distinguish
   the whole expression from the nodes in the tree.

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

 *********************************************************************************/

public class RegularExpression{
   
   
   private RETreeNode
   root;
   
   public RegularExpression(RETreeNode nd)throws Exception{
      if (nd == null)
         throw new Exception("null passed as root to RegularExpression constructor.");
      else
         root = nd;
   }
   
   public String convertToJavaPattern() {
      return root.convertToJavaPattern();
   }
   public String toString(){
      return root.toString();
   }
   
   public RETreeNode getRoot(){
      return root;
   }


   // these definitions will work if you recursive definitions are correct
   public RegularExpression reversedLanguage() {

      try{

         RETreeNode temp = null;
         temp = root.reversedLanguage();

         return new RegularExpression(temp);
      }
      catch(Exception e){
         return null;
      }
   }

   public RegularExpression prefixLanguage(){

      try{

         RETreeNode temp = null;
         temp = root.prefixLanguage();

         return new RegularExpression(temp);
      }
      catch(Exception e){
         return null;
      }

   }

   public RegularExpression suffixLanguage(){

      try{

         RETreeNode temp = null;
         temp = root.suffixLanguage();

         return new RegularExpression(temp);
      }
      catch(Exception e){
         return null;
      }

   }

   public boolean isEmpty(){

      return root.isEmpty();
   }


}

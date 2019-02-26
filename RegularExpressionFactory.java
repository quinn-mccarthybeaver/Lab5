/*

A class for creating a RegularExpression object from a string input with the method

static RegularExpression makeRegularExpression(String s) throws Exception

It uses a recursive descent parser to create the expression tree for the string,
assuming it is well formed.

Here is the grammar for the expression language with

Variables:  S, A, B, C

Terminals:  a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q,
            r, s, t, u, v, w, x, y, z, 0, .

            and

            +, ?, *, |

            and

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
strings for the variable on the left of the ::=

S ::=  S '|' A | A
A ::=  A B | B
B ::=  B ? | B + | B * | C
C ::= :  a | b | c | d | e | f | g | h | i | j | k | l | m | n | o |
         p | q   r | s | t | u | v | w | x | y | z | 0 | . | ( S )


White space in the input string will be ignored, so

a   ?   b c 

parses the same way as

a?bc


 If the input string is not in the language defined by the grammar,
 it throws an exception.

 As it uses static variables, it would not work for multiple active threads.

 ************************************************************************************/
import java.util.*;

public class RegularExpressionFactory{
   
   private static final char
   // should be a character that is NOT otherwise in the collection
   END_OF_STRING = '$';
   
   // for the input string to be parsed
   private static char[] inputStr;
   
   // a position within inputStr
   private static int currPos = 0;
   
   // some set variables for lookahead sets
   public static TreeSet<Character>
   // this set should not contain any of + ? * ( ) | or ws
   // since ws is skipped over and the others have special meaning 
   baseLetters;
   
   private static TreeSet<Character>
   expStarters,      // (, a-z, 0, .
   postfixOps,  // +, ?, *
   expEnders;  // ), end of string
   
   static{
      // initialize the set variables
      
      baseLetters = new TreeSet<Character>();
      for (int i = 'a'; i <= (int) 'z'; i++)
         baseLetters.add((char)i);
      baseLetters.add('.');
      baseLetters.add('0');
      
      expStarters = new TreeSet<Character>();
      expStarters.add('(');
      expStarters.addAll(baseLetters);
      
      postfixOps = new TreeSet<Character>();
      postfixOps.add('+');
      postfixOps.add('?');
      postfixOps.add('*');
      
      
      expEnders =  new TreeSet<Character>();
      expEnders.add(')');
      expEnders.add(END_OF_STRING);
   }
   
   public static String
   PATTERN_SET_FOR_BASE_LETTERS;
   
   static {
      // construct a Java Pattern string base letters, that is,
      // the actual letters that do not have a special meaning
      
      StringBuilder bldr = new StringBuilder();
      bldr.append('[');
      // -, ^, ], and \ have special meaning in character sets,
      // so copy baseLetters but make special provision for them.
      // this scheme may not work
      
      TreeSet<Character> baseLettersCopy = new TreeSet<Character>();
      
      for (Character c : baseLetters)
         if (c != '0' && c != '.')  // 0 means the empty set for us;
                                    // . refers to all the characters in base letters,
                                    // except for 0 and .
            baseLettersCopy.add(c);
      
      if (baseLettersCopy.contains('-')) {
         bldr.append("\\-");
         baseLettersCopy.remove('-');
      }
      if (baseLettersCopy.contains('\\')) {
         bldr.append("\\\\");
         baseLettersCopy.remove('\\');
      }
      if (baseLettersCopy.contains('^')) {
         bldr.append("\\^");
         baseLettersCopy.remove('^');
      }
      if (baseLettersCopy.contains(']')) {
         bldr.append("\\]");
         baseLettersCopy.remove(']');
      }
      
      for (Character c : baseLettersCopy)
         bldr.append(c);
      
      bldr.append(']');
      
      PATTERN_SET_FOR_BASE_LETTERS = bldr.toString();
      
   }
   
   // return the next token w/o consuming it
   private static char lookahead(){
      // assumes ws has been skipped already
      
      if (currPos >= inputStr.length)
         return END_OF_STRING;
      else
         return inputStr[currPos];
   }
   
   // advance position until either it's off the end of
   // inputStr or references a non-ws character
   private static void skipWS(){
      
      if (inputStr != null)
         while (currPos < inputStr.length && Character.isWhitespace(inputStr[currPos]))
            currPos++;
   }
   
   // assumes if currPos is still in inputStr, it's on a non-ws character
   private static void consume(){
      currPos++;
      skipWS();
   }
   
   
   // method for the S grammar variable
   private static RETreeNode S() throws Exception{
      
      char lk = lookahead();
      
      if (!expStarters.contains(lk))
         throw new Exception("invalid char \'" + lk + 
               "\' cannot start an expression");
      else{
         RETreeNode 
         temp = null,
         subT = A();
         
         lk = lookahead();
         
         while (lk == '|'){
            consume();
            lk = lookahead();
            if (!expStarters.contains(lk))
               throw new Exception("invalid char \'" + lk + 
                     "\' not in first of A");
            temp = A();
            subT = new REBopTreeNode('|', subT, temp);
            lk = lookahead();
         }
         
         if (!expEnders.contains(lk))
            throw new Exception("The next character, '" + lk + "', cannot end an expression.");
         else
            return subT;
      }
   }     
   
   // for the A grammar variable
   // assume the lookahead has been tested before the call
   // and is okay
   private static RETreeNode A() throws Exception{
      
      RETreeNode
      res = B(),
      temp = null;
      
      char lk = lookahead();
      
      while (expStarters.contains(lk)){
         temp = B();
         res = new REBopTreeNode('X', res, temp);
         lk = lookahead();
      }
      
      if (lk != '|' && !expEnders.contains(lk))
         throw new Exception("Expecting | or end of expression token.");
      else
         return res;
   }
   
   
   // assumes the lookahead is okay for B
   private static RETreeNode B() throws Exception{
      
      RETreeNode res = C();
      
      char lk = lookahead();
      
      while (postfixOps.contains(lk)){
         res = new REUopTreeNode(lk, res);
         consume();
         lk = lookahead();
      }
      
      if (lk != '|' && !expEnders.contains(lk) && !expStarters.contains(lk))
         throw new Exception("Invalid character in input.");
      else
         return res;
   }
   
   private static RETreeNode C()throws Exception{
      
      RETreeNode res = null;
      
      char lk = lookahead();
      
      if (baseLetters.contains(lk)){
         consume();
         return new RELeafTreeNode(lk);
      }
      else if (lk =='('){// should be (S)
         consume();
         lk = lookahead();
         if (!expStarters.contains(lk))
            throw new Exception("Expecting an expression beginning token.");
         else{
            res = S();
            
            lk = lookahead();
            
            if (lk != ')')
               throw new Exception("Right paren expected.");
            else{
               consume();
               return res;
            }
         }
      }
      return res;
   }
   
   // attempts the construction for the input string s
   public static RegularExpression makeRegularExpression(String s) throws Exception{
      
      if (s == null)
         throw new Exception("null string passed to makeRegularExpression.");
      else if (s.length() == 0)
         throw new Exception("empty string passed to makeRegularExpression.");
      
      // test that it contains only legit characters
      else {
         
         // construct a string for the acceptable characters,
         // which are the base letters and ? * + | ( )
         // and ws
         StringBuilder bldr = new StringBuilder();
         bldr.append('[');
         // -, ^, ], and \ have special meaning in character sets,
         // so copy baseLetters but make special provision for them.
         // this scheme may not work
         
         TreeSet<Character> baseLettersCopy = new TreeSet<Character>();
         
         for (Character c : baseLetters)
            baseLettersCopy.add(c);
         
         if (baseLettersCopy.contains('-')) {
            bldr.append("\\-");
            baseLettersCopy.remove('-');
         }
         if (baseLettersCopy.contains('\\')) {
            bldr.append("\\\\");
            baseLettersCopy.remove('\\');
         }
         if (baseLettersCopy.contains('^')) {
            bldr.append("\\^");
            baseLettersCopy.remove('^');
         }
         if (baseLettersCopy.contains(']')) {
            bldr.append("\\]");
            baseLettersCopy.remove(']');
         }
         
         // add the ws class
         bldr.append("\\s");
         for (Character c : baseLettersCopy)
            bldr.append(c);
         
         // add the other operators
         bldr.append("|?*+()]+");
         String t = bldr.toString();
         if (!s.matches(t))
            throw new Exception("invalid character in string passed to makeRegularExpression.");
         else{  // it passes basic testing; try to parse it
            
            currPos = 0;
            inputStr = s.toCharArray();
            skipWS();
            
            RETreeNode root = S();
            
            return new RegularExpression(root);
         }
      }
   }
   
   
   public static void main(String[] a) throws Exception{
      
      RegularExpression
      re1 = makeRegularExpression("a|bc(.?)+"),
      re2 = makeRegularExpression("a"),
      re3 = makeRegularExpression("0"),
      re4 = makeRegularExpression("."),
      re5 = makeRegularExpression("cba"),
      re6 = makeRegularExpression("a?"),
      re7 = makeRegularExpression("a+"),
      re8 = makeRegularExpression("a*"),
      re9 = makeRegularExpression("a|z"),
      re10 = makeRegularExpression("ab|c*"),
      re11 = makeRegularExpression("abc|de?(fg*)"),
      re12 = makeRegularExpression("d|a*"),
      re13 = makeRegularExpression("a l f a l f a | c l o v e r | g r a s s *"),
      re14 = makeRegularExpression("a|bc(.?)+"),
      re15 = makeRegularExpression("a"),
      re16 = makeRegularExpression("0"),
      re17 = makeRegularExpression("."),
      re18 = makeRegularExpression("cba"),
      re19 = makeRegularExpression("ad?"),
      re20 = makeRegularExpression("aj+"),
      re21 = makeRegularExpression("ak*"),
      re22 = makeRegularExpression("a|yz"),
      re23 = makeRegularExpression("a|bc*"),
      re24 = makeRegularExpression("de|fg+"),
      re25 = makeRegularExpression("h i * | b | a * 0 "),
      re26 = makeRegularExpression("hi*|b|a*0"),
      re27 = makeRegularExpression("alfalfa|clover|grass*");
      
      System.out.println("" + re1);
      System.out.println("" + re2);
      System.out.println("" + re3);
      System.out.println("" + re4);
      System.out.println("" + re5);
      System.out.println("" + re6);
      System.out.println("" + re7);
      System.out.println("" + re8);
      System.out.println("" + re9);
      System.out.println("" + re10);
      System.out.println("" + re11);
      System.out.println("" + re12);
      System.out.println("" + re13);
      System.out.println("" + re27);
      System.out.println("" + re14);
      System.out.println("" + re15);
      System.out.println("" + re16);
      System.out.println("" + re17);
      System.out.println("" + re18);
      System.out.println("" + re19);
      System.out.println("" + re20);
      System.out.println("" + re21);
      System.out.println("" + re22);
      System.out.println("" + re23);
      System.out.println("" + re24);
      System.out.println("" + re25);
      System.out.println("" + re26);
   }
}



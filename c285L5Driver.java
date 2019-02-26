/*

Some Test cases for the NFA class

These have fairly simple expressions (< 4 operators), but a wide variety of
arrangements.

 */

public class c285L5Driver{
   
   private static String[] 
         testExpressions = {
               
               // 0
               "a",
               "b",
               "c",
               "d",
               "e",
               "f",
               "g",
               "h",
               "i",
               "j",
               
               // 10
               "k",
               "l",
               "m",
               "n",
               "o",
               "p",
               "q",
               "r",
               "s",
               "t",
               
               // 20
               "u",
               "v",
               "w",
               "x",
               "y",
               "z",
               "0",
               ".",
               "a|b",
               "0|.",
               
               // 30
               ".|0",
               "0|0",
               "z|0",
               "ab",
               "f?",
               "g+",
               "h*",
               "0?",
               "0+",
               "0*",
               
               // 40
               "i|j|k",
               "l|(m|n)",
               "o(pq)",
               "q|rs)",
               "tu|v",
               "w(x|y)",
               "(z|a)b",
               "c|d?",
               "e|f+",
               ".+0|ab+c",

               // 50
               "g|h*",
               "i?|j",
               "k+|l",
               "m*|n",
               "(o|p)?",
               "(q|r)+",
               "(s|t)*",
               "uv?",
               "wx+",
               "yz*",
               
               // 60
               "a?b",
               "c+d",
               "e?f",
               "(gh)?",
               "(ij)+",
               "(kl)*",
               "mnop",
               "(q(rs))t",
               "(uv)(wx)",
               "(y(zab))",
               
               // 70
               "c(d(ef))",
               "g|h|i|j",
               "(k|(l|m))|n",
               "(o|p)|(q|r)",
               "s|(t|u|v)",
               "w|(x|(y|z))",
               "mno|p",
               "(q(rs))|t",
               "(uv)|(wx)",
               "y|(zab)",
               
               // 80
               "c|(d(ef))",
               "mn|op",
               "(q|(rs))t",
               "(uv)(w|x)",
               "(y(za|b))",
               "c(d|(ef))",
               "m|nop", 
               "(q(r|s))t",
               "(u|v)(wx)",
               "y(z|ab)",
               
               // 90
               "c(d(e|f))",
               "(g|h|i)j",
               "(k|(l|m))n",
               "(o|p)(q|r)",
               "s(t|u|v)",
               "w((x|(y|z)))",
               "(g|h)i|j",
               "(k(l|m))|n",
               "(o|p)|(qr)",
               "s|(t|u)v",
               
               // 100
               "w|x(y|z)",
               "gh|i|j",
               "(k|(lm))|n",
               "(op)|(q|r)",
               "s|(tu|v)",
               "w|(x|(yz))",
               "(0a*)?",
               "(0b+)?"
   };

   
   
   public static void main(String[] a){
      
      
      int i,j;
      boolean
      machineResult = false;
      String 
      reStr = null,
      patternStr,
      testStr;
      RegularExpression regExp = null;
      int count = 0;
      for (i = 0; i < testExpressions.length; i++){
         try{
            reStr = testExpressions[i];
            regExp = RegularExpressionFactory.makeRegularExpression(reStr);
            patternStr = regExp.convertToJavaPattern();
            System.out.println("\nTests for reg exp #" + i + ", " + regExp.toString() + '\n');
            System.out.println("Is it empty? " + (regExp.isEmpty()? "Yes" : "No"));
            System.out.println("Reversal is " + regExp.reversedLanguage().toString());
            System.out.println("Prefixes are " + regExp.prefixLanguage().toString());
            System.out.println("Suffixes are " + regExp.suffixLanguage().toString());
         }
         catch(Exception e){
            System.out.println("Attempt to test reg exp #" + i + " failed.\n" +
                  e.getMessage());
         }
      }
      
   }
}

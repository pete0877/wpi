/* Generated By:JJTree&JavaCC: Do not edit this line. teaTokenManager.java */
import java.io.*;

public class teaTokenManager implements teaConstants
{
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0xfe0L) != 0L)
         {
            jjmatchedKind = 33;
            return 1;
         }
         return -1;
      case 1:
         if ((active0 & 0x280L) != 0L)
            return 1;
         if ((active0 & 0xd60L) != 0L)
         {
            jjmatchedKind = 33;
            jjmatchedPos = 1;
            return 1;
         }
         return -1;
      case 2:
         if ((active0 & 0x840L) != 0L)
            return 1;
         if ((active0 & 0x520L) != 0L)
         {
            jjmatchedKind = 33;
            jjmatchedPos = 2;
            return 1;
         }
         return -1;
      case 3:
         if ((active0 & 0x400L) != 0L)
         {
            jjmatchedKind = 33;
            jjmatchedPos = 3;
            return 1;
         }
         if ((active0 & 0x120L) != 0L)
            return 1;
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 33:
         jjmatchedKind = 31;
         return jjMoveStringLiteralDfa1_0(0x200000L);
      case 37:
         return jjStopAtPos(0, 30);
      case 38:
         return jjMoveStringLiteralDfa1_0(0x80000L);
      case 40:
         return jjStopAtPos(0, 14);
      case 41:
         return jjStopAtPos(0, 15);
      case 42:
         return jjStopAtPos(0, 28);
      case 43:
         return jjStopAtPos(0, 26);
      case 44:
         return jjStopAtPos(0, 32);
      case 45:
         return jjStopAtPos(0, 27);
      case 47:
         return jjStopAtPos(0, 29);
      case 59:
         return jjStopAtPos(0, 16);
      case 60:
         jjmatchedKind = 22;
         return jjMoveStringLiteralDfa1_0(0x800000L);
      case 61:
         jjmatchedKind = 17;
         return jjMoveStringLiteralDfa1_0(0x100000L);
      case 62:
         jjmatchedKind = 24;
         return jjMoveStringLiteralDfa1_0(0x2000000L);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x200L);
      case 101:
         return jjMoveStringLiteralDfa1_0(0x100L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0xc0L);
      case 118:
         return jjMoveStringLiteralDfa1_0(0x20L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 123:
         return jjStopAtPos(0, 12);
      case 124:
         return jjMoveStringLiteralDfa1_0(0x40000L);
      case 125:
         return jjStopAtPos(0, 13);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private final int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 38:
         if ((active0 & 0x80000L) != 0L)
            return jjStopAtPos(1, 19);
         break;
      case 61:
         if ((active0 & 0x100000L) != 0L)
            return jjStopAtPos(1, 20);
         else if ((active0 & 0x200000L) != 0L)
            return jjStopAtPos(1, 21);
         else if ((active0 & 0x800000L) != 0L)
            return jjStopAtPos(1, 23);
         else if ((active0 & 0x2000000L) != 0L)
            return jjStopAtPos(1, 25);
         break;
      case 102:
         if ((active0 & 0x80L) != 0L)
            return jjStartNfaWithStates_0(1, 7, 1);
         break;
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x40L);
      case 111:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(1, 9, 1);
         return jjMoveStringLiteralDfa2_0(active0, 0x820L);
      case 124:
         if ((active0 & 0x40000L) != 0L)
            return jjStopAtPos(1, 18);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x420L);
      case 114:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(2, 11, 1);
         break;
      case 115:
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      case 116:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(2, 6, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 100:
         if ((active0 & 0x20L) != 0L)
            return jjStartNfaWithStates_0(3, 5, 1);
         break;
      case 101:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(3, 8, 1);
         break;
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x400L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(4, 10, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 3;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 2:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 34)
                     kind = 34;
                  jjCheckNAdd(2);
                  break;
               case 1:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 33)
                     kind = 33;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 33)
                     kind = 33;
                  jjCheckNAdd(1);
                  break;
               case 1:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 33)
                     kind = 33;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
};
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, "\166\157\151\144", "\151\156\164", "\151\146", 
"\145\154\163\145", "\144\157", "\167\150\151\154\145", "\146\157\162", "\173", "\175", "\50", 
"\51", "\73", "\75", "\174\174", "\46\46", "\75\75", "\41\75", "\74", "\74\75", 
"\76", "\76\75", "\53", "\55", "\52", "\57", "\45", "\41", "\54", null, null, null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
static final long[] jjtoToken = {
   0xfffffffe1L, 
};
static final long[] jjtoSkip = {
   0x1eL, 
};
private ASCII_CharStream input_stream;
private final int[] jjrounds = new int[3];
private final int[] jjstateSet = new int[6];
protected char curChar;
public teaTokenManager(ASCII_CharStream stream)
{
   if (ASCII_CharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public teaTokenManager(ASCII_CharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(ASCII_CharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 3; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(ASCII_CharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

private final Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public final Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { 
      while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedPos == 0 && jjmatchedKind > 35)
   {
      jjmatchedKind = 35;
   }
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

}

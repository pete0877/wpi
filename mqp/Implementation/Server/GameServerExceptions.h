// GameServerExceptions.h
//

#if !defined(GameServerExceptions_h)
#define GameServerExceptions_h

/*
 * Following are the exceptions that the GameServer handles and that the Engine
 * throws:
 */



// GameNotSupportedException - thrown in case requested/referenced game type is not 
//	supported.
class GameNotSupportedException : public CException {};



// GameOverException - thrown in case client sends a board that is already in game-over
//  state.
class GameOverException : public CException {};



// HelperDeadException - thrown in case a helper died or does not respond
class HelperDeadException : public CException {};



// HelperDeadException - thrown in case a tree node could not be evaluated
class NodeEvaluationFailedException : public CException {};



#endif
/* GameServerExceptions_h */

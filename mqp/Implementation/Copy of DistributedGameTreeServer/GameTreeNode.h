// GameTreeNode.h
//

#if !defined(GameTreeNode_h)
#define GameTreeNode_h

/*
 * GameTreeNode Class is used to create game trees. It encapsulates
 * any information that might be required by the system to make the
 * decision as to what the next move should be.
 */

#include "resource.h"
#include "Types.h"
#include <afxmt.h>
#include "MoveDecision.h"

class GameTreeNode
{
public:

    GameTreeNode();    
    void addChildNode(GameTreeNode* childnode);
    vector<GameTreeNode*> getChildNodes();
    int getPly();
    void setPly(int new_ply);
    GameState getGameState();
    void setGameState(const GameState& new_gamestate);
    LevelType getLevel();
    void setLevel(LevelType new_level);
    Move getLastMove();
    void setLastMove(const Move& new_lastmove);
    int getQuality();
    void setQuality(int new_quality);
    GameTreeNode* getParentNode();
    void setParentNode(GameTreeNode* new_parentnode);
    boolean isProcessingDone();
    void markProcessingDone();
    void markChildProcessingDone(int quality);
    boolean getEvaluationFailed();
    void setEvaluationFailed(boolean new_evaluationfailed);
    float getPctCompleted();
    void setPctCompleted(float new_pctcompleted);
	MoveDecision generateDecision();
	void setProcessingSemaphore(CSemaphore* new_processingSemaphore);
	~GameTreeNode();
    
protected:

    int						ply;    
    LevelType				level;
    Move					lastMove;
    boolean					processingDone;
    boolean					consideredChildren;
    boolean					evaluationFailed;
    float					pctCompleted;
    GameTreeNode*			parentNode;
    vector<GameTreeNode*>	childrenNodes;
    int						doneChildProcessings;
    CSemaphore				doneChildProcessingsLock;
	CSemaphore*				processingSemaphore;

private:


};

#endif /* GameTreeNode_h */

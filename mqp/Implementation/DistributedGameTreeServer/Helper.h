// Helper.h
//

#if !defined(Helper_h)
#define Helper_h

/*
 * Helper Class provides logical connection between the current
 * process on the game server and the physical helper process on a
 * different machine. Helper Class uses DCom in all network
 * communication. It provides game state  evaluation services, game
 * state split services and other. It throws HelperDeadException
 * exception.
 */

#include <afxmt.h>
#include "resource.h"
#include "HelperPerformance.h"
#include "InternetAddress.h"
#include "JobManager.h"
#include "Types.h"
#include "GameType.h"
#include "GameTreeNode.h"

class Helper
{
public:
	void test();

    /*
     * Helper() - default constructor.
     */
    Helper();
    Helper(const HelperPerformance& new_performance, const InternetAddress& new_address, const GameType& new_gametype);
    /*
     * THROWS HelperDeadException exception
     */
    HelperPerformance getPerformance();
    /*
     * throws HelperDeadException exception
     */
    void setPerformance(const HelperPerformance& new_performance);
    /*
     * throws HelperDeadException exception
     */
    InternetAddress getAddress();
    /*
     * throws HelperDeadException exception
     */
    void setAddress(const InternetAddress& new_address);
    /*
     * throws HelperDeadException exception
     */
    GameType getGameType();
    /*
     * throws HelperDeadException exception
     */
    void setGameType(const GameType& new_gametype);
    boolean getAvailable();
    void setAvailable(boolean new_available);
    long getHelperID();
    void setHelperID(long new_helperid);
    /*
     * throws HelperDeadException exception
     */
    long estimateEvaluationTurnaround(GameTreeNode* node);
    long estimateEvaluationTurnaround(int ply);
    /*
     * throws HelperDeadException exception
     */
    void evaluateBoard(GameTreeNode* node, long timelimit);
    /*
     * throws HelperDeadException exception
     */
    void splitBoard(GameTreeNode* node);
    /*
     * THROWS:
     *    HelperDeadException exception
     *    GameOverException exception
     */
    Move* makeRapidDesicion(const GameState& gamestate);
    void lockJobQueue();
    void unlockJobQueue();

	void setLogContext(LogContext* new_logContext);

    ~Helper();

protected:
	LogContext*			logContext;
	void				log(CString event);
    HelperPerformance	performance;
    InternetAddress		address;
    JobManager			jobManager;
    CSemaphore			jobManagerLock;
    GameType			gametype;
    boolean				available;
    long				helperID;


};

#endif /* Helper_h */

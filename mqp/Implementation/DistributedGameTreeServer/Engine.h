// Engine.h
//

#if !defined(Engine_h)
#define Engine_h

/*
 * Engine Class is responsible for providing game decision services
 * for games of various types. It also provides estimation on how
 * long a move desicion might take.
 */

#include "resource.h"
#include "HelperRegistry.h"
#include "EngineConfiguration.h"
#include "MoveDecision.h"
#include "GameType.h"
#include "SimpleDateTime.h"
#include "GameTreeNode.h"
#include "Helper.h"
#include "DistributionAlgorithm.h"

class Engine
{
public:

    /*
     * Engine () - default constructor.
     */
    Engine();
    /*
     * Engine () - copy constructor.
     */
    Engine(const Engine& new_engine);
    /*
     * Engine() - constructor with the default configuration for the
     * engine.
     */
    Engine(const EngineConfiguration& new_configuration);
    /*
     * getConfiguration() - returns current engine configuration.
     */
    EngineConfiguration getConfiguration();
    /*
     * setConfiguration() - overwrites the current engine configuration
     * with the one provided as the argument.
     */
    void setConfiguration(const EngineConfiguration& new_configuration);
    /*
     * isGameImplemented() - returns true if the engine supports given
     * game type, else returns false.
     */
    boolean isGameImplemented(const GameType& gametype);
    /*
     * getAllGameTypes() - returns all game types supported by the
     * Engine.
     */
    vector<GameType> getAllGameTypes();
    /*
     * estimateMoveTime() - returns float value of seconds that the
     * engine would need to come up with the decision move if all
     * resources were free.
     * THROWS:
     *    GameNotSupportedException
     */
    long estimateMoveTime(const GameType& gametype, int ply);
    /*
     * makeMove() - returns decision move to a given board setup within
     * certain amount of time.
     * THROWS:
     *    GameNotSupportedException
     *    GameOverException
     */
    MoveDecision makeMove(const GameState& gamestate, const GameType& gametype, int ply, SimpleDateTime* deadline);

	HelperRegistry* getHelperRegistry();

	void setDistributionAlgorithm(DistributionAlgorithm *new_algorithm);

	DistributionAlgorithm* getDistributionAlgorithm();

	void setLogContext(LogContext* new_logContext);

    ~Engine();

protected:

    EngineConfiguration			configuration;
	HelperRegistry*				helperRegistry;
	DistributionAlgorithm*		algorithm;
	LogContext*					logContext;
	void						log(CString event);

private:


};

#endif /* Engine_h */

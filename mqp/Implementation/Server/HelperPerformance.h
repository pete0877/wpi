// HelperPerformance.h
//

#if !defined(HelperPerformance_h)
#define HelperPerformance_h

/*
 * HelperPerformance Class is used by each helper to store and
 * evaluate certain parameters, such as the average time that a
 * particular helper needs to evlauate a board, or overhead time in
 * communication.
 */

#include "resource.h"
#include "Types.h"
#include <afxmt.h>


class HelperPerformance
{
public:

    HelperPerformance();
    HelperPerformance(const HelperPerformance& new_helperperformance);
    HelperPerformance(const HelperPerformanceData& new_helperperformancedata);
    /*
     * HelperPerformance() - constructor. Creates the new instance of
     * the class based on averages drawn from the provided list of
     * exising performances.
     */
    HelperPerformance(vector<HelperPerformance> new_helperperformance_list);
    void update(const HelperPerformanceData& new_helperperformancedata, long new_turnaroundtime);
    float getBranchingFactor();
    void setBranchingFactor(float new_branchingfactor);
    float getOverheadTime();
    void setOverheadTime(float new_overheadtime);
    float getEvaluationTime();
    void setEvaluationTime(float new_evaluationtime);
    float getSplitTime();
    void setSplitTime(float new_splittime);
    float getPctCompleted();
    void setPctCompleted(float new_pctcompleted);
    float getAlphaBetaGain();
    void setAlphaBetaGain(float new_alphabetagain);
    float getTotalWorkTime();
    void setTotalWorkTime(float new_worktime);
    long estimateEvaluationTime(int ply);
    const HelperPerformance& operator=(const HelperPerformance& new_helperperformance);
    boolean operator==(const HelperPerformance& new_helperperformance);
    
protected:

    void					lock();
    void					unlock();
    HelperPerformanceData	helperPerformanceData;
    long					overHeadTime;
    CCriticalSection		dataLock;

};

#endif /* HelperPerformance_h */

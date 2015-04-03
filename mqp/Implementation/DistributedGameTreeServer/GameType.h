// GameType.h
//

#if !defined(GameType_h)
#define GameType_h

/*
 * GameType Class contains any descriptive informaiton directly
 * related to a particular game type (e.g. game name, author, game
 * version, description, copyright, etc.)
 */

#include "resource.h"
#include "Types.h"
#include "afx.h"
// DEL #include <afx.h>

class GameType
{
public:
   /*
    * (following is a standard constructor)
    */
    GameType();

   /*
    * (following is a standard copy constructor)
    */
    GameType(const GameType& gametype);

   /*
    * (following is a customized copy constructor)
    */
    GameType(const GameTypeData& new_gametypedata);

   /*
    * (following is a standard member access function)
    */
    CString getGameName();

   /*
    * (following is a standard member access function)
    */
    void setGameName(CString new_gamename);

   /*
    * (following is a standard member access function)
    */
    CString getCompanyName();

   /*
    * (following is a standard member access function)
    */
    void setCompanyName(CString new_companyname);

   /*
    * (following is a standard member access function)
    */
    CString getGameVersion();

   /*
    * (following is a standard member access function)
    */
    void setGameVersion(CString new_gameversion);

   /*
    * (following is a standard member access function)
    */
    CString getLegalCopyright();

   /*
    * (following is a standard member access function)
    */
    void setLegalCopyright(CString new_legalcopyright);

   /*
    * (following is a standard member access function)
    */
    CString getLegalTrademarks();

   /*
    * (following is a standard member access function)
    */
    void setLegalTrademarks(CString new_legaltrademarks);

   /*
    * (following is a standard member access function)
    */
    CString getGameDescription();

   /*
    * (following is a standard member access function)
    */
    void setGameDescription(CString new_gamedescription);

   /*
    * (following is a standard member access function)
    */
    CString getGameEncoding();

   /*
    * (following is a standard member access function)
    */
    void setGameEncoding(CString new_gameencoding);

   /*
    * toString() - converts entire game type into a representative
    *    string of characters which can later be displayed.
    */
    CString toString();

   /*
    * (following is a standard assigment operator)
    */
    const GameType& operator=(const GameType& gametype);

    /*
     * (following is a standard comparison operator)
     */
    boolean operator==(const GameType& gametype);

    /*
     * (following is a standard desctructor)
     */
    ~GameType();

   /*
    * (following is a standard member access function)
    */
    int getGameStateSize();

   /*
    * (following is a standard member access function)
    */
    void setGameStateSize(int new_gamestatesize);

	/*
    * (following is a standard member access function)
    */
    int getMoveSize();

   /*
    * (following is a standard member access function)
    */
    void setMoveSize(int new_movesize);

	/*
    * (following is a standard member access function)
    */
    int getMoveListSize();

   /*
    * (following is a standard member access function)
    */
    void setMoveListSize(int new_movelistsize);

protected:

    GameTypeData	gameTypeData;

private:


};

#endif /* GameType_h */

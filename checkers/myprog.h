#ifndef COMPUTER_H
#define COMPUTER_H

#define Empty 0x00
#define Piece 0x20
#define King 0x60
#define Red 0x00
#define White 0x80

#define number(x) ((x)&0x1f)
//number of the square
#define empty(x) ((((x)>>5)&0x03)==0?1:0)
//100000
//if its an empty space then its 6th bit is 0
#define piece(x) ((((x)>>5)&0x03)==1?1:0)
//if its a piece then its 6th bit is a 1
#define king(x) ((((x)>>5)&0x03)==3?1:0)
//if its a king then its 6th and 7th bits are 1's
#define color(x) ((((x)>>7)&1)+1) //has to be 0 or 1 before the 1 is added to it
//the color is defined by the highest order bit + 1 and its equality should be equal to the player => color(square) == player number
//bits=> 01100101 = player 1s king on square 5
//00100000 player 1s piece on square 0
//10111111 player 2s piece on square 32
#define Clear 0x1f

struct State {
    int player;
    char board[8][8];
    char movelist[48][12]; /* The following comments were added by Tim Andersen
                  Here Scott has set the maximum number of possible legal moves to be 48.
                                  Be forewarned that this number might not be correct, even though I'm
                                  pretty sure it is.  
                  The second array subscript is (supposed to be) the maximum number of 
                  squares that a piece could visit in a single move.  This number is arrived
                  at be recognizing that an opponent will have at most 12 pieces on the 
                  board, and so you can at most jump twelve times.  However, the number
                  really ought to be 13 since we also have to record the starting position
                  as part of the move sequence.  I didn't code this, and I highly doubt 
                  that the natural course of a game would lead to a person jumping all twelve
                  of an opponents checkers in a single move, so I'm not going to change this. 
                  I'll leave it to the adventuresome to try and devise a way to legally generate
                  a board position that would allow such an event.  
                  Each move is represented by a sequence of numbers, the first number being 
                  the starting position of the piece to be moved, and the rest being the squares 
                  that the piece will visit (in order) during the course of a single move.  The 
                  last number in this sequence is the final position of the piece being moved.  */
    int numLegalMoves;
};

void PrintBoard(char board[8][8])
{
	char newBoard[8][8];
    int x,y;
    for(y=0; y<8; y++)
    {
       for(x=0; x<8; x++)
       {
           if(x%2 != y%2) {
               if(!empty(board[y][x])) {
            	   if (color(board[y][x]) == 1)
            	   {
            		   if (king(board[y][x]))
            		   {
            			   newBoard[y][x] = 'R';
            		   }
            		   else
            		   {
            			   newBoard[y][x] = 'r';
            		   }
            	   }
            	   else
            	   {
            		   if (king(board[y][x]))
					   {
            			   newBoard[y][x] = 'W';
					   }
					   else
					   {
						   newBoard[y][x] = 'w';
					   }
            	   }
               } else newBoard[y][x] = ' ';
           } else newBoard[y][x] = ' ';
           fprintf(stderr,"%c",newBoard[y][x]);
       }
       fprintf(stderr,"\n");
    }
    fprintf(stderr,"\n\n\n");
}

void CopyState(char *dest, char src);
void ResetBoard(void);
void AddMove(char move[12]);
void FindKingMoves(char board[8][8], int x, int y);
void FindMoves(int player, char board[8][8], int x, int y);
void AddJump(char move[12]);
int FindKingJump(int player, char board[8][8], char move[12], int len, int x, int y);
int FindJump(int player, char board[8][8], char move[12], int len, int x, int y);
int FindLegalMoves(struct State *state);
int EVAL(char Board[8][8]);
int MaxVal(char currBoard[8][8], int alpha, int beta, int depth);
int MinVal(char currBoard[8][8], int alpha, int beta, int depth);
void FindBestMove(int player);
void NumberToXY(char num, int *x, int *y);
int MoveLength(char move[12]);
int TextToMove(char *mtext, char move[12]);
void MoveToText(char move[12], char *mtext);
void PerformMove(char board[8][8], char move[12], int mlen, int player);
int heuristicEvaluation(struct State * state);
int offensivePawns(int x, int y, struct State * state);
int middleKings(int x, int y, struct State * state);
int jumpAvoidance(int x, int y, struct State * state);
int hangOnWallsAndHomeRow(int x, int y, int piecesColor);
void TrackPieces(struct State * state);
void testPiecesBounds1(void);

void testAllRedJumps(void);
void testAllWhiteJumps(void);
void testAllRed(void);
void testAllWhite(void);
void testSelection001(void);
void testSelection002(void);

#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/times.h>
#include <time.h>
#include <setjmp.h>
#include "myprog.h"

//#ifndef CLK_TCK
//#define CLK_TCK CLOCKS_PER_SEC
//#endif
#define MAX(x, y) (((x) > (y)) ? (x) : (y))
#define MIN(x, y) (((x) < (y)) ? (x) : (y))

int maxInt = 2147483647;
float SecPerMove;
char board[8][8];
char bestmove[12];
int me,cutoff,endgame = 0;
long NumNodes;
int MaxDepth;
int RED = 1;
int WHITE = 2;
int threeSecondDepth = 6;
int oneSecondDepth = 4;
int tenSecondDepth = 7;
jmp_buf env;

/*** For timing ***/
clock_t start_t;
struct tms bff;

/*** For the jump list ***/
int jumpptr = 0;
int jumplist[48][12];

/*** For the move list ***/
int numLegalMoves = 0;
int movelist[48][12];

/* Print the amount of time passed since my turn began */
void PrintTime(void)
{
    clock_t current;
    float total;

    current = clock();
    total = (float) ((float)current-(float)start_t)/(int)CLOCKS_PER_SEC;
    fprintf(stderr, "Time = %f\n", total);
}

/* Determine if I'm low on time */
int LowOnTime(void)
{

    clock_t current;
    float total;

    current = clock();
    total = (float) ((float)current-(float)start_t)/CLOCKS_PER_SEC;
    if(total >= (SecPerMove- SecPerMove != 1 ? .5 : .25))
    {
    	fprintf(stderr, "YES, I AM LOW ON TIME\n");
    	return 1;
    }
    else return 0;
}

/* Copy a square state */
void CopyState(char *dest, char src)
{
    char state;

    *dest &= Clear;
    state = src & 0xE0;
    *dest |= state;
}

/* Reset board to initial configuration */
void ResetBoard(void)
{
        int x,y;
        char pos;

        pos = 0;
        for(y=0; y<8; y++)
        for(x=0; x<8; x++)
        {
                if(x%2 != y%2) {
                        board[y][x] = pos;
                        if(y<3 || y>4) board[y][x] |= Piece; else board[y][x] |= Empty;
                        if(y<3) board[y][x] |= Red;
                        if(y>4) board[y][x] |= White;
                        pos++;
                } else board[y][x] = 0;
        }
    endgame = 0;
}

/* Add a move to the legal move list */
void AddMove(char move[12])
{
    int i;

    for(i=0; i<12; i++) movelist[numLegalMoves][i] = move[i];
    numLegalMoves++;
}

/* Finds legal non-jump moves for the King at position x,y */
void FindKingMoves(char board[8][8], int x, int y)
{
    int i,j,x1,y1;
    char move[12];

    memset(move,0,12*sizeof(char));

    /* Check the four adjacent squares */
    for(j=-1; j<2; j+=2)
    for(i=-1; i<2; i+=2)
    {
        y1 = y+j; x1 = x+i;
        /* Make sure we're not off the edge of the board */
        if(y1<0 || y1>7 || x1<0 || x1>7) continue;
        if(empty(board[y1][x1])) {  /* The square is empty, so we can move there */
            move[0] = number(board[y][x])+1;
            move[1] = number(board[y1][x1])+1;
            AddMove(move);
        }
    }
}

/* Finds legal non-jump moves for the Piece at position x,y */
void FindMoves(int player, char board[8][8], int x, int y)
{
    int i,j,x1,y1;
    char move[12];

    memset(move,0,12*sizeof(char));

    /* Check the two adjacent squares in the forward direction */
    if(player == 1) j = 1; else j = -1;
    for(i=-1; i<2; i+=2)
    {
        y1 = y+j; x1 = x+i;
        /* Make sure we're not off the edge of the board */
        if(y1<0 || y1>7 || x1<0 || x1>7) continue;
        if(empty(board[y1][x1])) {  /* The square is empty, so we can move there */
            move[0] = number(board[y][x])+1;
            move[1] = number(board[y1][x1])+1;
            AddMove(move);
        }
    }
}

/* Adds a jump sequence the the legal jump list */
void AddJump(char move[12])
{
    int i;

    for(i=0; i<12; i++) jumplist[jumpptr][i] = move[i];
    jumpptr++;
}

/* Finds legal jump sequences for the King at position x,y */
int FindKingJump(int player, char board[8][8], char move[12], int len, int x, int y)
{
    int i,j,x1,y1,x2,y2,FoundJump = 0;
    char one,two,mymove[12],myboard[8][8];

    memcpy(mymove,move,12*sizeof(char));

    /* Check the four adjacent squares */
    for(j=-1; j<2; j+=2)
    for(i=-1; i<2; i+=2)
    {
        y1 = y+j; x1 = x+i;
        y2 = y+2*j; x2 = x+2*i;
        /* Make sure we're not off the edge of the board */
        if(y2<0 || y2>7 || x2<0 || x2>7) continue;
        one = board[y1][x1];
        two = board[y2][x2];
        /* If there's an enemy piece adjacent, and an empty square after him, we can jump */
        if(!empty(one) && color(one) != player && empty(two)) {
            /* Update the state of the board, and recurse */
            memcpy(myboard,board,64*sizeof(char));
            myboard[y][x] &= Clear;
            myboard[y1][x1] &= Clear;
            mymove[len] = number(board[y2][x2])+1;
            FoundJump = FindKingJump(player,myboard,mymove,len+1,x+2*i,y+2*j);
            if(!FoundJump) {
                FoundJump = 1;
                AddJump(mymove);
            }
        }
    }
    return FoundJump;
}

/* Finds legal jump sequences for the Piece at position x,y */
int FindJump(int player, char board[8][8], char move[12], int len, int x, int y)
{
    int i,j,x1,y1,x2,y2,FoundJump = 0;
    char one,two,mymove[12],myboard[8][8];

    memcpy(mymove,move,12*sizeof(char));

    /* Check the two adjacent squares in the forward direction */
    j = player == 1 ? 1 : -1;
    for(i=-1; i<2; i+=2)
    {
        y1 = y+j; x1 = x+i;
        y2 = y+2*j; x2 = x+2*i;
        /* Make sure we're not off the edge of the board */
        if(y2<0 || y2>7 || x2<0 || x2>7) continue;
        one = board[y1][x1];
        two = board[y2][x2];
        /* If there's an enemy piece adjacent, and an empty square after him, we can jump */
        if(!empty(one) && color(one) != player && empty(two)) {
            /* Update the state of the board, and recurse */
            memcpy(myboard,board,64*sizeof(char));
            myboard[y][x] &= Clear;
            myboard[y1][x1] &= Clear;
            mymove[len] = number(board[y2][x2])+1;
            FoundJump = FindJump(player,myboard,mymove,len+1,x+2*i,y+2*j);
            if(!FoundJump) {
                FoundJump = 1;
                AddJump(mymove);
            }
        }
    }
    return FoundJump;
}

/* Determines all of the legal moves possible for a given state */
int FindLegalMoves(struct State *state)
{
//	fprintf(stderr, "FindLegalMove\n");
    int column,row, x, y;
    char move[12], board[8][8];

    memset(move,0,12*sizeof(char));
    jumpptr = numLegalMoves = 0;
    memcpy(board,state->board,64*sizeof(char));
    memset(movelist,0,48*12*sizeof(char));
    memset(jumplist,0,48*12*sizeof(char));
    
    /* Loop through the board array, determining legal moves/jumps for each piece */
    for(row=0; row<8; row++)
    for(column=0; column<8; column++)
    {
        if(column%2 != row%2 && color(board[row][column]) == state->player && !empty(board[row][column])) {
            if(king(board[row][column])) { /* King */
                move[0] = number(board[row][column])+1;
                FindKingJump(state->player,board,move,1,column,row);
                if(!jumpptr) FindKingMoves(board,column,row);
            }
            else if(piece(board[row][column])) { /* Piece */
                move[0] = number(board[row][column])+1;
                FindJump(state->player,board,move,1,column,row);
                if(!jumpptr) FindMoves(state->player,board,column,row);
            }
        }
    }
    if(jumpptr) {
        for(x=0; x<jumpptr; x++)
        for(y=0; y<12; y++)
        state->movelist[x][y] = jumplist[x][y];
        state->numLegalMoves = jumpptr;
    }
    else {
        for(x=0; x<numLegalMoves; x++)
        for(y=0; y<12; y++)
        state->movelist[x][y] = movelist[x][y];
        state->numLegalMoves = numLegalMoves;
    }
    return (jumpptr+numLegalMoves);
}

/* Converts a square label to it's x,y position */
void NumberToXY(char num, int *x, int *y)
{
    int i=0,newy,newx;

    for(newy=0; newy<8; newy++)
    for(newx=0; newx<8; newx++)
    {
        if(newx%2 != newy%2) {
            i++;
            if(i==(int) num) {
                *x = newx;
                *y = newy;
                return;
            }
        }
    }
    *x = 0;
    *y = 0;
}

/* Returns the length of a move */
int MoveLength(char move[12])
{
    int i = 0;
    while(i<12 && move[i]) i++;
    return i;
}

/* Converts the text version of a move to its integer array version */
int TextToMove(char *mtext, char move[12])
{
    int i=0,len=0,last;
    char val,num[64];

    while(mtext[i] != '\0') {
        last = i;
        while(mtext[i] != '\0' && mtext[i] != '-') i++;
        strncpy(num,&mtext[last],i-last);
        num[i-last] = '\0';
        val = (char) atoi(num);
        if(val <= 0 || val > 32) return 0;
        move[len] = val;
        len++;
        if(mtext[i] != '\0') i++;
    }
    if(len<2 || len>12) return 0; else return len;
}

/* Converts the integer array version of a move to its text version */
void MoveToText(char move[12], char *mtext)
{
    int i;
    char temp[8];

    mtext[0] = '\0';
    for(i=0; i<12; i++) {
        if(move[i]) {
            sprintf(temp,"%d",(int)move[i]);
            strcat(mtext,temp);
            strcat(mtext,"-");
        }
    }
    mtext[strlen(mtext)-1] = '\0';
}

/* Performs a move on the board, updating the state of the board */
void PerformMove(char board[8][8], char move[12], int mlen, int player)
{
    int i,j,srcSquareX,srcSquareY,destSquareX,destSquareY,otherDestSquareX,otherDestSquareY, homeRow=0, otherHomeRow=7;

    NumberToXY(move[0],&srcSquareX,&srcSquareY);
    NumberToXY(move[mlen-1],&destSquareX,&destSquareY);
    CopyState(&board[destSquareY][destSquareX],board[srcSquareY][srcSquareX]);
    if(destSquareY == homeRow || destSquareY == otherHomeRow) board[destSquareY][destSquareX] |= King;
    board[srcSquareY][srcSquareX] &= Clear;

    //if this move is a jump
    NumberToXY(move[1],&otherDestSquareX,&otherDestSquareY);
    if(abs(otherDestSquareX-srcSquareX) == 2) {
        for(i=0,j=1; j<mlen; i++,j++) {
            if(move[i] > move[j]) {
                destSquareY = -1;
                if((move[i]-move[j]) == 9) destSquareX = -1; else destSquareX = 1;
            }
            else {
                destSquareY = 1;
                if((move[j]-move[i]) == 7) destSquareX = -1; else destSquareX = 1;
            }
            NumberToXY(move[i],&srcSquareX,&srcSquareY);
            board[srcSquareY+destSquareY][srcSquareX+destSquareX] &= Clear;
        }
    }
}

int main(int argc, char *argv[])
{
	if (argc == 5 && !strncmp(argv[4], "TEST", strlen("TEST")))
	{
		testAllRedJumps();
		testAllWhiteJumps();
		testAllRed();
		testAllWhite();
		testSelection001();
		testSelection002();
		return 0;
	}
    char buf[1028],move[12];
    int mlen,player1;
#ifndef DEBUG
	int len;
#endif
    /* Convert command line parameters */
    SecPerMove = (float) atof(argv[1]); /* Time allotted for each move */

    /* Determine if I am player 1 (red) or player 2 (white) */
#ifdef DEBUG
    buf[0] = 'P'; buf[1]='l'; buf[2]='a'; buf[3]='y'; buf[4]='e'; buf[5]='r'; buf[6]='2';
#else
    len=read(STDIN_FILENO,buf,1028);
    buf[len]='\0';
#endif
    if(!strncmp(buf,"Player1", strlen("Player1")))
    {
        player1 = 1;
    }
    else
    {
        player1 = 0;
    }
    if(player1) me = 1; else me = 2;

    /* Set up the board */
    ResetBoard();

    if (player1) {
        start_t = clock();
        goto determine_next_move;
    }
#ifdef DEBUG
    int i = 2;
#endif
    for(;;) {
#ifdef DEBUG
    	player1= !(i%2) ? 0 : 1 ;
    	i++;
#else
        len=read(STDIN_FILENO,buf,1028);
        buf[len]='\0';
#endif
        /* Read the other player's move from the pipe */
        start_t = clock();
        memset(move,0,12*sizeof(char));

        /* Update the board to reflect opponents move */
        mlen = TextToMove(buf,move);
        PerformMove(board,move,mlen, me);
#ifdef DEBUG
        PrintBoard(board);
#endif

determine_next_move:

		MaxDepth = SecPerMove == 1 ? oneSecondDepth : SecPerMove == 3 ? threeSecondDepth : tenSecondDepth ;
        /* Find my move, update board, and write move to pipe */
        if(player1) FindBestMove(1); else FindBestMove(2);
        if(bestmove[0] != 0) { /* There is a legal move */
            mlen = MoveLength(bestmove);
#ifndef DEBUG
			PerformMove(board,bestmove,mlen, me);
#endif
            MoveToText(bestmove,buf);
        }
        else exit(1); /* No legal moves available, so I have lost */

	 	/* Write the move to the pipe */
        write(STDOUT_FILENO,buf,strlen(buf));
        printf("%s", "\n");
        fflush(stdout);
    }
    return 0;
}

/* and the PerformMove function */
void FindBestMove(int player) {
	struct State state;

	/* Set up the current state */
	state.player = player;

	memcpy(state.board, board, 64 * sizeof(char));

	/* Find the legal moves for the current state */
	FindLegalMoves(&state);

	memset(bestmove, 0, 12 * sizeof(char));
	int x, currBestMove = -1, currBestVal = -maxInt;
	for (x = 0; x < state.numLegalMoves; x++) {
		int rval = 0;
		char nextBoard[8][8];
		memcpy(nextBoard, state.board, 64 * sizeof(char));
		PerformMove(nextBoard, state.movelist[x], MoveLength(state.movelist[x]), player);

		int i = setjmp(env);
		if (i != 0) goto bailout;

		rval = MinVal(nextBoard, -maxInt, maxInt, MaxDepth);
		if (currBestVal < rval) {
			currBestVal = rval;
			currBestMove = x;
		}
bailout:
		if (LowOnTime() && numLegalMoves != 0)
		{
			fprintf(stderr, "Low on time, picking random move\n");
			PrintTime();
			memcpy(bestmove, state.movelist[rand()%state.numLegalMoves], 12 * sizeof(char));
		}
	}
	memcpy(bestmove, state.movelist[currBestMove], MoveLength(state.movelist[currBestMove]));
}

int MinVal(char currBoard[8][8], int alpha, int beta, int depth) {
	struct State state;
	int x;
	depth--;


	state.player = me == RED ? WHITE : RED ;
	memcpy(state.board, currBoard, 64 * sizeof(char));
	if (depth <= 0) return heuristicEvaluation(&state);

	FindLegalMoves(&state);

	for (x = 0; x < state.numLegalMoves; x++) {
		char nextBoard[8][8];
		memcpy(nextBoard, state.board, 64 * sizeof(char));
		PerformMove(nextBoard, state.movelist[x], MoveLength(state.movelist[x]), state.player);
		beta = MIN(beta, MaxVal(nextBoard, alpha, beta, depth));
		if (LowOnTime())
		{
			longjmp(env, 1);
		}
		if (beta <= alpha)
			return alpha;
	}
	return beta;
}

/*Find the best move for us*/
int MaxVal(char currBoard[8][8], int alpha, int beta, int depth) {
	struct State state;
	int x;
	depth--;

	state.player = me;
	memcpy(state.board, currBoard, 64 * sizeof(char));
	if (depth <= 0) return heuristicEvaluation(&state);
	FindLegalMoves(&state);

	for (x = 0; x < state.numLegalMoves; x++) {
		char nextBoard[8][8];
		memcpy(nextBoard, state.board, 64 * sizeof(char));
		PerformMove(nextBoard, state.movelist[x], MoveLength(state.movelist[x]), state.player);
		alpha = MAX(alpha, MinVal(nextBoard, alpha, beta, depth));
		if (LowOnTime())
		{
			longjmp(env, 1);
		}
		if (alpha >= beta)
			return beta;
	}
	return alpha;
}

/*
 * The thought process behind this heuristic:
 * 1. Pawns that are closer to becoming kings are more valuable
 * 2. Controlling the middle is of more importance
 * 3. Typically, I don't want to move directly into a position to be jumped
 */
int heuristicEvaluation(struct State * state) {

	int row, column, p1Score = 0, p2Score = 0, numWhitePieces = 0, numRedPieces = 0;
	int KING_MATERIAL_ADV = 10;
	int PAWN_MATERIAL_ADV = 5;

	if (!endgame)
	for (row = 0; row < 8; row++)
		for (column = 0; column < 8; column++) {
			if (row % 2 != column % 2 && !empty(state->board[row][column])) {
				if (color(state->board[row][column]) == RED) {
					if (king(state->board[row][column]))
					{
						p1Score += KING_MATERIAL_ADV;
					}
					else if (piece(state->board[row][column]))
					{
						p1Score += PAWN_MATERIAL_ADV;
					}
					++numRedPieces;
//					p1Score += king(state->board[row][column]) ? middleKings(row, column, state) : 0;
//					p1Score -= jumpAvoidance(row, column, state) ? king(state->board[row][column]) ? KING_PENALTY : PAWN_PENALTY : 0 ;
//					p1Score += hangOnWallsAndHomeRow(row, column, color(state->board[row][column]));
				}
				else
				{
					if (king(state->board[row][column]))
					{
						p2Score += KING_MATERIAL_ADV;
					}
					else if (piece(state->board[row][column]))
					{
						p2Score += PAWN_MATERIAL_ADV;
					}
					++numWhitePieces;
//					p2Score += king(state->board[row][column]) ? middleKings(row, column, state) : 0;
//					p2Score -= jumpAvoidance(row, column, state) ? king(state->board[row][column]) ? KING_PENALTY : PAWN_PENALTY : 0 ;
//					p2Score += hangOnWallsAndHomeRow(row, column, color(state->board[row][column]));
				}
			}
		}
	int difference = p1Score - p2Score;
	return me == RED ? difference : -1*difference;
}

int hangOnWallsAndHomeRow(int row, int column, int piecesColor)
{
	int DEFENSIVE_BONUS = endgame ? -10 : 5 ;
/*	if ((piecesColor == RED && me == RED && row == 0) || (piecesColor == WHITE && me == WHITE && row == 7) ||
			((piecesColor == WHITE && me == WHITE && (column == 0 || column == 7)))) //&& (row >= 2 && row <= 6)))
	{
		return DEFENSIVE_BONUS;
	}
*/	
	if (piecesColor == RED && me == RED)
	{
		if (row ==0 || ((column == 0 || column == 7)&& row >= 2))
			return DEFENSIVE_BONUS;
			
	}
	else if (piecesColor == WHITE && me == WHITE)
	{
                if (row == 7 || ((column == 0 || column == 7)&& row >= 6))
                        return DEFENSIVE_BONUS;
	}
	
	return 0;
}

int middleKings(int row, int column, struct State * state)
{
	int MIDDLE_BONUS = endgame ? 6 : 3;
	int advancementBonus = 0;
	int advancementDirection = color(state->board[row][column]) == RED ? 1 : -1 ;

	if (row >= (3+advancementDirection) && row <= (4+advancementDirection))
	{
		advancementBonus += MIDDLE_BONUS;
	}
	advancementBonus += advancementDirection == -1 ? row : row-7 ;
	return advancementBonus;
}

/* Initially I am only checking if I can be jumped by pawns
 * note: we are only ever in this method if we are a valid piece*/
int jumpAvoidance(int row, int column, struct State * state)
{
    int newCols,advancementDirection,newColumnA,frontRow,newColumnB,rearRow = 0;
    char possibleEnemy,possibleBlank;
    advancementDirection = color(state->board[row][column]) == RED ? 1 : -1;
    //if there is an enemy piece in front of me in either square
    //and there is an empty space behind me in the opposite square
    //i can be jumped
    rearRow = row + (-1*advancementDirection);
    frontRow = row + (1*advancementDirection);
    for(newCols=-1; newCols<2; newCols+=2)
    {
        newColumnA = column+newCols*advancementDirection;
        newColumnB = column-newCols*advancementDirection;
        /* Make sure we're not off the edge of the board */
        if(rearRow<0 || rearRow>7 || newColumnB<0 || newColumnB>7 || newColumnA<0 || newColumnB>7) continue;
        possibleEnemy = state->board[frontRow][newColumnA];
        possibleBlank = state->board[rearRow][newColumnB];
        /* If there's an enemy piece adjacent, and an empty square after him, we can jump */
        if(!empty(possibleEnemy) && color(possibleEnemy) != color(state->board[row][column]) && empty(possibleBlank)) {
            return 1;
        }
    }
    return 0;
}

void testAllRedJumps(void)
{
	//selection004
	FILE* fp;
	fp = fopen ("/home/reuben/dev/ai/checkers/makeboard/boards/twoRedOnOneWhite.bin", "rb");
	struct State state1;
	fread(state1.board, sizeof(char), 64, fp);
	state1.player = 1;
	int score1 = 0;
	score1 = heuristicEvaluation(&state1);
	state1.player = 2;
	int score2 = 0;
	score2 = heuristicEvaluation(&state1);
	if (score1 != 1 && score2 != -1) fprintf(stderr, "testAllRedJumps Score1 was %d Score2 was %d\n", score1, score2);
}

void testAllWhiteJumps(void)
{
	//selection003
	FILE* fp;
	fp = fopen ("/home/reuben/dev/ai/checkers/makeboard/boards/twoWhiteOnOneRed.bin", "rb");
	struct State state1;
	fread(state1.board, sizeof(char), 64, fp);
	state1.player = 1;
	int score1 = 0;
	score1 = heuristicEvaluation(&state1);
	state1.player = 2;
	int score2 = 0;
	score2 = heuristicEvaluation(&state1);
	if (score1 != -1 && score2 != 1) fprintf(stderr, "testAllWhiteJumps Score1 was %d Score2 was %d\n", score1, score2);
}

void testAllRed(void)
{
	FILE* fp;
	fp = fopen ("/home/reuben/dev/ai/checkers/makeboard/boards/allRed.bin", "rb");
	struct State state1;
	fread(state1.board, sizeof(char), 64, fp);
	state1.player = 1;
	int score1 = 0;
	score1 = heuristicEvaluation(&state1);
	state1.player = 2;
	int score2 = 0;
	score2 = heuristicEvaluation(&state1);
	if (score1 != 90 && score2 != -90) fprintf(stderr, "testAllRed Score1 was %d Score2 was %d\n", score1, score2);
}

void testAllWhite(void)
{
	FILE* fp;
	fp = fopen ("/home/reuben/dev/ai/checkers/makeboard/boards/allWhite.bin", "rb");
	struct State state1;
	fread(state1.board, sizeof(char), 64, fp);
	state1.player = 1;
	int score1 = 0;
	score1 = heuristicEvaluation(&state1);
	state1.player = 2;
	int score2 = 0;
	score2 = heuristicEvaluation(&state1);
	if (score1 != -90 && score2 != 90) fprintf(stderr, "testAllWhite Score1 was %d Score2 was %d\n", score1, score2);
}

void testSelection001(void)
{
	//selection001
	FILE* fp;
	fp = fopen ("/home/reuben/dev/ai/checkers/makeboard/boards/selection001.bin", "rb");
	struct State state1;
	fread(state1.board, sizeof(char), 64, fp);
	state1.player = 1;
	int score1 = 0;
	score1 = heuristicEvaluation(&state1);
	state1.player = 2;
	int score2 = 0;
	score2 = heuristicEvaluation(&state1);
	if (score1 != -11 && score2 != 11) fprintf(stderr, "testSelection001 Score1 was %d Score2 was %d\n", score1, score2);
}

void testSelection002(void){
	//selection002
	FILE* fp;
	fp = fopen ("/home/reuben/dev/ai/checkers/makeboard/boards/selection002.bin", "rb");
	struct State state1;
	fread(state1.board, sizeof(char), 64, fp);
	state1.player = 1;
	int score1 = 0;
	score1 = heuristicEvaluation(&state1);
	state1.player = 2;
	int score2 = 0;
	score2 = heuristicEvaluation(&state1);
	if (score1 != 27 && score2 != -27) fprintf(stderr, "testSelection002 Score1 was %d Score2 was %d\n", score1, score2);
}

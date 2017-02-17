#include <iostream>
#include <limits.h>
#include "d_matrix.h"
#include "d_except.h"
#include <list>
#include <fstream>

using namespace std;

typedef int ValueType; // The type of the value in a cell
const int blank = -1;  // Indicates that a cell is blank

const int squareSize = 3;  //  The number of cells in a small square
//  (usually 3).  The board has
//  SquareSize^2 rows and SquareSize^2
//  columns.

const int boardSize = squareSize * squareSize;

const int minValue = 1;
const int maxValue = 9;

//int numSolutions = 0;

class Board
// Stores the entire Sudoku board
{
public:
    Board(int);
    void Clear();
    void Initialize(ifstream &fin);
    void Print();
    bool IsBlank(int, int);
    ValueType GetCell(int, int);
    void ClearCell(int i,int j,int val);
    void  SetCell(int i,int j, ValueType val);
    void PrintConflicts();
    bool Solved();
    bool SolvePuzzle();
    void GetEmpty(int &row, int &col);
    friend ostream& operator << (ostream& ostr, vector<bool> &v);
    int getRecursions();
private:
    
    // The following matrices go from 1 to BoardSize in each
    // dimension.  I.e. they are each (BoardSize+1) X (BoardSize+1)
    vector<vector<bool>> conflictRows;
    vector<vector<bool>> conflictCols;
    vector<vector<bool>> conflictSq;
    matrix<ValueType> value;
    int recursions;
};


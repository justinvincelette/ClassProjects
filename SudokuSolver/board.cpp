//
//  board.cpp
//  vinxie-4a
//
//  Created by AphroditeJo on 3/19/16.
//  Copyright © 2016 AphroditeJo. All rights reserved.
//

#include "board.hpp"
#include <vector>
Board::Board(int sqSize)
: value(boardSize+1,boardSize+1)
// Board constructor
{
    vector<bool> vec(9,false);
    for (int i=0; i<9; i++) {
        // Initialize the conflict rows, columns, and the sqaure matrix.
        conflictRows.push_back(vec);
        conflictCols.push_back(vec);
        conflictSq.push_back(vec);
    }
}
int GetSquare(int i, int j){
    /* Pass the location of the entry as arguments to the function
     and return the sqaure number. */
    if(i<=3 && j<=3)
        return 1;
    else if(i<=3 && j<=6)
        return 2;
    else if(i<=3 && j<=9)
        return 3;
    else if(i<=6 && j<=3)
        return 4;
    else if(i<=6 && j<=6)
        return 5;
    else if(i<=6 && j<=9)
        return 6;
    else if(i<=9 && j<=3)
        return 7;
    else if(i<=9 && j<=6)
        return 8;
    else if(i<=9 && j<=9)
        return 9;
    return 0;
}

void Board::Clear()
// Clear the entire board.
{
    vector<bool> vec(9,false);
    for (int i=0; i<9; i++) {
        conflictRows[i] = vec;
        conflictCols[i] = vec;
        conflictSq[i] = vec;
    }
    value = matrix<ValueType> (boardSize + 1, boardSize + 1);
}

void Board::ClearCell(int i, int j, int val){
    value[i][j] = 0;
    conflictRows[i-1][val-1] = false;
    conflictCols[j-1][val-1] = false;
    conflictSq[GetSquare(i, j)-1][val-1] = false;
    
}

void  Board::SetCell(int i,int j, ValueType val)
// set cell i,j to val and update conflicts
{
    value[i][j] = val;
    conflictRows[i-1][val-1] = true;
    conflictCols[j-1][val-1] = true;
    conflictSq[GetSquare(i, j)-1][val-1] = true;
}

void Board::PrintConflicts(){
// Prints conflict vectors for puzzle
    int i,j;
    for(i=0;i<9;i++){
        for(j=0;j<9;j++)
            cout << conflictRows[i][j] << " ";
        cout << endl;
    }
    cout << endl;
    for (i=0; i<9; i++) {
        for (j=0; j<9; j++) {
            cout << conflictCols[i][j] << " ";
        }
        cout << endl;
    }
    cout << endl;
    for (i=0; i<9; i++) {
        for (j=0; j<9; j++) {
            cout << conflictSq[i][j] << " ";
        }
        cout << endl;
    }
}
void Board::Initialize(ifstream &fin)
// Read a Sudoku board from the input file.
{
    recursions = 0;
    char ch;
    
    Clear();
    for (int i = 1; i <= boardSize; i++)
        for (int j = 1; j <= boardSize; j++)
        {
            fin >> ch;
            
            // If the read char is not Blank
            if (ch != '.')
            {
                SetCell(i,j,ch-'0');   // Convert char to int
            }
        }
}

int SquareNumber(int i, int j)
// Return the square number of cell i,j (counting from left to right,
// top to bottom.  Note that i and j each go from 1 to BoardSize
{
    // Note that (int) i/SquareSize and (int) j/SquareSize are the x-y
    // coordinates of the square that i,j is in.
    
    return squareSize * ((i-1)/squareSize) + (j-1)/squareSize + 1;
}

ostream &operator<<(ostream &ostr, vector<bool> &v)
// Overloaded output operator for vector class.
{
    for (int i = 0; i < v.size(); i++)
        ostr << v[i] << " ";
    ostr << endl;
    return ostr;
}

ValueType Board::GetCell(int i, int j)
// Returns the value stored in a cell.  Throws an exception
// if bad values are passed.
{
    if (i >= 1 && i <= boardSize && j >= 1 && j <= boardSize)
        return value[i][j];
    
    else
        //cout << i << " " << j << endl;
        return 0;
    /*throw rangeError("bad value in GetCell");*/
}

bool Board::IsBlank(int i, int j)
// Returns true if cell i,j is blank, and false otherwise.
{
    if(GetCell(i, j) == 0)
        return true;
    else
        return false;
}

void Board::Print()
// Prints the current board.
{
    for (int i = 1; i <= boardSize; i++)
    {
        if ((i-1) % squareSize == 0)
        {
            cout << " -";
            for (int j = 1; j <= boardSize; j++)
                cout << "---";
            cout << "-";
            cout << endl;
        }
        for (int j = 1; j <= boardSize; j++)
        {
            if ((j-1) % squareSize == 0)
                cout << "|";
            if (!IsBlank(i,j))
                cout << " " << GetCell(i,j) << " ";
            else
                cout << "   ";
        }
        cout << "|";
        cout << endl;
    }
    
    cout << " -";
    for (int j = 1; j <= boardSize; j++)
        cout << "---";
    cout << "-";
    cout << endl << endl;;
}

bool Board::Solved(){
// Checks to see if the board is solved, this is true when all conflict vectors are filled with true's
    int i,j;
    for(i=0;i<9;i++){
        for (j=0; j<9; j++) {
            if(!conflictCols[i][j] || !conflictRows[i][j] || !conflictSq[i][j])
                return false; // as soon as one false is found in any of the vectors, we know the board is not solved
        }
    }
    return true;
}

bool Board::SolvePuzzle(){
// Recursive backtracking function to solve puzzle
    recursions++;
    if(Solved()){ // First check if board is solved, stop and return here if true
        return true;
    }

    int i, row, col;
    GetEmpty(row, col); // Get the next empty cell we want to add a number to
    for(i=1;i<=9;i++){ // Loop which will try to enter numbers in the cell, starting with 1
        if(!conflictRows[row-1][i-1] && !conflictCols[col-1][i-1] && !conflictSq[GetSquare(row, col)-1][i-1]){ // Only try this number if there are no conflicts for it
            SetCell(row, col, i);
            //Print();
            if(SolvePuzzle()){ // Call recursively
                return true;
            }
                ClearCell(row, col, i); // If it reaches here, it means we backtracked and the number entered was not part of the solution, so try next number
            
        }
    }
    return false; // Getting here means there were no correct numbers to be entered into the current cell, this will kick off the backtracking
}
void Board::GetEmpty(int &row, int &col) {
// Gets the next empty cell to add a number to
// It picks the cell that has the least number of possibilities, which greatly reduces the overall time for solving the puzzle
    int minPossibilities = 100, currPossibilities = 0, tempRow = 0, tempCol = 0;
    // Must loop through entire board
    for (row = 1; row <= 9; row++) {
        for (col = 1; col <= 9; col++) {
            if (IsBlank(row, col)) { // We only care about the blank cells
                for (int i = 1; i <= 9; i++) { // Check if numbers 1 through 9 are possibilities and keep count
                    if (!conflictRows[row-1][i-1] && !conflictCols[col-1][i-1] && !conflictSq[GetSquare(row, col)-1][i-1]) {
                        currPossibilities++;
                    }
                }
                if (currPossibilities < minPossibilities) { // Check to see if this cell has the lowest number of possibilities, and if so then set it
                    minPossibilities = currPossibilities;
                    tempRow = row;
                    tempCol = col;
                }
                currPossibilities = 0;
            }
        }
    }
    // Sets the correct row/columns for the cell with minimum possibilities
    row = tempRow;
    col = tempCol;
}

int Board::getRecursions() {
// Return the number of recursions it took to solve this puzzle
    return recursions;
}

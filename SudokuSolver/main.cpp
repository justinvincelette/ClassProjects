#include <unistd.h>
#include "board.hpp"
int main()
{
    ifstream fin;
    int recursions = 0, boards = 0, average;
    
    // Read the sample grid from the file.
    string fileName = "sudoku.txt";
    
    fin.open(fileName.c_str());
    if (!fin)
    {
        cerr << "Cannot open " << fileName << endl;
        exit(1);
    }
    
    try
    {
        Board b1(squareSize);
        
        while (fin && fin.peek() != 'Z')
        {
            b1.Initialize(fin);
            b1.Print();
            b1.SolvePuzzle();
            b1.Print();
            cout << "Number of recursions: " << b1.getRecursions() << endl;
            usleep(1000000);
            recursions = recursions + b1.getRecursions();
            boards++;
        }
        average = recursions / boards;
        cout << endl << "Total number of recursions: " << recursions << endl;
        cout << "Average number of recursions: " << average << endl;
    }
    catch  (indexRangeError &ex)
    {
        cout << ex.what() << endl;
        exit(1);
    }
}

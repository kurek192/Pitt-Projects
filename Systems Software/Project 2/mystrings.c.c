//WILLIAM KUREK
//CS 449
//PROJECT 2: mystrings

#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {

    if (argc != 2) { //ensure a valid number of arguments
        printf("Invalid number of arguments");
        return 0;
    }

    FILE *inFile;
    
    inFile = fopen(argv[1], "rb"); //read in file in binary mode

    if (inFile == NULL) {//esure file exists
        printf("File does not exist!");
        return 0;
    }

    char theString[1000]; //string buffer with maximum size of 1000
    char character; //for reading in individual character
    int stringLength = 0; //counter for string length
    
    while (feof(inFile) == 0) { //while the end of file has not been reached
        fread(&character, sizeof (character), 1, inFile); //read in individual character

        if (character > 31 && character < 127) { //check if characters are ASCII 32-126
            theString[stringLength] = character; if so, assign character to theString character array
            stringLength++; //increment string size
            continue;
        }

        theString[stringLength] = '\0'; //add terminating character to the end of string

        if (3 <= stringLength) //if stringLength is greater or equal to 3 (string is greater or equal to 4) print the string
            printf("%s\n", theString);

        stringLength = 0; //reset variables for next loop/string
        theString[0] = '\0';

    }

    fclose(inFile); //close file
    return 0; //terminate program

}


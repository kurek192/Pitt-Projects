//WILLIAM KUREK
//CS 449
//PROJECT 1
//IMAGE TRANSFORMATION

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#pragma pack(1) //ensure no packing in the structures

typedef struct { //BITMAP header information storage after read in
    unsigned short formatIdentifier;
    unsigned long size;
    unsigned short firstReserve;
    unsigned short secondReserve;
    unsigned long offset;
} BMPHEADER;

typedef struct { //DIB header information storage after read in
    unsigned long size;
    unsigned long width;
    unsigned long height;
    unsigned short colorPlanes;
    unsigned short bitPerPixel;
    unsigned long compression;
    unsigned long imageSize;
    unsigned long horizontal;
    unsigned long vertical;
    unsigned long paletteColors;
    unsigned long importantColors;
} DIBHEADER;

typedef struct { //Pixel Array to store each individual pixel
    unsigned char blue;
    unsigned char green;
    unsigned char red;
} PIXEL;

int main(int argc, char *argv[]) {

    if (argc != 3) { // check whether there are three arguments
        printf("Incorrect number of arguments.");
        return 0;
    }

    FILE *inFile; //declare file pointer
    BMPHEADER bmpheader;
    DIBHEADER dibheader;
    PIXEL pixel;
    unsigned char RGBchar;
    double RGB = 0;
    double temp = 0;

    inFile = fopen(argv[2], "rb+"); //open file in binary read/write mode

    fread(&bmpheader, sizeof (BMPHEADER), 1, inFile); //read in the BMP header

    if (bmpheader.formatIdentifier != 19778) { //check whether the file is a valid bitmap
        printf("File format not supported.");
        fclose(inFile);
        return 0;
    }

    fread(&dibheader, sizeof (DIBHEADER), 1, inFile); //read in the DIB header

    if (dibheader.size != 40) { //check whether the file format is the correct size
        printf("File format size not supported.");
        fclose(inFile);
        return 0;
    }

    if (dibheader.bitPerPixel != 24) { //check whether the bit per pixel is correct
        printf("File format bit per pixel not supported.");
        fclose(inFile);
        return 0;
    }

    int padding;
    int width = dibheader.width;
    int height = dibheader.height;
    long row, column;

    fseek(inFile, bmpheader.offset, SEEK_SET); //seek to the bmpheader offset to read in pixels

    if (strcmp(argv[1], "-invert") != 0 && strcmp(argv[1], "-grayscale") != 0) { //check to see if a valid argument was passed
        printf("Invalid argument.");
        fclose(inFile);
        return 0;
    }

    if (strcmp(argv[1], "-invert") == 0) {  //check if invert argument was passed
        padding = 0;
        for (row = height; row > 0; row--) {
            for (column = width; column > 0; column--) {
                fread(&pixel, sizeof (PIXEL), 1, inFile); //read in each individual pixel to the pixel array
                pixel.blue = ~pixel.blue; //set each pixel to its inverted bitwise NOT
                pixel.green = ~pixel.green;
                pixel.red = ~pixel.red;
                fseek(inFile, -3, SEEK_CUR); //seek backwards to prevent overwriting next pixel
                fwrite(&pixel, sizeof (PIXEL), 1, inFile); //write new pixel information
                fseek(inFile, 0, SEEK_CUR);
                padding += sizeof (PIXEL); //add to the padding
            }
            if (padding % 4 != 0) { //check for padding
                padding = 4 - (padding % 4);
                fseek(inFile, padding, SEEK_CUR); //seek past padding to next pixel
            }
        }
    }

    if (strcmp(argv[1], "-grayscale") == 0) { //check if grayscale argument was passed
        padding = width % 4;
        for (row = height; row > 0; row--) {
            for (column = width; column > 0; column--) {
                fread(&pixel, sizeof (PIXEL), 1, inFile); //read in pixel
                RGB = ((.2126 * (((double) (pixel.red))) / 255)+(.7152 * (((double) (pixel.green)) / 255))+(.0722 * (((double) (pixel.blue)) / 255)));

                if (RGB <= .0031308) {
                    RGB = (12.92 * RGB) * 255;         //grayscale operations
                }
                if (RGB > .0031308) {
                    temp = pow(RGB, (1 / 2.4));
                    RGB = (1.055 * temp - .055) * 255;
                }

                RGBchar = (unsigned char) RGB; //cast new RGB back to char
                pixel.blue = RGBchar; //set new pixels
                pixel.green = RGBchar;
                pixel.red = RGBchar;
                fseek(inFile, -3, SEEK_CUR); //seek backwards
                fwrite(&pixel, sizeof (PIXEL), 1, inFile); //write new pixel information
                fseek(inFile, 0, SEEK_CUR);

            }
            if (padding != 0) { //check for padding
                fseek(inFile, padding, SEEK_CUR); //seek past padding
            }
        }
    }

    fclose(inFile); //close the file
    return 0; //return 0, and exit the program

}







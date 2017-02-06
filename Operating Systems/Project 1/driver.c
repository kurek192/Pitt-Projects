/*    WILLIAM KUREK
 *    CS 1550
 *    PROJECT 1
 *    COMPILE: gcc -g -o driver driver.c library.c
 *    RUN: ./driver
 */

#include <stdio.h>

typedef unsigned short int color_t;
void init_graphics();
void exit_graphics();
void clear_screen();
char getkey();
void sleep_ms(long ms);
void draw_rect(int x1, int y1, int width, int height, color_t change);
void draw_text(int x, int y, const char *text, color_t change);

int main() {
    clear_screen();
    init_graphics();

    //colors
    color_t black = encodeColor(0, 0, 0);
    color_t green = encodeColor(1, 60, 3);
    color_t red = encodeColor(30, 0, 1);
    color_t blue = encodeColor(1,1,60);
    color_t change = encodeColor(30, 0, 1);;

    //draw text
    draw_text(40, 40, "Welcome to my driver!", red);
    draw_text(40, 40 + 16, "Press WASD to move!", green);
    draw_text(40, 40 + 32, "Press 1234 to change the color!",blue);

    char key;
    int x = (640 - 10) / 2;
    int y = (480 - 20) / 2;

    do {
        draw_rect(x, y, 20, 20, black);//clear square

        key = getkey();//red key press

        if (key == 'w') y -= 10;
        else if (key == 's') y += 10;
        else if (key == 'a') x -= 10;
        else if (key == 'd') x += 10;
        else if (key == '1') change = black;
        else if (key == '2') change = green;
        else if (key == '3') change = blue;
        else if (key == '4') change = red;

        draw_rect(x, y, 20, 20, change);//update square location
        sleep_ms(20);
    } while (key != 'q');

    exit_graphics();
    clear_screen();

    return 0;
}
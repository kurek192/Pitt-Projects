/*    WILLIAM KUREK
 *    CS 1550
 *    PROJECT 1
 */

#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <linux/fb.h>
#include <termios.h>
#include "iso_font.h"

#define CLEARCODE "\033[2J"

typedef unsigned short int color_t; 

struct fb_var_screeninfo fb_var;
struct fb_fix_screeninfo fb_fix;
struct termios terminal;
unsigned short * screenMem;
size_t size;
int fid = -1;

void clear_screen() {
    write(1, CLEARCODE, 4);
}

void init_graphics() {
   
    fid = open("/dev/fb0", O_RDWR);

    ioctl(fid, FBIOGET_FSCREENINFO, &fb_fix);
    ioctl(fid, FBIOGET_VSCREENINFO, &fb_var);

    size = fb_fix.line_length * fb_var.yres_virtual;

    screenMem = (unsigned short *) mmap(0, size, PROT_READ | PROT_WRITE, MAP_SHARED, fid, 0); //map screen size into memory

    ioctl(STDIN_FILENO, TCGETS, &terminal);//disable echo/ICANON mode
    terminal.c_lflag &= ~ICANON;
    terminal.c_lflag &= ~ECHO;
    ioctl(STDIN_FILENO, TCSETS, &terminal);

    clear_screen();

}

void exit_graphics() {
    clear_screen();
   
    ioctl(STDIN_FILENO, TCGETS, &terminal); //enable ICANON and ECHO
    terminal.c_lflag |= ICANON;
    terminal.c_lflag |= ECHO;
    ioctl(STDIN_FILENO, TCSETS, &terminal);

    munmap(screenMem, size);//clear mapping
    
    close(fid);//close fid

}

char getkey() {
    char input;
    fd_set fdset;
    struct timeval tv;

    tv.tv_sec = 0;
    tv.tv_usec = 0;

    FD_ZERO(&fdset);
    FD_SET(STDIN_FILENO, &fdset);

    int key = select(STDIN_FILENO + 1, &fdset, NULL, NULL, &tv);

    if (key)
        read(STDIN_FILENO, &input, sizeof (char));

    return input;
}

void sleep_ms(long ms) {
    struct timespec time;
    time.tv_sec = 0;
    time.tv_nsec = ms * 1000000;
    nanosleep(&time, NULL);
}

void draw_pixel(int x, int y, color_t color) {
    *(screenMem + y * fb_var.xres_virtual + x) = color;
}

void draw_rect(int x1, int y1, int width, int height, color_t c) {
    int i;

    for (i = x1; i < x1 + width; i++) {
        int j;
        for (j = y1; j < y1 + height; j++)
            draw_pixel(i, j, c);
    }
}

void draw_char(int x, int y, int a, color_t c) {
    if (a >= ISO_CHAR_MIN || a <= ISO_CHAR_MAX) {
        char ch = 0x01;
        int i;
        int j;
        for (i = 0; i < ISO_CHAR_HEIGHT; i++) {
            char row = iso_font[a * 16 + i]; //find the first 1 byte integer of the character     
            for (j = 0; j < 8; j++) { //isolates one bit for us to use to draw              
                char result = (row & (ch << j)) >> j;
                if ((int) result == 1)
                    draw_pixel(x + j, y + i, c);
            }
        }
    }
}

void draw_text(int x, int y, char *string, color_t c) {
    int i = 0;
    while (string[i] != '\0') {
        draw_char(x, y, string[i], c);
        x += 8;
        i++;
    }
}

color_t encodeColor(int r, int g, int b) {
    color_t col = 0; //upper 5 bits represent the red color  
    col = (col + (r << 11)); //add R to col and shift 11 bits, anything past the leftmost 5 bits will be cut off                                                 
    col = (col + ((g & (0x003f)) << 5)); //mask G for 6 bits, shift 5 bits and then add to col 
    col = (col + (b & 0x001f)); //mask B for 5 bits, add to col

    return col;
}





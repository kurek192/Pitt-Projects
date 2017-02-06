//WILLIAM KUREK
//CS 449
//mymalloctester.c

#include <stdio.h>
#include <unistd.h>
#include "mymalloc.h"

int main() {
    
    //print current brk
    printf("brk: %p\n", sbrk(0));
    printf("Allocating memory: a = 100, b = 200, c = 300, d = 400, e = 500 , f = 600, g = 700, h = 800\n");
    
    //allocate memory
    char *a = my_firstfit_malloc(100);
    char *b = my_firstfit_malloc(200);
    char *c = my_firstfit_malloc(300);
    char *d = my_firstfit_malloc(400);
    char *e = my_firstfit_malloc(500);
    char *f = my_firstfit_malloc(600);
    char *g = my_firstfit_malloc(700);
    char *h = my_firstfit_malloc(800);
    
    //print memory locations
    printf("a address: %p\n", a);
    printf("b address: %p\n", b);
    printf("c address: %p\n", c);
    printf("d address: %p\n", d);
    printf("e address: %p\n", e);
    printf("f address: %p\n", f);
    printf("g address: %p\n", g);
    printf("h address: %p\n", h);
    
    printf("Freeing memory: b = 200, c = 300, d = 400\n");
    
    //free b, c, and d
    my_free(b);
    my_free(c);
    my_free(d);
    
    printf("Allocating memory: i = 900\n");
    
    //allocate memory for i
    char *i = my_firstfit_malloc(900);
    
    //check if first fit was successfully implemented 
    if (i < e && i < f && i < g && i < h) {
        printf("SUCCESS: ");
        printf("i address: %p\n", i);
    } else {
        printf("FAIL\n");
        printf("i address: %p\n", i);
    }
    
    //free remaining memory
    my_free(a);
    my_free(e);
    my_free(f);
    my_free(g);
    my_free(h);
    my_free(i);	

    return 0;
}
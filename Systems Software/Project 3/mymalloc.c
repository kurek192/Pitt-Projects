//WILLIAM KUREK
//CS 449
//mymalloc.c

#include <stdio.h>
#include <unistd.h>
#include "mymalloc.h"

struct NODE {
    struct NODE *next;
    struct NODE *previous;
    int free;
    int size;
};

struct NODE *head;
struct NODE *current;
void *pointer;


//allocates memory using the first-fit algorithm

void *my_firstfit_malloc(int size) {
    
    if (head == NULL) { //if first NODE is null, create first NODE
        head = sbrk((int) sizeof (struct NODE) +size);
        head->previous = NULL;
        head->next = NULL;
        head->free = 1;
        head->size = (int) sizeof (struct NODE) +size;
        pointer = ((void *) head) + sizeof (struct NODE);

        return pointer;
    }

    current = head;
    
    while (current->next != NULL) //find empty space until next is NULL
    {
        //check current NODE for size and free
        if (current->free) {
            current = current->next; //NODE is in use, iterate to next NODE
        } 

	else if ((!current->free) && (current->size < (((int) sizeof (struct NODE) +size)))) {
            current = current->next; //NODE is free, but not of sufficient size, iterate to next NODE
        } 

	else { //NODE is free and of sufficient size        
            if ((current->size - ((int) sizeof (struct NODE) +size)) > sizeof (struct NODE)) { //split the NODE          
                struct NODE *newNode;
                newNode = (struct NODE *) (((void *) current) + ((int) sizeof (struct NODE) +size));
                newNode->previous = current;
                newNode->next = current->next;
                newNode->size = current->size - ((int) sizeof (struct NODE) +size);
                newNode->free = 0;

                pointer = ((void *) current) + sizeof (struct NODE);
                current->next = newNode;
                current->size = ((int) sizeof (struct NODE) +size);
                current->free = 1;
                return pointer;

            } else { //size remaining is less than that of a NODE, so FREE and return the location            
                current->free = 1;
                pointer = ((void *) current) + sizeof (struct NODE);
                return pointer;
            }
        }
    }

    //allocate more memory because the end of the linked list has been reached
    current->next = sbrk(((int) sizeof (struct NODE) +size));
    current->next->previous = current;
    current->next->next = NULL;
    current->next->free = 1;
    current->next->size = ((int) sizeof (struct NODE) +size);
    pointer = ((void *) current->next) + sizeof (struct NODE);

    return pointer;
}

//deallocates a pointer that was originally allocated by *my_firstfit_malloc

void my_free(void *ptr) {

    current = (struct NODE *) (ptr - sizeof (struct NODE));

    if (current->previous == NULL) { //the first NODE is freed if previous is NULL
        if (current->next == NULL) {
            //adjust the heap, since the current NODE is the only free memory
            head = NULL;
            sbrk(-(current->size));
        } else {
            //check next allocation
            if (!(current->next->free)) {
                //set NODE to unallocated, merge sizes, and set next
                current->free = 0;
                current->size += current->next->size;
                current->next = current->next->next;
            } else {
                current->free = 0; //set current as unallocated because next is free
            }
        }

    }
    else if ((current->previous != NULL) && (current->next == NULL)) { //check if last NODE is being freed

        if (!(current->previous->free)) { //check allocation 

            current->previous->size += current->size; //merge
            current = current->previous; //set current back
        }

        //set size, move current back and adjust the heap
        if (current == head) {
            head = NULL;
        } else {
            current->previous->next = NULL;
        }

        sbrk(-(current->size));
    }//if neither next or previous is null, we are freeing a middle NODE
    else {

        if (!(current->previous->free)) { //check allocation of previous
            //previous is unallocated
            current->previous->size += current->size; //merge sizes
            //set current and previous
            current->previous->next = current->next;
            current->next->previous = current->previous;
            current = current->previous;
        }

        //check allocation
        if (!(current->next->free)) {
            //next is unallocated
            current->size += current->next->size; //merge sizes
            //set current and previous
            current->next->next->previous = current;
            current->next = current->next->next;
        }
        //set current allocation to unallocated
        current->free = 0;
    }
}
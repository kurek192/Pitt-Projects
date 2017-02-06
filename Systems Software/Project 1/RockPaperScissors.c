//WILLIAM KUREK
//CS 449
//PROJECT 1
//ROCK,PAPER,SCISSORS

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void start(void); //function prototype declaration
int nextRound(void);
int roundOutcome(int);
int overallOutcome(void);
int victor(char, char);
int gameover(void);

int playerScore; //global variable declaration
int computerScore;
int roundNumber = 0;
char computerChoice[9];

int main(void) {

    char choice[3];

    printf("Welcome to Rock, Paper, Scissors \n");
    while (strcmp(choice, "yes") != 0 && strcmp(choice, "no") != 0) { //loop until user input yes or no
        printf("Would you like to play? ");
        scanf("%s", choice);
    }

    if (strcmp(choice, "no") == 0) { //if no, terminate the program
        return 0;;

    } else { //otherwise, start the game
        start(); 
    }
    return 0;;
}

void start(void) {
    int result = 0;

    while (result == 0) { //loop until zero is returned from roundOutcome
        int roundWinner = nextRound(); //return the winner of the round in the form of 0, -1, or 1
        printf("\nThe computer chose %s.", computerChoice);
        result = roundOutcome(roundWinner); //pass the winner of the round and return the result of the overall results of the tournament
    }
    gameover(); //if the player or computer wins, the gameover() function is called
}

int nextRound(void) {

    int random;
    char choice[8];
    random = rand() % (3 - 1 + 1) + 1; //generates a random number

    roundNumber = roundNumber + 1; //increment the round number

    while ((strcmp(choice, "rock") != 0) && (strcmp(choice, "paper") != 0) && (strcmp(choice, "scissors") != 0)) { //loop until rock, paper, or scissors is inputted
        printf("\nROUND %d:\n ", roundNumber);
        printf("\nWhat is your choice? ");
        scanf("%s", choice);
    }

    if (random == 1) { //generate the computers choice using the random number
        strcpy(computerChoice, "rock");
    } else if (random == 2) {
        strcpy(computerChoice, "paper");
    } else {
        strcpy(computerChoice, "scissors");
    }

    return victor(choice[0], computerChoice[0]); //return the the winner of the round

}

int roundOutcome(int roundWinner) {

    if (roundWinner == -1) { //if -1 is passed through the function, the user loses
        printf(" You LOSE this game!  ");
        computerScore += 1;
    } else if (roundWinner == 1) { //if 1 is passed through the function, the user wins
        printf(" You WIN this game!  ");
        playerScore += 1;
    } else {
        printf(" It's a TIE!"); //if 0 is passed through the function, it's a tie
    }

    return overallOutcome(); //determine the overall score of the game, and return the result back to the function start()
}

int overallOutcome(void) {

    printf("\nThe overall score is now:\n You: %d  Computer: %d \n", playerScore, computerScore);

    //if the player reaches the winning score of 3, return 1
    //if the computer reaches the winning score of 3, return -1
    //otherwise, return 0 and the game continues
    if (computerScore == 3) {  
        printf("\nGAME OVER! THE COMPUTER WON!\n");
        return -1;
    } else if (playerScore == 3) {
        printf("\nGAME OVER! YOU WON!\n");
        return 1;
    } else {
        return 0;
    }
}

int victor(char player, char computer) {

    if (player == 'r') { //takes first char of the player and computer choices to decide the winner of the round
        if (computer == 'r') {
            return 0; //return 0 if tie
        } else if (computer == 'p') {
            return -1;  //return -1 if lose
        } else {
            return 1; //return 1 if win
        }
    }
    if (player == 'p') {
        if (computer == 'r') {
            return 1;
        } else if (computer == 'p') {
            return 0;
        } else {
            return -1;
        }
    }
    if (player == 's') {
        if (computer == 'r') {
            return -1;
        } else if (computer == 'p') {
            return 1;
        } else {
            return 0;
        }
    } 
}

int gameover(void) {

    char choice[3]; //initialize char for string input of size 3

    while (strcmp(choice, "yes") != 0 && strcmp(choice, "no") != 0) { //loop until valid input
        printf("\nWould you like to play again? ");
        scanf("%s", choice);
    }
    if (strcmp(choice, "no") == 0) { //if no, exit the program
        return 0;;
    } else { //otherwise, if yes, reset all scores and round and call the start() function
        playerScore = 0;
        computerScore = 0;
        roundNumber = 0;
        start();
    }
}
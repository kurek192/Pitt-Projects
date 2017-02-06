//WILLIAM KUREK
//CS 449
//PROJECT 4: BLACKJACK

#include <stdio.h>
#include <stdlib.h>

FILE *deck; //deck file declaration
char choice[6]; //input buffer
int playerTotal, dealerTotal; //the hand total for the dealer or player
int playerAces, dealerAces; //the total number of aces in either dealer or players hand
int dealerCard, playerCard; //the current card of the player or dealer
int status; //the game status

//PLAYER FUNCTION
//This is where the user, the player, gets to play the game if the game status = 1

int player() {

    //it is the players turn while the player has not busted and continues to hit
    while (status == 1) {
        printf("The dealer:\n? + %d\n\n", dealerCard);
        playerCard = draw(&playerAces);

        //check if the player drew an ACE and intelligently determine if it should be interpreted as a 1 or an 11  
        if (playerCard == 11) {
            int newTotal = playerTotal + 11;
            if (newTotal > 21) {
                playerAces = playerAces - 1;
                playerCard = 1;
            } else
                playerCard = 11;
        }

        //check if the player drew an ACE and intelligently determine if it should be interpreted as a 1 or an 11    
        if (playerTotal > 21) {
            if (playerAces > 0) {
                playerTotal -= 10;
                playerAces = playerAces - 1;
                printf("\nYou had an Ace! Your current hand equals: %d", playerTotal);
            }
        }

        printf("You:\n%d + %d = %d ", playerTotal, playerCard, playerTotal + playerCard);
        playerTotal += playerCard;

        //check if the player drew an ACE and intelligently determine if it should be interpreted as a 1 or an 11
        if (playerTotal > 21) {
            if (playerAces > 0) {
                playerTotal -= 10;
                playerAces = playerAces - 1;
                printf("\nYou had an Ace! Your current hand equals: %d", playerTotal);
                status = 1;
            } else {
                status = -1; //set status to -1 if the player busted
            }
        }

        if (status == -1) { //player went over 21, set status = -1, they busted
            printf("BUSTED!");
        }

        printf("\n\n");

        //if status = 1, continue player turn by asking to hit or stand
        if (status == 1) {
            choice[0] = '0';
            while ((strcmp(choice, "hit") != 0) && (strcmp(choice, "stand") != 0)) { //loop while the input is not "hit" or "stand"
                printf("Would you like to \"hit\" or \"stand\"?: ");
                scanf("%s", choice);
                printf("\n");
            }

            if (strcmp(choice, "hit")) //if the player stands, status = 0 and their turn is over
                status = 0;
        }
    }
}


//DEALER FUNCTION
//This is where the dealer gets to play the game if the game status = 1 

int dealer() {

    printf("It's the dealers turn to play...\n\n");

    //print the dealers current hand revealing the face down card
    printf("The dealer:\n%d + %d = %d\n\n", dealerTotal, dealerCard, dealerTotal + dealerCard);
    dealerTotal += dealerCard;

    //determine whether the dealer should hit or stand
    if (dealerTotal >= 17) {
        status = 0;
    } else {
        status = 1;
    }

    //if not, the dealer continues to play
    while (status == 1) {

        //draw a dealer card and intelligently determine if it should be interpreted as a 1 or an 11 
        dealerCard = draw(&dealerAces);
        if (dealerCard == 11) {
            int newTotal = dealerTotal + 11;
            if (newTotal > 21) {
                dealerAces = dealerAces - 1;
                dealerCard = 1;
            } else
                dealerCard = 11;
        }

        printf("The dealer:\n%d + %d = %d\n\n", dealerTotal, dealerCard, dealerTotal + dealerCard);
        dealerTotal += dealerCard;

        //check if the dealer busted 
        //check for aces and intelligently determine if it should be interpreted as a 1 or an 11 
        if (dealerTotal > 21) {
            if (dealerAces > 0) {
                dealerTotal -= 10;
                dealerAces = dealerAces - 1;
            } else {
                status = -1; //the dealer busted
            }
        } else if (dealerTotal >= 17) { //determine whether the dealer should hit or stand
            status = 0; //the dealer stands
        } else {
            status = 1; //the dealer hits
        }
    }
}

//DRAW FUNCTION
//This function draws a card from the /dev/cards driver

int draw(int *a) {

    char card[1];
    int value;
    fread(card, 1, 1, deck); //read the driver and return a card
    card[0] = (card[0] % 13); //mod 13 to convert all values to 0-12

    if (card[0] == 0) { //0 = ACE
        *a = *a + 1;
        value = 11;
    } else if (card[0] == 10) { //10 = JACK
        value = 10;
    } else if (card[0] == 11) { //11 = QUEEN
        value = 10;
    } else if (card[0] == 12) { //12 = KING
        value = 10;
    } else {
        value = card[0] + 1; //1 = 2, 2 = 3, 3 = 4, 4 = 5 , 5 = 6, 6 = 7, 7 = 8, 8 = 9, 9 = 10
    }
    return value; // return the drawn card
}

//MAIN FUNCTION
//opens the deck and prepares the program to play
//once the player and dealer have played, determine the winner

int main() {

    //open the /dev/cards driver
    deck = fopen("/dev/cards", "r");
    //set the game status = 1
    status = 1;

    //draw two dealer cards, and one player card
    dealerTotal = draw(&dealerAces);
    dealerCard = draw(&dealerAces);
    playerTotal = draw(&playerAces);

    //player plays first, call player function
    player();

    //determine whether the player busted, and if the dealer should play
    if (status == -1) {
        printf("You busted. Dealer wins.\n");
        return 0;
    } else {
        dealer(); //if the player did not bust, call the dealer function
    }

    //if status = 0, the game is over and both the dealer and the player did not bust
    //determine the winner
    if (status == 0) {
        if (playerTotal > dealerTotal) {
            if (playerTotal == 21) {//if the players hand = 21 then it is a blackjack
                printf("BLACKJACK! ");
            }
            printf("You won!\nDEALER: %d YOU: %d\n", dealerTotal, playerTotal);
        } else {
            printf("You lost!\nDEALER: %d YOU: %d\n", dealerTotal, playerTotal);
        }

    } else {
        printf("Dealer Busted. You win!\n");
    }

    return 0; //terminate the program

}

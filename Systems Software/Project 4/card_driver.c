//WILLIAM KUREK
//PROJECT 4 - CARD DRIVER

#include <linux/fs.h>
#include <linux/init.h>
#include <linux/miscdevice.h>
#include <linux/module.h>
#include <asm/uaccess.h>
#include <linux/random.h>

static unsigned char cards[52]; //the deck array
static unsigned char index; //keep track of the index to make sure deck is not exhausted

char get_random_byte(int max) {
    char c;
    get_random_bytes(&c, 1);
    c = (c < 0) ? 0 - c : c;
    return c % max;
}

static ssize_t card_read(struct file * file, char * buf,
        size_t count, loff_t *ppos) {

    unsigned char card, temp, n;
    int i, j, k;

    //if driver has just been initiated or deck has been exhausted, start new deck and shuffle
    for (i = 0; i < count; i++) {
        if (index > 51) {

            //start new deck
            for (j = 0; j < 52; j++) {
                cards[j] = j;
            }

            //shuffle array of cards using get_random_byte for random indices 
            for (k = 51; k > 0; k--) {
                n = get_random_byte(k + 1);
                temp = cards[k];
                cards[k] = cards[n];
                cards[n] = temp;
            }

            index = 0;
        }

        //return a card while incrementing the index
        card = cards[index++];
        if(copy_to_user(buf, &card, 1)){
		return -EINVAL;
	} 
        buf++;
        *ppos = *ppos + 1;
    }
    return count;
}



static const struct file_operations card_fops = {
    .owner = THIS_MODULE,
    .read = card_read,
};

static struct miscdevice card_dev = {

    MISC_DYNAMIC_MINOR,

    "card_driver",

    &card_fops
};

static int __init
card_init(void) {

    int ret;
    index = 52;

    ret = misc_register(&card_dev);
    if (ret)
        printk(KERN_ERR
            "Unable to register cards device\n");
    return ret;
}

module_init(card_init);

static void __exit
card_exit(void) {
    misc_deregister(&card_dev);
}

module_exit(card_exit);

MODULE_LICENSE("GPL");
MODULE_VERSION("dev");
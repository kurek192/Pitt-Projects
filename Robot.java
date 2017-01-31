//ROBOT CLASS
//HOLDS AND SETS LOCATION OF THE ROBOT
public class Robot {

        private int x;
        private int y;
        private int oldx;
        private int oldy;

        public Robot() {
            this.x = 1;
            this.y = 1;
        }

        public void moveNW() {
            this.x--;
            this.y++;

        }

        public void moveN() {

            this.y++;
        }

        public void moveNE() {

            this.x++;
            this.y++;
        }

        public void moveE() {

            this.x++;
        }

        public void moveSE() {

            this.x++;
            this.y--;
        }

        public void moveS() {

            this.y--;
        }

        public void moveSW() {
     
            this.x--;
            this.y--;
        }

        public void moveW() {
       
            this.x--;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
        
        public int getOldX() {
            return this.oldx;
        }

        public int getOldY() {
            return this.oldy;
        }
        
        public void setOld(int x, int y){
            this.oldx = x;
            this.oldy = y;
        }
        
        public void setLocation(int x, int y){
            this.x = x;
            this.y = y;
        }

    }
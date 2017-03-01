/**
 * Created by rhyspatten on 18/02/17.
 */
public class minimax_deepRedYellow extends AIModule
{

    public int eval(final GameStateModule game, int playerID){
        int altPlayer;
        if(playerID == 2){
            altPlayer = 1;
        }else{
            altPlayer = 2;
        }
        if(game.isGameOver()){
            if(game.getWinner() == playerID){
                return Integer.MAX_VALUE;
            }else if(game.getWinner() == altPlayer){
                return Integer.MIN_VALUE;
            }else{
                return 0;
            }
        }
        int score = 0;
        int[] getat = new int[43];
        for(int i=0; i<6; i++){
            for(int j=0; j<7; j++){
                getat[i*6+j] = game.getAt(j,i);
            }
        }
        //horizonal 3s
        for(int i=0; i<game.getHeight(); i++){
            for(int j=0; j<game.getWidth()-3; j++){
                int count =0;
                if(getat[i*6+j]== playerID){
                    count++;
                }
                if(getat[i*6+j+1]== playerID){
                    count++;
                }
                if(getat[i*6+j+2]== playerID){
                    count++;
                }
                if(getat[i*6+j+3]== playerID){
                    count++;
                }
                if(getat[i*6+j]== altPlayer){
                    count--;
                }
                if(getat[i*6+j+1]== altPlayer){
                    count--;
                }
                if(getat[i*6+j+2]== altPlayer){
                    count--;
                }
                if(getat[i*6+j+3]== altPlayer){
                    count--;
                }

                if(count == 3){
                    score++;
                }else if(count == -3){
                    score--;
                }
            }
        }
        //vertical 3s
        for(int i=0; i<game.getHeight()-3; i++){
            for(int j=0; j<game.getWidth(); j++){
                int count =0;
                if(getat[i*6+j]== playerID){
                    count++;
                }
                if(getat[(i+1)*6+j]== playerID){
                    count++;
                }
                if(getat[(i+2)*6+j]== playerID){
                    count++;
                }
                if(getat[(i+3)*6+j]== playerID){
                    count++;
                }
                if(getat[i*6+j]== altPlayer){
                    count--;
                }
                if(getat[(i+1)*6+j]== altPlayer){
                    count--;
                }
                if(getat[(i+2)*6+j]== altPlayer){
                    count--;
                }
                if(getat[(i+3)*6+j]== altPlayer){
                    count--;
                }

                if(count == 3){
                    score++;
                }else if(count == -3){
                    score--;
                }
            }
        }
        //diagonal bot left to top right 3s
        for(int i=0; i<game.getHeight()-3; i++) {
            for (int j = 0; j < game.getWidth() - 3; j++) {
                int count =0;
                if(getat[i*6+j]== playerID){
                    count++;
                }
                if(getat[(i+1)*6+j+1]== playerID){
                    count++;
                }
                if(getat[(i+2)*6+j+2]== playerID){
                    count++;
                }
                if(getat[(i+3)*6+j+3]== playerID){
                    count++;
                }
                if(getat[i*6+j]== altPlayer){
                    count--;
                }
                if(getat[(i+1)*6+j+1]== altPlayer){
                    count--;
                }
                if(getat[(i+2)*6+j+2]== altPlayer){
                    count--;
                }
                if(getat[(i+3)*6+j+3]== altPlayer){
                    count--;
                }

                if(count == 3){
                    score++;
                }else if(count == -3){
                    score--;
                }
            }
        }
        //diagonal bot right to top left 3s
        for(int i=3; i<game.getHeight(); i++) {
            for (int j = 0; j < game.getWidth() - 3; j++) {
                int count =0;
                if(getat[i*6+j]== playerID){
                    count++;
                }
                if(getat[(i-1)*6+j+1]== playerID){
                    count++;
                }
                if(getat[(i-2)*6+j+2]== playerID){
                    count++;
                }
                if(getat[(i-3)*6+j+3]== playerID){
                    count++;
                }
                if(getat[i*6+j]== altPlayer){
                    count--;
                }
                if(getat[(i-1)*6+j+1]== altPlayer){
                    count--;
                }
                if(getat[(i-2)*6+j+2]== altPlayer){
                    count--;
                }
                if(getat[(i-3)*6+j+3]== altPlayer){
                    count--;
                }

                if(count == 3){
                    score++;
                }else if(count == -3){
                    score--;
                }
            }
        }
        return score;
    }
    long startTime;
    long finishThreshold;
    public void getNextMove(final GameStateModule game)
    {
        startTime = System.nanoTime();
        finishThreshold = 499000000; //get it as close to 500ms as possible
        int bestScore = Integer.MIN_VALUE;

        int depth = 6;

        for (int i = 0; i < game.getWidth(); i++) {
            GameStateModule game2 = game.copy();
            if(game.canMakeMove(i)) {
                game2.makeMove(i);
                int mm = minmax(game2, depth, 0, game.getActivePlayer());
                if (mm > bestScore) {
                    bestScore = mm;
                    chosenMove = i;
                }else if (mm == bestScore && i == 3) {
                    bestScore = mm;
                    chosenMove = i;
                }
            }
        }
    }


    public int minmax(GameStateModule game, int depth, int maxPlayer, int Pid){
        if(!(System.nanoTime() - startTime < finishThreshold)){
            return 0;
        }
        if(depth == 0 || game.isGameOver()){
            return eval(game, Pid);
        }
        if(maxPlayer == 1){
            int bestVal = Integer.MIN_VALUE;
            for (int i = 0; i < game.getWidth(); i++) {
                if(game.canMakeMove(i)) {
                    GameStateModule child = game.copy();
                    child.makeMove(i);
                    int v = minmax(child, depth - 1,0, Pid);
                    bestVal = Math.max(bestVal, v);
                }
            }
            return bestVal;
        }else{
            int bestVal = Integer.MAX_VALUE;
            for (int i = 0; i < game.getWidth(); i++) {
                if(game.canMakeMove(i)) {
                    GameStateModule child = game.copy();
                    child.makeMove(i);
                    int v = minmax(child, depth - 1,1, Pid);
                    bestVal = Math.min(bestVal, v);
                }
            }
            return bestVal;
        }
    }
}
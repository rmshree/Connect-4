import java.util.Random;

/**
 * Created by rhyspatten on 18/02/17.
 */
public class MMAI extends AIModule
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
            }else{
                return Integer.MIN_VALUE;
            }
        }
        int score = 0;
        //horizonal 3s
        for(int i=0; i<game.getHeight(); i++){
            for(int j=0; j<game.getWidth()-2; j++){
                if(game.getAt(j,i) == playerID && game.getAt(j+1,i) == playerID && game.getAt(j+2,i) == playerID){
                    score++;
                }else if(game.getAt(j,i) == altPlayer && game.getAt(j+1,i) == altPlayer && game.getAt(j+2,i) == altPlayer ){
                    score--;
                }
            }
        }
        //vertical 3s
        for(int i=0; i<game.getWidth(); i++){
            for(int j=0; j<game.getHeight()-2; j++){
                if(game.getAt(j,i) == playerID && game.getAt(j,i+1) == playerID && game.getAt(j,i+2) == playerID){
                    score++;
                }else if(game.getAt(j,i) == altPlayer && game.getAt(j,i+1) == altPlayer && game.getAt(j,i+2) == altPlayer){
                    score--;
                }
            }
        }
        //diagonal bot left to top right 3s
        for(int i=0; i<game.getWidth()-2; i++) {
            for (int j = 0; j < game.getHeight() - 2; j++) {
                if (game.getAt(j, i) == playerID && game.getAt(j+1, i + 1) == playerID && game.getAt(j+2, i + 2) == playerID) {
                    score++;
                } else if (game.getAt(j, i) == altPlayer && game.getAt(j+1, i + 1) == altPlayer && game.getAt(j+2, i + 2) == altPlayer)  {
                    score--;
                }
            }
        }
        //diagonal bot right to top left 3s
        for(int i=2; i<game.getWidth(); i++) {
            for (int j = 0; j < game.getHeight() - 2; j++) {
                if (game.getAt(j, i) == playerID && game.getAt(j+1, i - 1) == playerID && game.getAt(j+2, i - 2) == playerID) {
                    score++;
                } else if (game.getAt(j, i) == altPlayer && game.getAt(j+1, i - 1) == altPlayer && game.getAt(j+2, i - 2) == altPlayer) {
                    score--;
                }
            }
        }
        return score;
    }

    public void getNextMove(final GameStateModule game)
    {
        int bestScore = Integer.MIN_VALUE;
        for(int i = 0; i < game.getWidth(); i++) {
            GameStateModule game2 = game.copy();
            if (game.canMakeMove(i)) {
                game2.makeMove(i);
                int mm = minmax(game2, 8, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, game.getActivePlayer());
                if(mm > bestScore){
                    bestScore = mm;
                    chosenMove = i;
                }
            }
        }
    }

    public int minmax(GameStateModule game, int depth, int alpha, int beta, int maxPlayer, int Pid){

        if(depth == 0 || game.isGameOver()){
            return eval(game, Pid);
        }
        if(maxPlayer == 1){
            int bestVal = Integer.MIN_VALUE;
            for(int i = 0; i < game.getWidth(); i++) {
                if(game.canMakeMove(i)) {
                    GameStateModule child = game.copy();
                    child.makeMove(i);
                    int v = minmax(child, depth - 1, alpha, beta, 0, Pid);
                    bestVal = Math.max(bestVal, v);
                    alpha = Math.max(alpha,bestVal);
                    if(beta <= alpha){
                        break;
                    }
                }
            }
            return bestVal;
        }else{
            int bestVal = Integer.MAX_VALUE;
            for(int i = 0; i < game.getWidth(); i++) {
                if(game.canMakeMove(i)) {
                    GameStateModule child = game.copy();
                    child.makeMove(i);
                    int v = minmax(child, depth - 1, alpha, beta, 1, Pid);
                    bestVal = Math.min(bestVal, v);
                    beta = Math.min(beta, bestVal);
                    if(beta <= alpha){
                        break;
                    }
                }
            }
            return bestVal;
        }
    }
}
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.*;

/**
 * Created by rhyspatten on 18/02/17.
 */
public class MMAI extends AIModule
{

    HashMap<Long, Integer> transpositionTable;

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
            for(int j=0; j<game.getWidth()-2; j++){
                if(getat[i*6+j]== playerID && getat[i*6+j+1] == playerID && getat[i*6+j+2] == playerID){
                    score++;
                }else if(getat[i*6+j] == altPlayer && getat[i*6+j+1] == altPlayer && getat[i*6+j+2] == altPlayer ){
                    score--;
                }
            }
        }
        //vertical 3s
        for(int i=0; i<game.getWidth(); i++){
            for(int j=0; j<game.getHeight()-2; j++){
                if(getat[i*6+j] == playerID && getat[(i+1)*6+j] == playerID && getat[(i+2)*6+j] == playerID){
                    score++;
                }else if(getat[i*6+j] == altPlayer && getat[(i+1)*6+j] == altPlayer && getat[(i+2)*6+j] == altPlayer){
                    score--;
                }
            }
        }
        //diagonal bot left to top right 3s
        for(int i=0; i<game.getWidth()-2; i++) {
            for (int j = 0; j < game.getHeight() - 2; j++) {
                if (getat[i*6+j] == playerID && getat[(i+1)*6+j+1] == playerID && getat[(i+2)*6+j+2] == playerID) {
                    score++;
                } else if (getat[i*6+j] == altPlayer && getat[(i+1)*6+j+1]  == altPlayer && getat[(i+2)*6+j+2] == altPlayer)  {
                    score--;
                }
            }
        }
        //diagonal bot right to top left 3s
        for(int i=2; i<game.getWidth(); i++) {
            for (int j = 0; j < game.getHeight() - 2; j++) {
                if (getat[i*6+j] == playerID && getat[(i-1)*6+j+1]  == playerID && getat[(i-2)*6+j+2] == playerID) {
                    score++;
                } else if (getat[i*6+j] == altPlayer && getat[(i-1)*6+j+1]== altPlayer && getat[(i-2)*6+j+2] == altPlayer) {
                    score--;
                }
            }
        }
        return score;
    }
    long[][][] zobrist;
    public void getNextMove(final GameStateModule game)
    {
        int bestScore = Integer.MIN_VALUE;
        transpositionTable = new HashMap<Long, Integer>();
        zobrist = new long[7][6][2];
        Random randomno = new Random();
        for(int i=0; i<6; i++)
            for(int j=0; j<7; j++) {
                zobrist[j][i][0] = randomno.nextLong();
                zobrist[j][i][1] = randomno.nextLong();
            }
        for(int i = 0; i < game.getWidth(); i++) {
            GameStateModule game2 = game.copy();
            if (game.canMakeMove(i)) {
                game2.makeMove(i);
                int mm = minmax(game2, 8, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, game.getActivePlayer(), getZHash(game2));
                if(mm > bestScore){
                    bestScore = mm;
                    chosenMove = i;
                }
            }
        }
    }

    long getZHash(GameStateModule game){
        long hashkey = 0;
        for(int i=0; i<7; i++){
            for(int j=0; j<6; j++){
                int at = game.getAt(i,j);
                if(at == 1 || at==2){
                    hashkey ^=zobrist[i][j][at-1];
                }
            }
        }
        return hashkey;
    }

    long updateZkey(long oldKey, int moveX, int moveY, int pid) {
        return oldKey ^ zobrist[moveX][moveY][pid];
    }

    public int minmax(GameStateModule game, int depth, int alpha, int beta, int maxPlayer, int Pid, long curZkey){

        if(depth == 0 || game.isGameOver()){
            return eval(game, Pid);
        }
        if(maxPlayer == 1){
            int bestVal = Integer.MIN_VALUE;
            for(int i = 0; i < game.getWidth(); i++) {
                if(game.canMakeMove(i)) {
                    GameStateModule child = game.copy();

                    child.makeMove(i);
                    int y = game.getHeightAt(i);
                    // the move just make is at i,y
                    curZkey = updateZkey(curZkey, i, y, child.getAt(i,y)-1);
//                    curZkey = getZHash(child);
                    int v;
                    if(transpositionTable.containsKey(curZkey)){
                        v = transpositionTable.get(curZkey);
                    }else{
                        v = minmax(child, depth - 1, alpha, beta, 0, Pid, curZkey);
                        transpositionTable.put(curZkey, v);
                    }
                    curZkey = updateZkey(curZkey, i, y, child.getAt(i,y)-1);
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
                    int y = game.getHeightAt(i);// the move just make is at i,y
                    int v;
                    curZkey = updateZkey(curZkey, i, y, child.getAt(i,y)-1);
//                    curZkey = getZHash(child);
                    if(transpositionTable.containsKey(curZkey)){
                        v = transpositionTable.get(curZkey);
                    }else{
                        v = minmax(child, depth - 1, alpha, beta, 1, Pid, curZkey);
                        transpositionTable.put(curZkey, v);
                    }
                    curZkey = updateZkey(curZkey, i, y, child.getAt(i,y)-1);
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by rhyspatten on 18/02/17.
 */
public class alphabeta_patten extends AIModule
{

    HashMap<Long, Integer> transpositionTable;
    HashMap<Long, Integer> orderingTable;
    boolean first;

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
        //horizontal 3s
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
    long[][][] zobrist;
    long startTime;
    long finishThreshold;
    public void getNextMove(final GameStateModule game)
    {
        startTime = System.nanoTime();
        finishThreshold = 499000000; //get it as close to 500ms as possible
        int bestScore = Integer.MIN_VALUE;
        first = true;

        zobrist = new long[7][6][2];
        Random randomno = new Random();
        for(int i=0; i<6; i++) {
            for (int j = 0; j < 7; j++) {
                zobrist[j][i][0] = randomno.nextLong();
                zobrist[j][i][1] = randomno.nextLong();
            }
        }
        int depth = 8;
        int nextChosen = 0;

        while(System.nanoTime() - startTime < finishThreshold && depth < 42) {
            transpositionTable = new HashMap<Long, Integer>();//reset table for next depth
//            System.out.print("trying "+depth+"\n");
            boolean done = true;
            for (int i = 0; i < game.getWidth(); i++) {
                GameStateModule game2 = game.copy();
                if (game.canMakeMove(i) && System.nanoTime()-startTime<finishThreshold) {
                    game2.makeMove(i);
                    int mm = minmax(game2, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, game.getActivePlayer(), getZHash(game2));
                    if (mm > bestScore) {
                        bestScore = mm;
                        nextChosen = i;
                    } else if (mm == bestScore && i == 3) {
                        bestScore = mm;
                        nextChosen = i;
                    }
                    if(mm == Integer.MAX_VALUE){
                        chosenMove = i;
                        return;//if we can win, do so and finish the game
                    }
                }
                if(!(System.nanoTime()-startTime<finishThreshold)){
                    done = false;
//                    System.out.print("failed to do "+depth+"\n");
                }
            }
            if(done) {
                chosenMove = nextChosen;
            }
            orderingTable = transpositionTable;//remember the table to do ordering at the next depth!
            first = false;
            depth++;

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
        if(!(System.nanoTime() - startTime < finishThreshold)){
            return 0;
        }

        if(depth == 0 || game.isGameOver()){
            return eval(game, Pid);
        }
        if(maxPlayer == 1){
            int bestVal = Integer.MIN_VALUE;
            double[] orderedMove = new double[game.getWidth()];
            HashMap<Double, Integer> map = new HashMap<>();
            ArrayList<Double> scores = new ArrayList<>();
            for(int i=0; i < game.getWidth(); i++) {
                GameStateModule child = game.copy();
                double score;
                if(game.canMakeMove(i)) {
                    child.makeMove(i);
                    if(first) {
                        score = eval(child, Pid);
                    }else{
                        int y = game.getHeightAt(i);
                        long newkey = updateZkey(curZkey, i, y, child.getAt(i, y) - 1);
                        if(orderingTable.containsKey(newkey)) {
                            score = orderingTable.get(newkey);//sort based on the last depth search
                        }else{
                            score = eval(child, Pid);
                        }
                    }
                }else{
                    score = (double)Integer.MIN_VALUE;
                }
                while(scores.contains(score)){
                    score+=0.1;//make them unique
                }
                map.put(score,i);
                orderedMove[i] = score;
                scores.add(score);
            }
            Arrays.sort(orderedMove);
            ArrayList<Integer> moves = new ArrayList<>();
            for(int i=game.getWidth()-1; i>=0; i--){
                moves.add(map.get(orderedMove[i]));
            }
            for(int i : moves) {
                if(game.canMakeMove(i)) {
                    GameStateModule child = game.copy();
                    child.makeMove(i);
                    int y = game.getHeightAt(i);
                    // the move just made is at i,y
                    curZkey = updateZkey(curZkey, i, y, child.getAt(i,y)-1);
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
            double[] orderedMove = new double[game.getWidth()];
            HashMap<Double, Integer> map = new HashMap<>();
            ArrayList<Double> scores = new ArrayList<>();
            for(int i=0; i < game.getWidth(); i++) {
                GameStateModule child = game.copy();
                double score;
                if(game.canMakeMove(i)) {
                    child.makeMove(i);
                    if(first) {
                        score = eval(child, Pid);
                    }else{
                        int y = game.getHeightAt(i);
                        long newkey = updateZkey(curZkey, i, y, child.getAt(i, y) - 1);
                        if(orderingTable.containsKey(newkey)) {
                            score = orderingTable.get(newkey);//sort based on the last depth search
                        }else{
                            score = eval(child, Pid);
                        }
                    }
                }else{
                    score = (double)Integer.MIN_VALUE;
                }
                while(scores.contains(score)){
                    score+=0.1;//make them unique
                }
                map.put(score,i);
                orderedMove[i] = score;
                scores.add(score);
            }
            Arrays.sort(orderedMove);
            ArrayList<Integer> moves = new ArrayList<>();
            for(int i=0; i<game.getWidth(); i++){
                moves.add(map.get(orderedMove[i]));
            }

            for(int i : moves) {
                if(game.canMakeMove(i)) {
                    GameStateModule child = game.copy();
                    child.makeMove(i);
                    int y = game.getHeightAt(i);// the move just made is at i,y
                    int v;
                    curZkey = updateZkey(curZkey, i, y, child.getAt(i,y)-1);
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
import java.util.*;
import java.awt.Point;

public class MiniMax extends AIModule {
    private int our_player; //to keep track of the current player

    public ArrayList<Point> player1 = new ArrayList<Point>();
    public ArrayList<Point> player2 = new ArrayList<Point>();


    //chooses the move that leads to the highest expected value.
    @Override
    public void getNextMove(final GameStateModule state) {
        int i = 1;
        final GameStateModule game = state.copy();
        this.our_player = game.getActivePlayer();
        while (!terminate) {
            System.out.print("\n\ndepth: " + i + "\n\n");
            //need iterative deepening minimax function here
            MiniMaxDecision(game, i);
            i++; //depth
        }
    }

    /// Drops a coin into column x for the active player.
    public int makeMove(final GameStateModule game, int column) {
        // store the move to a arraylist. Might need for eval? Also unmake if needed..
        int row = game.getHeightAt(column) - 1;
        if (game.getActivePlayer() == 1) {
            this.player1.add(new Point(row, column));
        } else {
            this.player2.add(new Point(row, column));
        }
        System.out.print("\ncolumn: " + column + " row: " + row);
        game.makeMove(column);
        return row;
    }

    /// Undo the last played move.
    public void unmakeMove(final GameStateModule game, int column, int row){
        game.unMakeMove();
        if (game.getActivePlayer() == 1){
            this.player1.remove(new Point(row,column));
        }else{
            this.player2.remove(new Point(row,column));
        }
    }


    public int minValue(final GameStateModule game, int depth){
        int util = Integer.MAX_VALUE;
        final int[] moves = new int[game.getWidth()];
        int min;

        // Fill in what moves are legal.
        int numLegalMoves = 0;
        for(int i = 0; i < game.getWidth(); ++i)
            if(game.canMakeMove(i))
                moves[numLegalMoves++] = i;

        for (int i = 0; i < numLegalMoves && !terminate; i++){
            int row = makeMove(game,moves[i]);
            min = maxValue(game, depth - 1);
            if (min <= util){
                util = min;
            }
            unmakeMove(game, moves[i],row);
        }
            return util;
    }//minVal

    public int maxValue(final GameStateModule game, int depth) {
        int util = -1*Integer.MAX_VALUE;
        final int[] moves = new int[game.getWidth()];
        int max;

        // Fill in what moves are legal.
        int numLegalMoves = 0;
        for(int i = 0; i < game.getWidth(); ++i)
            if(game.canMakeMove(i))
                moves[numLegalMoves++] = i;

        for (int i = 0; i < numLegalMoves && !terminate; i++){
            int row = makeMove(game,moves[i]);
            max = minValue(game, depth - 1);
            if (max > util){
                util = max;
            }
            unmakeMove(game, moves[i],row);
        }
            return util;
    }//maxVal

    public void MiniMaxDecision(final GameStateModule game, int depth) {
        int util = -1*Integer.MAX_VALUE;
        final int[] moves = new int[game.getWidth()];
        int max;

        // Fill in what moves are legal.
        int numLegalMoves = 0;
        for(int i = 0; i < game.getWidth(); ++i)
            if(game.canMakeMove(i))
                moves[numLegalMoves++] = i;

        // For each possible action, returns the maximum value given depth
        // update result if the new result is greater than the previous calculated max
    }
}
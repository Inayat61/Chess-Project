/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chess.example.player;

import com.chess.example.board.Alliance;
import com.chess.example.board.Board;
import com.chess.example.board.Move;
import com.chess.example.pieces.King;
import com.chess.example.pieces.Piece;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.carrot2.shaded.guava.common.collect.ImmutableList;
import org.carrot2.shaded.guava.common.collect.Iterables;

/**
 *
 * @author ADMIN
 */
public abstract class Player {
// attack on king, watching the  moves of opponent and if its overlap kings positions then it is in check
    protected static List<Move> calculateAttacksOnTitles(int piecePosition, List<Move> moves) {
        final List<Move> attackMoves=new ArrayList<>();
        for(final Move move: moves){
            if(piecePosition==move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        
    return ImmutableList.copyOf(attackMoves);
            }
    
    protected final Board board;
    final King playerKing;
    protected final List<Move> legalMoves;
   private final boolean isInCheck;
   private int index=0;
    
    Player(final Board board,final List<Move> legalMoves,final List<Move> opponentMove){
        this.board=board;
        this.playerKing=establishKing();
        this.legalMoves= ImmutableList.copyOf(Iterables.concat(legalMoves,CalculateKingCastles(legalMoves, opponentMove)));
        this.isInCheck=!Player.calculateAttacksOnTitles(this.playerKing.getPiecePosition(),opponentMove).isEmpty();
    }
    
    public final King establishKing() {
       try{
            Piece piece1=null;
        for( Piece piece: getActivePiece()){
            if(piece.getPieceType().isKing()){
               
                 piece1=piece;
                return (King) piece;
               
            }
                //  System.out.println(piece+" after if");
        }
       
        System.out.println(piece1+" after if");}
       catch(Exception e){
           System.out.println(e);
       }
      //  throw new RuntimeException("Not a Valid Board!!!!!!");
 return null;   }
    public  boolean junk()
    {
        return King.isInCheck(this.board);
    }
    public King getPlayerKing(){
        return this.playerKing;
    }
    public int getKingPosition(){
        return this.playerKing.getPiecePosition();
    }
    public List<Move> getLegalMove(){
        return this.legalMoves;
    }
    public boolean isMoveLegal(final Move move){
       return this.legalMoves.contains(move);
    }
    public boolean isInCheck(){
    
    return this.isInCheck;
    }
    public boolean isInCheckMade(){
    return this.isInCheck && !hasEscapeMoves();
    }
    // u are not in check but u cannot move to any position cuz u move u are in check.
    public boolean isToStaleMade(){
    return !this.isInCheck && !hasEscapeMoves();
    }
    public boolean isCastled(){
    return false;
    }
    public MoveTransition makeMove(final Move move){
        // if it is not a part of legal moves a player then 
        //move transition doesnot led us to the  new board but it return thhe same board as alegal.
        if(!isMoveLegal(move)){
         //   System.out.println("makemove function inside if");
            return new MoveTransition(this.board, move,MoveStatus.ILLEGAL_MOVE);
        }
       // System.out.println("makemove function outside if");
        // if condition fail then then execute the method of new board and 
        //check is their any attack on current player king , if yes then u cannot make a move
        final Board transitionBoard=move.execute();
        // after the execution there is no current execution player
        //but we get the king position and then check it is harming king or not 
        final List<Move> kingAttack=Player.calculateAttacksOnTitles(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                                                                    transitionBoard.currentPlayer().getLegalMove());
        if(!kingAttack.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVE_PLAYER_INCHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    protected boolean hasEscapeMoves() {
        for(final Move move: this.legalMoves){
            final MoveTransition transition =makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
      return false;  
    }
   
    
    public abstract Collection<Piece> getActivePiece();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract List<Move> CalculateKingCastles(List<Move> playerLegals,List<Move> opponentLegals);
    
    
}

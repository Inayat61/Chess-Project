/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chess.example.pieces;

import com.chess.example.board.Alliance;
import com.chess.example.board.Board;
import com.chess.example.board.BoardUtils;
import com.chess.example.board.Move;
import com.chess.example.board.Move.AttackMove;
import com.chess.example.board.Move.MajorAttackMove;
import com.chess.example.board.Move.MajorMove;
import com.chess.example.board.Tile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

/**
 *
 * @author ADMIN
 */
public class Knight extends Piece {
    private final static int [] CANDIDATE_MOVE_CORDINATE={-17,-15,-6,6,15,17,-10,10};
    public Knight( Alliance pieceAlliance,
                   final int piecePosition) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance,true);
    }
    
        public Knight( Alliance pieceAlliance,
                   final int piecePosition,final boolean isFirstMove) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance,isFirstMove);
    }


    @Override
    public List<Move> calculateLegalMove(final Board board) {
      //  int candidateDestinationCordinate;
        final List<Move> legalMoves=new ArrayList<>();
        
        for(final int currentCandidate: CANDIDATE_MOVE_CORDINATE){
               final int candidateDestinationCordinate=this.piecePosition+currentCandidate;
               
               if( BoardUtils.isValidTileCordinate(candidateDestinationCordinate)){
                   if( isFirstColumnExclusions(this.piecePosition, currentCandidate)||
                          isSecondColumnExclusions(this.piecePosition, currentCandidate)||
                          isSevenColumnExclusions(this.piecePosition, currentCandidate)||
                          isEightColumnExclusions(this.piecePosition, currentCandidate)){
                       continue;
                   }
                   final Tile candidateDestinationTile=board.getTile( candidateDestinationCordinate);
                   if(! candidateDestinationTile.isTileOccupied()){
                       legalMoves.add(new MajorMove(board,this,candidateDestinationCordinate));
                   }
                   else{
                       final Piece pieceDestination=candidateDestinationTile.getPiece();
                       final Alliance pieceAlliance=pieceDestination.getPieceAlliance();
                       //Ememy piece is placed there
                       if(this.pieceAlliance!=pieceAlliance){
                           legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCordinate,pieceDestination)); 
                       }
                       
                   }
               }                                                                   

        }
      return ImmutableList.copyOf(legalMoves);
    }
        @Override
    // return new bishop used for new move
    public Knight movePiece(Move move) {
         return new Knight(move.getMovePiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
      public String toString(){
    
    return PieceType.KNIGHT.toString();
    }
    
    // here if the piece is in first column then FIRST_COLUMN will return true then 
    //  the position after that written in return statement is not valid for piece
  private static boolean isFirstColumnExclusions(final int currentPosition, final int candidateOfSet){
      return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOfSet==-17) || (candidateOfSet==-10) ||
                             candidateOfSet==6 || candidateOfSet==15);
}
     private static boolean isSecondColumnExclusions(final int currentPosition, final int candidateOfSet){
      return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOfSet==-10 || candidateOfSet==6);
}
         private static boolean isSevenColumnExclusions(final int currentPosition, final int candidateOfSet){
      return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOfSet==-6 || candidateOfSet==10);
}
          private static boolean isEightColumnExclusions(final int currentPosition, final int candidateOfSet){
      return BoardUtils.EIGTH_COLUMN[currentPosition] && ((candidateOfSet==-15) || (candidateOfSet==-6) ||
                             candidateOfSet==10 || candidateOfSet==17);
}
     
}

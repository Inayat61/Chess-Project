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
public class King extends Piece {
      private final static int [] CANDIATE_MOVE_COORDINATE={-9,-8,-1,1,7,8,9,-7};


    public King( final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.KING,piecePosition, pieceAlliance,true);
    }
    
    public King( final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.KING,piecePosition, pieceAlliance,isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMove(Board board) {
        final List<Move> legalMoves=new ArrayList<>();
        
        for(final int candidateCoordinateOffSet:CANDIATE_MOVE_COORDINATE){
             final int  candidateDestinationCordinate=this.piecePosition+candidateCoordinateOffSet;
             if(isFirstColumnExclusions(this.piecePosition,candidateCoordinateOffSet)|| 
                isEigthColumnExclusions(this.piecePosition,candidateCoordinateOffSet)){continue;}
             
             
             if(BoardUtils.isValidTileCordinate(candidateDestinationCordinate)){
                 final Tile candidateDestinationTile=board.getTile(candidateDestinationCordinate);
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
    // return new king used for new move
    public King movePiece(Move move) {
  return new King(move.getMovePiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
      public String toString(){
    
    return PieceType.KING.toString();
    }
    private static boolean isFirstColumnExclusions(final int currentPosition, final int candidateOfSet){
      return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOfSet==-9) || (candidateOfSet==-1) ||
                             candidateOfSet==7);
}
     private static boolean isEigthColumnExclusions(final int currentPosition, final int candidateOfSet){
      return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOfSet==-7 || candidateOfSet==1
               || candidateOfSet==9);
}
public static boolean isInCheck(Board chessBoard)
{
    if(chessBoard.blackPlayer().isInCheck())
        return true;
    return false;
}
}

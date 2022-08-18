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
public class Rook extends Piece{
    private final static int [] CANDIATE_MOVE_VECTOR_COORDINATE={-8,-1,1,8};


    public Rook(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.ROOK,piecePosition, pieceAlliance,true);
    }
    public Rook(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove){
        super(PieceType.ROOK,piecePosition, pieceAlliance,isFirstMove);
    }

    
    @Override
    public List<Move> calculateLegalMove(Board board) {
          final List<Move> legalMoves=new ArrayList<>();
        for(final int candidateCoordinateOffSet:CANDIATE_MOVE_VECTOR_COORDINATE){
            int candidateDestinationCoordinate=this.piecePosition;
            while(BoardUtils.isValidTileCordinate(candidateDestinationCoordinate)){
               if(isFirstColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffSet) || 
                   isEigthColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffSet))
               {
                   break;
               }
                candidateDestinationCoordinate+=candidateCoordinateOffSet;
                if(BoardUtils.isValidTileCordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile=board.getTile( candidateDestinationCoordinate);
                    if(! candidateDestinationTile.isTileOccupied()){
                       legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                   }
                   else{
                       final Piece pieceDestination=candidateDestinationTile.getPiece();
                       final Alliance pieceAlliance=pieceDestination.getPieceAlliance();
                       //Ememy piece is placed there
                       if(this.pieceAlliance!=pieceAlliance){
                           legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,pieceDestination)); 
                       }
                      break;   
                   }
                 
                }
            }
        }
   return ImmutableList.copyOf(legalMoves);
    }
        @Override
    // return new bishop used for new move
    public Rook movePiece(Move move) {
        return new Rook(move.getMovePiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
      public String toString(){
    
    return PieceType.ROOK.toString();
    }
    private static boolean isFirstColumnExclusion(final int currentPosition , final int candidateOfSet){
    return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOfSet ==-1);
    }
    
    private static boolean isEigthColumnExclusion(final int currentPosition , final int candidateOfSet){
    return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOfSet ==1);
    }
        }
    

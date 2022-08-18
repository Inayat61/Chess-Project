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
import com.chess.example.board.Move.PawnAttackMove;
import com.chess.example.board.Move.PawnMove;
import com.chess.example.board.Move.PawnPromotion;
import java.util.ArrayList;
import java.util.List;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

/**
 *
 * @author ADMIN
 */
public class Pawn extends Piece {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADMIN
 */
    private final static int [] CANDIATE_MOVE_COORDINATE={8,16,7,9};


    public Pawn(final  Alliance pieceAlliance,final int piecePosition) {
        super(Piece.PieceType.PAWN,piecePosition, pieceAlliance,true);
    }
    
    public Pawn(final  Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(Piece.PieceType.PAWN,piecePosition, pieceAlliance,isFirstMove);
   }
    
    @Override
    public List<Move> calculateLegalMove(Board board) {
          final List<Move> legalMoves=new ArrayList<>();
        for(final int currentCandidateOffSet:CANDIATE_MOVE_COORDINATE){
           final  int candidateDestinationCoordinate=this.piecePosition+ (this.getPieceAlliance().getDirection()*currentCandidateOffSet);
             if(!BoardUtils.isValidTileCordinate(candidateDestinationCoordinate)){
                 continue;
             }
             if(currentCandidateOffSet==8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                 if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                      legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                 }
                 else{
                 legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
             } }
             else if(currentCandidateOffSet==16 && this.isFirstMove() &&
                     ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack())|| 
                     (BoardUtils.SECOND_RANK[this.piecePosition]&&this.getPieceAlliance().isWhite()))){
                 final int behindCandidateDestinationCoordinate=this.piecePosition+(this.pieceAlliance.getDirection()*8);
                 if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                     !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                     legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
                     
                 }
                 
             }else if(/*Attack point of white pawn*/currentCandidateOffSet==7 &&
                     !((BoardUtils.EIGTH_COLUMN[this.piecePosition]&& this.pieceAlliance.isWhite() ||
                     (BoardUtils.FIRST_COLUMN[this.piecePosition]&& this.pieceAlliance.isBlack())))){
                 if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                     final Piece pieceOnCandidate=board.getTile(candidateDestinationCoordinate).getPiece();
                     if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                            if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                                legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate)));
                            
                            }else{
                             legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate));
                            }
                     }
                 }else if(board.getEnPassantPawn()!=null){
                     if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition+(this.pieceAlliance.getOppositeDirection()))){
                         final Piece pieceOnCandidate =board.getEnPassantPawn();
                         if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                             legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                         }
                     }
                 
                 }   
             }
             else if(/*Attack point of black pawn*/currentCandidateOffSet==9 &&
                      !((BoardUtils.FIRST_COLUMN[this.piecePosition]&& this.pieceAlliance.isWhite() ||
                     (BoardUtils.EIGTH_COLUMN[this.piecePosition]&& this.pieceAlliance.isBlack())))){
                 if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                     final Piece pieceOnCandidate=board.getTile(candidateDestinationCoordinate).getPiece();
                     if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                          if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate))
                              {
                                  legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate)));
                             
                                  
                              }
                              else{
                             legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate));
                                      }
                     }
                 }else if(board.getEnPassantPawn()!=null){
                     if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition-(this.pieceAlliance.getOppositeDirection()))){
                         final Piece pieceOnCandidate =board.getEnPassantPawn();
                         if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                             legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                         }
                     }
                 
                 }
                 
             }
        }
      
         return ImmutableList.copyOf(legalMoves);
    }
        @Override
    // return new bishop used for new move
    public Pawn movePiece(Move move) {
     return new Pawn(move.getMovePiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
      public String toString(){
    
        return Piece.PieceType.PAWN.toString();
    }
      public Piece getPromotionPiece(){
          return new Queen(this.pieceAlliance,this.piecePosition,false);
      }
    
}


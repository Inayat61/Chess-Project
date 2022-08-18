/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chess.example.pieces;

import com.chess.example.board.Alliance;
import com.chess.example.board.Board;
import com.chess.example.board.Move;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;
    Piece( final PieceType pieceType,
           final int piecePosition,
           Alliance pieceAlliance,final boolean isFirstMove){
        this.pieceType=pieceType;
        this.piecePosition=piecePosition;
        this.pieceAlliance=pieceAlliance;
        // TODO some more work
        this.isFirstMove=isFirstMove;
        this.cachedHashCode=computeHashCode();
    }
    //object equality instead of reference equality.
    public boolean  equals(final Object other){
        if(this==other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece =(Piece) other;
        return piecePosition==otherPiece.getPiecePosition() && pieceType==otherPiece.getPieceType() &&
                pieceAlliance==otherPiece.getPieceAlliance() && isFirstMove==otherPiece.isFirstMove();
        }
    public int hashCode(){
        return this.cachedHashCode;
    }
    public int getPiecePosition(){
        return this.piecePosition;
    }
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    public boolean isFirstMove(){
   
        return this.isFirstMove;
    }
    public PieceType getPieceType(){
    return this.pieceType;
    }
    public int getPieceValue(){
        return this.pieceType.getPieceValue();
    }

    // we  also use SET here because moves are not in order but for simplicity we use list.
    public abstract List<Move> calculateLegalMove(final Board board);
    //its return new piece with an updated position
    public abstract Piece movePiece(Move move);

    private int computeHashCode() {
         int result=pieceType.hashCode();
    result=31*result+pieceAlliance.hashCode();
    result=31*+result+piecePosition;
    result=31*result+(isFirstMove ? 1 : 0);
    return result;
    }
    public enum PieceType{
        KNIGHT("N",300){
            public boolean isKing(){
            return false;
        }

            @Override
            public boolean isRook() {
         return false;
            }
        },
        PAWN("P",100){
        public boolean isKing(){
            return false;
        }

            @Override
            public boolean isRook() {
           return false;
            }
        },
        
        BISHOP("B",300){
            public boolean isKing(){
            return false;
        }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R",500){
        
            public boolean isKing(){
            return false;
        }

            @Override
            public boolean isRook() {
             return true;
            }
        },
        QUEEN("Q",900){
            public boolean isKing(){
            return false;
        }

            @Override
            public boolean isRook() {
         return false; 
            }
        },
        KING("K",10000){
            public boolean isKing(){
            return true;
        }

            @Override
            public boolean isRook() {
              return false;
            }
            
        };
        private String pieceName;
        private int pieceValue;
    PieceType(final String pieceName, final int pieceValue){
        this.pieceName=pieceName;
        this.pieceValue =pieceValue;
    }
        @Override
        public String toString(){
          return  this.pieceName;
        }
        public int getPieceValue(){
            return this.pieceValue;
        }
        public abstract boolean isKing();

        public abstract boolean isRook();
      
}
}

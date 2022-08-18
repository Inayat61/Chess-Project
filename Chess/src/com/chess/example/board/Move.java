/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chess.example.board;

import com.chess.example.board.Board.Builder;
import com.chess.example.pieces.Pawn;
import com.chess.example.pieces.Piece;
import com.chess.example.pieces.Rook;

/**
 *
 * @author ADMIN
 */
public abstract class Move {
    protected final Board board;
   protected final Piece movedPiece;
   protected final int destinationCoordinate;
    protected final boolean isFirstMove;
    
    public static final Move NULL_MOVE=new NullMove();
    
    private Move(final Board board,
        final Piece movedPiece,
        final int destinationCoordinate)
    {
    this.board=board;
    this.destinationCoordinate=destinationCoordinate;
    this.movedPiece=movedPiece;
    this.isFirstMove=movedPiece.isFirstMove();
    }
    private Move(final Board board,final int destinationCoordinate){
        this.board=board;
        this.destinationCoordinate=destinationCoordinate;
        this.movedPiece=null;
        this.isFirstMove=false;
    }
    
    
    @Override
    public int hashCode(){
      final int prime =31;
        int result =1;
        result= prime*result+ this.destinationCoordinate;
        result=prime*result+ this.movedPiece.hashCode();
        result=prime*result+this.movedPiece.getPiecePosition();
    return result;
    }
    @Override
    public boolean equals(final Object other){
        if(this==other){
            return true;
        }
        if(!(other instanceof Move)){
        return false;
        }
        final Move otherMove=(Move) other;
        return  getCurrentCoordinate()==otherMove.getCurrentCoordinate() &&
                getDestinationCoordinate()== otherMove.getDestinationCoordinate()&&
                getMovePiece().equals(otherMove.getMovePiece());
  }
    public Board getBoard(){
        return this.board;
    }
    public int getCurrentCoordinate(){
       // System.out.print( this.getMovePiece().getPiecePosition()+" ");
        return this.getMovePiece().getPiecePosition();
    
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    public Piece getMovePiece(){
        return this.movedPiece;
    }
    public boolean isAttack(){
    return false;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }
    
    // whenn u have legal move then it create a new board , its like a board builder.
    public Board execute() {
        //create new board
        final Board.Builder builder=new Board.Builder();
        //traverse through all current player pieces for placing them on new board at the same position as they are in previous one 
        for(final Piece piece: this.board.currentPlayer().getActivePiece()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        //this for the opponent piece same as above.
        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePiece()){
            builder.setPiece(piece);
        }
        //move the moved pieces
        builder.setPiece(this.movedPiece.movePiece(this));
        //now it change the turn
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
     return builder.build();
    }
    public static class MajorAttackMove extends AttackMove{

        public MajorAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate,final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
        @Override
          public boolean equals(final Object other){
            return this==other || other instanceof MajorAttackMove && super.equals(other);
        }
    
    }
    public static final class MajorMove extends Move{

        public MajorMove(final Board board, final Piece movedPiece,final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        
        public boolean equals(final Object other){
            return this==other || other instanceof MajorMove && super.equals(other);
        }
        
        public String toString(){
            return movedPiece.getPieceType().toString();// + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    public static  class AttackMove extends Move{
        final Piece attackedPiece;

        public AttackMove(final Board board,final Piece movedPiece,final int destinationCoordinate,final Piece attackedPiece ) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece=attackedPiece;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode()+super.hashCode();
        }
        @Override
        public boolean equals(final Object other){
            if(this==other)
                return true;
            if(!(other instanceof AttackMove))
                return false;
            final AttackMove otherAttackMove=(AttackMove)other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        
        }
        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
}
    
        public static final class PawnMove extends Move{

        public PawnMove(final Board board, final Piece movedPiece,final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        public boolean equals(final Object other){
            return this==other || other instanceof PawnMove && super.equals(other);
        }
        
    }
           public static  class PawnAttackMove extends AttackMove{
     
        public PawnAttackMove(final Board board,final Piece movedPiece,final int destinationCoordinate,final Piece attackedPiece ) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
          
        }
        public boolean equals(final Object other){
            return this==other || other instanceof PawnAttackMove && super.equals(other);
        }
}
           
        public static final class PawnEnPassantAttackMove extends PawnAttackMove{
     
        public PawnEnPassantAttackMove(final Board board,final Piece movedPiece,final int destinationCoordinate,final Piece attackedPiece ) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
          
        }
          public boolean equals(final Object other){
            return this==other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }
        public Board execute(){
            final Builder builder =new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePiece()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
             for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePiece()){
                if(!piece.equals(this.getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
}
        public static class PawnPromotion extends Move
        {
                final Move decoratedMove;
                final Pawn promotedPawn;
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(),decoratedMove.getMovePiece(),decoratedMove.getDestinationCoordinate());
            this.decoratedMove=decoratedMove;
            this.promotedPawn=(Pawn) decoratedMove.getMovePiece();
        }
        
                @Override
            public int hashCode(){
                return decoratedMove.hashCode()+ (31* promotedPawn.hashCode());
            }
                @Override
            public boolean equals(final Object other){
                return this==other || other instanceof PawnPromotion && (super.equals(other));
            }
            public Board execute(){
                final  Board pawnMovedBoard=this.decoratedMove.execute();
                final Board.Builder builder=new Builder();
                for(final Piece piece : pawnMovedBoard.currentPlayer().getActivePiece()){
                    if(!this.promotedPawn.equals(piece)){
                        builder.setPiece(piece);
                    }
                }
                for(final Piece piece :  pawnMovedBoard.currentPlayer().getOpponent().getActivePiece()){
                    builder.setPiece(piece);
                }
                builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
                builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
                return builder.build();
                
            }
            public boolean isAttack(){
                return this.decoratedMove.isAttack();
            }
            public Piece getAttackedPiece(){
                return this.decoratedMove.getAttackedPiece();
            }
        
        }
        
      public static final class PawnJump extends Move{

        public PawnJump(final Board board, final Piece movedPiece,final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public Board execute(){
            final Builder builder=new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePiece()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePiece()){
                    builder.setPiece(piece);
            }
            final Pawn movedPawn=(Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
            
        
        }
        
    }
      
      
     static abstract class CastleMove extends Move{
         protected final Rook castleRook;
         protected final int castleRookStart;
         protected final int castleRookDestination;
         

        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate, 
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook=castleRook;
            this.castleRookStart=castleRookStart;
            this.castleRookDestination=castleRookDestination;
        }
        public Rook getCastleRook(){
            return this.castleRook;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }
        public Board execute(){
            final Builder builder=new Builder();
      for(final Piece piece: this.board.currentPlayer().getActivePiece()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePiece()){
                
                    builder.setPiece(piece);
            }
            
            builder.setPiece(this.movedPiece.movePiece(this));
            ////////////////////////////////////////////////////////////////////////
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(),this.castleRookDestination));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        
        public int hashCode(){
            final int prime=31;
            int result=super.hashCode();
            result=prime* result+ this.castleRook.hashCode();
            result=prime*result+this.castleRookDestination;
            return result;
        }
        
        public boolean equals(final Object other){
            if(this==other){
                return true;
            }
            if(!(other instanceof CastleMove)){
                return false;
            }
            final CastleMove otherCastleMove=(CastleMove)other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
        
        
    }
     
      public static final class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(final Board board,
                                  final Piece movedPiece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook,castleRookStart,castleRookDestination);
        }
        
         public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof KingSideCastleMove)) {
                return false;
            }
            final KingSideCastleMove otherKingSideCastleMove = (KingSideCastleMove) other;
            return super.equals(otherKingSideCastleMove) && this.castleRook.equals(otherKingSideCastleMove.getCastleRook());
        }
        
    }
      
      public static final class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(final Board board, 
                                   final Piece movedPiece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart,castleRookDestination);
        }
         public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof QueenSideCastleMove)) {
                return false;
            }
            final QueenSideCastleMove otherQueenSideCastleMove = (QueenSideCastleMove) other;
            return super.equals(otherQueenSideCastleMove) && this.castleRook.equals(otherQueenSideCastleMove.getCastleRook());
        }
       
    }
      
       public static final class NullMove extends Move{

        public NullMove() {
            super(null, 65);
        }
        @Override
         public Board execute(){
             throw new RuntimeException("cannot execute the null move!");
         }
         @Override
         public int getCurrentCoordinate(){
             return -1;
         }
        
    }
       public static class MoveFactory{
            private MoveFactory(){
                throw new RuntimeException("Not instantiable");
            }
            public static Move createMove(final Board board, final int currentCoordinatte, final int destinationCoordinate){
                for(final Move move : board.getAllLegalMoves())
                {
                    if(move.getCurrentCoordinate()==currentCoordinatte && move.getDestinationCoordinate()== destinationCoordinate)
                    {
                        return move;
                    }
                }
            
            return NULL_MOVE;}
       
       }
      
}

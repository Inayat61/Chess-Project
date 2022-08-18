/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chess.example.board;

import com.chess.example.pieces.Bishop;
import com.chess.example.pieces.King;
import com.chess.example.pieces.Knight;
import com.chess.example.pieces.Pawn;
import com.chess.example.pieces.Piece;
import com.chess.example.pieces.Queen;
import com.chess.example.pieces.Rook;
import com.chess.example.player.BlackPlayer;
import com.chess.example.player.Player;
import com.chess.example.player.WhitePlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.carrot2.shaded.guava.common.collect.ImmutableList;
import org.carrot2.shaded.guava.common.collect.Iterables;


/**
 *
 * @author ADMIN
 */
public class Board {
    private final List<Tile> gameBoard;
    private final List<Piece> whitePiece;
     private final List<Piece> blackPiece;
     
     private final WhitePlayer whitePlayer;
     private final BlackPlayer blackPlayer;
     private final Player currentPlayer;
             final static Builder builder =new Builder();
     private final Pawn enPassantPawn;
     
    private Board(Builder builder){
        this.gameBoard= createGameBoard(builder);
        this.whitePiece=calculateActivePlaces(this.gameBoard,Alliance.WHITE);
        this.blackPiece=calculateActivePlaces(this.gameBoard,Alliance.BLACK);
        this.enPassantPawn=builder.enPassantPawn;
        final List<Move> whiteStandardLegalMove=calculateLegalMove(this.whitePiece);
        final List<Move> blackStandardLegalMove=calculateLegalMove(this.blackPiece);
    
        this.whitePlayer=new WhitePlayer(this,whiteStandardLegalMove,blackStandardLegalMove);
        this.blackPlayer=new BlackPlayer(this,whiteStandardLegalMove,blackStandardLegalMove);
            this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
                
     }
    public Tile getTile(final int titleCordinate){
      
        return gameBoard.get(titleCordinate);
    }

    private List<Tile> createGameBoard(Builder builder) {
        final Tile [] tiles=new Tile[BoardUtils.NUM_TITLES];
        for(int i=0;i<BoardUtils.NUM_TITLES;i++){
            tiles[i]=Tile.createTile(i, builder.boardConfig.get(i));
          //  System.out.println(builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
        
    }

    public static Board createStandardBoard(){
        
        builder.setPiece(new Rook(Alliance.BLACK,0));
        builder.setPiece(new Knight(Alliance.BLACK,1));
        builder.setPiece(new Bishop(Alliance.BLACK,2));
        builder.setPiece(new Queen(Alliance.BLACK,3));
        builder.setPiece(new King(Alliance.BLACK,4));
        builder.setPiece(new Bishop(Alliance.BLACK,5));
        builder.setPiece(new Knight(Alliance.BLACK,6));
        builder.setPiece(new Rook(Alliance.BLACK,7));
        builder.setPiece(new Pawn(Alliance.BLACK,8));
        builder.setPiece(new Pawn(Alliance.BLACK,9));
        builder.setPiece(new Pawn(Alliance.BLACK,10));
        builder.setPiece(new Pawn(Alliance.BLACK,11));
        builder.setPiece(new Pawn(Alliance.BLACK,12));
        builder.setPiece(new Pawn(Alliance.BLACK,13));
        builder.setPiece(new Pawn(Alliance.BLACK,14));
        builder.setPiece(new Pawn(Alliance.BLACK,15));
                
           //white pieces  
          builder.setPiece(new Pawn(Alliance.WHITE,48));
          builder.setPiece(new Pawn(Alliance.WHITE,49));
          builder.setPiece(new Pawn(Alliance.WHITE,50));
          builder.setPiece(new Pawn(Alliance.WHITE,51));
          builder.setPiece(new Pawn(Alliance.WHITE,52));
          builder.setPiece(new Pawn(Alliance.WHITE,53));
          builder.setPiece(new Pawn(Alliance.WHITE,54));
          builder.setPiece(new Pawn(Alliance.WHITE,55));
          builder.setPiece(new Rook(Alliance.WHITE,56)); 
          builder.setPiece(new Knight(Alliance.WHITE,57));
          builder.setPiece(new Bishop(Alliance.WHITE,58));
          builder.setPiece(new Queen(Alliance.WHITE,59));
          builder.setPiece(new King(Alliance.WHITE,60));
          builder.setPiece(new Bishop(Alliance.WHITE,61));
          builder.setPiece(new Knight(Alliance.WHITE,62));
          builder.setPiece(new Rook(Alliance.WHITE,63)); 
                             
                  builder.setMoveMaker(Alliance.WHITE);
                  return builder.build();
         
    }
    public Player whitePlayer(){
        return this.whitePlayer;
    }
    public Player blackPlayer(){
        return this.blackPlayer;
    }
   public  Player currentPlayer(){
       return this.currentPlayer;
   }
    

    public String toString(){
    final StringBuilder builder=new StringBuilder();
    for(int i=0;i<BoardUtils.NUM_TITLES;i++){
        final String tileText=this.gameBoard.get(i).toString();
        builder.append(String.format("%3s",tileText));
        if((i+1)%BoardUtils.NUM_TITLE_PER_ROW==0)
        {
            builder.append("\n");
        }
    }
    return builder.toString();
    }
    public List<Piece> getBlackPiece(){
        return this.blackPiece;
    }
    
    public Collection<Piece> getWhitePiece(){
        return this.whitePiece;
    }
    
    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }
    
    private static List<Piece> calculateActivePlaces(final Collection<Tile> gameBoard, final Alliance alliance) {
     int count =0;
        final List<Piece> activePiece=new ArrayList<>();
      for(final Tile tile : gameBoard){
          if(tile.isTileOccupied()){
              final Piece piece= tile.getPiece();
              if(piece.getPieceAlliance()==alliance){
                  activePiece.add(piece);
              }
          }
      }
       return ImmutableList.copyOf(activePiece);
    
    }
    
    
    
    

    private List<Move> calculateLegalMove(Collection<Piece> pieces) {
         final List<Move> legalMoves=new ArrayList<>();
         for(final Piece piece: pieces){
             legalMoves.addAll(piece.calculateLegalMove(this));
         }
        
        return ImmutableList.copyOf(legalMoves);
    }

  public  Iterable<Move> getAllLegalMoves() {
      return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMove(),this.blackPlayer.getLegalMove()));
    }

 
    // used to create the new board
    public static class Builder{
        HashMap<Integer,Piece> boardConfig;
        Alliance nextMoveMaker;
         Pawn enPassantPawn;
        public Builder(){
            this.boardConfig=new HashMap<>();
        }
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            System.out.println(piece);
                    
            return this;
        }
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker=nextMoveMaker;
            return this;
    }
        public Board build(){
            return new Board(this);
        }

        void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn=enPassantPawn;
        }
    }
}

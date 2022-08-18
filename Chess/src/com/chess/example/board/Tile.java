package com.chess.example.board;
import com.chess.example.pieces.Piece;
import java.util.HashMap;
import java.util.Map;
import org.carrot2.shaded.guava.common.collect.ImmutableMap;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADMIN
 */
// this class will tell about tille that it has piece or not in it.
public abstract class Tile {
    
    /* the variable is protected for access of package condition and 
     final keyword is used so that it will be initialized on calling constructor.*/ 
    protected final int tileCordinate;
    
    private static final Map<Integer,isEmptyTile> emptyTile=createAllPossibleEmptyTiles();
   
    //the constructor is used to initialize the variable
    private Tile(int tileCordinate){
        this.tileCordinate=tileCordinate;
    }
    
   /*this function is abstract beacuse it will overide by the class 
        which tell true or false about occupation of tile*/
    public abstract boolean isTileOccupied();
    
    /* this method will tell is there is a piece lying on tile or not ,
        of yes then return that piece othervise null*/
    public abstract Piece getPiece();
    public int getTileCoordinate(){
    return this.tileCordinate;
    }

    private static Map<Integer, isEmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer,isEmptyTile> emptyTileMap=new HashMap<>();
        for(int i=0;i<BoardUtils.NUM_TITLES;i++){
            emptyTileMap.put(i, new isEmptyTile(i));
        }
        //Collection.unmodiableMap();
        return ImmutableMap.copyOf(emptyTileMap); 
    }
    
    public static Tile createTile(final int tileCordinate,Piece piece ){
    return piece!=null ? new TileOccupied(tileCordinate,piece) : emptyTile.get(tileCordinate);
    }
    
   /* this class will tell us that tile is empty and not piece is on it*/ 
    public static final class isEmptyTile extends Tile { 
        // Initialing cordinate
       private isEmptyTile(final int tileCordinate) {
            super(tileCordinate);
        }
       public String toString(){
       
       return "-";
       }
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
           return null;
        }

       
    } 
    /* thie class will tell us that tile is occuopied with the piece which is placed on it*/
    public static final class TileOccupied extends Tile{
        private final Piece pieceOnTile;
      private TileOccupied(int tileCordinate,  Piece pieceOnTile) {
            super(tileCordinate);
            this.pieceOnTile=pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }
        // black piece will show in lower case and white piece in upper for convnience
        public String toString(){
       
       return getPiece().getPieceAlliance().isBlack()? getPiece().toString().toLowerCase():
                    getPiece().toString();
       }
       

        @Override
        public Piece getPiece() {
        return this.pieceOnTile;
        }
    }
    
}

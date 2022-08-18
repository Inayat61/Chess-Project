/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chess.example.player;

import com.chess.example.board.Alliance;
import com.chess.example.board.Board;
import com.chess.example.board.Move;
import com.chess.example.board.Tile;
import com.chess.example.pieces.Piece;
import com.chess.example.pieces.Rook;
import java.util.ArrayList;
import java.util.List;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

/**
 *
 * @author ADMIN
 */
public class BlackPlayer extends Player {

    public BlackPlayer(Board board, List<Move> whiteStandardLegalMoves, List<Move> blackStandardLegalMove) {
        super(board,blackStandardLegalMove , whiteStandardLegalMoves);
    }


    @Override
    public List<Piece> getActivePiece() {
        return this.board.getBlackPiece();
    }

    @Override
    public Alliance getAlliance() {
     return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
   return this.board.whitePlayer();
     }

    @Override
    protected List<Move> CalculateKingCastles(final List<Move> playerLegals,final List<Move> opponentLegals) {
    final List<Move> kingCastle=new ArrayList<>();
        //this will check thst it is kings first move and he is not in check!
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            //this will check the king side castle tiles are occupied or not.
            if(this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile=this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttacksOnTitles(5, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTitles(6,opponentLegals).isEmpty()&&
                       rookTile.getPiece().getPieceType().isRook()){
                    kingCastle.add(new Move.KingSideCastleMove(this.board, 
                                                               this.playerKing, 
                                                               6,
                                                               (Rook)rookTile.getPiece(),
                                                               rookTile.getTileCoordinate(),
                                                               5));
                    
                    }
                }
            }
            //this is from queen side castling
              if(this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() &&
                 !this.board.getTile(3).isTileOccupied()){
                   final Tile rookTile=this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && 
                        Player.calculateAttacksOnTitles(2, opponentLegals).isEmpty()&&
                        Player.calculateAttacksOnTitles(3, opponentLegals).isEmpty()&&
                       rookTile.getPiece().getPieceType().isRook()){
                    kingCastle.add(new Move.QueenSideCastleMove(this.board,
                                                                this.playerKing,
                                                                2,    
                                                               (Rook)rookTile.getPiece(),
                                                               rookTile.getTileCoordinate(),
                                                               3));
                    
                
                }
              }
            
        }
        
        return ImmutableList.copyOf(kingCastle);  }
    
}

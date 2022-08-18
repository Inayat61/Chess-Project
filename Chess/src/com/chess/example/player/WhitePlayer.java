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
import java.util.Collection;
import java.util.List;
import org.carrot2.shaded.guava.common.collect.ImmutableList;

/**
 *
 * @author ADMIN
 */
public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,final  List<Move> whiteStandardLegalMoves,final  List<Move> blackStandardLegalMove) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMove);
    }

    @Override
    public Collection<Piece> getActivePiece() {
        return this.board.getWhitePiece();
          }

    @Override
    public Alliance getAlliance() {
      return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
     return this.board.blackPlayer();   }
    //this method is used for castling of whiteking.
    @Override
    protected List<Move> CalculateKingCastles(final List<Move> playerLegals,final List<Move> opponentLegals) {
        final List<Move> kingCastle=new ArrayList<>();
        //this will check thst it is kings first move and he is not in check!
        //TODO eror
       if(this.playerKing.isFirstMove() && !this.isInCheck()){
            //this will check the king side castle tiles are occupied or not.
            if(this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){
                final Tile rookTile=this.board.getTile(63);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttacksOnTitles(61, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTitles(62,opponentLegals).isEmpty()&&
                       rookTile.getPiece().getPieceType().isRook()){
                    kingCastle.add(new Move.KingSideCastleMove(this.board, 
                                                               this.playerKing, 
                                                               62,
                                                               (Rook)rookTile.getPiece(),
                                                               rookTile.getTileCoordinate(),
                                                               61));
                    }
                }
            }
             //this is from queen side castling
              if(this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() &&
                 !this.board.getTile(57).isTileOccupied()){
                   final Tile rookTile=this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                        Player.calculateAttacksOnTitles(58, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTitles(59,opponentLegals).isEmpty()&&
                       rookTile.getPiece().getPieceType().isRook()){
                    kingCastle.add(new Move.QueenSideCastleMove(this.board,
                                                                this.playerKing,
                                                                58,    
                                                               (Rook)rookTile.getPiece(),
                                                               rookTile.getTileCoordinate(),
                                                               59));
                    }
                
                }
              }
           return ImmutableList.copyOf(kingCastle);
     
        }
    
        }


    


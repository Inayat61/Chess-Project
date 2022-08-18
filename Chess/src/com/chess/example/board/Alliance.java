package com.chess.example.board;


import com.chess.example.player.BlackPlayer;
import com.chess.example.player.BlackPlayer;
import com.chess.example.player.Player;
import com.chess.example.player.WhitePlayer;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author ADMIN
 */
public enum Alliance {
    WHITE {

       
       public int getDirection() {
           return -1;
        }

        @Override
        public boolean isWhite() {
        return true;
        }

        @Override
        public boolean isBlack() {
        return false;
        }

        @Override
        Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) {
        return whitePlayer;    
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGTH_RANK[position];
        }
    },
    BLACK {

        @Override
       public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
          return  false;        }

        @Override
        public boolean isBlack() {
            return true;
     

        }

        @Override
        Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) {
       return blackPlayer;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }
    };
  public  abstract int getDirection();
  public abstract int getOppositeDirection();
  public abstract boolean isWhite();
  public abstract boolean isBlack();
  public abstract boolean isPawnPromotionSquare(int position);
 abstract  Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}

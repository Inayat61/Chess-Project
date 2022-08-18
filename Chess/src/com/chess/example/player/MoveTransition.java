/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chess.example.player;

import com.chess.example.board.Board;
import com.chess.example.board.Move;

/**
 *
 * @author ADMIN
 */
public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;
    
    public MoveTransition(final Board transitionBoard,Move move, MoveStatus moveStatus){
        this.move=move;
        this.moveStatus=moveStatus;
        this.transitionBoard=transitionBoard;
    
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
    public Board getTransitionBoard(){
        return this.transitionBoard;
    }
    
}

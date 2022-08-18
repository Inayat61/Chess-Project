/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chess.example.board;

import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class BoardUtils {
    public static final boolean[] FIRST_COLUMN= initColumn(0);
    public static final boolean[] SECOND_COLUMN=initColumn(1);
    public static final boolean[] SEVENTH_COLUMN=initColumn(6);
    public static final boolean[] EIGTH_COLUMN=initColumn(7);
    
    public static final boolean [] EIGTH_RANK=initRow(0);
    public static final boolean [] SEVENTH_RANK=initRow(8);
    public static final boolean [] SIXTH_RANK=initRow(16);
    public static final boolean [] FIFTH_RANK=initRow(24);
    public static final boolean [] FOURTH_RANK=initRow(32);
    public static final boolean [] THIRD_RANK=initRow(40); 
    public static final boolean [] SECOND_RANK=initRow(48);
    
    public static final boolean [] FIRST_RANK=initRow(56);
    
//    public static final String [] ALGEBREIC_NOTATION=   initializeAlgebreicNotation();
//     public static final Map<String ,Integer> POSITION_TO_COORDINATE =initializePositionToCoordinate();
    
    public static final int NUM_TITLES=64;
    public static final int NUM_TITLE_PER_ROW=8;
    private BoardUtils(){
        throw new RuntimeException("You cannot instantiate me!");
    }
      public static boolean isValidTileCordinate(final int cordinate) {
        return cordinate>=0 && cordinate<NUM_TITLES; 
    }

    private static boolean[] initColumn(int columnNumber) {
      final boolean [] column =new boolean[NUM_TITLES];
      do{
          column[columnNumber]=true;
          columnNumber+=NUM_TITLE_PER_ROW;
          
      }while(columnNumber<NUM_TITLES);
    return column;}

    private static boolean[] initRow(int row) {
    final boolean[]rows=new boolean[NUM_TITLES];
    do{
    rows[row]=true;
    row++;
    }while(row % NUM_TITLE_PER_ROW!=0);
    return rows;
    }
    
//    public static int getCoordinateAtPosition(final String position){
//       return POSITION_TO_COORDINATE.get(position);
//    }
//    public static int getPositionCoordinate(final int coordinate){
//        return ALGEBREIC_NOTATION[coordinate];
//    
//    }
    
}

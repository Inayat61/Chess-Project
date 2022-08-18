/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chess.gui;

import com.chess.example.board.Move;
import com.chess.example.pieces.Piece;
import com.chess.gui.Table.MoveLog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import org.carrot2.shaded.guava.common.primitives.Ints;

/**
 *
 * @author ADMIN
 */

public class TakenPiecePanel extends JPanel {
    private final JPanel northPanel;
    private final JPanel southPanel;
    
    
    private static final EtchedBorder PANEL_BORDER= new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_COLOR= Color.green;
    private static final Dimension TAKEN_PIECES_DIMENSION= new Dimension(60,80);
    public TakenPiecePanel(){
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel=new JPanel(new GridLayout(8, 2));
        this.southPanel=new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel,BorderLayout.NORTH);
        this.add(this.southPanel,BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }
    
    public void redo(final MoveLog moveLog){
        this.southPanel.removeAll();
        this.northPanel.removeAll();
        
        final List<Piece> whiteTakenPieces=new ArrayList<>();
        final List<Piece> blackTakenPieces=new ArrayList<>();
        
        for(final Move move : moveLog.getMoves()){
            if(move.isAttack()){
                final Piece takenPiece=move.getAttackedPiece();
                if(takenPiece.getPieceAlliance().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                }else   if(takenPiece.getPieceAlliance().isBlack()){
                    blackTakenPieces.add(takenPiece);
                }
                else{
                    throw new RuntimeException("should not reach here!");
                }
            }
        }
        Collections.sort(whiteTakenPieces, new Comparator<Piece>(){

            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(),o2.getPieceValue() );
            }
            
        
        });
        
        Collections.sort(blackTakenPieces, new Comparator<Piece>(){

            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(),o2.getPieceValue() );
            }
            
        
        });
        
    for(final Piece takenPiece : whiteTakenPieces){
        try{
             final BufferedImage image = ImageIO.read(new File("piecesPics/"+ takenPiece.getPieceAlliance().toString().substring(0,1)+""+takenPiece.toString()+".gif"));
      
        final ImageIcon icon =new ImageIcon(image);
        final JLabel imageLabel=new JLabel();
        this.northPanel.add(imageLabel);
        }catch(final IOException e) {
//             System.out.print(takenPiece.getPieceAlliance().toString().substring(0,1));
        
System.out.print(e);
        }
    }
    
     for(final Piece takenPiece : blackTakenPieces){
        try{
            
        final BufferedImage image = ImageIO.read(new File("piecesPics/"+ takenPiece.getPieceAlliance().toString().substring(0,1)+ ""+ takenPiece.toString()+".gif"));
        final ImageIcon icon =new ImageIcon(image);
        final JLabel imageLabel=new JLabel();
        /// changed from program
        this.southPanel.add(imageLabel);
       
        //System.out.println(takenPiece.toString());
        }catch(final IOException e) {
             System.out.println(takenPiece.getPieceAlliance().toString().substring(0,1));
             System.out.println(takenPiece.toString());
        
//   System.out.print(e);
        }
    }
     validate();
    
    }
}

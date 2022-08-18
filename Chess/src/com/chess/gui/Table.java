/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADMIN
 */
package com.chess.gui;

import com.chess.example.board.Board;
import com.chess.example.board.BoardUtils;
import com.chess.example.board.Move;
import com.chess.example.board.Tile;
import com.chess.example.pieces.Piece;
import com.chess.example.player.MoveTransition;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.carrot2.shaded.guava.common.collect.Lists;
//import piecesPics.pics.*;

public class Table {
    private final JFrame gameFrame;
    private final TakenPiecePanel takenPiecePanel;
    private final BoardPanel boardPanel;
    private final MoveLog moveLog;
    
    private  Board chessBoard;
    
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highLightLegalMoves;

    
    private final static Dimension OUTER_FRAME_DIMENSION=new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION=new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION=new Dimension(10,10);
    
         private final Color lightTileColor=Color.decode("#ffe0bd");
        private final Color darkTileColor=Color.decode("#a1665e");
        private static String defaultPieceImagePath="piecesPics/";

    public Table(){
        this.gameFrame=new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar= createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard= Board.createStandardBoard();
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.boardPanel=new BoardPanel();
        this.takenPiecePanel= new TakenPiecePanel();
        this.moveLog=new MoveLog();
        highLightLegalMoves=false;
        //this.gameFrame.add(this.takenPiecePanel,BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.boardDirection=BoardDirection.NORMAL;
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar=new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferenceMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu =new JMenu("File");
        final JMenuItem openPGN=new JMenuItem("Load O+PGN Files");
        openPGN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open that PGN file");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem=new JMenuItem("Exist");
        exitMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
   return fileMenu;
    }
    private JMenu createPreferenceMenu(){
        final JMenu preferenceMenu=new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem=new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection =boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
             }
        });
        preferenceMenu.add(flipBoardMenuItem);
        
        preferenceMenu.addSeparator();
        
        final JCheckBoxMenuItem legalMoveHighlighterCheckbox=new JCheckBoxMenuItem("Highlight Legal Move", false);
        
        legalMoveHighlighterCheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                    highLightLegalMoves=legalMoveHighlighterCheckbox.isSelected();
            }
        });
        preferenceMenu.add(legalMoveHighlighterCheckbox);
        return preferenceMenu;
    }
    public enum BoardDirection{
        NORMAL {

            @Override
            public List<TilePanel> traverse(final List<TilePanel> boardTile) {
                return boardTile;
            }

            @Override
            public BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {

            @Override
            public List<TilePanel> traverse(List<TilePanel> boardTile) {
                return Lists.reverse(boardTile);
            }

            @Override
           public BoardDirection opposite() {
                return NORMAL;
            }
        };
      public   abstract List<TilePanel> traverse(final List<TilePanel> boardTile);
      public  abstract BoardDirection opposite();
    
    }
    private class BoardPanel extends JPanel{
       final List<TilePanel> boardTile;

        public BoardPanel() {
            super(new GridLayout(8,8));
            this.boardTile = new ArrayList<>();
            for(int i=0;i<BoardUtils.NUM_TITLES;i++){
                final TilePanel tilePanel=new TilePanel(this,i);
                this.boardTile.add(tilePanel);
                add(tilePanel);
                
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
       public void drawBoard(final Board board){
           removeAll();
           for(final TilePanel tilePanel : boardDirection.traverse(boardTile)){
               tilePanel.drawTile(board);
               add(tilePanel);
           }
           validate();
           repaint();
       }
    }
    public static class MoveLog{
        private final List<Move> moves;

        public MoveLog() {
            this.moves = new ArrayList<>();
        }
        public List<Move> getMoves(){
            return this.moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public int size(){
            return this.moves.size();
        }
        public void clear(){
            this.moves.clear();
        }
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }
    }
    private class TilePanel extends JPanel{
        private final int tileId;
   

        public TilePanel(final BoardPanel boardPanel,
                         final  int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            addMouseListener(new MouseListener()  {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    //first cick
                    if(SwingUtilities.isRightMouseButton(e)){
                            sourceTile=null;
                            destinationTile=null;
                            humanMovedPiece=null;
                    }
                    else  if(SwingUtilities.isLeftMouseButton(e)){
                        if(sourceTile==null){
                            
                            sourceTile=chessBoard.getTile(tileId);
                            humanMovedPiece=sourceTile.getPiece();
                           // System.out.println(sourceTile.getPiece());
                            highLightLegals(chessBoard);
                            if(humanMovedPiece==null){
                                sourceTile=null;
                            } 
                    }else{
                          
                            destinationTile=chessBoard.getTile(tileId);
                            final Move move=Move.MoveFactory.createMove(chessBoard, 
                                                                        sourceTile.getTileCoordinate(),
                                                                        destinationTile.getTileCoordinate());
                            final MoveTransition transition=chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard=transition.getTransitionBoard();
                                moveLog.addMove(move);
                                ///////////////////////////////////////
                            }
                            sourceTile=null;
                            destinationTile=null;
                            humanMovedPiece=null;    
                        }
                       
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                try{
                                takenPiecePanel.redo(moveLog);
                                boardPanel.drawBoard(chessBoard);
                              }catch(Exception e){
                                  System.out.println("error");
                              }}
                        });
                                
                    
                    }
                    }

                @Override
                public void mousePressed(final MouseEvent e) {
                 }
 
                @Override
                public void mouseReleased(final MouseEvent e) {
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                }

               
            });
            validate();
        }
        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            highLightLegals(board);
            validate();
            repaint();
            
        
        }
        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
                try {
                    
// by using one parameter here we are saving image by using  their first letter.
           
                    File f=new File(defaultPieceImagePath +
                                                           board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1)+
                                                            board.getTile(this.tileId).getPiece().toString()+".gif");
                    final BufferedImage image=ImageIO.read(f);
                    
                add(new JLabel(new ImageIcon(image)));
                } catch (IOException ex) {
                    System.out.println("Caught");
                }
            
            }
        
        }
        
        private void highLightLegals(final Board board){
           if(chessBoard.blackPlayer().junk()) {
               
               highLightLegalMoves=false;
               System.out.println("isIncheck");
            
            int i=chessBoard.blackPlayer().getKingPosition();
            JLabel label=new JLabel();
               boardPanel.boardTile.get(i).setBackground(Color.red);
            
            }
           else if( chessBoard.whitePlayer().isInCheck()){
           
                 highLightLegalMoves=false;
               System.out.println("isIncheck");
              // if(board.getTile(chessBoard.whitePlayer().getKingPosition()).isTileOccupied())
                try {
                    
                 add(new JLabel(new ImageIcon(ImageIO.read(new File("red_dot/red_dot.png")))));
                } catch (IOException ex) {
                    System.out.println("Caught");
                }
         
            
           }
                    if(chessBoard.blackPlayer().isInCheckMade()) {
               
               highLightLegalMoves=false;
               System.out.println("isIncheckMade");
            
            int i=chessBoard.blackPlayer().getKingPosition();
            JLabel label=new JLabel();
               boardPanel.boardTile.get(i).setBackground(Color.red);
            
            }
               
               
               
           else if( chessBoard.whitePlayer().isInCheckMade()){
           
                 highLightLegalMoves=false;
               System.out.println("isIncheckMade");
            
              // if(board.getTile(chessBoard.whitePlayer().getKingPosition()).isTileOccupied())
                try {
           
                 add(new JLabel(new ImageIcon(ImageIO.read(new File("red_dot/red_dot.png")))));
                 
                } catch (IOException ex) {
                    System.out.println("Caught");
                }
         
            
           }
           
           
           else{
               highLightLegalMoves=true;
           }
            if(highLightLegalMoves){
                for(final Move move : pieceLegalMove(board)){
                    if(move.getDestinationCoordinate()==this.tileId){
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("green/green_dot.png")))));
                            
                        
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        
        
        }
        private List<Move> pieceLegalMove(final Board board ){
            if(humanMovedPiece!=null && humanMovedPiece.getPieceAlliance()==board.currentPlayer().getAlliance()){
                return humanMovedPiece.calculateLegalMove(board);
            }
            return Collections.emptyList();
        }

        private void assignTileColor() {
            if(BoardUtils.EIGTH_RANK[this.tileId] ||
                    BoardUtils.SIXTH_RANK[this.tileId]||
                    BoardUtils.FOURTH_RANK[this.tileId] ||
                    BoardUtils.SECOND_RANK[this.tileId])
            {
                setBackground(this.tileId%2==0 ? lightTileColor : darkTileColor);
            }
            else if(BoardUtils.SEVENTH_RANK[this.tileId] ||
                    BoardUtils.FIFTH_RANK[this.tileId]||
                    BoardUtils.THIRD_RANK[this.tileId] ||
                    BoardUtils.FIRST_RANK[this.tileId] )
            {
                setBackground(this.tileId%2!=0 ? lightTileColor : darkTileColor);
            }
                    
        }
        
    }
    
}

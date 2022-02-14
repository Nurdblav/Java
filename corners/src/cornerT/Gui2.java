package cornerT;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.*;
import java.util.*;

class Corners {
    static ImageIcon firstPlayer = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/3.jpg");
    static ImageIcon secondPlayer = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/4.jpg");
    static ImageIcon possible = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/5.jpg");
    final Random random = new Random();
    private customJButton[][] field = new customJButton[8][8];
    //false - first player's turn
    //true - second player's turn
    private boolean turn;
    //0 - active game
    //1 - first player win
    //2 - second player win
    private int gameEnd;
    // first player's buttons
    private ArrayList<customJButton> firstButtons = new ArrayList<customJButton>(1);
    // second player's buttons
    private ArrayList<customJButton> secondButtons = new ArrayList<customJButton>(1);

    private customJButton pressedButton;
    private JButton northButton;

    public Corners(JButton northButton) {
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if ((i>=0 && i<=3) && (j>=0 && j<=3)){
                    this.field[i][j] = new customJButton(i, j,1);
                    this.firstButtons.add(this.field[i][j]);
                    this.field[i][j].setIcon(this.firstPlayer);
                    //this.field[i][j].addActionListener(new customAction(this.field[i][j],this));
                }
                else if((i>=4 && i<=7) && (j>=4 && j<=7)){
                    this.field[i][j] = new customJButton(i, j,2);
                    this.secondButtons.add(this.field[i][j]);
                    this.field[i][j].setIcon(this.secondPlayer);
                    //this.field[i][j].addActionListener(new customAction(this.field[i][j],this));
                }
                else{
                    this.field[i][j] = new customJButton(i, j,0);
                    //this.field[i][j].addActionListener(new customAction(this.field[i][j],this));
                }
            }
        }
        Collections.reverse(this.firstButtons);
        this.turn = false;
        this.gameEnd = 0;
        this.pressedButton = null;
        this.northButton = northButton;
    }

    public synchronized void firstMove() {
        while (this.turn){
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        ArrayList<customJButton> posMoves = new ArrayList();
        for (customJButton fM : this.firstButtons){
            this.pressedButton = fM;
            //проверка на восток
            int xCor = this.pressedButton.getxCor();
            int yCor = this.pressedButton.getyCor();
            boolean possibleTurn = true;
            boolean multiTurn = false;
            while (possibleTurn){
                possibleTurn = false;
                if (yCor + 1 < 8) {
                    if ((this.field[xCor][yCor + 1].getType() == 0) && (!multiTurn)) {
                        posMoves.add(this.field[xCor][yCor + 1]);
                        this.field[xCor][yCor + 1].setType(-1);
                        this.field[xCor][yCor + 1].setIcon(this.possible);
                    } else if ((yCor + 2 < 8) && (this.field[xCor][yCor + 1].getType() != 0) && (this.field[xCor][yCor + 2].getType() == 0)) {
                        posMoves.add(this.field[xCor][yCor + 2]);
                        this.field[xCor][yCor + 2].setType(-1);
                        this.field[xCor][yCor + 2].setIcon(this.possible);
                        multiTurn = true;
                        yCor += 2;
                        possibleTurn = true;
                    }
                }
            }
            //проверка на юго-восток
            xCor = this.pressedButton.getxCor();
            yCor = this.pressedButton.getyCor();
            possibleTurn = true;
            multiTurn = false;
            while (possibleTurn){
                possibleTurn = false;
                if ((xCor + 1 < 8) && (yCor + 1 < 8)) {
                    if ((this.field[xCor + 1][yCor + 1].getType() == 0) && (!multiTurn)) {
                        posMoves.add(this.field[xCor + 1][yCor + 1]);
                        this.field[xCor + 1][yCor + 1].setType(-1);
                        this.field[xCor + 1][yCor + 1].setIcon(this.possible);
                    } else if ((xCor + 2 < 8) && (yCor + 2 < 8) && (this.field[xCor + 1][yCor + 1].getType() != 0) && (this.field[xCor + 2][yCor + 2].getType() == 0)) {
                        posMoves.add(this.field[xCor + 2][yCor + 2]);
                        this.field[xCor + 2][yCor + 2].setType(-1);
                        this.field[xCor + 2][yCor + 2].setIcon(this.possible);
                        multiTurn = true;
                        yCor += 2;
                        xCor += 2;
                        possibleTurn = true;
                    }
                }
            }
            //проверка на юг
            xCor = this.pressedButton.getxCor();
            yCor = this.pressedButton.getyCor();
            possibleTurn = true;
            multiTurn = false;
            while (possibleTurn){
                possibleTurn = false;
                if ((xCor + 1 < 8)) {
                    if ((this.field[xCor + 1][yCor].getType() == 0) && (!multiTurn)) {
                        posMoves.add(this.field[xCor + 1][yCor]);
                        this.field[xCor + 1][yCor].setType(-1);
                        this.field[xCor + 1][yCor].setIcon(this.possible);
                    } else if ((xCor + 2 < 8) && (this.field[xCor + 1][yCor].getType() != 0) && (this.field[xCor + 2][yCor].getType() == 0)) {
                        posMoves.add(this.field[xCor + 1][yCor]);
                        this.field[xCor + 2][yCor].setType(-1);
                        this.field[xCor + 2][yCor].setIcon(this.possible);
                        multiTurn = true;
                        xCor += 2;
                        possibleTurn = true;
                    }
                }
            }
            if (!posMoves.isEmpty()){
                break;
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customJButton actButton = posMoves.get(random.nextInt(posMoves.size()));
        if (this.turn){
            this.setNorthButton("Ход синих");
        }
        else{
            this.setNorthButton("Ход жёлтых");
        }
        this.firstButtons.remove(this.pressedButton);
        this.field[actButton.getxCor()][actButton.getyCor()].setType(this.pressedButton.getType());
        this.field[actButton.getxCor()][actButton.getyCor()].setIcon(this.pressedButton.getIcon());
        this.field[this.pressedButton.getxCor()][this.pressedButton.getyCor()].setIcon(null);
        this.field[this.pressedButton.getxCor()][this.pressedButton.getyCor()].setType(0);
        this.firstButtons.add(actButton);
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if (this.field[i][j].getType() == -1){
                    this.field[i][j].setType(0);
                    this.field[i][j].setIcon(null);
                }
            }
        }
        this.turn = !this.turn;
        int isWin = this.checkWin(this.gameEnd);
        if (isWin == 1){
            this.setNorthButton("Победили синие");
        }
        else if (isWin == 2){
            this.setNorthButton("Победили жёлтые");
        }
        this.setGameEnd(isWin);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notify();
    }

    public synchronized void secondMove() {
        while (!this.turn){
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        ArrayList<customJButton> posMoves = new ArrayList();
        for (customJButton fM : this.secondButtons){
            this.pressedButton = fM;
            //проверка на запад
            int xCor = this.pressedButton.getxCor();
            int yCor = this.pressedButton.getyCor();
            boolean possibleTurn = true;
            boolean multiTurn = false;
            while (possibleTurn){
                possibleTurn = false;
                if (yCor - 1 > 0) {
                    if ((this.field[xCor][yCor - 1].getType() == 0) && (!multiTurn)) {
                        posMoves.add(this.field[xCor][yCor - 1]);
                        this.field[xCor][yCor - 1].setType(-1);
                        this.field[xCor][yCor - 1].setIcon(possible);
                    } else if ((yCor - 2 > 0) && (this.field[xCor][yCor - 1].getType() != 0) && (this.field[xCor][yCor - 2].getType() == 0)) {
                        posMoves.add(this.field[xCor][yCor - 2]);
                        this.field[xCor][yCor - 2].setType(-1);
                        this.field[xCor][yCor - 2].setIcon(possible);
                        multiTurn = true;
                        yCor -= 2;
                        possibleTurn = true;
                    }
                }
            }
            //проверка на северо-запад
            xCor = this.pressedButton.getxCor();
            yCor = this.pressedButton.getyCor();
            possibleTurn = true;
            multiTurn = false;
            while (possibleTurn){
                possibleTurn = false;
                if ((xCor - 1 > -1) && (yCor - 1 > -1)) {
                    if ((this.field[xCor - 1][yCor - 1].getType() == 0) && (!multiTurn)) {
                        posMoves.add(this.field[xCor - 1][yCor - 1]);
                        this.field[xCor - 1][yCor - 1].setType(-1);
                        this.field[xCor - 1][yCor - 1].setIcon(possible);
                    } else if ((xCor - 2 > -1) && (yCor - 2 > -1) && (this.field[xCor - 1][yCor - 1].getType() != 0) && (this.field[xCor - 2][yCor - 2].getType() == 0)) {
                        posMoves.add(this.field[xCor - 2][yCor - 2]);
                        this.field[xCor - 2][yCor - 2].setType(-1);
                        this.field[xCor - 2][yCor - 2].setIcon(possible);
                        multiTurn = true;
                        yCor -= 2;
                        xCor -= 2;
                        possibleTurn = true;
                    }
                }
            }
            //проверка на север
            xCor = this.pressedButton.getxCor();
            yCor = this.pressedButton.getyCor();
            possibleTurn = true;
            multiTurn = false;
            while (possibleTurn) {
                possibleTurn = false;
                if ((xCor - 1 > -1)) {
                    if ((this.field[xCor - 1][yCor].getType() == 0) && (!multiTurn)) {
                        posMoves.add(this.field[xCor - 1][yCor]);
                        this.field[xCor - 1][yCor].setType(-1);
                        this.field[xCor - 1][yCor].setIcon(possible);
                    } else if ((xCor - 2 > -1) && (this.field[xCor - 1][yCor].getType() != 0) && (this.field[xCor - 2][yCor].getType() == 0)) {
                        posMoves.add(this.field[xCor - 2][yCor]);
                        this.field[xCor - 2][yCor].setType(-1);
                        this.field[xCor - 2][yCor].setIcon(possible);
                        multiTurn = true;
                        xCor -= 2;
                        possibleTurn = true;
                    }
                }
            }
            if (!posMoves.isEmpty()){
                break;
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customJButton actButton = posMoves.get(random.nextInt(posMoves.size()));
        if (this.turn){
            this.setNorthButton("Ход синих");
        }
        else{
            this.setNorthButton("Ход жёлтых");
        }
        this.secondButtons.remove(this.pressedButton);
        this.field[actButton.getxCor()][actButton.getyCor()].setType(this.pressedButton.getType());
        this.field[actButton.getxCor()][actButton.getyCor()].setIcon(this.pressedButton.getIcon());
        this.field[this.pressedButton.getxCor()][this.pressedButton.getyCor()].setIcon(null);
        this.field[this.pressedButton.getxCor()][this.pressedButton.getyCor()].setType(0);
        this.secondButtons.add(actButton);
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if (this.field[i][j].getType() == -1){
                    this.field[i][j].setType(0);
                    this.field[i][j].setIcon(null);
                }
            }
        }
        this.turn = !this.turn;
        int isWin = this.checkWin(this.gameEnd);
        if (isWin == 1){
            this.setNorthButton("Победили синие");
        }
        else if (isWin == 2){
            this.setNorthButton("Победили жёлтые");
        }
        this.setGameEnd(isWin);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notify();
    }

    public void newGane () {
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if ((i>=0 && i<=3) && (j>=0 && j<=3)){
                    this.field[i][j].setxCor(i);
                    this.field[i][j].setyCor(j);
                    this.field[i][j].setType(1);
                    this.field[i][j].setIcon(this.firstPlayer);
                }
                else if((i>=4 && i<=7) && (j>=4 && j<=7)){
                    this.field[i][j].setxCor(i);
                    this.field[i][j].setyCor(j);
                    this.field[i][j].setType(2);
                    this.field[i][j].setIcon(this.secondPlayer);
                }
                else{
                    this.field[i][j].setxCor(i);
                    this.field[i][j].setyCor(j);
                    this.field[i][j].setType(0);
                    this.field[i][j].setIcon(null);
                }
            }
        }
        this.turn = false;
        this.gameEnd = 0;
        this.pressedButton = null;
    }

    public int checkWin (int gameEnd){
        boolean firstWin = true;
        boolean secondWin = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i>=0 && i<=3) && (j>=0 && j<=3)){
                    if (this.field[i][j].getType() != 2){
                        secondWin = false;
                    }
                }
                else if((i>=4 && i<=7) && (j>=4 && j<=7)){
                    if (this.field[i][j].getType() != 1){
                        firstWin = false;
                    }
                }
            }
        }
        if (firstWin){
            return 1;
        }
        else if (secondWin){
            return  2;
        }
        else {
            return 0;
        }
    }

    public customJButton getPressedButton() {
        return pressedButton;
    }

    public void setPressedButton(customJButton pressedButton) {
        this.pressedButton = pressedButton;
    }

    public void setNorthButton(String text) {
        this.northButton.setText(text);
    }

    public JButton getNorthButton() {
        return northButton;
    }

    public customJButton[][] getField() {
        return field;
    }

    public void setField(customJButton[][] field) {
        this.field = field;
    }

    public boolean getTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public int getGameEnd() {
        return gameEnd;
    }

    public void setGameEnd(int gameEnd) {
        this.gameEnd = gameEnd;
    }
}

class Player1 extends Thread{
    public Corners game;

    public Player1(Corners game) {
        this.game = game;
    }

    @Override
    public void run (){
        while (this.game.getGameEnd() == 0 ){
            this.game.firstMove();
        }
    }
}

class Player2 extends Thread{
    public Corners game;

    public Player2(Corners game) {
        this.game = game;
    }

    @Override
    public void run (){
        while (this.game.getGameEnd() == 0 ){
            this.game.secondMove();
        }
    }
}

class customJButton extends JButton{
    private int xCor;
    private int yCor;
    //-1 - пустая ячейка, в которую возможно сделать ход
    //0 - пустая ячейка
    //1 - синия шашка
    //2 - жёлтая шашка
    private int type;

    public customJButton (int x, int y , int buttonType){
        super();
        this.xCor = x;
        this.yCor = y;
        this.type = buttonType;
    }

    public int getxCor() {
        return xCor;
    }

    public void setxCor(int xCor) {
        this.xCor = xCor;
    }

    public int getyCor() {
        return yCor;
    }

    public void setyCor(int yCor) {
        this.yCor = yCor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

class Board extends JPanel{
    public Corners game;

    public Board(Corners currentGame){
        this.game = currentGame;
        setLayout(new GridLayout(8, 8, 5, 5));
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                add(this.game.getField()[i][j]);
            }
        }
        setSize(300, 400);
        setVisible(true);
    }

}

public class Gui2 {
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run (){
                JFrame f = new JFrame("Уголки");
                JButton bn = new JButton("Ход синих");
                bn.setPreferredSize(new Dimension(400, 50));
                f.add(bn, BorderLayout.NORTH);
                Corners game = new Corners(bn);
                JPanel playBoard = new Board(game);
                f.add(playBoard);
                f.setVisible(true);
                f.setLocation(400,200);
                f.setSize(600,600);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Player1 Blue = new Player1(game);
                Player2 Yellow = new Player2 (game);
                Blue.start();
                Yellow.start();
            }
        });
    }
}
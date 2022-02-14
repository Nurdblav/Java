package corner;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.*;
import java.util.*;

class Corners {
    private customJButton[][] field = new customJButton[8][8];
    //false - first player's turn
    //true - second player's turn
    private boolean turn;
    //0 - active game
    //1 - first player win
    //2 - second player win
    private int gameEnd;
    // first player's turns counter

    private customJButton pressedButton;
    private JButton northButton;

    public Corners(JButton northButton) {
        ImageIcon firstPlayer = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/3.jpg");
        ImageIcon secondPlayer = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/4.jpg");
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if ((i>=0 && i<=3) && (j>=0 && j<=3)){
                    this.field[i][j] = new customJButton(i, j,1);
                    this.field[i][j].setIcon(firstPlayer);
                    this.field[i][j].addActionListener(new customAction(this.field[i][j],this));
                }
                else if((i>=4 && i<=7) && (j>=4 && j<=7)){
                    this.field[i][j] = new customJButton(i, j,2);
                    this.field[i][j].setIcon(secondPlayer);
                    this.field[i][j].addActionListener(new customAction(this.field[i][j],this));
                }
                else{
                    this.field[i][j] = new customJButton(i, j,0);
                    this.field[i][j].addActionListener(new customAction(this.field[i][j],this));
                }
            }
        }
        this.turn = false;
        this.gameEnd = 0;
        this.pressedButton = null;
        this.northButton = northButton;
    }

    public void newGane () {
        ImageIcon firstPlayer = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/3.jpg");
        ImageIcon secondPlayer = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/4.jpg");
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if ((i>=0 && i<=3) && (j>=0 && j<=3)){
                    this.field[i][j].setxCor(i);
                    this.field[i][j].setyCor(j);
                    this.field[i][j].setType(1);
                    this.field[i][j].setIcon(firstPlayer);
                }
                else if((i>=4 && i<=7) && (j>=4 && j<=7)){
                    this.field[i][j].setxCor(i);
                    this.field[i][j].setyCor(j);
                    this.field[i][j].setType(2);
                    this.field[i][j].setIcon(secondPlayer);
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

class customAction implements ActionListener{
    private customJButton actButton;
    private Corners game;

    public customAction(customJButton curButton, Corners currentGame) {
        this.game = currentGame;
        this.actButton = curButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ImageIcon firstPlayer = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/3.jpg");
        ImageIcon secondPlayer = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/4.jpg");
        ImageIcon possible = new ImageIcon("C:\\Users\\wasto\\Desktop\\uni\\46гр/5.jpg");
        customJButton[][] gameField = this.game.getField();
        if (this.game.getGameEnd() == 0){
            if ((actButton.getType() == 1) && (! this.game.getTurn())){
                for (int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        if (gameField[i][j].getType() == -1){
                            gameField[i][j].setType(0);
                            gameField[i][j].setIcon(null);
                        }
                    }
                }
                //проверка на восток
                int xCor = actButton.getxCor();
                int yCor = actButton.getyCor();
                boolean possibleTurn = true;
                boolean multiTurn = false;
                    while (possibleTurn){
                        possibleTurn = false;
                        if (yCor + 1 < 8) {
                            if ((gameField[xCor][yCor + 1].getType() == 0) && (!multiTurn)) {
                                gameField[xCor][yCor + 1].setType(-1);
                                gameField[xCor][yCor + 1].setIcon(possible);
                            } else if ((yCor + 2 < 8) && (gameField[xCor][yCor + 1].getType() != 0) && (gameField[xCor][yCor + 2].getType() == 0)) {
                                gameField[xCor][yCor + 2].setType(-1);
                                gameField[xCor][yCor + 2].setIcon(possible);
                                multiTurn = true;
                                yCor += 2;
                                possibleTurn = true;
                            }
                        }
                    }
                //проверка на юго-восток
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor + 1 < 8) && (yCor + 1 < 8)) {
                        if ((gameField[xCor + 1][yCor + 1].getType() == 0) && (!multiTurn)) {
                            gameField[xCor + 1][yCor + 1].setType(-1);
                            gameField[xCor + 1][yCor + 1].setIcon(possible);
                        } else if ((xCor + 2 < 8) && (yCor + 2 < 8) && (gameField[xCor + 1][yCor + 1].getType() != 0) && (gameField[xCor + 2][yCor + 2].getType() == 0)) {
                            gameField[xCor + 2][yCor + 2].setType(-1);
                            gameField[xCor + 2][yCor + 2].setIcon(possible);
                            multiTurn = true;
                            yCor += 2;
                            xCor += 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на юг
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor + 1 < 8)) {
                        if ((gameField[xCor + 1][yCor].getType() == 0) && (!multiTurn)) {
                            gameField[xCor + 1][yCor].setType(-1);
                            gameField[xCor + 1][yCor].setIcon(possible);
                        } else if ((xCor + 2 < 8) && (gameField[xCor + 1][yCor].getType() != 0) && (gameField[xCor + 2][yCor].getType() == 0)) {
                            gameField[xCor + 2][yCor].setType(-1);
                            gameField[xCor + 2][yCor].setIcon(possible);
                            multiTurn = true;
                            xCor += 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на юго-запад
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor + 1 < 8) && (yCor - 1 > -1)){
                    if ((gameField[xCor+1][yCor-1].getType() == 0) && (!multiTurn)){
                        gameField[xCor+1][yCor-1].setType(-1);
                        gameField[xCor+1][yCor-1].setIcon(possible);
                    }
                    else if((xCor + 2 < 8) && (yCor - 2 > -1) && (gameField[xCor+1][yCor-1].getType() != 0)  && (gameField[xCor+2][yCor-2].getType() == 0)){
                        gameField[xCor+2][yCor-2].setType(-1);
                        gameField[xCor+2][yCor-2].setIcon(possible);
                        multiTurn = true;
                        yCor -= 2;
                        xCor += 2;
                        possibleTurn = true;
                    }
                    }
                }
                //проверка на запад
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if (yCor - 1 > 0) {
                        if ((gameField[xCor][yCor - 1].getType() == 0) && (!multiTurn)) {
                            gameField[xCor][yCor - 1].setType(-1);
                            gameField[xCor][yCor - 1].setIcon(possible);
                        } else if ((yCor - 2 > 0) && (gameField[xCor][yCor - 1].getType() != 0) && (gameField[xCor][yCor - 2].getType() == 0)) {
                            gameField[xCor][yCor - 2].setType(-1);
                            gameField[xCor][yCor - 2].setIcon(possible);
                            multiTurn = true;
                            yCor -= 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на северо-запад
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor - 1 > -1) && (yCor - 1 > -1)) {
                        if ((gameField[xCor - 1][yCor - 1].getType() == 0) && (!multiTurn)) {
                            gameField[xCor - 1][yCor - 1].setType(-1);
                            gameField[xCor - 1][yCor - 1].setIcon(possible);
                        } else if ((xCor - 2 > -1) && (yCor - 2 > -1) && (gameField[xCor - 1][yCor - 1].getType() != 0) && (gameField[xCor - 2][yCor - 2].getType() == 0)) {
                            gameField[xCor - 2][yCor - 2].setType(-1);
                            gameField[xCor - 2][yCor - 2].setIcon(possible);
                            multiTurn = true;
                            yCor -= 2;
                            xCor -= 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на север
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor - 1 > -1)) {
                        if ((gameField[xCor - 1][yCor].getType() == 0) && (!multiTurn)) {
                            gameField[xCor - 1][yCor].setType(-1);
                            gameField[xCor - 1][yCor].setIcon(possible);
                        } else if ((xCor - 2 > -1) && (gameField[xCor - 1][yCor].getType() != 0) && (gameField[xCor - 2][yCor].getType() == 0)) {
                            gameField[xCor - 2][yCor].setType(-1);
                            gameField[xCor - 2][yCor].setIcon(possible);
                            multiTurn = true;
                            xCor -= 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на северо-восток
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor - 1 > -1) && (yCor + 1 < 8)) {
                        if ((gameField[xCor - 1][yCor + 1].getType() == 0) && (!multiTurn)) {
                            gameField[xCor - 1][yCor + 1].setType(-1);
                            gameField[xCor - 1][yCor + 1].setIcon(possible);
                        } else if ((xCor - 2 > -1) && (yCor + 2 < 8) && (gameField[xCor - 1][yCor + 1].getType() != 0) && (gameField[xCor - 2][yCor + 2].getType() == 0)) {
                            gameField[xCor - 2][yCor + 2].setType(-1);
                            gameField[xCor - 2][yCor + 2].setIcon(possible);
                            multiTurn = true;
                            yCor += 2;
                            xCor -= 2;
                            possibleTurn = true;
                        }
                    }
                }
                this.game.setField(gameField);
                this.game.setPressedButton(actButton);
            }
            else if ((actButton.getType() == 2) && ( this.game.getTurn())){
                for (int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        if (gameField[i][j].getType() == -1){
                            gameField[i][j].setType(0);
                            gameField[i][j].setIcon(null);
                        }
                    }
                }
                //проверка на восток
                int xCor = actButton.getxCor();
                int yCor = actButton.getyCor();
                boolean possibleTurn = true;
                boolean multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if (yCor + 1 < 8) {
                        if ((gameField[xCor][yCor + 1].getType() == 0) && (!multiTurn)) {
                            gameField[xCor][yCor + 1].setType(-1);
                            gameField[xCor][yCor + 1].setIcon(possible);
                        } else if ((yCor + 2 < 8) && (gameField[xCor][yCor + 1].getType() != 0) && (gameField[xCor][yCor + 2].getType() == 0)) {
                            gameField[xCor][yCor + 2].setType(-1);
                            gameField[xCor][yCor + 2].setIcon(possible);
                            multiTurn = true;
                            yCor += 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на юго-восток
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor + 1 < 8) && (yCor + 1 < 8)) {
                        if ((gameField[xCor + 1][yCor + 1].getType() == 0) && (!multiTurn)) {
                            gameField[xCor + 1][yCor + 1].setType(-1);
                            gameField[xCor + 1][yCor + 1].setIcon(possible);
                        } else if ((xCor + 2 < 8) && (yCor + 2 < 8) && (gameField[xCor + 1][yCor + 1].getType() != 0) && (gameField[xCor + 2][yCor + 2].getType() == 0)) {
                            gameField[xCor + 2][yCor + 2].setType(-1);
                            gameField[xCor + 2][yCor + 2].setIcon(possible);
                            multiTurn = true;
                            yCor += 2;
                            xCor += 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на юг
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor + 1 < 8)) {
                        if ((gameField[xCor + 1][yCor].getType() == 0) && (!multiTurn)) {
                            gameField[xCor + 1][yCor].setType(-1);
                            gameField[xCor + 1][yCor].setIcon(possible);
                        } else if ((xCor + 2 < 8) && (gameField[xCor + 1][yCor].getType() != 0) && (gameField[xCor + 2][yCor].getType() == 0)) {
                            gameField[xCor + 2][yCor].setType(-1);
                            gameField[xCor + 2][yCor].setIcon(possible);
                            multiTurn = true;
                            xCor += 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на юго-запад
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor + 1 < 8) && (yCor - 1 > -1)){
                        if ((gameField[xCor+1][yCor-1].getType() == 0) && (!multiTurn)){
                            gameField[xCor+1][yCor-1].setType(-1);
                            gameField[xCor+1][yCor-1].setIcon(possible);
                        }
                        else if((xCor + 2 < 8) && (yCor - 2 > -1) && (gameField[xCor+1][yCor-1].getType() != 0)  && (gameField[xCor+2][yCor-2].getType() == 0)){
                            gameField[xCor+2][yCor-2].setType(-1);
                            gameField[xCor+2][yCor-2].setIcon(possible);
                            multiTurn = true;
                            yCor -= 2;
                            xCor += 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на запад
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if (yCor - 1 > 0) {
                        if ((gameField[xCor][yCor - 1].getType() == 0) && (!multiTurn)) {
                            gameField[xCor][yCor - 1].setType(-1);
                            gameField[xCor][yCor - 1].setIcon(possible);
                        } else if ((yCor - 2 > 0) && (gameField[xCor][yCor - 1].getType() != 0) && (gameField[xCor][yCor - 2].getType() == 0)) {
                            gameField[xCor][yCor - 2].setType(-1);
                            gameField[xCor][yCor - 2].setIcon(possible);
                            multiTurn = true;
                            yCor -= 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на северо-запад
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor - 1 > -1) && (yCor - 1 > -1)) {
                        if ((gameField[xCor - 1][yCor - 1].getType() == 0) && (!multiTurn)) {
                            gameField[xCor - 1][yCor - 1].setType(-1);
                            gameField[xCor - 1][yCor - 1].setIcon(possible);
                        } else if ((xCor - 2 > -1) && (yCor - 2 > -1) && (gameField[xCor - 1][yCor - 1].getType() != 0) && (gameField[xCor - 2][yCor - 2].getType() == 0)) {
                            gameField[xCor - 2][yCor - 2].setType(-1);
                            gameField[xCor - 2][yCor - 2].setIcon(possible);
                            multiTurn = true;
                            yCor -= 2;
                            xCor -= 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на север
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor - 1 > -1)) {
                        if ((gameField[xCor - 1][yCor].getType() == 0) && (!multiTurn)) {
                            gameField[xCor - 1][yCor].setType(-1);
                            gameField[xCor - 1][yCor].setIcon(possible);
                        } else if ((xCor - 2 > -1) && (gameField[xCor - 1][yCor].getType() != 0) && (gameField[xCor - 2][yCor].getType() == 0)) {
                            gameField[xCor - 2][yCor].setType(-1);
                            gameField[xCor - 2][yCor].setIcon(possible);
                            multiTurn = true;
                            xCor -= 2;
                            possibleTurn = true;
                        }
                    }
                }
                //проверка на северо-восток
                xCor = actButton.getxCor();
                yCor = actButton.getyCor();
                possibleTurn = true;
                multiTurn = false;
                while (possibleTurn){
                    possibleTurn = false;
                    if ((xCor - 1 > -1) && (yCor + 1 < 8)) {
                        if ((gameField[xCor - 1][yCor + 1].getType() == 0) && (!multiTurn)) {
                            gameField[xCor - 1][yCor + 1].setType(-1);
                            gameField[xCor - 1][yCor + 1].setIcon(possible);
                        } else if ((xCor - 2 > -1) && (yCor + 2 < 8) && (gameField[xCor - 1][yCor + 1].getType() != 0) && (gameField[xCor - 2][yCor + 2].getType() == 0)) {
                            gameField[xCor - 2][yCor + 2].setType(-1);
                            gameField[xCor - 2][yCor + 2].setIcon(possible);
                            multiTurn = true;
                            yCor += 2;
                            xCor -= 2;
                            possibleTurn = true;
                        }
                    }
                }
                this.game.setField(gameField);
                this.game.setPressedButton(actButton);
            }
            else if (actButton.getType() == -1){
                if (this.game.getTurn()){
                    this.game.setNorthButton("Ход синих");
                }
                else{
                    this.game.setNorthButton("Ход жёлтых");
                }
                gameField[actButton.getxCor()][actButton.getyCor()].setType(this.game.getPressedButton().getType());
                gameField[actButton.getxCor()][actButton.getyCor()].setIcon(this.game.getPressedButton().getIcon());
                gameField[this.game.getPressedButton().getxCor()][this.game.getPressedButton().getyCor()].setIcon(null);
                gameField[this.game.getPressedButton().getxCor()][this.game.getPressedButton().getyCor()].setType(0);
                for (int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        if (gameField[i][j].getType() == -1){
                            gameField[i][j].setType(0);
                            gameField[i][j].setIcon(null);
                        }
                    }
                }
                this.game.setTurn(!this.game.getTurn());
                this.game.setField(gameField);
                int isWin = this.game.checkWin(this.game.getGameEnd());
                if (isWin == 1){
                    this.game.setNorthButton("Победили синие");
                }
                else if (isWin == 2){
                    this.game.setNorthButton("Победили жёлтые");
                }
                this.game.setGameEnd(isWin);

            }
        }
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

public class Gui {
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
            }
        });
    }
}

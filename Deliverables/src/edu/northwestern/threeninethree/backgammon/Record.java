package edu.northwestern.threeninethree.backgammon;

public class Record {

    private int wins;
    private int losses;

    public Record(){
        this.wins = 0;
        this.losses = 0;
    }

    public int getWins(){
        return this.wins;
    }

    public int getLosses(){
        return this.losses;
    }

    public void addWin(){
        this.wins++;
    }

    public void subtractWin(){
        this.wins--;
    }

    public void addLoss(){
        this.losses++;
    }

    public void subtractLoss(){
        this.losses--;
    }

    public void forfeitWin(){
        this.subtractWin();
        this.addLoss();
    }

    public void forfeitWinTo(Record rrr){
        this.subtractWin();
        this.addLoss();
        rrr.addWin();
        rrr.subtractLoss();
    }
}

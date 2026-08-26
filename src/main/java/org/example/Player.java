package org.example;

public class Player {
    private String name;
    private Integer currentCell;
    private boolean winStatus;
    Player(String name, Integer currentCell){
        this.name=name;
        this.currentCell=currentCell;
        this.winStatus=false;
    }

    public void setWinStatus(boolean winStatus) {
        this.winStatus = winStatus;
    }

    public boolean isWinStatus() {
        return winStatus;
    }

    public String getName() {
        return name;
    }

    public Integer getCurrentCell() {
        return currentCell;
    }


    void moveToCell(Integer newCell){
        this.currentCell=newCell;
    }
}

package ch.supsi.minesweeper.model;

public class GameModel extends AbstractModel implements GameEventHandler, PlayerEventHandler{

    private static GameModel myself;

    private GameModel() {
        super();
    }

    public static GameModel getInstance() {
        if (myself == null) {
            myself = new GameModel();
        }

        return myself;
    }

    @Override
    public void newGame() {

    }

    @Override
    public void save() {

    }

    @Override
    public void move() {
        return;
    }

    // add all the relevant missing behaviours
    // ...

}

package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.model.AbstractModel;

public interface ControlledView extends DataView {

    void initialize(EventHandler eventHandler, AbstractModel model);

}

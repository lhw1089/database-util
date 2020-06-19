package com.jean.database.api.view.treeitem;

import com.jean.database.api.view.ViewContext;
import com.jean.database.api.view.action.ICloseable;
import com.jean.database.api.view.action.IContextMenu;
import com.jean.database.api.view.action.IMouseAction;
import com.jean.database.api.view.action.IRefreshable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;


public abstract class BaseTreeItem<T> extends TreeItem<T> implements IContextMenu, IMouseAction, IRefreshable, ICloseable {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "onOpen", false);
    private final ViewContext viewContext;

    public BaseTreeItem(T value, ViewContext viewContext) {
        super(value);
        this.viewContext = viewContext;
    }

    public boolean isOpen() {
        return open.get();
    }

    public BooleanProperty openProperty() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open.set(open);
    }

    @Override
    public ContextMenu getContextMenu() {
        return null;
    }

    @Override
    public void click() {
    }

    @Override
    public void doubleClick() {
    }

    @Override
    public void select() {
    }

    @Override
    public void refresh() {
    }

    @Override
    public void close() {

    }

    public ViewContext getViewContext() {
        return viewContext;
    }
}

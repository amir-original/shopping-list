package shop.frontend;

import javax.swing.*;
import java.awt.*;

public class ShoppingListPage {

    private JFrame page;
    private final int width;
    private final int height;
    private final String title;
    private Component alertBox;

    public ShoppingListPage(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    void generate() {
        page = new JFrame();
        initConfig();
    }

    void initConfig() {
        page.setTitle(title);
        page.setSize(width, height);
        page.setLayout(null);
        page.setVisible(true);
    }

    void addComponents(Component[] components) {
        for (Component component : components)
            addComponent(component);
    }

    void addUiItem(UiItem item) {
        addComponent(item.getNameComponent());
        addComponent(item.getQuantityComponent());
    }

    void addComponent(Component component) {
        page.add(component);
        page.repaint();
    }

    Component[] getAllComponents() {
        return page.getContentPane().getComponents();
    }

    void removeUiItem(UiItem uiItem) {
        removeComponent(uiItem.getQuantityComponent());
        removeComponent(uiItem.getNameComponent());
    }

    void removeComponent(Component component) {
        page.getContentPane().remove(component);
        page.repaint();
        page.revalidate();
    }

    void throwAlert(Component alert) {
        clearAlert();
        addComponent(alert);
        alertBox = alert;
    }

    void clearAlert() {
        if (isAlertNotEmpty())
            removeComponent(alertBox);
    }

    public boolean isAlertNotEmpty() {
        return !isAlertEmpty();
    }

    public boolean isAlertEmpty() {
        return alertBox == null;
    }
}

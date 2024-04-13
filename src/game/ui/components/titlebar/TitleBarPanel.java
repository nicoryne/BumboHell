package game.ui.components.titlebar;

import game.exceptions.FontHandlerException;
import game.util.handlers.FontHandler;
import game.util.handlers.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TitleBarPanel extends JPanel {

    private JLabel iconLabel;

    private JLabel titleLabel;

    private JPanel buttonContainer;

    private TitleBarButton exitButton;

    private TitleBarButton minimizeButton;

    private int iconWidth = 32;

    private int iconHeight = 32;

    private static final Dimension BUTTON_CONTAINER_SIZE = (new Dimension(100, 50));
    private static final Dimension TITLE_PANEL_SIZE = (new Dimension(768, 40));
    private static final Color PANEL_BACKGROUND_COLOR = Color.black;
    private static final Color TEXT_FOREGROUND_COLOR = Color.white;
    private static final String FONT_NAME = "font-1.ttf";
    private static final float FONT_SIZE = 16f;

    public TitleBarPanel(JFrame windowFrame){
        this.iconLabel = new JLabel();
        this.titleLabel = new JLabel();
        this.buttonContainer = new JPanel();
        this.exitButton = new TitleBarButton("X", e -> System.exit(0));
        this.minimizeButton = new TitleBarButton("---", e -> windowFrame.setExtendedState(Frame.ICONIFIED));

        DraggableTitleBarListener draggableTitleBarListener = new DraggableTitleBarListener(this, windowFrame);
        windowFrame.addMouseMotionListener(draggableTitleBarListener);
        windowFrame.addMouseListener(draggableTitleBarListener);

        decorateComponents();

        this.setLayout(new BorderLayout());
        this.setBackground(PANEL_BACKGROUND_COLOR);
        this.setPreferredSize(TITLE_PANEL_SIZE);
        this.add(iconLabel, BorderLayout.WEST);
        this.add(titleLabel, BorderLayout.CENTER);
        this.add(buttonContainer, BorderLayout.EAST);
    }

    private void decorateComponents() {
        decorateIcon();
        decorateTitleLabel();
        decorateButtonContainer();
    }

    private void decorateIcon() {
        ImageIcon originalIcon = ImageHandler.getImageIcon("src/res/icon.png");
        assert originalIcon != null;
        ImageIcon resizedIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

        iconLabel.setIcon(resizedIcon);
        iconLabel.setLayout(new BorderLayout());
        iconLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 20, 0, 0),
                iconLabel.getBorder()));
    }

    private void decorateTitleLabel(){
        Font titleFont;
        try {
            titleFont = FontHandler.getFont(FONT_NAME, FONT_SIZE);
        } catch (FontHandlerException e) {
            throw new RuntimeException(e);
        }

        titleLabel.setText("umbo Hell");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(TEXT_FOREGROUND_COLOR);
    }

    private void decorateButtonContainer() {
        buttonContainer.setLayout(new BorderLayout());
        buttonContainer.setPreferredSize(BUTTON_CONTAINER_SIZE);
        buttonContainer.setOpaque(false);
        buttonContainer.add(exitButton, BorderLayout.EAST);
        buttonContainer.add(minimizeButton, BorderLayout.CENTER);
    }
}
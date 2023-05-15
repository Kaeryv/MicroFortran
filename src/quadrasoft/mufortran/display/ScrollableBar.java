package quadrasoft.mufortran.display;

import javax.swing.*;
import java.awt.*;

public class ScrollableBar extends JComponent implements SwingConstants {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    static {
        UIManager.put("ScrollableBarUI", "quadrasoft.mufortran.gui.ScrollableBarUI");
    }

    private Component comp;

    private boolean horizontal, small;

    private int inc;

    public ScrollableBar(Component comp) {
        this(comp, HORIZONTAL);
    }

    public ScrollableBar(Component comp, int orientation) {
        this.comp = comp;
        if (orientation == HORIZONTAL) {
            horizontal = true;
        } else {
            horizontal = false;
        }
        small = true;
        inc = 8;
        updateUI();
    }

    public Component getComponent() {
        return comp;
    }

    public void setComponent(Component comp) {
        if (this.comp != comp) {
            Component old = this.comp;
            this.comp = comp;
            firePropertyChange("component", old, comp);
        }
    }

    public int getIncrement() {
        return inc;
    }

    public void setIncrement(int inc) {
        if (inc > 0 && inc != this.inc) {
            int old = this.inc;
            this.inc = inc;
            firePropertyChange("increment", old, inc);
        }
    }

    @Override
    public String getUIClassID() {
        return "ScrollableBarUI";
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public boolean isSmallArrows() {
        return small;
    }

    public void setSmallArrows(boolean small) {
        if (small != this.small) {
            boolean old = this.small;
            this.small = small;
            firePropertyChange("smallArrows", old, small);
        }
    }

    public void setOrientation(int orientation) {
        if (orientation == HORIZONTAL) {
            horizontal = true;
        } else {
            horizontal = false;
        }

        updateUI();
    }

    //@Override
    //public void updateUI() {
    //    setUI(UIManager.getUI(this));
    //    invalidate();
    //}
}

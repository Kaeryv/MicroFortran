package quadrasoft.mufortran.display;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ScrollableBarUI extends ComponentUI
        implements SwingConstants, MouseListener, ChangeListener, PropertyChangeListener, MouseWheelListener {

    private ScrollableBar sb;
    private JViewport scroll;
    private JButton scrollF, scrollB;
    private boolean pressed = false;
    private int inc;

    public static ComponentUI createUI(JComponent c) {
        return new ScrollableBarUI();
    }

    protected JButton createButton(int direction, int width, boolean small) {
        JButton button = new ScrollButton(direction, width, small);
        button.setAlignmentX(0.5f);
        button.setAlignmentY(0.5f);
        return button;
    }

    @Override
    public void installUI(JComponent c) {

        sb = (ScrollableBar) c;

        inc = sb.getIncrement();
        boolean small = sb.isSmallArrows();

        int sbSize = ((Integer) (UIManager.get("ScrollBar.width"))).intValue();
        scrollB = createButton(sb.isHorizontal() ? WEST : NORTH, sbSize, small);
        scrollB.setVisible(false);
        scrollB.addMouseListener(this);

        scrollF = createButton(sb.isHorizontal() ? EAST : SOUTH, sbSize, small);
        scrollF.setVisible(false);
        scrollF.addMouseListener(this);

        int axis = sb.isHorizontal() ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS;
        sb.setLayout(new BoxLayout(sb, axis));

        scroll = new JViewport() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            protected LayoutManager createLayoutManager() {
                return new ViewportLayout() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Dimension minimumLayoutSize(Container parent) {
                        Component view = ((JViewport) parent).getView();
                        if (view == null) {
                            return new Dimension(4, 4);
                        } else {
                            Dimension d = view.getPreferredSize();
                            if (sb.isHorizontal()) {
                                return new Dimension(4, (int) d.getHeight());
                            } else {
                                return new Dimension((int) d.getWidth(), 4);
                            }
                        }
                    }
                };
            }
            // ...
        };

        Component box = sb.getComponent();
        scroll.setView(box);

        sb.add(scrollB);
        sb.add(scroll);
        sb.add(scrollF);

        scroll.addChangeListener(this);
        sb.addPropertyChangeListener(this);
        sb.addMouseWheelListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        pressed = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true;
        final Object o = e.getSource();
        Thread scroller = new Thread(new Runnable() {
            @Override
            public void run() {
                int accl = 500;
                Point p;
                while (pressed) {
                    p = scroll.getViewPosition();
                    sb.setVisible(false);
                    if (sb.isHorizontal()) {
                        if (o.equals(scrollB)) {
                            p.x -= inc;
                            if (p.x < 0) {
                                p.x = 0;
                                scroll.setViewPosition(p);
                                sb.setVisible(true);
                                return;
                            }
                        } else {
                            if (scroll.getViewSize().width - p.x - scroll.getExtentSize().width > inc) {
                                p.x += inc;
                            } else {
                                p.x = scroll.getViewSize().width - scroll.getExtentSize().width;
                                scroll.setViewPosition(p);
                                sb.setVisible(true);
                                return;
                            }
                        }
                    } else {
                        if (o.equals(scrollB)) {
                            p.y -= inc;
                            if (p.y < 0) {
                                p.y = 0;
                                scroll.setViewPosition(p);
                                sb.setVisible(true);
                                return;
                            }
                        } else {
                            if (scroll.getViewSize().height - p.y - scroll.getExtentSize().height > inc) {
                                p.y += inc;
                            } else {
                                p.y = scroll.getViewSize().height - scroll.getExtentSize().height;
                                scroll.setViewPosition(p);
                                sb.setVisible(true);
                                return;
                            }
                        }
                    }
                    // ...
                    scroll.setViewPosition(p);
                    sb.setVisible(true);
                    try {
                        Thread.sleep(accl);
                        if (accl <= 10)
                            accl = 10;
                        else
                            accl /= 2;
                    } catch (InterruptedException ie) {
                    }
                }
            }
        });
        scroller.start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Point p = scroll.getViewPosition();
        if (e.getWheelRotation() > 0) {
            if (sb.isHorizontal()) {
                if (scroll.getViewSize().width - p.x - scroll.getExtentSize().width > inc) {
                    p.x += inc;
                } else {
                    p.x = scroll.getViewSize().width - scroll.getExtentSize().width;
                    scroll.setViewPosition(p);
                    return;
                }
            } else {
                if (scroll.getViewSize().height - p.y - scroll.getExtentSize().height > inc) {
                    p.y += inc;
                } else {
                    p.y = scroll.getViewSize().height - scroll.getExtentSize().height;
                    scroll.setViewPosition(p);
                    return;
                }
            }
        } else {
            if (sb.isHorizontal()) {
                p.x -= inc;
                if (p.x < 0) {
                    p.x = 0;
                    scroll.setViewPosition(p);
                    return;
                }
            } else {
                p.y -= inc;
                if (p.y < 0) {
                    p.y = 0;
                    scroll.setViewPosition(p);
                    return;
                }
            }
        }
        // ...
        scroll.setViewPosition(p);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("increment".equals(evt.getPropertyName())) {
            inc = ((Integer) evt.getNewValue()).intValue();
        } else if ("smallArrows".equals(evt.getPropertyName())) {
            boolean small = ((Boolean) evt.getNewValue()).booleanValue();
            ((ScrollButton) scrollB).setSmallArrows(small);
            ((ScrollButton) scrollF).setSmallArrows(small);
        } else if ("component".equals(evt.getPropertyName())) {
            scroll.setView((Component) evt.getNewValue());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Point p = scroll.getViewPosition();
        boolean cond = sb.isHorizontal() ? sb.getWidth() < scroll.getViewSize().width
                : sb.getHeight() < scroll.getViewSize().height;
        if (cond) {
            if (sb.isHorizontal() && p.x != 0)
                scrollB.setVisible(true);
            else if (!sb.isHorizontal() && p.y != 0)
                scrollB.setVisible(true);
            else
                scrollB.setVisible(false);

            if (sb.isHorizontal() && p.x != scroll.getViewSize().width - scroll.getExtentSize().width)
                scrollF.setVisible(true);
            else if (!sb.isHorizontal() && p.y != scroll.getViewSize().height - scroll.getExtentSize().height)
                scrollF.setVisible(true);
            else
                scrollF.setVisible(false);
        } else {
            scrollB.setVisible(false);
            scrollF.setVisible(false);
            sb.doLayout();
        }
    }

    @Override
    public void uninstallUI(JComponent c) {
        scroll.removeChangeListener(this);
        sb.removePropertyChangeListener(this);
        sb.removeMouseWheelListener(this);
        sb.removeAll();
    }

}

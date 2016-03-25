package org.swingeasy;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Window.setFocusCycleRoot(true);<br>
 * Window.setFocusTraversalPolicyProvider(true);<br>
 * JComponent layeredPane = (JComponent) Window.getRootPane().getComponents()[1];<br>
 * JComponent container = (JComponent) layeredPane.getComponents()[0];<br>
 * CustomFocusTraversalPolicy policy = new CustomFocusTraversalPolicy(Arrays.asList(container.getComponents()));<br>
 * this.setFocusTraversalPolicy(policy);<br>
 * <br>
 * (static) vertical focus traversal based on x,y position; use this in forms/miglayout
 *
 * @author Jurgen
 */
public class CustomFocusTraversalPolicy extends FocusTraversalPolicy {
    protected final List<Component> orderedList;

    protected int lastIndex = -1;

    protected static final Logger logger = Logger.getLogger(CustomFocusTraversalPolicy.class.getName());

    public static final Comparator<Component> XY = new Comparator<Component>() {
        @Override
        public int compare(Component o1, Component o2) {
            return new CompareToBuilder().append(o1.getX(), o2.getX()).append(o1.getY(), o2.getY()).toComparison();
        }
    };

    public static final Comparator<Component> YX = new Comparator<Component>() {
        @Override
        public int compare(Component o1, Component o2) {
            return new CompareToBuilder().append(o1.getY(), o2.getY()).append(o1.getX(), o2.getX()).toComparison();
        }
    };

    protected CustomFocusTraversalPolicy(List<Component> fixedList) {
        this(fixedList, CustomFocusTraversalPolicy.XY);
    }

    protected CustomFocusTraversalPolicy(List<Component> fixedList, Comparator<Component> sort) {
        this.orderedList = new ArrayList<Component>();
        for (Component component : fixedList) {
            if (component.isFocusable()) {
                this.orderedList.add(component);
            }
        }
        Collections.sort(this.orderedList, sort);
    }

    protected boolean canHaveFocus(Component aComponent) {
        if (aComponent instanceof JTextComponent) {
            return JTextComponent.class.cast(aComponent).isEditable() && aComponent.isEnabled();
        }
        return aComponent.isEnabled();
    }

    public Component componentAfter(Component startComponent, Container aContainer, Component aComponent) {
        int index = this.indexOf(aComponent);
        this.debug(index + ":" + aComponent.getName());
        if (index == -1) {
            this.debug("index not found, setting to " + this.lastIndex);
            index = this.lastIndex;
        }
        index++;
        if (index >= this.orderedList.size()) {
            index = 0;
        }
        this.lastIndex = index;
        Component jComponent = this.orderedList.get(index);
        if (!this.canHaveFocus(jComponent) && (startComponent.getName() != null) && !startComponent.getName().equals(jComponent.getName())) {
            return this.componentAfter(startComponent, aContainer, jComponent);
        }
        this.debug(" > " + index + ":" + jComponent.getName());
        return jComponent;
    }

    public Component componentBefore(Component startComponent, Container aContainer, Component aComponent) {
        int index = this.indexOf(aComponent);
        this.debug(index + ":" + aComponent.getName());
        if (index == -1) {
            this.debug("index not found, setting to " + this.lastIndex);
            index = this.lastIndex;
        }
        index--;
        if (index < 0) {
            index = this.orderedList.size() - 1;
        }
        this.lastIndex = index;
        Component jComponent = this.orderedList.get(index);
        if (!this.canHaveFocus(jComponent) && (startComponent.getName() != null) && !startComponent.getName().equals(jComponent.getName())) {
            return this.componentBefore(startComponent, aContainer, jComponent);
        }
        this.debug(" > " + index + ":" + jComponent.getName());
        return jComponent;
    }

    protected void debug(String debug) {
        CustomFocusTraversalPolicy.logger.fine(debug);
    }

    /**
     * @see java.awt.FocusTraversalPolicy#getComponentAfter(java.awt.Container, java.awt.Component)
     */
    @Override
    public Component getComponentAfter(Container aContainer, Component aComponent) {
        return this.componentAfter(aComponent, aContainer, aComponent);
    }

    /**
     * @see java.awt.FocusTraversalPolicy#getComponentBefore(java.awt.Container, java.awt.Component)
     */
    @Override
    public Component getComponentBefore(Container aContainer, Component aComponent) {
        return this.componentBefore(aComponent, aContainer, aComponent);
    }

    /**
     * @see java.awt.FocusTraversalPolicy#getDefaultComponent(java.awt.Container)
     */
    @Override
    public Component getDefaultComponent(Container aContainer) {
        return this.getFirstComponent(aContainer);
    }

    /**
     * @see java.awt.FocusTraversalPolicy#getFirstComponent(java.awt.Container)
     */
    @Override
    public Component getFirstComponent(Container aContainer) {
        return this.orderedList.get(0);
    }

    /**
     * @see java.awt.FocusTraversalPolicy#getLastComponent(java.awt.Container)
     */
    @Override
    public Component getLastComponent(Container aContainer) {
        return this.orderedList.get(this.orderedList.size() - 1);
    }

    protected int indexOf(Component aComponent) {
        int index = this.orderedList.indexOf(aComponent);
        while ((aComponent != null) && (index == -1)) {
            aComponent = aComponent.getParent();
            index = this.orderedList.indexOf(aComponent);
        }
        return index;
    }
}

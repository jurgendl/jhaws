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

    public static final Comparator<Component> XY = (o1, o2) -> new CompareToBuilder().append(o1.getX(), o2.getX()).append(o1.getY(), o2.getY()).toComparison();

    public static final Comparator<Component> YX = (o1, o2) -> new CompareToBuilder().append(o1.getY(), o2.getY()).append(o1.getX(), o2.getX()).toComparison();

    protected CustomFocusTraversalPolicy(List<Component> fixedList) {
        this(fixedList, CustomFocusTraversalPolicy.XY);
    }

    protected CustomFocusTraversalPolicy(List<Component> fixedList, Comparator<Component> sort) {
        orderedList = new ArrayList<>();
        for (Component component : fixedList) {
            if (component.isFocusable()) {
                orderedList.add(component);
            }
        }
        Collections.sort(orderedList, sort);
    }

    protected boolean canHaveFocus(Component aComponent) {
        if (aComponent instanceof JTextComponent) {
            return JTextComponent.class.cast(aComponent).isEditable() && aComponent.isEnabled();
        }
        return aComponent.isEnabled();
    }

    public Component componentAfter(Component startComponent, Container aContainer, Component aComponent) {
        int index = indexOf(aComponent);
        debug(index + ":" + aComponent.getName());
        if (index == -1) {
            debug("index not found, setting to " + lastIndex);
            index = lastIndex;
        }
        index++;
        if (index >= orderedList.size()) {
            index = 0;
        }
        lastIndex = index;
        Component jComponent = orderedList.get(index);
        if (!canHaveFocus(jComponent) && startComponent.getName() != null && !startComponent.getName().equals(jComponent.getName())) {
            return componentAfter(startComponent, aContainer, jComponent);
        }
        debug(" > " + index + ":" + jComponent.getName());
        return jComponent;
    }

    public Component componentBefore(Component startComponent, Container aContainer, Component aComponent) {
        int index = indexOf(aComponent);
        debug(index + ":" + aComponent.getName());
        if (index == -1) {
            debug("index not found, setting to " + lastIndex);
            index = lastIndex;
        }
        index--;
        if (index < 0) {
            index = orderedList.size() - 1;
        }
        lastIndex = index;
        Component jComponent = orderedList.get(index);
        if (!canHaveFocus(jComponent) && startComponent.getName() != null && !startComponent.getName().equals(jComponent.getName())) {
            return componentBefore(startComponent, aContainer, jComponent);
        }
        debug(" > " + index + ":" + jComponent.getName());
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
        return componentAfter(aComponent, aContainer, aComponent);
    }

    /**
     * @see java.awt.FocusTraversalPolicy#getComponentBefore(java.awt.Container, java.awt.Component)
     */
    @Override
    public Component getComponentBefore(Container aContainer, Component aComponent) {
        return componentBefore(aComponent, aContainer, aComponent);
    }

    /**
     * @see java.awt.FocusTraversalPolicy#getDefaultComponent(java.awt.Container)
     */
    @Override
    public Component getDefaultComponent(Container aContainer) {
        return getFirstComponent(aContainer);
    }

    /**
     * @see java.awt.FocusTraversalPolicy#getFirstComponent(java.awt.Container)
     */
    @Override
    public Component getFirstComponent(Container aContainer) {
        return orderedList.get(0);
    }

    /**
     * @see java.awt.FocusTraversalPolicy#getLastComponent(java.awt.Container)
     */
    @Override
    public Component getLastComponent(Container aContainer) {
        return orderedList.get(orderedList.size() - 1);
    }

    protected int indexOf(Component aComponent) {
        int index = orderedList.indexOf(aComponent);
        while (aComponent != null && index == -1) {
            aComponent = aComponent.getParent();
            index = orderedList.indexOf(aComponent);
        }
        return index;
    }
}

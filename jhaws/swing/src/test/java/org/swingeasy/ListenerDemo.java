package org.swingeasy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.event.EventListenerList;

/**
 * @author Jurgen
 */
public class ListenerDemo {
	private static class PropertyChangeListenerAdapter implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			System.out.println(evt.getPropertyName() + ": " + evt.getOldValue() + " > " + evt.getNewValue());
		}
	}

	static final class WeakPropertyChangeListener<T> implements PropertyChangeListener {
		private WeakReference<T> delegate;

		private String removeMethodName = "removePropertyChangeListener";

		@SuppressWarnings("unchecked")
		private Class<T> listenerClass = (Class<T>) PropertyChangeListener.class;

		public WeakPropertyChangeListener(T delegate) {
			this.delegate = new WeakReference<T>(delegate);
		}

		protected void detach(Object source) {
			Method remover;
			try {
				remover = source.getClass().getDeclaredMethod(this.getRemoveMethodName(), this.getListenerClass());
			} catch (SecurityException ex) {
				throw new RuntimeException(ex);
			} catch (NoSuchMethodException ex) {
				throw new RuntimeException(ex);
			}
			try {
				remover.invoke(source, this);
			} catch (IllegalArgumentException ex) {
				throw new RuntimeException(ex);
			} catch (IllegalAccessException ex) {
				throw new RuntimeException(ex);
			} catch (InvocationTargetException ex) {
				throw new RuntimeException(ex);
			}
		}

		public Class<?> getListenerClass() {
			return this.listenerClass;
		}

		public String getRemoveMethodName() {
			return this.removeMethodName;
		}

		@Override
		final public void propertyChange(PropertyChangeEvent event) {
			PropertyChangeListener listener = (PropertyChangeListener) this.delegate.get();
			if (listener == null) {
				this.detach(event.getSource());
			} else {
				listener.propertyChange(event);
			}
		}

		public void setListenerClass(Class<T> listenerClass) {
			this.listenerClass = listenerClass;
		}

		public void setRemoveMethodName(String removeMethodName) {
			this.removeMethodName = removeMethodName;
		}
	}

	public static void main(String[] args) {
		PropertyChangeListener l1 = new PropertyChangeListenerAdapter();
		PropertyChangeListener l2 = new WeakPropertyChangeListener<PropertyChangeListenerAdapter>(new PropertyChangeListenerAdapter());
		PropertyChangeListener weakproxy = WeakReferencedListener.wrap(PropertyChangeListener.class, new PropertyChangeListenerAdapter());
		ListenerDemo listenerTest = new ListenerDemo();
		listenerTest.addPropertyChangeListener(l1);
		listenerTest.addPropertyChangeListener(l2);
		System.out.println(weakproxy);
		listenerTest.addPropertyChangeListener(weakproxy);
		listenerTest.firePropertyChangeEvent(new PropertyChangeEvent(listenerTest, "bool", false, true));
		System.out.println();
		Runtime.getRuntime().gc();
		listenerTest.firePropertyChangeEvent(new PropertyChangeEvent(listenerTest, "bool", true, false));
		System.out.println(weakproxy);
	}

	protected EventListenerList listenerList = new EventListenerList();

	public void addPropertyChangeListener(PropertyChangeListener testListener) {
		this.listenerList.add(PropertyChangeListener.class, testListener);
	}

	public void firePropertyChangeEvent(PropertyChangeEvent event) {
		for (PropertyChangeListener listener : this.listenerList.getListeners(PropertyChangeListener.class)) {
			listener.propertyChange(event);
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener testListener) {
		this.listenerList.remove(PropertyChangeListener.class, testListener);
	}
}

package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeEvent;

/**
 * Abstract class to allow subclasses to use PropertyChangeSupport. Handles
 * PropertyChangeListeners and fires PropertyChangeEvents for subclasses.
 * 
 * @author Joachim Antfolk
 * @version 1.0
 * @since 2020-03-08
 */
public abstract class AbstractModel implements PropertyChange {

	private final PropertyChangeSupport pcs;

	public AbstractModel() {
		pcs = new PropertyChangeSupport(this);
	}

	/**
	 * Adds listener to this object
	 * 
	 * @param listener PropertyChangeListener to be added
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	/**
	 * Removes listener from this object
	 * 
	 * @param listener PropertyChangeListener to be removed
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	/**
	 * Notifies any potential listeners that a change has occurred
	 * 
	 * @param evt PropertyChangeEvent to be fired
	 */
	protected void firePropertyChange(PropertyChangeEvent evt) {
		pcs.firePropertyChange(evt);
	}

	/**
	 * Notifies any potential listeners that a change has occurred
	 * 
	 * @param property name of the fired property
	 * @param oldValue the property's value before change
	 * @param newValue the property's value after change
	 */
	protected void firePropertyChange(String property, Object oldValue, Object newValue) {
		pcs.firePropertyChange(property, oldValue, newValue);
	}

	/**
	 * Notifies any potential listeners that a change has occurred
	 * 
	 * @param property name of the fired property
	 * @param oldValue the property's value before change
	 * @param newValue the property's value after change
	 */
	protected void firePropertyChange(String property, int oldValue, int newValue) {
		pcs.firePropertyChange(property, oldValue, newValue);
	}
}

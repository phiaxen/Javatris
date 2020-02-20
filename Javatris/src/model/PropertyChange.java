package model;

import java.beans.PropertyChangeListener;

/**
 * Interface for use of PropertyChangeSupport
 * @author Joachim Antfolk
 * @version 2020-02-19
 */
public interface PropertyChange {

	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
}

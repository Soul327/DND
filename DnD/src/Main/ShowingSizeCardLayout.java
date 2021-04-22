package Main;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

public class ShowingSizeCardLayout extends CardLayout {

	public Dimension preferredLayoutSize(Container container) {

		Insets insets = container.getInsets();

		Dimension dim = null;

		for (Component component : container.getComponents()) {
			if (component.isVisible()) {
				dim = component.getPreferredSize();
			}
		}

		dim.width += insets.left + insets.right;
		dim.height += insets.top + insets.bottom;

		return dim;

	}
}
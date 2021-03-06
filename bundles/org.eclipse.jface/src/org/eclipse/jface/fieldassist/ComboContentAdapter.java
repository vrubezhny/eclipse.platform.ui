/*******************************************************************************
 * Copyright (c) 2005, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.fieldassist;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

/**
 * An {@link IControlContentAdapter} for SWT Combo controls. This is a
 * convenience class for easily creating a {@link ContentProposalAdapter} for
 * combo fields.
 *
 * @since 3.2
 */
public class ComboContentAdapter implements IControlContentAdapter,
		IControlContentAdapter2 {

	@Override
	public String getControlContents(Control control) {
		return ((Combo) control).getText();
	}

	@Override
	public void setControlContents(Control control, String text,
			int cursorPosition) {
		((Combo) control).setText(text);
		((Combo) control)
				.setSelection(new Point(cursorPosition, cursorPosition));
	}

	@Override
	public void insertControlContents(Control control, String text,
			int cursorPosition) {
		Combo combo = (Combo) control;
		String contents = combo.getText();
		Point selection = combo.getSelection();
		StringBuilder sb = new StringBuilder();
		sb.append(contents.substring(0, selection.x));
		sb.append(text);
		if (selection.y < contents.length()) {
			sb.append(contents.substring(selection.y, contents.length()));
		}
		combo.setText(sb.toString());
		selection.x = selection.x + cursorPosition;
		selection.y = selection.x;
		combo.setSelection(selection);
	}

	@Override
	public int getCursorPosition(Control control) {
		return ((Combo) control).getSelection().x;
	}

	@Override
	public Rectangle getInsertionBounds(Control control) {
		// This doesn't take horizontal scrolling into affect.
		// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=204599
		Combo combo = (Combo) control;
		int position = combo.getSelection().y;
		String contents = combo.getText();
		GC gc = new GC(combo);
		gc.setFont(combo.getFont());
		Point extent = gc.textExtent(contents.substring(0, Math.min(position, contents.length())));
		gc.dispose();
		return new Rectangle(combo.getClientArea().x + extent.x, combo.getClientArea().y, 1,
				combo.getClientArea().height);
	}

	@Override
	public void setCursorPosition(Control control, int index) {
		((Combo) control).setSelection(new Point(index, index));
	}

	/**
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter2#getSelection(org.eclipse.swt.widgets.Control)
	 *
	 * @since 3.4
	 */
	@Override
	public Point getSelection(Control control) {
		return ((Combo) control).getSelection();
	}

	/**
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter2#setSelection(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.graphics.Point)
	 *
	 * @since 3.4
	 */
	@Override
	public void setSelection(Control control, Point range) {
		((Combo) control).setSelection(range);
	}

}

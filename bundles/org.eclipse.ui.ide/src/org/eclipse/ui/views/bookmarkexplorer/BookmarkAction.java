/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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

package org.eclipse.ui.views.bookmarkexplorer;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

/*
*
* Marked for deletion, see Bug 550439
*
* @noreference
* @noinstantiate This class is not intended to be instantiated by clients.
* @noextend This class is not intended to be subclassed by clients.
*/
@Deprecated
/**
 * An abstract class for all bookmark view actions.
 */
abstract class BookmarkAction extends SelectionProviderAction {
	private BookmarkNavigator view;

	/**
	 * Creates a bookmark action.
	 */
	protected BookmarkAction(BookmarkNavigator view, String label) {
		super(view.getViewer(), label);
		this.view = view;
	}

	/**
	 * Returns the bookmarks view.
	 */
	public BookmarkNavigator getView() {
		return view;
	}

	/**
	 * Execute the specified undoable operation
	 */
	void execute(IUndoableOperation operation, String title,
			IProgressMonitor monitor, IAdaptable uiInfo) {
		try {
			PlatformUI.getWorkbench().getOperationSupport()
					.getOperationHistory().execute(operation, monitor, uiInfo);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof CoreException) {
				ErrorDialog.openError(view.getShell(), title,
						null, ((CoreException)e.getCause()).getStatus());
			} else {
				IDEWorkbenchPlugin.log(title, e);
			}
		}
	}

}

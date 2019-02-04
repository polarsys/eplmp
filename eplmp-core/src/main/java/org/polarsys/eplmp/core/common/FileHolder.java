/*******************************************************************************
  * Copyright (c) 2017-2019 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/

package org.polarsys.eplmp.core.common;

import java.util.Set;

/**
 * Interface implemented by objects that hold binary files.  
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since V1.0
 */
public interface FileHolder {
    
    Set<BinaryResource> getAttachedFiles();
    
}

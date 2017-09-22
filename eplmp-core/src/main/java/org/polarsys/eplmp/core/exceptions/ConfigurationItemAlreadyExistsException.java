/*******************************************************************************
  * Copyright (c) 2017 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/

package org.polarsys.eplmp.core.exceptions;


import org.polarsys.eplmp.core.product.ConfigurationItem;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class ConfigurationItemAlreadyExistsException extends EntityAlreadyExistsException {
    private final ConfigurationItem mConfigurationItem;
    
    
    public ConfigurationItemAlreadyExistsException(String pMessage) {
        super(pMessage);
        mConfigurationItem = null;
    }

    public ConfigurationItemAlreadyExistsException(Locale pLocale, ConfigurationItem pConfigurationItem) {
        this(pLocale, pConfigurationItem, null);
    }

    public ConfigurationItemAlreadyExistsException(Locale pLocale, ConfigurationItem pConfigurationItem, Throwable pCause) {
        super(pLocale, pCause);
        mConfigurationItem=pConfigurationItem;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mConfigurationItem);     
    }
}

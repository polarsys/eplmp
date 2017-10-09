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
package org.polarsys.eplmp.core.hooks;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

@XmlSeeAlso({SimpleWebhookApp.class, SNSWebhookApp.class})
@Inheritance
@Table(name = "WEBHOOKAPP")
@Entity
public abstract class WebhookApp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public WebhookApp() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public abstract String getAppName();

}

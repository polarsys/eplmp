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
package org.polarsys.eplmp.core.configuration;

import org.polarsys.eplmp.core.product.ConfigurationItem;
import org.polarsys.eplmp.core.security.ACL;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is an instance of a product, its main attributes are
 * the serial number and the configuration item it is an instance of.
 *
 * The composition of the instance may vary according to modifications
 * applied on it so to track this evolution the part collection is kept
 * on several {@link ProductInstanceIteration}.
 *
 * @author Florent Garin
 * @version 2.0, 24/02/14
 * @since V2.0
 */
@Table(name="PRODUCTINSTANCEMASTER")
@IdClass(org.polarsys.eplmp.core.configuration.ProductInstanceMasterKey.class)
@Entity
@NamedQueries({
        @NamedQuery(name="ProductInstanceMaster.findByPathData", query="SELECT p.productInstanceMaster FROM ProductInstanceIteration p WHERE :pathDataMasterList member of p.pathDataMasterList"),
        @NamedQuery(name="ProductInstanceMaster.findByConfigurationItemId", query="SELECT pim FROM ProductInstanceMaster pim WHERE pim.instanceOf.id = :ciId AND pim.instanceOf.workspace.id = :workspaceId"),
        @NamedQuery(name="ProductInstanceMaster.findByPart", query="SELECT DISTINCT pim FROM ProductInstanceMaster pim JOIN ProductBaseline pb JOIN BaselinedPart bp WHERE pim.instanceOf = pb.configurationItem AND pb.partCollection = bp.partCollection AND bp.targetPart.partRevision = :partRevision ORDER BY pb.configurationItem.id")
})
public class ProductInstanceMaster implements Serializable {


    @Column(name="SERIALNUMBER", length = 100)
    @Id
    private String serialNumber;

    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "CONFIGURATIONITEM_ID", referencedColumnName = "ID"),
            @JoinColumn(name = "WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private ConfigurationItem instanceOf;

    @OneToMany(mappedBy = "productInstanceMaster", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("iteration ASC")
    private List<ProductInstanceIteration> productInstanceIterations = new ArrayList<>();

    @OneToOne(orphanRemoval = true, cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private ACL acl;

    public ProductInstanceMaster() {
    }

    public ProductInstanceMaster(ConfigurationItem configurationItem, String serialNumber) {
        this.instanceOf = configurationItem;
        this.serialNumber = serialNumber;
    }

    public ConfigurationItem getInstanceOf() {
        return instanceOf;
    }
    public void setInstanceOf(ConfigurationItem instanceOf) {
        this.instanceOf = instanceOf;
    }

    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<ProductInstanceIteration> getProductInstanceIterations() {
        return productInstanceIterations;
    }

    public ProductInstanceIteration createNextIteration() {
        ProductInstanceIteration lastProductInstanceIteration = getLastIteration();
        int iteration;
        if(lastProductInstanceIteration==null){
            iteration = 1;
        }else{
            iteration = lastProductInstanceIteration.getIteration()+1;
        }
        ProductInstanceIteration productInstanceIteration = new ProductInstanceIteration(this,iteration);
        this.productInstanceIterations.add(productInstanceIteration);
        return productInstanceIteration;
    }
    public void removeIteration(ProductInstanceIteration prodInstI){
        this.productInstanceIterations.remove(prodInstI);
    }

    public ProductInstanceIteration getLastIteration() {
        int index = productInstanceIterations.size()-1;
        if(index < 0)
            return null;
        else
            return productInstanceIterations.get(index);
    }

    public ACL getAcl() {
        return acl;
    }

    public void setAcl(ACL acl) {
        this.acl = acl;
    }

    public void setProductInstanceIterations(List<ProductInstanceIteration> productInstanceIterations) {
        this.productInstanceIterations = productInstanceIterations;
    }

    public String getIdentifier(){
        return instanceOf.getId() + "-" + serialNumber;
    }

    public String getConfigurationItemId(){
        return instanceOf.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        ProductInstanceMaster that = (ProductInstanceMaster) o;

        if (instanceOf != null ? !instanceOf.equals(that.instanceOf) : that.instanceOf != null){
            return false;
        }
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = serialNumber != null ? serialNumber.hashCode() : 0;
        result = 31 * result + (instanceOf != null ? instanceOf.hashCode() : 0);
        return result;
    }
}

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

package org.polarsys.eplmp.core.meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The Folder class is a unitary element of a tree structure.
 * Like in a regular file system, folder may contain other folders.
 * Folder is a way to organize entities like {@link Tag} could be.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="FOLDER")
@Entity
public class Folder implements Serializable, Comparable<Folder> {

    @Column(length=1024)
    @javax.persistence.Id
    private String completePath="";
            
    @ManyToOne
    private Folder parentFolder;
    
    public Folder() {
    }

    public Folder(String pCompletePath) {
        completePath = pCompletePath;
        if (!isRoot() && !isHome()){
            int index = completePath.lastIndexOf('/');
            parentFolder = new Folder(completePath.substring(0, index));
        }
    }

    public Folder(String pParentFolderPath, String pShortName) {
        this(pParentFolderPath + "/" + pShortName);
    }

    public String getWorkspaceId(){
        return Folder.parseWorkspaceId(completePath);
    }
    
    public static String parseWorkspaceId(String pCompletePath){
        if(!pCompletePath.contains("/")) {
            return pCompletePath;
        }else{
            int index = pCompletePath.indexOf('/');
            return pCompletePath.substring(0, index);
        }
    }
    
    public boolean isRoot() {
        return !completePath.contains("/");
    }
    
    public boolean isHome() {
        try {
            int index = completePath.lastIndexOf('/');
            return completePath.charAt(index+1) == '~';
        } catch (IndexOutOfBoundsException pIOOBEx) {
            return false;
        }
    }
    
    public boolean isPrivate() {
        try {
            int index = completePath.indexOf('/');
            return completePath.charAt(index+1) == '~';
        } catch (IndexOutOfBoundsException pIOOBEx) {
            return false;
        }
    }
    
    public String getOwner() {
        String owner = null;
        if (isPrivate()) {
            int beginIndex = completePath.indexOf('/');
            int endIndex = completePath.indexOf("/", beginIndex+1);
            if(endIndex==-1) {
                endIndex = completePath.length();
            }
            
            owner = completePath.substring(beginIndex+2, endIndex);
        }
        return owner;
    }
    
    @Override
    public String toString() {
        return completePath;
    }
    
    public String getCompletePath() {
        return completePath;
    }
    
    public Folder getParentFolder() {
        return parentFolder;
    }
    
    public Folder[] getAllFolders() {
        Folder currentFolder = this;
        Deque<Folder> foldersStack = new ArrayDeque<>();
        
        while (!(currentFolder == null)) {
            foldersStack.push(currentFolder);
            currentFolder = currentFolder.getParentFolder();
        }
        
        Folder[] folders = new Folder[foldersStack.size()];

        int i = 0;

        while(!foldersStack.isEmpty()){
            folders[i++] = foldersStack.pop();
        }

        return folders;
    }
    
    public String getShortName() {
        if(isRoot()) {
            return completePath;
        }

        int index = completePath.lastIndexOf('/');
        return completePath.substring(index + 1);
    }

    public String getRoutePath() {
        int index = completePath.indexOf('/');

        if (index == -1) {
            return "";
        } else {
            return completePath.substring(index + 1).replaceAll("/", ":");
        }
    }

    
    public static Folder createRootFolder(String pWorkspaceId) {
        return new Folder(pWorkspaceId);
    }
    
    public static Folder createHomeFolder(String pWorkspaceId, String pLogin) {
        return new Folder(pWorkspaceId + "/~" + pLogin);
    }
    
    public Folder createSubFolder(String pShortName) {
        Folder subFolder = new Folder();
        subFolder.completePath=completePath + "/" + pShortName;
        subFolder.parentFolder=this;
        return subFolder;
    }
    
    
    @Override
    public int hashCode() {
        return completePath.hashCode();
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof Folder)) {
            return false;
        }
        Folder folder = (Folder) pObj;
        return folder.completePath.equals(completePath);
    }
    
    @Override
    public int compareTo(Folder pFolder) {
        return completePath.compareTo(pFolder.completePath);
    }
}

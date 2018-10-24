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

package org.polarsys.eplmp.server.util;

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.core.util.AlphanumericComparator;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Asmae CHADID on 02/03/15, Ludovic BAREL on 10/18.
 *
 */
public class ProductUtil {


    public static final String WORKSPACE_ID = "TestWorkspace";
    public static final String VERSION = "A";
    public static final int ITERATION = 1;
    public static final String PART_ID = "TestPart";
    public static final String PART_MASTER_TEMPLATE_ID = "template";
    public static final String PART_TYPE = "PartType";
    public static final String USER_2_NAME = "user2";
    public static final String USER_2_LOGIN = "user2";
    public static final String USER_1_NAME = "user1";
    public static final String USER_1_LOGIN = "user1";

    public static final String USER_1_MAIL = "user1@docdoku.com";
    public static final String USER_1_LANGUAGE = "fr";
    public static final String USER_2_MAIL = "user2@docdoku.com";
    public static final String USER_2_LANGUAGE = "en";
    public static final String USER_GRP1 = "grp1";

    public static String WORKSPACE_DESCRIPTION = "description of the workspace";

    //BEGIN : DEFAULT PARTS
    /**  createTestableParts method will generate a default structure for each part set as follow :
     *
     *      |--> part1
     *             |
     *             |--> part7
     *             |
     *             |--> part8
     *
     *      |--> part2
     *             |
     *             |--> part9
     *             |
     *             |--> part10
     *
     *      |--> part3
     *             |
     *             |--> part11
     *                     |
     *                     |--> part14
     *
     *      |--> part4
     *
     *      |--> part5
     *             |
     *             |--> part12
     *                     |
     *                     |--> part15
     *                             |
     *                             |--> part17
     *
     *      |--> part6
     *             |
     *             |--> part13
     *                     |
     *                     |--> part16
     *                             |
     *                             |--> part18
     *                                     |
     *                                     |--> pPart19
     *                                             |
     *                                             |--> part20
     */

    public final static String[] defaultPartsNumber_list = {

            "PART-001","PART-002","PART-003","PART-004","PART-005",
            "PART-006","PART-007","PART-008","PART-009","PART-010",
            "PART-011","PART-012","PART-013","PART-014","PART-015",
            "PART-016","PART-017","PART-018","PART-019","PART-020"
    };

    /*
     *   Mapping rule  :  defaultMappingUsageLink_tab [i][i] => PART-XYZ related parts
     *   example :
     *
     *           |--> part1
     *                   |
     *                   |--> part7
     *                   |
     *                   |--> part8
     *
     *      so we have :
     *
     *          defaultMappingUsageLink_tab [0] => PART-001
     *          defaultMappingUsageLink_tab [0][0] = {6,7}
     *
     *      with :
     *
     *          defaultPartsNumber_list[6] => PART-007
     *          defaultPartsNumber_list[7] => PART-008
     *
     *     When PART-XYZ have no related the array will be empty
     */
    protected final static int [][] defaultMappingUsageLink_tab = {

            {6,7}, {8,9}, {10}, {}, {11},//related to : part1, part2, part3, part4, part5
            {12}, {}, {}, {}, {},     //related to    : part6, part7, part8, part9, part10
            {13}, {14}, {15}, {}, {16},//related to   : part11, part12, part13, part14, part15
            {17}, {}, {18}, {19}, {}//related to      : part16, part17, part18, part19, part20
    };

    private static Workspace workspace;
    public static User user;
    private static List<String> createdNumber_list = new ArrayList<>();
    protected static List<PartMaster> existingPart_list = new ArrayList<>();
    private static Comparator<CharSequence> STRING_COMPARATOR = new AlphanumericComparator();
    private static HashMap<String,HashMap<String,Object>> metaData_list = new HashMap<>();
    private static List<String> rootsPartNumberInBuild = new ArrayList<>();

    public  static final String PARTMASTER_MAP_KEY = "PartMaster";
    public  static final String REVISIONS_MAP_KEY = "Revisions";
    public  static final String ITERATION_MAP_KEY = "Iterations";

    public static void createTestableParts() throws Exception {

        //in case a creation was done before
        createdNumber_list.clear();
        existingPart_list.clear();

        workspace = new Workspace();
        Account account = new Account(USER_1_LOGIN,USER_1_LOGIN,USER_1_MAIL,"en",new Date(),"GMT");
        user = new User(workspace,account);

        workspace.setEnabled(true);
        workspace.setFolderLocked(false);
        workspace.setId(WORKSPACE_ID);
        workspace.setAdmin(account);
        for(String partNumber : defaultPartsNumber_list){

            boolean isCreated = (createdNumber_list.contains(partNumber));
            if(!isCreated){

                existingPart_list.add(buildPartMaster(partNumber));
            }
            rootsPartNumberInBuild.clear();
        }
        sort(existingPart_list,existingPart_list.size());
        createMetaData(existingPart_list.size());
    }

    public static PartMaster getPartMasterWith(String partNumber){

        return (PartMaster) metaData_list.get(partNumber).get(PARTMASTER_MAP_KEY);
    }

    public static  List<PartRevision> getPartRevisionsOf(String partNumber){

        return ((List<PartRevision>)getPartMetaData(partNumber).get(REVISIONS_MAP_KEY));
    }

    public static  void setPartIterationsOf(String partNumber, List<PartIteration> newList){

        metaData_list.get(partNumber).put(ITERATION_MAP_KEY,newList);
    }

    public static  void setPartRevisionsOf(String partNumber, List<PartRevision> newList){

        metaData_list.get(partNumber).put(REVISIONS_MAP_KEY,newList);
    }

    public static  List<PartIteration> getPartIterationsOf(String partNumber){

        return ((List<PartIteration>)getPartMetaData(partNumber).get(ITERATION_MAP_KEY));
    }

    public static PartRevision getPartRevisionWith(String version, String partNumber){

        return getRevisionWith(version, partNumber,((List<PartRevision>)getPartMetaData(partNumber).get(REVISIONS_MAP_KEY)).size());
    }

    public static void addRevisionToPartWith(String partNumber, PartRevision revision, boolean checedkout){

        //avoid double
        if(getRevisionWith(revision.getVersion(),partNumber,
                ( (List<PartRevision>)getPartMetaData(partNumber).get(REVISIONS_MAP_KEY)).size()) != null){

            return;
        }

        if(checedkout){

            revision.setCheckOutUser(user);
        }

        ((List<PartRevision>)getPartMetaData(partNumber).get(REVISIONS_MAP_KEY)).add(revision);
    }

    public static void addRevisionWithPartLinkTo(PartMaster partMaster, String[] usageMembers, boolean released){

        PartRevision partRevision = partMaster.createNextRevision(user);
        PartIteration partIteration = partRevision.createNextIteration(user);
        List<PartUsageLink> usage = new ArrayList<>();
        for(String member : usageMembers){

            PartUsageLink partUsageLink=  new PartUsageLink();
            partUsageLink.setComponent(getPartMasterWith(member));
            partUsageLink.setOptional(false);
            partUsageLink.setAmount(1);
            partUsageLink.setReferenceDescription(member + "-UsageLink");
            usage.add(partUsageLink);
        }
        partIteration.setComponents(usage);

        if(released){

            partRevision.release(user);
        }

        //update meta data
        addIterationToRevision(partRevision.getVersion(), partMaster.getNumber(), partIteration);
        addRevisionToPartWith(partMaster.getNumber(), partRevision, false);
    }

    public static void addIterationToRevision(String version, String forPartNumber, PartIteration iteration){

        if(!exist(iteration.getIteration(),forPartNumber)){

            ((List<PartIteration>)getPartMetaData(forPartNumber).get(ITERATION_MAP_KEY)).add(iteration);
        }
    }

    public static void addSubstituteInLastIterationOfLastRevisionTo(PartMaster partMaster, String[] substituteTab, String toPartNumber){

        List<PartSubstituteLink> substituteLinks = new ArrayList<>();
        List<PartUsageLink> usages = new ArrayList<>();
        for(PartUsageLink partUsageLink : partMaster.getLastRevision().getLastIteration().getComponents()){

            if( partUsageLink.getComponent() == null){

                usages.add(partUsageLink);
                continue;
            }
            if(partUsageLink.getComponent().getNumber().equals(toPartNumber)){

                for(int i = 0; i < substituteTab.length;i++){

                    PartSubstituteLink partSubstituteLink = new PartSubstituteLink();
                    partSubstituteLink.setSubstitute(getPartMasterWith(substituteTab[i]));
                    partSubstituteLink.setAmount(1);
                    partSubstituteLink.setReferenceDescription(substituteTab[i]+"-Substitute");
                    substituteLinks.add(partSubstituteLink);
                    partUsageLink.setSubstitutes(substituteLinks);
                }

            }
        }

        //update meta data
        setPartIterationsOf(partMaster.getNumber(), partMaster.getLastRevision().getPartIterations());
    }

    private static HashMap<String, Object> getPartMetaData(String number){

        return metaData_list.get(number);
    }

    public static void areThoseOfRevision(String version, List<PartIteration> inList, String forPartNumber){

        for(PartIteration pI : inList){

            assertEquals(forPartNumber, pI.getPartNumber());
            assertEquals(version, pI.getPartRevision().getVersion());
            assertFalse(pI.getPartRevision().isCheckedOut());
        }
    }

    /**
     *
     * Map structure
     *
     *     "PartMaster" => root part ( PartMaster )
     *     "Revisions" => HashMap<String, List<Revision>>
     *     "Iterations" => HashMap<Integer, List<Iteration>
     */
    private static void createMetaData(int partCountNumber){

        if(partCountNumber == 0 ){

            return;
        }
        PartMaster tmpPart = existingPart_list.get(partCountNumber - 1);
        HashMap<String,Object> tmpDateMap = new HashMap<>();
        tmpDateMap.put(PARTMASTER_MAP_KEY,tmpPart);
        List<PartIteration> iteration_list = new ArrayList<>();
        for(PartRevision revision : tmpPart.getPartRevisions()) {

            iteration_list.addAll(revision.getPartIterations());
        }
        tmpDateMap.put(REVISIONS_MAP_KEY,tmpPart.getPartRevisions());
        tmpDateMap.put(ITERATION_MAP_KEY,iteration_list);
        metaData_list.put(tmpPart.getNumber(),tmpDateMap);
        createMetaData(partCountNumber-1);
    }

    private static boolean exist(int iteration,String partNumber) {

        for (PartIteration partIteration : getPartIterationsOf(partNumber)){

            if (iteration == partIteration.getIteration()) {

                return true;
            }
        }
        return false;
    }

    private static PartRevision getRevisionWith(String version,String partNumber,int lastIndex){

        if(lastIndex == 0){

            return null;
        }
        PartRevision result = ( (List<PartRevision>)metaData_list.get(partNumber).get(REVISIONS_MAP_KEY)).get(lastIndex - 1);
        if(version.equals(result.getVersion())){

            return  result;
        }
        return getRevisionWith(version,partNumber,lastIndex-1);
    }

    private static void sort(List<PartMaster> list, int size)
    {

        if (size == 1) {

            return;
        }
        for (int i=0; i<size-1; i++) {

            if (STRING_COMPARATOR.compare(list.get(i).getNumber(), list.get(i + 1).getNumber()) > 0) {

                PartMaster temp = list.get(i);
                list.set(i, list.get(i + 1));
                list.set(i + 1, temp);
            }
        }
        sort(list, size-1);
    }
    private static PartMaster buildPartMaster(String number) throws Exception {

        PartMaster tmpPart = new PartMaster(workspace,number,user);
        PartRevision tmpRevision = tmpPart.createNextRevision(user);
        if (!rootsPartNumberInBuild.contains(number)) {

            rootsPartNumberInBuild.add(number);
        }
        PartIteration tmpIteration = buildIteration(defaultMappingUsageLink_tab[Integer.parseInt(number.split("-")[1]) - 1], tmpRevision);
        for(PartUsageLink link : tmpIteration.getComponents()){

            if(!existingPart_list.contains(link.getComponent())){

                existingPart_list.add(link.getComponent());
            }
        }
        createdNumber_list.add(number);
        return tmpPart;
    }

    private static PartIteration buildIteration(int[] related, PartRevision revision) throws Exception {

        PartIteration iteration = revision.createNextIteration(user);
        iteration.setComponents(buildUsageLink(revision.getPartNumber(), related));
        return iteration;
    }

    private static List<PartUsageLink> buildUsageLink(String revisionPartNumber, int[] related ) throws Exception {

        List<PartUsageLink> result =  new ArrayList<>();

        for(int pointer : related) {

            PartUsageLink partUsageLink = new PartUsageLink();
            partUsageLink.setOptional(false);
            partUsageLink.setAmount(1);
            if(rootsPartNumberInBuild.contains(defaultPartsNumber_list[pointer])){

                throw new Exception("loop detected "+revisionPartNumber+" => "+defaultPartsNumber_list[pointer]);
            }
            PartMaster component = buildPartMaster(defaultPartsNumber_list[pointer]);
            partUsageLink.setReferenceDescription(component.getNumber() + "-UsageLink");
            partUsageLink.setComponent(component);
            result.add(partUsageLink);
        }
        return result;
    }
    //END : DEFAULT PARTS
}
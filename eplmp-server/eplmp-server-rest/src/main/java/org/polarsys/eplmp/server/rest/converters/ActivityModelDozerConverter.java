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

package org.polarsys.eplmp.server.rest.converters;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.polarsys.eplmp.core.workflow.ActivityModel;
import org.polarsys.eplmp.core.workflow.ParallelActivityModel;
import org.polarsys.eplmp.core.workflow.SequentialActivityModel;
import org.polarsys.eplmp.core.workflow.TaskModel;
import org.polarsys.eplmp.server.rest.dto.ActivityModelDTO;
import org.polarsys.eplmp.server.rest.dto.TaskModelDTO;

import java.util.ArrayList;
import java.util.List;


public class ActivityModelDozerConverter extends DozerConverter<ActivityModel, ActivityModelDTO> {

    private Mapper mapper;

    public ActivityModelDozerConverter() {
        super(ActivityModel.class, ActivityModelDTO.class);
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @Override
    public ActivityModelDTO convertTo(ActivityModel activityModel, ActivityModelDTO activityModelDTO) {

        List<TaskModelDTO> taskModelsDTO = new ArrayList<>();
        for (int i = 0; i < activityModel.getTaskModels().size(); i++) {
            taskModelsDTO.add(mapper.map(activityModel.getTaskModels().get(i), TaskModelDTO.class));
        }

        ActivityModelDTO.Type type;
        Integer tasksToComplete = null;
        Integer relaunchStep = null;

        if (activityModel.getRelaunchActivity() != null) {
            relaunchStep = activityModel.getRelaunchActivity().getStep();
        }

        if (activityModel instanceof SequentialActivityModel) {
            type = ActivityModelDTO.Type.SEQUENTIAL;
        } else if (activityModel instanceof ParallelActivityModel) {
            type = ActivityModelDTO.Type.PARALLEL;
            tasksToComplete = ((ParallelActivityModel) activityModel).getTasksToComplete();
        } else {
            throw new IllegalArgumentException("ActivityModel type not supported");
        }

        return new ActivityModelDTO(activityModel.getStep(), taskModelsDTO, activityModel.getLifeCycleState(), type, tasksToComplete, relaunchStep);
    }

    @Override
    public ActivityModel convertFrom(ActivityModelDTO activityModelDTO, ActivityModel pActivityModel) {

        List<TaskModel> taskModels = new ArrayList<>();
        for (int i = 0; i < activityModelDTO.getTaskModels().size(); i++) {
            taskModels.add(mapper.map(activityModelDTO.getTaskModels().get(i), TaskModel.class));
        }

        ActivityModel activityModel;

        switch (activityModelDTO.getType()) {
            case SEQUENTIAL: {
                activityModel = new SequentialActivityModel();
                activityModel.setTaskModels(taskModels);
                break;
            }
            case PARALLEL: {
                activityModel = new ParallelActivityModel();
                activityModel.setTaskModels(taskModels);
                ((ParallelActivityModel) activityModel).setTasksToComplete(activityModelDTO.getTasksToComplete());
                break;
            }
            default: {
                throw new IllegalArgumentException("ActivityModelDTO type not supported");
            }
        }

        activityModel.setStep(activityModelDTO.getStep());
        activityModel.setLifeCycleState(activityModelDTO.getLifeCycleState());
        return activityModel;
    }
}

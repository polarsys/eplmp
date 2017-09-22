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

import org.polarsys.eplmp.core.workflow.Activity;
import org.polarsys.eplmp.core.workflow.ParallelActivity;
import org.polarsys.eplmp.core.workflow.SequentialActivity;
import org.polarsys.eplmp.core.workflow.Task;
import org.polarsys.eplmp.server.rest.dto.ActivityDTO;
import org.polarsys.eplmp.server.rest.dto.TaskDTO;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.DozerConverter;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

public class ActivityDozerConverter extends DozerConverter<Activity, ActivityDTO> {

    private Mapper mapper;

    public ActivityDozerConverter() {
        super(Activity.class, ActivityDTO.class);
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @Override
    public ActivityDTO convertTo(Activity activity, ActivityDTO activityDTO) {
        List<TaskDTO> tasksDTO = new ArrayList<>();

        for (int i = 0; i < activity.getTasks().size(); i++) {
            tasksDTO.add(mapper.map(activity.getTasks().get(i), TaskDTO.class));
        }

        ActivityDTO.Type type;
        Integer tasksToComplete = null;
        Integer relaunchStep = null;

        if (activity.getRelaunchActivity() != null) {
            relaunchStep = activity.getRelaunchActivity().getStep();
        }

        if (activity instanceof SequentialActivity) {
            type = ActivityDTO.Type.SEQUENTIAL;
        } else if (activity instanceof ParallelActivity) {
            type = ActivityDTO.Type.PARALLEL;
            tasksToComplete = ((ParallelActivity) activity).getTasksToComplete();
        } else {
            throw new IllegalArgumentException("Activity type not supported");
        }

        return new ActivityDTO(activity.getStep(), tasksDTO, activity.getLifeCycleState(), type, tasksToComplete, activity.isComplete(), activity.isStopped(), activity.isInProgress(), activity.isToDo(), relaunchStep);
    }

    @Override
    public Activity convertFrom(ActivityDTO activityDTO, Activity pActivity) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < activityDTO.getTasks().size(); i++) {
            tasks.add(mapper.map(activityDTO.getTasks().get(i), Task.class));
        }

        Activity activity;

        switch (activityDTO.getType()) {
            case SEQUENTIAL:
                activity = new SequentialActivity();
                break;
            case PARALLEL:
                activity = new ParallelActivity();
                ((ParallelActivity) activity).setTasksToComplete(activityDTO.getTasksToComplete());
                break;
            default:
                throw new IllegalArgumentException("ActivityDTO type not supported");
        }

        activity.setStep(activityDTO.getStep());
        activity.setTasks(tasks);
        activity.setLifeCycleState(activityDTO.getLifeCycleState());
        return activity;
    }
}

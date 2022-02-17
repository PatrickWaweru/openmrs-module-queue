/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.queue.web.resources;

import java.util.Arrays;

import org.openmrs.module.queue.model.Queue;
import org.openmrs.module.queue.model.QueueEntry;
import org.openmrs.module.queue.web.resources.custom.response.GenericSingleObjectResult;
import org.openmrs.module.queue.web.resources.custom.response.PropValue;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * Probably, this sub-resource is unnecessary, the idea is; with a searchHandler -
 * QueueCountSearchHandler, I can achieve the following URL formats; /ws/rest/v1/queue/<UUID>/count
 * - returns the count of queue entries for the specified resource
 * /ws/rest/v1/queue/<UUID>/count?status=Waiting for Service Queue Count
 * /ws/rest/v1/queue/<UUID>/count?status=With Service /ws/rest/v1/queue/<UUID>/count?status=Waiting
 * For Service&v=custom:(count) -returns only the count.
 */
@SuppressWarnings("unused")
@SubResource(parent = QueueResource.class, path = "count", supportedClass = QueueEntry.class, supportedOpenmrsVersions = {
        "2.0 - 2.*" })
public class QueueEntryCountSubResource extends DelegatingSubResource<QueueEntry, Queue, QueueResource> {
	
	@Override
	public Queue getParent(QueueEntry queueEntryCount) {
		return queueEntryCount.getQueue();
	}
	
	@Override
	public void setParent(QueueEntry queueEntryCount, Queue queue) {
		queueEntryCount.setQueue(queue);
	}
	
	@Override
	public PageableResult doGetAll(Queue queue, RequestContext requestContext) throws ResponseException {
		return new GenericSingleObjectResult(Arrays.asList(new PropValue("queueName", queue.getName()),
		    new PropValue("queueEntriesCount", queue.getQueueEntries().size())));
	}
	
	@Override
	public QueueEntry getByUniqueId(String s) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected void delete(QueueEntry queueEntryCount, String s, RequestContext requestContext) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public QueueEntry newDelegate() {
		return new QueueEntry();
	}
	
	@Override
	public QueueEntry save(QueueEntry queueEntry) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(QueueEntry queueEntry, RequestContext requestContext) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		DelegatingResourceDescription resourceDescription = new DelegatingResourceDescription();
		if (representation instanceof CustomRepresentation) {
			resourceDescription = null;
		} else if (representation instanceof DefaultRepresentation) {
			resourceDescription.addProperty("count");
			resourceDescription.addProperty("display");
		} else if (representation instanceof RefRepresentation) {
			resourceDescription.addProperty("count");
			resourceDescription.addProperty("display");
			resourceDescription.addProperty("queue", Representation.REF);
		} else if (representation instanceof FullRepresentation) {
			resourceDescription.addProperty("count");
			resourceDescription.addProperty("display");
			resourceDescription.addProperty("queue", Representation.FULL);
		}
		return resourceDescription;
	}
	
	@PropertyGetter("display")
	public String getDisplay(QueueEntry queueEntry) {
		//Display queue name
		return queueEntry.getQueue().getName();
	}
	
	@Override
	public String getResourceVersion() {
		return "2.3";
	}
}
/**
 * Aptana Studio
 * Copyright (c) 2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.portal.ui.dispatch.actionControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mortbay.util.ajax.JSON;

import com.aptana.configurations.processor.ConfigurationStatus;
import com.aptana.core.projects.templates.IProjectTemplate;
import com.aptana.core.projects.templates.TemplateType;
import com.aptana.projects.internal.wizards.NewProjectWizard;

/**
 * Action controller for Template operations
 * 
 * @author nle
 */
public class TemplateActionController extends AbstractActionController
{
	private static final String ID = "id"; //$NON-NLS-1$
	private static final String NAME = "name"; //$NON-NLS-1$
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$
	private static final String TEMPLATE_TYPE = "type"; //$NON-NLS-1$

	private static final String[] ALL_TYPES = new String[] { TemplateType.PHP.name(), TemplateType.PYTHON.name(),
			TemplateType.RAILS.name(), TemplateType.RUBY.name(), TemplateType.TITANIUM_DESKTOP.name(),
			TemplateType.TITANIUM_MOBILE.name(), TemplateType.WEB.name() };

	public void configurationStateChanged(ConfigurationStatus status, Set<String> attributesChanged)
	{
	}

	/**
	 * Return the template types
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ControllerAction
	public Object getTemplateTypes()
	{
		return JSON.toString(ALL_TYPES);
	}

	/**
	 * Return the template for give types
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ControllerAction
	public Object getTemplates(Object templateTypes)
	{
		List<TemplateType> types = new ArrayList<TemplateType>();
		if (!(templateTypes instanceof Object[]) || ((Object[]) templateTypes).length < 1)
		{
			templateTypes = ALL_TYPES;
		}

		for (Object object : (Object[]) templateTypes)
		{
			if (object instanceof String)
			{
				TemplateType templateType = TemplateType.valueOf((String) object);
				if (templateType != null)
				{
					types.add(templateType);
				}
			}
		}

		List<IProjectTemplate> templates = NewProjectWizard.getProjectTemplates(types.toArray(new TemplateType[types
				.size()]));
		List<Map<String, String>> templateObjects = new ArrayList<Map<String, String>>();

		for (IProjectTemplate template : templates)
		{
			if (template.getId() == null || template.getId().length() == 0)
			{
				continue;
			}

			Map<String, String> properties = new HashMap<String, String>();
			properties.put(ID, template.getId());
			properties.put(NAME, template.getDisplayName());
			properties.put(DESCRIPTION, template.getDescription());
			properties.put(TEMPLATE_TYPE, template.getType().name());
			templateObjects.add(properties);
		}

		return JSON.toString(templateObjects.toArray(new Map[templateObjects.size()]));
	}
}

/**
 * 
 */
package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.models.collaborators.Collaborator;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author sriram
 * 
 */
public interface CollaboratorProperties extends PropertyAccess<Collaborator> {

    ValueProvider<Collaborator, String> institution();

}
